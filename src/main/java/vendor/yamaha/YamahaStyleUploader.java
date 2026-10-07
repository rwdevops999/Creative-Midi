package vendor.yamaha;

import creative.scenes.sysex.convertor.SysexToHexStringConvertor;
import creative.scenes.sysex.util.SysexWriter;
import util.ApplicationInfo;

import javax.sound.midi.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class YamahaStyleUploader {
    private SysexWriter writer = new SysexWriter();

    // Unique Sub-ID for Yamaha File Protocol (F0 43 50 ...)
    private static final byte[] YAMAHA_HEADER = {(byte) 0x43, (byte) 0x50, (byte) 0x00};

    /**
     * Helper to assemble a complete SysEx packet.
     * Combines Status Start (F0) + Yamaha IDs + Command + Payload + Status End (F7)
     */
    private static byte[] buildSysExPacket(byte[] cmdType, byte[] payload) {
        int totalLen = 1 + YAMAHA_HEADER.length + cmdType.length + payload.length + 1;
        byte[] packet = new byte[totalLen];

        int idx = 0;
        packet[idx++] = (byte) (0xF0 & 0xFF); // Geforceerde bitmasker voor de startbyte

        System.arraycopy(YAMAHA_HEADER, 0, packet, idx, YAMAHA_HEADER.length);
        idx += YAMAHA_HEADER.length;

        System.arraycopy(cmdType, 0, packet, idx, cmdType.length);
        idx += cmdType.length;

        System.arraycopy(payload, 0, packet, idx, payload.length);
        idx += payload.length;

        packet[idx] = (byte) (0xF7 & 0xFF); // Geforceerde bitmasker voor de stopbyte

        return packet;
    }

    private volatile byte[] psrFileHandle = null;

    public void uploadStyle(File file, SysexWriter writer) throws Exception {
        logToDisk("=== START GEAVANCEERDE MSD SESSION ===");

        if (!file.exists()) {
            logToDisk("FOUT: Bestand bestaat niet!");
            return;
        }

        int fileSize = (int) file.length();
        byte[] fileBytes = new byte[fileSize];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileBytes);
        }
        logToDisk("Bestand binair ingelezen. Grootte: " + fileSize + " bytes.");

        // 1. Doe de 7-bit codering vooraf en bereken de ENCODED size bytes (392 bytes -> 0x01 0x88)
        byte[] encodedBytes = encode7to8Yamaha(fileBytes);
        int encodedSize = encodedBytes.length;
        byte sizeHex2 = (byte) ((encodedSize >> 8) & 0xFF); // 0x01
        byte sizeHex3 = (byte) (encodedSize & 0xFF);        // 0x88
        logToDisk("7-bit codering voltooid. Gecodeerde grootte: " + encodedSize + " bytes.");

        // =========================================================================
        // LOKALE HARDWARE-BYPASS: Zoek direct naar Port 2 (Digital Keyboard-2)
        // =========================================================================
        logToDisk("START: Output device handmatig scannen voor Port 2 Bulk");
        MidiDevice outputDevice = null;

        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            try {
                MidiDevice dev = MidiSystem.getMidiDevice(info);
                String portName = info.getName().toLowerCase();

                // We zoeken strictly naar 'digital keyboard' én het cijfer '2'
                if (portName.contains("digital keyboard") && portName.contains("2") && dev.getMaxReceivers() != 0) {
                    outputDevice = dev;
                    logToDisk("HARDWARE MATCH: Direct verbonden met Port 2: " + info.getName());
                    break;
                }
            } catch (MidiUnavailableException e) {
                logToDisk("Fout tijdens hardware poortinspectie: " + e.getMessage());
            }
        }

        // Ultieme veiligheids-fallback (pakt het standaardapparaat als de '2' niet wordt gevonden)
        if (outputDevice == null) {
            logToDisk("WAARSCHUWING: Port 2 niet direct gevonden via scan. Fallback naar standaard device.");
            outputDevice = ApplicationInfo.getInstance().getMidiOutputDevice();
        }

        // Open het apparaat op hardware-niveau
        if (!outputDevice.isOpen()) {
            logToDisk("Fysieke output poort openen...");
            outputDevice.open();
        }

        TrackedReceiver centralReceiver = null;
        try {
            centralReceiver = new TrackedReceiver(outputDevice);
        } catch (Exception e) {
            logToDisk("FATALE EXCEPTION BIJ AANMAAK RECEIVER: " + e.getMessage());
            return;
        }

        String activeDeviceName = centralReceiver.getDeviceName();
        logToDisk("GEFORCEERDE RUN SUCCESVOL GEKOPPELD AAN: " + activeDeviceName);

        // =========================================================================
        // INITIALISATIE: Dwing de Steinberg-driver in de actieve Schrijfstand
        // =========================================================================
        // =========================================================================
        // INITIALISATIE: MSD-Handshake mét Geforceerde Hardware-Pauzes
        // =========================================================================
        logToDisk("Initialiseren: Universal Device Identity Request afvuren");
        byte[] universalIdentityRequest = { (byte)0xF0, 0x7E, 0x7F, 0x06, 0x01, (byte)0xF7 };
        writer.sendSysex(centralReceiver, outputDevice, universalIdentityRequest);
        Thread.sleep(400); // Geef de USB-bus de tijd om de identiteitsvraag te verwerken

        logToDisk("Initialiseren: VAM Manager openen");
        byte[] msdOpenVam = { (byte)0xF0, 0x43, 0x50, 0x00, 0x00, 0x00, 0x02, 0x01, 0x02, (byte)0xF7 };
        writer.sendSysex(centralReceiver, outputDevice, msdOpenVam);
        Thread.sleep(400); // Wacht tot de VAM-omgeving stabiel is geladen

        logToDisk("Initialiseren: 60-byte Security Key injecteren");
        byte[] msdSecurityKey = {
                (byte)0xF0, 0x43, 0x50, 0x00, 0x00, 0x02, 0x02, 0x33, 0x00, 0x01, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x1B,
                0x78, 0x17, 0x3F, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x7F, 0x00, 0x00, 0x00, 0x32, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x27, 0x08, 0x00, 0x00, 0x02, 0x00, 0x04, 0x7F,
                0x7F, 0x7F, 0x7F, (byte)0xF7
        };
        writer.sendSysex(centralReceiver, outputDevice, msdSecurityKey);
        Thread.sleep(500); // CRUCIALE PAUZE: Het keyboard verifieert nu de sleutel in het RAM!

        logToDisk("Initialiseren: Handshake Bevestiging 1");
        byte[] msdConfirm1 = { (byte)0xF0, 0x43, 0x50, 0x00, 0x00, 0x01, 0x02, 0x00, (byte)0xF7 };
        writer.sendSysex(centralReceiver, outputDevice, msdConfirm1);
        Thread.sleep(400);

        logToDisk("Initialiseren: Handshake Bevestiging 2");
        byte[] msdConfirm2 = { (byte)0xF0, 0x43, 0x50, 0x00, 0x00, 0x01, 0x02, 0x01, (byte)0xF7 };
        writer.sendSysex(centralReceiver, outputDevice, msdConfirm2);

        // =========================================================================
        // HIER SCHAKELT DE PSR NU IN VOLLEDIGE RUST OOM NAAR FILE TRANSFER MODE
        // =========================================================================
        System.out.println("Wachten tot het keyboard grafisch omschakelt naar Transfer Mode...");
        logToDisk("Geforceerde rustpauze voor hardware-initialisatie van het scherm");
        Thread.sleep(2000); // 2 full seconden rust, exact de tijd die MSD nodig heeft om de boom te laden!

        logToDisk("USER Drive selecteren");
        byte[] msdSelectUser = { (byte)0xF0, 0x43, 0x50, 0x00, 0x05, 0x0B, 0x01, 0x42, 0x00, 0x00, 0x05, 0x00, 0x55, 0x53, 0x45, 0x52, (byte)0xF7 };
        writer.sendSysex(centralReceiver, outputDevice, msdSelectUser);
        Thread.sleep(400);

        logToDisk("USER Drive selecteren");
        byte[] msdSelectUser2 = { (byte)0xF0, 0x43, 0x50, 0x00, 0x05, 0x0B, 0x01, 0x42, 0x00, 0x00, 0x05, 0x00, 0x55, 0x53, 0x45, 0x52, (byte)0xF7 };
        writer.sendSysex(centralReceiver, outputDevice, msdSelectUser2);
        Thread.sleep(200);

        // =========================================================================
        // STAP 3: File Creation Header (Gecorrigeerd naar pure MSD Pad-Syntaxis)
        // =========================================================================
        logToDisk("STAP 3: File Header schrijven");
        byte[] createCmd = { 0x00, 0x05, 0x05 };
        byte[] timestampBytes = "2020 1 1 0 0 0".getBytes(StandardCharsets.US_ASCII);

        // Pure, zuivere MSD-opbouw die je in MusicSoft Downloader hebt gezien!
        String targetPathOnKeyboard = "USER:STYLE/aaa.sty";
        byte[] nameBytes = (targetPathOnKeyboard + "\0").getBytes(StandardCharsets.US_ASCII);
        byte nameLen = (byte) (nameBytes.length & 0xFF); // Wordt exact 19 bytes (0x13)

        int headerPayloadLen = 2 + timestampBytes.length + 7 + nameBytes.length;
        byte[] headerPayload = new byte[headerPayloadLen];
        int hIdx = 0;
        headerPayload[hIdx++] = 0x01;
        headerPayload[hIdx++] = 0x00;
        System.arraycopy(timestampBytes, 0, headerPayload, hIdx, timestampBytes.length);
        hIdx += timestampBytes.length;
        headerPayload[hIdx++] = 0x03;
        headerPayload[hIdx++] = 0x01;
        headerPayload[hIdx++] = sizeHex2; // 0x01
        headerPayload[hIdx++] = sizeHex3; // 0x88
        headerPayload[hIdx++] = 0x00;
        headerPayload[hIdx++] = nameLen;  // Wordt nu netjes 0x13
        headerPayload[hIdx++] = 0x00;
        System.arraycopy(nameBytes, 0, headerPayload, hIdx, nameBytes.length);

        writer.sendSysex(centralReceiver, outputDevice, buildSysExPacket(createCmd, headerPayload));
        Thread.sleep(250);

        // STAP 4: Streaming (392 bytes verdeeld over 128-byte chunks)
        byte[] streamCmd = { 0x00, 0x05, 0x06 };
        int chunkSize = 128;
        int packetCount = 0;
        int bytesSent = 0;

        logToDisk("STAP 4: Start streaming chunks...");
        while (bytesSent < encodedSize) {
            int currentChunkSize = Math.min(chunkSize, encodedSize - bytesSent);
            byte[] chunkPayload = new byte[6 + currentChunkSize];
            chunkPayload[0] = (byte) 0x01;
            chunkPayload[1] = (byte) 0x02;
            chunkPayload[2] = (byte) ((packetCount >> 7) & 0x7F);
            chunkPayload[3] = (byte) (packetCount & 0x7F);
            chunkPayload[4] = (byte) ((currentChunkSize >> 7) & 0x7F);
            chunkPayload[5] = (byte) (currentChunkSize & 0x7F);
            for (int i = 0; i < currentChunkSize; i++) {
                chunkPayload[6 + i] = encodedBytes[bytesSent + i];
            }
            writer.sendSysex(centralReceiver, outputDevice, buildSysExPacket(streamCmd, chunkPayload));
            bytesSent += currentChunkSize;
            packetCount++;
            Thread.sleep(150);
        }

        // =========================================================================
        // STAP 5: Bestandsbuffer sluiten op het keyboard (Laat deze exact zo staan!)
        // =========================================================================
        logToDisk("STAP 5: Bestandsbuffer sluiten op het keyboard");
        byte[] closeCmd = { 0x00, 0x05, 0x07 };
        byte[] closePayload = { 0x02, sizeHex2, sizeHex3 };
        writer.sendSysex(centralReceiver, outputDevice, buildSysExPacket(closeCmd, closePayload));
        Thread.sleep(400);

        // =========================================================================
        // NIEUW: STAP 5B - EXECUTE FLASH WRITE (Het officiële kopieer-bevel!)
        // =========================================================================
        logToDisk("STAP 5B: PSR de opdracht geven om de file DEFINITIEF te kopiëren naar flash");
        byte[] executeWriteCmd = { 0x00, 0x05, 0x08 };

        // Payload 0x02 geeft aan dat de staging-file naar de actieve USER-drive gemigreerd moet worden
        byte[] executePayload = { 0x02 };
        writer.sendSysex(centralReceiver, outputDevice, buildSysExPacket(executeWriteCmd, executePayload));

        logToDisk("Wachten tot de PSR klaar is met fysiek kopiëren naar flash...");
        Thread.sleep(1500); // Ruime pauze zodat de controller de FAT-tabel kan beschrijven!

        // =========================================================================
        // AFSLUITING: VAM-sessie beëindigen (Dit sluit de binaire poort-enveloppe)
        // =========================================================================
        logToDisk("AFSLUITING: VAM-sessie beëindigen");
        byte[] msdCloseVam = { (byte)0xF0, 0x43, 0x50, 0x00, 0x00, 0x01, 0x02, 0x00, (byte)0xF7 };
        writer.sendSysex(centralReceiver, outputDevice, msdCloseVam);
        Thread.sleep(600);

        // Poorten handmatig sluiten
        logToDisk("FINISH: USB-MIDI drivers flushen en poorten sluiten");
        centralReceiver.close();
        outputDevice.close();
        logToDisk("TRANSMISSIE EN FLUSH VOLLEDIG MET SUCCES AFGEROND!");    }

    /**
     * Converteert binaire 8-bit bytes naar Yamaha's 7-bit MIDI-safe formaat (7-to-8 packing).
     * Elke 7 originele bytes worden omgezet in 8 veilige MIDI-bytes met een controlebyte.
     */
    private static byte[] encode7to8Yamaha(byte[] source) {
        int sourceLen = source.length;
        int groups = (sourceLen + 6) / 7;
        byte[] target = new byte[groups * 8];

        int srcIdx = 0;
        int dstIdx = 0;

        while (srcIdx < sourceLen) {
            int chunkLen = Math.min(7, sourceLen - srcIdx);
            int controlByteIdx = dstIdx++; // Reserveer plek voor de controlebyte
            byte controlByte = 0;

            for (int i = 0; i < chunkLen; i++) {
                byte b = source[srcIdx + i];
                // Als bit 7 (MSB) actief is, zetten we die om naar de controlebyte
                if ((b & 0x80) != 0) {
                    controlByte |= (1 << i);
                }
                // Sla de databyte op ZONDER bit 7 (altijd safe onder 0x7F)
                target[dstIdx++] = (byte) (b & 0x7F);
            }

            // Vul eventuele lege plekken op met 0 als de file niet deelbaar is door 7
            for (int i = chunkLen; i < 7; i++) {
                target[dstIdx++] = 0;
            }

            // Plaats de samengestelde controlebyte vóór de 7 databytes
            target[controlByteIdx] = controlByte;
            srcIdx += chunkLen;
        }

        return target;
    }

    private Receiver addListener() {
// ==========================================
// NIEUW: MIDI-IN LUISTERAAR OPSTARTEN
// ==========================================
        MidiDevice inputDevice = null;
        Receiver javaMidiInReceiver = null;

// We proberen het actieve invoerapparaat op te halen (zorg dat dit gekoppeld is in je app)
// Dit bootst de manier na waarop je ook het outputDevice ophaalt.
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            try {
                MidiDevice dev = MidiSystem.getMidiDevice(info);
                // Zoek naar het apparaat dat "Digital Keyboard" of jouw PSR-naam bevat en invoerpoorten heeft
                if (info.getName().contains("Digital Keyboard") && info.getName().contains("2") && dev.getMaxTransmitters() != 0) {
                    inputDevice = dev;
                    break;
                }
            } catch (MidiUnavailableException e) {
                System.err.println("Kon MIDI-In apparaat niet inspecteren: " + e.getMessage());
            }
        }

        if (inputDevice != null) {
            try {
                if (!inputDevice.isOpen()) {
                    inputDevice.open();
                }

                // Maak een custom receiver die elk binnenkomend bericht van de PSR opvangt
                javaMidiInReceiver = new Receiver() {
                    @Override
                    public void send(MidiMessage message, long timeStamp) {
                        if (message instanceof SysexMessage) {
                            SysexMessage sxMsg = (SysexMessage) message;
                            byte[] incomingBytes = sxMsg.getData();

                            // Print de reactie van het keyboard direct in hexvorm in je console!
                            System.out.println("[< INBOUND VAN PSR]: " +
                                    SysexToHexStringConvertor.convertToHexString(incomingBytes));
                        }
                    }

                    @Override
                    public void close() {
                        // Sluiting afhandelen indien nodig
                    }
                };

                // Koppel onze luisteraar aan de zender (Transmitter) van de PSR-SX600
                inputDevice.getTransmitter().setReceiver(javaMidiInReceiver);
                System.out.println("MIDI-In Luisteraar succesvol actief. Poort is open.");

            } catch (MidiUnavailableException e) {
                System.err.println("Fout bij het openen van de MIDI-In poort: " + e.getMessage());
            }
        } else {
            System.err.println("WAARSCHUWING: Geen geldig MIDI-In apparaat gevonden. We luisteren blind.");
        }

        return javaMidiInReceiver;
    }

    private MidiDevice connectToPort2() {
        MidiDevice outputDevice = null;

// Loop door de apparaten om expliciet poort 2 (de bulk-poort) te selecteren!
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            try {
                MidiDevice dev = MidiSystem.getMidiDevice(info);
                // Zoek naar "Digital Keyboard" en zorg dat het de poort voor BESTANDSOVERDRACHT (Port 2) is
                if (info.getName().contains("Digital Keyboard") && info.getName().contains("2") && dev.getMaxReceivers() != 0) {
                    outputDevice = dev;
                    System.out.println("Succesvol verbonden met BULK-POORT: " + info.getName());
                    break;
                }
            } catch (MidiUnavailableException e) {
                System.err.println("Fout bij poortinspectie: " + e.getMessage());
            }
        }

        return outputDevice;
    }

    private static void logToDisk(String message) {
        try (FileWriter fw = new FileWriter("transfer_debug.log", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(new java.util.Date() + " - " + message);
            bw.flush(); // Garandeer dat de bytes fysiek op de harde schijf staan!
        } catch (IOException e) {
            System.err.println("Fout bij schrijven naar logbestand: " + e.getMessage());
        }
    }
}

package vendor.yamaha;

import creative.scenes.sysex.convertor.SysexToHexStringConvertor;
import creative.scenes.sysex.util.SysexWriter;
import util.ApplicationInfo;

import javax.sound.midi.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class YamahaStyleUploader {
    private SysexWriter writer = new SysexWriter();

    // Unique Sub-ID for Yamaha File Protocol (F0 43 50 ...)
    private static final byte[] YAMAHA_HEADER = { (byte)0x43, (byte)0x50, (byte)0x00 };

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

    public void uploadStyle(File file) throws IOException, InterruptedException {
        if (!file.exists()) {
            System.err.println("Error: File not found: " + file.getName());
            return;
        }

        String fileName = file.getName();
        int fileSize = (int) file.length();

        byte[] fileBytes = new byte[fileSize];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileBytes);
        }

        System.out.println("Starting transfer for: " + fileName + " (" + fileSize + " bytes)...");

        // =========================================================================
        // DIRECTE MIDI SELECTOR (Focust uitsluitend op Digital Keyboard-1)
        // =========================================================================
        MidiDevice outputDevice = ApplicationInfo.getInstance().getMidiOutputDevice();
        Receiver javaMidiInReceiver = addListener();

        // 2. Doe de 7-bit codering vooraf
        byte[] encodedBytes = encode7to8Yamaha(fileBytes);
        int encodedSize = encodedBytes.length;

        // STAP 1: Open VAM Manager
        byte[] vamOpenCmd = { 0x00, 0x00, 0x00 };
        byte[] vamOpenPayload = { 0x02, 0x01, 0x02 };
        writer.sendSysex(outputDevice, buildSysExPacket(vamOpenCmd, vamOpenPayload));
        Thread.sleep(150);

        // STAP 2: Device Security Key
        byte[] securityCmd = { 0x00, 0x00, 0x02 };
        byte[] securityPayload = {
                0x02, 0x33, 0x00, 0x01, 0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, (byte) 0x1B,
                0x78, 0x17, 0x3F, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x7F,
                0x00, 0x00, 0x00, 0x32, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x27, 0x08, 0x00, 0x00, 0x02, 0x00, 0x04, 0x7F,
                0x7F, 0x7F, 0x7F
        };
        writer.sendSysex(outputDevice, buildSysExPacket(securityCmd, securityPayload));
        Thread.sleep(150);

        // STAP 2B: Selecteer de USER drive
        byte[] selectDriveCmd = { 0x00, 0x05, 0x0B };
        byte[] userDrivePayload = { 0x01, 0x42, 0x00, 0x00, 0x05, 0x00, 0x55, 0x53, 0x45, 0x52 }; // USER
        writer.sendSysex(outputDevice, buildSysExPacket(selectDriveCmd, userDrivePayload));
        Thread.sleep(200);

        // =========================================================================
        // VRAAG DIRECTORY OP: Dwingt de PSR om de actieve map-inhoud terug te sturen!
        // =========================================================================
        System.out.println("Huidige map-inhoud opvragen van de PSR...");
        byte[] openDirCmd = { 0x00, 0x05, 0x04 };
        byte[] openDirPayload = { 0x01, 0x04, 0x08, 0x2B, 0x68, 0x00 };
        writer.sendSysex(outputDevice, buildSysExPacket(openDirCmd, openDirPayload));
        Thread.sleep(400); // Iets langere pauze zodat de PSR de map-inhoud kan terugsturen naar de console!

        // STAP 3: File Header met pure bestandsnaam
        byte[] createCmd = { 0x00, 0x05, 0x05 };
        byte[] timestampBytes = "2020 1 1 0 0 0".getBytes(StandardCharsets.US_ASCII);
        String targetPathOnKeyboard = "aaa.sty";
        byte[] nameBytes = (targetPathOnKeyboard + "\0").getBytes(StandardCharsets.US_ASCII);

        byte sizeHex2 = (byte) ((fileSize >> 8) & 0xFF);
        byte sizeHex3 = (byte) (fileSize & 0xFF);
        byte nameLen = (byte) (nameBytes.length & 0xFF);

        int headerPayloadLen = 2 + timestampBytes.length + 7 + nameBytes.length;
        byte[] headerPayload = new byte[headerPayloadLen];
        int hIdx = 0;
        headerPayload[hIdx++] = 0x01;
        headerPayload[hIdx++] = 0x00;
        System.arraycopy(timestampBytes, 0, headerPayload, hIdx, timestampBytes.length);
        hIdx += timestampBytes.length;
        headerPayload[hIdx++] = 0x03;
        headerPayload[hIdx++] = 0x01;
        headerPayload[hIdx++] = sizeHex2;
        headerPayload[hIdx++] = sizeHex3;
        headerPayload[hIdx++] = 0x00;
        headerPayload[hIdx++] = nameLen;
        headerPayload[hIdx++] = 0x00;
        System.arraycopy(nameBytes, 0, headerPayload, hIdx, nameBytes.length);

        writer.sendSysex(outputDevice, buildSysExPacket(createCmd, headerPayload));
        Thread.sleep(200);

        // STAP 4: Streaming
        byte[] streamCmd = { 0x00, 0x05, 0x06 };
        int chunkSize = 128;
        int packetCount = 0;
        int bytesSent = 0;

        System.out.println("Start gecodeerde 7-bit streaming...");
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
            writer.sendSysex(outputDevice, buildSysExPacket(streamCmd, chunkPayload));
            bytesSent += currentChunkSize;
            packetCount++;
            Thread.sleep(150);
        }

        System.out.println("Alle chunks verwerkt! Totaal aantal pakketten: " + packetCount);
        Thread.sleep(500);

        // STAP 5: Sluitings-handshake
        byte[] closeCmd = { 0x00, 0x05, 0x07 };
        byte[] closePayload = { 0x02, sizeHex2, sizeHex3 };
        writer.sendSysex(outputDevice, buildSysExPacket(closeCmd, closePayload));
        Thread.sleep(400);

        // =========================================================================
        // FIX: DE HARDE POORTSLUITING (Vergrendelt het bestand definitief!)
        // =========================================================================
        System.out.println("Midi-poorten fysiek flushen en sluiten...");
        try {
            if (outputDevice != null && outputDevice.isOpen()) {
                outputDevice.getReceiver().close(); // Sluit de receiver-link
                outputDevice.close();              // Sluit de fysieke USB-MIDI poort 2!
                System.out.println("Fysieke output poort 2 gesloten.");
            }
            if (javaMidiInReceiver != null) javaMidiInReceiver.close();
            if (outputDevice != null && outputDevice.isOpen()) {
                outputDevice.close(); // Sluit de fysieke USB-MIDI invoerpoort 2!
                System.out.println("Fysieke input poort 2 gesloten.");
            }
        } catch (Exception e) {
            System.err.println("Fout tijdens het sluiten van de poorten: " + e.getMessage());
        }

        System.out.println("Transmissie en opslag volledig voltooid!");
    }

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
}

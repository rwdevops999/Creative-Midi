package vendor.yamaha;

import creative.scenes.sysex.util.SysexWriter;
import util.ApplicationInfo;

import javax.sound.midi.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
/**
 * Experimental PSR-SX600 USER:\\STYLE client.
 * Listing and upload protocol reconstructed from one successful MIDI-OX capture.
 * Test with disposable files; not an official Yamaha implementation.
 * Do not invoke these blocking methods on the JavaFX Application Thread.
 */
public final class YamahaStyleUploader implements AutoCloseable {
    private final MidiDevice output, input;
    private final Receiver sender;
    private final YamahaTransfer transfer;

    //    private final List<byte[]> responses = new LinkedList<>();
    private final List<byte[]> responses = new CopyOnWriteArrayList<>();
    private final BlockingQueue<byte[]> midiHandshake = new LinkedBlockingQueue<>();

    private final Transmitter incoming;
    private final int timeoutMs;

    public YamahaStyleUploader() throws MidiUnavailableException {
        this("Digital Keyboard-1", 5000);
    }

    private static String createUniqueStyleName(Path styleFile) {
        String filename = styleFile.getFileName().toString();

        int dot = filename.lastIndexOf('.');
        String baseName = dot > 0
                ? filename.substring(0, dot)
                : filename;

        // Keep the filename compatible with our ASCII encoder.
        baseName = baseName.replaceAll("[^A-Za-z0-9_-]", "_");

        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyMMdd_HHmmss")
        );

        return baseName + "_" + timestamp + ".sty";
    }

    private static String asHex(byte[] data) {

        if (data == null) {
            return "NULL";
        }

        StringBuilder sb =
                new StringBuilder();

        for (byte b : data) {
            sb.append(
                    String.format(
                            "%02X ",
                            b & 0xFF
                    )
            );
        }

        return sb.toString().trim();
    }

    public YamahaStyleUploader(String deviceName, int timeoutMs) throws MidiUnavailableException {
        this.timeoutMs = timeoutMs;
        MidiDevice out = null, in = null;
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            MidiDevice d = MidiSystem.getMidiDevice(info);
            if (!info.getName().contains(deviceName)) continue;
            if (d.getMaxReceivers() != 0 && out == null) out = d;
            if (d.getMaxTransmitters() != 0 && in == null) in = d;
        }
        out = ApplicationInfo.getInstance().getMidiOutputDevice();

        if (out == null || in == null) throw new MidiUnavailableException("Missing MIDI input/output for " + deviceName);

        output = out; input = in;

        if (! output.isOpen()) {
            output.open();
        }
        try { input.open(); }
        catch (MidiUnavailableException ex) { output.close(); throw ex; }
        sender = output.getReceiver();
        transfer = new YamahaTransfer(sender);

        incoming = input.getTransmitter();
        incoming.setReceiver(new Receiver() {
            public void send(MidiMessage msg, long stamp) {
                if (msg instanceof SysexMessage) {
                    onBytesReceived(msg.getMessage().clone());
                }
            }
            public void close() { }
        });
    }

    public void onBytesReceived(byte[] rawResponse) {
        // poll() of offer() zorgt voor de directe overdracht naar de wachtende thread
        System.out.println("RECEIVED FROM PSR: " + asHex(rawResponse));
        boolean accepted = midiHandshake.offer(rawResponse);
        if (!accepted) {
            System.out.println("Geen thread die momenteel op deze bytes wacht. Rommel genegeerd.");
        }
    }

    public byte[] sendMessageAndWait(String s, long timeout, boolean sendFirst) {
        try {
            if (sendFirst) {
                send(hex(s));
            }

            // 3. HIER BLOKKEERT DE THREAD: We wachten maximaal 3 seconden.
            // Omdat de listener de array maakt, maakt de variabele lengte hier niks uit!
            byte[] responseBytes = midiHandshake.poll(timeout, TimeUnit.MILLISECONDS);

            if (responseBytes != null) {
                // 4. Voeg de ontvangen response toe aan de interne lijst (binnen dezelfde thread)
                responses.add(responseBytes);

                // 5. Geef de byte-array terug als resultaat van de functie
                return responseBytes;
            } else {
                System.err.println("[SendThread] Timeout: MIDI-apparaat reageerde niet binnen de tijd.");
            }

        } catch (InvalidMidiDataException e) {
            System.err.println("Fout in MIDI data formaat: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread werd onderbroken tijdens het wachten.");
        }

        return null; // Retourneer null als er een timeout of fout optreedt
    }
    private static byte[] hex(String s) {
        s = s.replaceAll("\\s", "");
        byte[] b = new byte[s.length()/2];
        for (int i=0;i<b.length;i++) b[i]=(byte)Integer.parseInt(s.substring(2*i,2*i+2),16);
        return b;
    }
    private void send(byte[] bytes) throws InvalidMidiDataException {
        transfer.sendSysex(bytes);
/*        SysexMessage m = new SysexMessage();
        m.setMessage(bytes, bytes.length);
        sender.send(m, -1); */
    }
    private void send(String s) throws InvalidMidiDataException { send(hex(s)); }
    private static boolean starts(byte[] b, int... prefix) {
        if (b.length < prefix.length) return false;
        for (int i=0;i<prefix.length;i++) if ((b[i]&255)!=prefix[i]) return false;
        return true;
    }
    public static boolean startsWith(byte[] array, byte[] prefix) {
        if (array == null || prefix == null) {
            return false;
        }
        if (array.length < prefix.length) {
            return false;
        }
        // Compares array[0...prefix.length] with prefix[0...prefix.length]
        return Arrays.equals(array, 0, prefix.length, prefix, 0, prefix.length);
    }
    private byte[] sendAndWaitForResponse(
            String s,
            byte[] expectedPrefix,
            long timeoutMs) throws Exception {

        long deadline = System.nanoTime()
                + TimeUnit.MILLISECONDS.toNanos(timeoutMs);

        // Remove stale responses before sending a new command.
        midiHandshake.clear();

        send(hex(s));

        while (true) {
            long remaining = deadline - System.nanoTime();

            if (remaining <= 0) {
                System.err.println(
                        "Timeout waiting for: " + asHex(expectedPrefix));
                return null;
            }

            byte[] response = midiHandshake.poll(
                    remaining, TimeUnit.NANOSECONDS);

            if (response == null) {
                System.err.println(
                        "Timeout waiting for: " + asHex(expectedPrefix));
                return null;
            }

            System.out.println("Queue received: " + asHex(response));

            responses.add(response);

            if (startsWith(response, expectedPrefix)) {
                return response;
            }

            System.out.println(
                    "Ignoring unexpected response: " + asHex(response));
        }
    }

    private byte[] waitForResponse(
            byte[] expectedPrefix,
            long timeoutMs) throws InterruptedException {

        long deadline = System.nanoTime()
                + TimeUnit.MILLISECONDS.toNanos(timeoutMs);

        while (true) {
            long remaining = deadline - System.nanoTime();

            if (remaining <= 0) {
                System.err.println(
                        "Timeout waiting for: " + asHex(expectedPrefix));
                return null;
            }

            byte[] response = midiHandshake.poll(
                    remaining, TimeUnit.NANOSECONDS);

            if (response == null) {
                System.err.println(
                        "Timeout waiting for: " + asHex(expectedPrefix));
                return null;
            }

            System.out.println("Queue received: " + asHex(response));

            responses.add(response);

            if (startsWith(response, expectedPrefix)) {
                return response;
            }

            System.out.println(
                    "Ignoring unexpected response: " + asHex(response));
        }
    }

    private void begin() throws Exception {
        responses.clear();
        System.out.println("BEGIN");
//        send("F0 43 50 00 00 00 01 F7");
//        sendBytesSequential("F0 43 50 00 00 00 01 F7", 1000);
        byte[] expected1 = {
                (byte) 0xF0,
                0x43, 0x50, 0x00,
                0x00, 0x00, 0x02, 0X01, 0X02
        };
        sendAndWaitForResponse("F0 43 50 00 00 00 01 F7", expected1, 1000);
//        send("F0 43 50 00 00 01 01 F7");
/*        byte[] expected2 = {
                (byte) 0xF0,
                0x43, 0x50, 0x00,
                0x00, 0x01, 0x02
        };
        sendAndWaitForResponse("F0 43 50 00 00 01 01 F7", expected2, 1000);
//        waitForResponse(expected2, 1000);
*/
        // MS2S queries a larger device/session information record here.
        byte[] expected2 = {
                (byte) 0xF0,
                0x43, 0x50, 0x00,
                0x00, 0x00, 0x02, 0X02
        };
        sendAndWaitForResponse("F0 43 50 00 00 02 01 F7", expected2, 1000);

        byte[] expected3 = {
                (byte) 0xF0,0x43,0x50,0x00,0x00,0x01,0x02,0x00
        };
        sendAndWaitForResponse("F0 43 50 00 00 01 01 F7", expected3, 1000);

        byte[] expected4 = {
                (byte) 0xF0,0x43,0x50,0x00,0x00,0x01,0x02,0x01
        };
        sendAndWaitForResponse("F0 43 50 00 00 01 00 01 F7", expected4, 1000);
    }
    private void end() throws Exception {
        System.out.println("ENDING TRANSFER MODE");

        byte[] expected = {
                (byte) 0xF0,
                0x43, 0x50, 0x00, 0x00,
                0x01, 0x02, 0x00
        };

        long start = System.currentTimeMillis();

        sendAndWaitForResponse(
                "F0 43 50 00 00 01 00 00 F7",
                expected,
                10000
        );

        System.out.println(
                "END WAIT RETURNED after " +
                        (System.currentTimeMillis() - start) + " ms"
        );
    }

    private void driveAndIdentity() throws Exception {
        System.out.println("IDENTIFY");
//        send("F0 43 50 00 05 0B 00 00 F7");
        byte[] expected = {
                (byte) 0xF0,
                0x43, 0x50, 0x00,
                0x05, 0x0B, 0x01
        };
        // TRANSFERT END MODE
//        sendAndWaitForResponse("F0 43 50 00 00 01 00 00 F7", expected, 1000);

        sendAndWaitForResponse("F0 43 50 00 05 0B 00 01 F7", expected, 1000);
//        send("F0 43 50 00 00 07 01 F7");
        byte[] expected2 = {
                (byte) 0xF0,
                0x43, 0x50, 0x00,
                0x00, 0x07, 0x02
        };

        sendAndWaitForResponse("F0 43 50 00 00 07 01 F7", expected2, 1000);
//        waitForResponse(expected2, 1000);
    }
    /** List filenames (not folders) from USER:\\STYLE. */
    public synchronized List<String> getStyleFiles() throws Exception {
        end();
        begin();
        try {
            driveAndIdentity();
            // The captured directory query is for drive 1, path \\STYLE, mask *.*.
//            send("F0 43 50 00 05 04 00 3F 00 0F 00 31 3A 5C 53 54 59 4C 00 45 5C 2A 2E 2A 00 F7");
            byte[] expected1 = {
                    (byte) 0xF0,
                    0x43, 0x50, 0x00,
                    0x05, 0x04, 0x01
            };

//            byte[] first = waitForResponse(expected1, 1000);
            byte[] first = sendAndWaitForResponse("F0 43 50 00 05 04 00 3F 00 0F 00 31 3A 5C 53 54 59 4C 00 45 5C 2A 2E 2A 00 F7", expected1, 10000);
            Set<String> names = new LinkedHashSet<>();
            collectName(first,names);
            for (int i=0;i<10000;i++) {
//                send("F0 43 50 00 05 05 00 F7");
                byte[] expected2 = {
                        (byte) 0xF0,
                        0x43, 0x50, 0x00,
                        0x05
                };
                byte[] b = sendAndWaitForResponse("F0 43 50 00 05 05 00 F7", expected2, 1000);
//                byte[] b = waitForResponse(expected2, 1000);
                if (starts(b,0xF0,0x43,0x50,0x00,0x05,0x7F)) break;
                if (!starts(b,0xF0,0x43,0x50,0x00,0x05,0x05,0x01))
                    throw new IOException("Unexpected directory response");
                collectName(b,names);
            }
            return new ArrayList<>(names);
        } finally { try { end(); } catch (Exception ignored) {} }
    }
    private static void collectName(byte[] packet, Set<String> names) {
        if (packet == null || packet.length < 30) {
            return;
        }

        StringBuilder filename = new StringBuilder();

        for (int i = 28; i < packet.length - 1; i++) {
            int value = packet[i] & 0xFF;

            if (value == 0) {
                continue;
            }

            if (value >= 32 && value <= 126) {
                filename.append((char) value);
            }
        }

        String result = filename.toString().trim();

        // Temporary workaround for the observed Yamaha encoding.
        result = result.replace("Caf@i.sty", "Café.sty");

        if (result.toLowerCase(Locale.ROOT).endsWith(".sty")) {
            names.add(result);
        }
    }

    /**
     * Upload a style file. EXPERIMENTAL: 7-bit packing and flow-control are
     * inferred from a successful capture, but not yet checked against the
     * original WRAA.sty. Disabled by default to prevent corrupted uploads.
     */
    public synchronized void upload(Path styleFile) throws Exception {
        String original = styleFile.getFileName().toString();
        if (!original.toLowerCase(Locale.ROOT).endsWith(".sty"))
            throw new IllegalArgumentException("Expected .sty file: " + original);
        String stem = original.substring(0, original.length() - 4);
        // ASCII-only destination required by the verified packet builder.
        String destinationName = createUniqueStyleName(styleFile);

        System.out.println("Destination filename: " + stem + ".sty");

        upload(styleFile, stem + ".sty");
    }

    /** Upload to an explicitly named NEW USER:\\STYLE file. Never overwrites. */
    public synchronized void upload(Path styleFile, String destinationName) throws Exception {
        if (!Files.isRegularFile(styleFile))
            throw new IOException("Not a regular file: " + styleFile);
        byte[] data = Files.readAllBytes(styleFile);
        // Prebuild and validate EVERYTHING before entering transfer mode.
        List<byte[]> packets = YamahaStyleUploadPackets.buildDataPackets(data);
//        byte[] create = YamahaStyleUploadPackets.buildCreateRequest(destinationName);
        byte[] setup = YamahaStyleUploadPackets.buildTransferRequest(destinationName, data.length);

        // Preflight listing is a separate session, using your working code.
/*        for (String existing : getStyleFiles()) {
            if (existing.equalsIgnoreCase(destinationName))
                throw new java.nio.file.FileAlreadyExistsException(
                        "Keyboard USER:\\STYLE\\" + destinationName);
        }
*/
        boolean started = false;
        try {
            begin();
            started = true;
            driveAndIdentity();
            System.out.println("STORAGE PREPARATION");

            byte[] storageResponse = sendMessageAndWait(
                    "F0 43 50 00 05 06 00 01 F7",
                    2000, true
            );

            System.out.println("Storage response: " +
                    (storageResponse == null
                            ? "TIMEOUT"
                            : asHex(storageResponse)));

            // Clear unsolicited replies from identity/drive queries, if any.
            midiHandshake.clear();

            // MS2S create-file request. Capture shows 05 7F 01 status,
            // followed by 00 05 02 status messages. We require a positive
            // status before sending any file bytes.
/*          send(create);
            byte[] createReply = awaitOneOf(10000,
                    new byte[] {(byte)0xF0,0x43,0x50,0x00,0x05,0x7F,0x01});
            // The capture's status code is 00 in the first byte after 05 7F 01.
            if (createReply.length < 9 || (createReply[7] & 0xff) != 0)
                throw new IOException("Keyboard rejected file creation: " + asHex(createReply));
*/
            midiHandshake.clear();
            send(setup);
            // The reference capture shows 03 00 as transfer-ready ACK.
            awaitOneOf(10000, new byte[] {(byte)0xF0,0x43,0x50,0x00,0x03,0x00,(byte)0xF7});

            for (int i = 0; i < packets.size(); i++) {
                midiHandshake.clear();
                send(packets.get(i));
                awaitOneOf(10000, new byte[] {(byte)0xF0,0x43,0x50,0x00,0x03,0x00,(byte)0xF7});
                System.out.printf("Style upload: %d/%d packets%n", i + 1, packets.size());
            }

            // Captured end-of-data request, then 03 03 completion indication.
            midiHandshake.clear();

            send(new byte[] {
                    (byte) 0xF0, 0x43, 0x50, 0x00,
                    0x03, 0x02, (byte) 0xF7
            });

// Wait for the actual transfer-completion response.
// Ignore intermediate 00 05 02 notifications.
            byte[] completed = awaitOneOf(
                    30000,
                    new byte[] {
                            (byte) 0xF0, 0x43, 0x50, 0x00,
                            0x03, 0x03
                    }
            );

            System.out.println("Transfer completed: " + asHex(completed));
/*            send("F0 43 50 00 03 02 F7");
            awaitOneOf(15000, new byte[] {(byte)0xF0,0x43,0x50,0x00,0x03,0x03,(byte)0xF7});

            byte[] completionReply = sendAndWaitForResponse(
                    "F0 43 50 00 03 02 F7",
                    new byte[] {
                            (byte) 0xF0, 0x43, 0x50, 0x00,
                            0x03, 0x03, (byte) 0xF7
                    },
                    30000
            );

            if (completionReply == null) {
                throw new IOException(
                        "Timeout waiting for upload completion");
            }
*/

            System.out.println("Upload completion acknowledged: " + destinationName);
        } finally {
            if (started) {
                try { end(); }
                catch (Exception e) { System.err.println("Could not exit transfer mode: " + e); }
            }
        }
    }

    @Override public void close() {
        try { incoming.close(); } catch (Exception ignored) {}
        try { sender.close(); } catch (Exception ignored) {}
        try { input.close(); } catch (Exception ignored) {}
        try { output.close(); } catch (Exception ignored) {}
    }

    private byte[] awaitOneOf(long timeoutMillis, byte[] expected) throws Exception {
        return waitForResponse(expected, timeoutMillis);
    }

    private static final class YamahaTransfer {
        private final Receiver receiver;

        SysexWriter sysexWriter;
        YamahaTransfer(Receiver receiver) {
            this.receiver = receiver;
            this.sysexWriter = new SysexWriter();
        }

        private static String toHex(byte[] data) {

            StringBuilder sb =
                    new StringBuilder();

            for (byte b : data) {
                sb.append(
                        String.format(
                                "%02X ",
                                b & 0xFF
                        )
                );
            }

            return sb.toString().trim();
        }

        public void sendSysex(byte[] message) throws InvalidMidiDataException {
            SysexMessage sysex = new SysexMessage();

            sysex.setMessage(
                    SysexMessage.SYSTEM_EXCLUSIVE,
                    message,
                    message.length
            );

            receiver.send(sysex, -1);

            System.out.println(
                    "SEND TO PSR: " + toHex(message)
            );
        }
    }
}

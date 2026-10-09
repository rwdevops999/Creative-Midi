package vendor.yamaha;

import javax.sound.midi.*;
import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

/**
 * Experimental PSR-SX600 USER:\\STYLE client.
 * Listing and upload protocol reconstructed from one successful MIDI-OX capture.
 * Test with disposable files; not an official Yamaha implementation.
 * Do not invoke these blocking methods on the JavaFX Application Thread.
 */
public final class YamahaStyleUploaderOriginal implements AutoCloseable {
    private final MidiDevice output, input;
    private final Receiver sender;
    private final BlockingQueue<byte[]> responses = new LinkedBlockingQueue<>();
    private final Transmitter incoming;
    private final int timeoutMs;

    public YamahaStyleUploaderOriginal() throws MidiUnavailableException {
        this("Digital Keyboard-1", 5000);
    }

    public YamahaStyleUploaderOriginal(String deviceName, int timeoutMs) throws MidiUnavailableException {
        this.timeoutMs = timeoutMs;
        MidiDevice out = null, in = null;
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            MidiDevice d = MidiSystem.getMidiDevice(info);
            if (!info.getName().contains(deviceName)) continue;
            if (d.getMaxReceivers() != 0 && out == null) out = d;
            if (d.getMaxTransmitters() != 0 && in == null) in = d;
        }
        if (out == null || in == null) throw new MidiUnavailableException("Missing MIDI input/output for " + deviceName);
        output = out; input = in;
        output.open();
        try { input.open(); }
        catch (MidiUnavailableException ex) { output.close(); throw ex; }
        sender = output.getReceiver();
        incoming = input.getTransmitter();
        incoming.setReceiver(new Receiver() {
            public void send(MidiMessage msg, long stamp) {
                if (msg instanceof SysexMessage) responses.offer(msg.getMessage().clone());
            }
            public void close() { }
        });
    }

    private static byte[] hex(String s) {
        s = s.replaceAll("\\s", "");
        byte[] b = new byte[s.length()/2];
        for (int i=0;i<b.length;i++) b[i]=(byte)Integer.parseInt(s.substring(2*i,2*i+2),16);
        return b;
    }
    private void send(byte[] bytes) throws InvalidMidiDataException {
        SysexMessage m = new SysexMessage();
        m.setMessage(bytes, bytes.length);
        sender.send(m, -1);
    }
    private void send(String s) throws InvalidMidiDataException { send(hex(s)); }
    private static boolean starts(byte[] b, int... prefix) {
        if (b.length < prefix.length) return false;
        for (int i=0;i<prefix.length;i++) if ((b[i]&255)!=prefix[i]) return false;
        return true;
    }
    private byte[] waitFor(int... prefix) throws InterruptedException, TimeoutException {
        long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMs);
        while (true) {
            long left = deadline - System.nanoTime();
            if (left <= 0) throw new TimeoutException("No Yamaha reply for " + Arrays.toString(prefix));
            byte[] b = responses.poll(left, TimeUnit.NANOSECONDS);
            if (b == null) throw new TimeoutException("No Yamaha reply for " + Arrays.toString(prefix));
            if (starts(b, prefix)) return b;
        }
    }
    /**
     * Reproduce the initialization order observed in the successful MS2S log.
     * Only request messages are transmitted; response payloads are never echoed.
     */
    private void begin() throws Exception {
        responses.clear();
        send("F0 43 50 00 00 00 01 F7");
        waitFor(0xF0,0x43,0x50,0x00,0x00,0x00,0x02,0x01,0x02);

        // MS2S queries a larger device/session information record here.
        send("F0 43 50 00 00 02 01 F7");
        waitFor(0xF0,0x43,0x50,0x00,0x00,0x02,0x02);

        send("F0 43 50 00 00 01 01 F7");
        waitFor(0xF0,0x43,0x50,0x00,0x00,0x01,0x02,0x00);

        send("F0 43 50 00 00 01 00 01 F7");
        waitFor(0xF0,0x43,0x50,0x00,0x00,0x01,0x02,0x01);
    }
    private void end() throws Exception {
        send("F0 43 50 00 00 01 00 00 F7");
        waitFor(0xF0,0x43,0x50,0x00,0x00,0x01,0x02);
    }
    private void driveAndIdentity() throws Exception {
        send("F0 43 50 00 05 0B 00 00 F7");
        waitFor(0xF0,0x43,0x50,0x00,0x05,0x0B,0x01);
        send("F0 43 50 00 05 0B 00 01 F7");
        waitFor(0xF0,0x43,0x50,0x00,0x05,0x0B,0x01);
        send("F0 43 50 00 00 07 01 F7");
        waitFor(0xF0,0x43,0x50,0x00,0x00,0x07,0x02);
    }
    /** List filenames (not folders) from USER:\\STYLE. */
    public synchronized List<String> getStyleFiles() throws Exception {
        begin();
        try {
            driveAndIdentity();
            // The captured directory query is for drive 1, path \\STYLE, mask *.*.
            send("F0 43 50 00 05 04 00 3F 00 0F 00 31 3A 5C 53 54 59 4C 00 45 5C 2A 2E 2A 00 F7");
            byte[] first = waitFor(0xF0,0x43,0x50,0x00,0x05,0x04,0x01);
            Set<String> names = new LinkedHashSet<>();
            collectName(first,names);
            for (int i=0;i<10000;i++) {
                send("F0 43 50 00 05 05 00 F7");
                byte[] b = waitFor(0xF0,0x43,0x50,0x00,0x05);
                if (starts(b,0xF0,0x43,0x50,0x00,0x05,0x7F)) break;
                if (!starts(b,0xF0,0x43,0x50,0x00,0x05,0x05,0x01))
                    throw new IOException("Unexpected directory response");
                collectName(b,names);
            }
            return new ArrayList<>(names);
        } finally { try { end(); } catch (Exception ignored) {} }
    }
    private static void collectName(byte[] packet, Set<String> names) {
        // Yamaha directory entries insert 00 separators within filename text.
        // Scan printable tail for a .sty name; format is capture-derived.
        if (packet.length < 20) return;
        StringBuilder s = new StringBuilder();
        for (int i=20;i<packet.length-1;i++) {
            int v=packet[i]&255;
            if (v>=32 && v<=126) s.append((char)v);
            else if (v!=0) s.append(' ');
        }
        String text=s.toString();
        java.util.regex.Matcher m=java.util.regex.Pattern.compile("([A-Za-z0-9 _.'()\\-]+\\.sty)",java.util.regex.Pattern.CASE_INSENSITIVE).matcher(text);
        if(m.find()) names.add(m.group(1).trim());
    }
    /**
     * Upload a style file. EXPERIMENTAL: 7-bit packing and flow-control are
     * inferred from a successful capture, but not yet checked against the
     * original WRAA.sty. Disabled by default to prevent corrupted uploads.
     */
    public synchronized void upload(Path styleFile) throws Exception {
        throw new UnsupportedOperationException(
            "Arbitrary .sty upload is not yet verified. Please provide the original WRAA.sty " +
            "to validate Yamaha's 7-bit encoding and file-open control fields.");
    }
    @Override public void close() {
        try { incoming.close(); } catch (Exception ignored) {}
        try { sender.close(); } catch (Exception ignored) {}
        try { input.close(); } catch (Exception ignored) {}
        try { output.close(); } catch (Exception ignored) {}
    }
}

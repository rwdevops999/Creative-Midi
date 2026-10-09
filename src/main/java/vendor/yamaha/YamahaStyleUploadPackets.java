package vendor.yamaha;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Offline packet builder reconstructed and byte-for-byte validated against
 * the 113 MIDI-OX WRAA.sty data packets (23667 original bytes).
 * Does not send MIDI or modify keyboard files.
 */
public final class YamahaStyleUploadPackets {
    private YamahaStyleUploadPackets() {}
    public static final int CHUNK_SIZE = 210;

    /** Yamaha 7-bit data-packing: one MSB mask then up to seven low-7-bit bytes. */
    public static byte[] packData(byte[] data, int offset, int length) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int p = offset; p < offset + length; p += 7) {
            int count = Math.min(7, offset + length - p);
            int mask = 0;
            for (int j = 0; j < count; j++)
                if ((data[p + j] & 0x80) != 0) mask |= 1 << (6 - j);
            out.write(mask);
            for (int j = 0; j < count; j++) out.write(data[p + j] & 0x7f);
        }
        return out.toByteArray();
    }

    public static List<byte[]> buildDataPackets(byte[] fileBytes) {
        if (fileBytes == null || fileBytes.length == 0)
            throw new IllegalArgumentException("Empty style file");
        int count = (fileBytes.length + CHUNK_SIZE - 1) / CHUNK_SIZE;
        // Sequence format above 0x7f is not established by the reference capture.
        if (count > 128)
            throw new IllegalArgumentException("More than 128 packets: sequence extension unverified");
        List<byte[]> packets = new ArrayList<>(count);
        for (int seq = 0; seq < count; seq++) {
            int start = seq * CHUNK_SIZE;
            byte[] packed = packData(fileBytes, start, Math.min(CHUNK_SIZE, fileBytes.length - start));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(0xf0); out.write(0x43); out.write(0x50); out.write(0x01); out.write(0x01);
            out.write((packed.length + 3) & 0x7f);
            out.write(0x02); out.write(0x02); out.write(seq);
            out.writeBytes(packed);
            int sum = seq;
            for (byte b : packed) sum += b & 0xff;
            out.write((124 - sum) & 0x7f);
            out.write(0xf7);
            packets.add(out.toByteArray());
        }
        return packets;
    }

    public static List<byte[]> buildDataPackets(Path styleFile) throws java.io.IOException {
        return buildDataPackets(Files.readAllBytes(styleFile));
    }

    /** Only ASCII destination names are currently supported. */
    private static byte[] packedDestination(String filename) {

        System.out.println("packedDestination filename = [" + filename + "]");

        if (filename == null ||
                !filename.matches("[A-Za-z0-9_.-]+\\.sty")) {

            throw new IllegalArgumentException(
                    "Invalid filename: [" + filename + "]"
            );
        }

        byte[] raw = ("1:\\STYLE\\" + filename)
                .getBytes(StandardCharsets.US_ASCII);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        for (int i = 0; i < raw.length; i++) {
            if (i > 0 && i % 7 == 0) {
                out.write(0);
            }
            out.write(raw[i]);
        }

        out.write(0);
        return out.toByteArray();
    }

    public static byte[] buildCreateRequest(String filename) {
        byte[] path = packedDestination(filename);
        if (path.length > 127) throw new IllegalArgumentException("Destination path too long");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.writeBytes(new byte[] {(byte)0xf0,0x43,0x50,0x00,0x05,0x01,0x00,0x00,0x00});
        out.write(path.length + 1); out.write(0x00);
        out.writeBytes(path); out.write(0xf7);
        return out.toByteArray();
    }

    public static byte[] buildTransferRequest(String filename, long size) {
        if (size <= 0 || size > 0x7ffffffffL)
            throw new IllegalArgumentException("Unsupported file size");
        byte[] path = packedDestination(filename);
        if (path.length > 127) throw new IllegalArgumentException("Destination path too long");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.writeBytes(new byte[] {(byte)0xf0,0x43,0x50,0x00,0x06,0x01,0x00,0x05});
        for (int shift = 28; shift >= 0; shift -= 7) out.write((int)(size >>> shift) & 0x7f);
        out.writeBytes(new byte[] {0x05,0,0,0,0,0,0});
        out.write(path.length + 1); out.write(0x00);
        out.writeBytes(path); out.write(0xf7);
        return out.toByteArray();
    }
}

package creative.scenes.sysexgen2.entity;

import lombok.Getter;

import java.util.HexFormat;

import static util.Constants.BIG_ENDIAN;

public class DataBlock {
    @Getter
    private byte msb;
    @Getter
    private byte lsb;

    public DataBlock() {
        this.msb = 0;
        this.lsb = 0;
    }

    public DataBlock(byte msb, byte lsb) {
        this.msb = msb;
        this.lsb = lsb;
    }

    public DataBlock(Integer msb, Integer lsb) {
        this();
        if (msb != null) {
            this.msb = (byte)(msb & 0xFF);
        }
        if (lsb != null) {
            this.lsb = (byte) (lsb & 0xFF);
        }
    }

    public DataBlock(Integer ival) {
        this.msb = (byte) ((ival >> 8) & 0xFF);
        this.lsb = (byte) (ival & 0xFF);;
    }

    public Integer getValue() {
        return ((this.msb & 0xFF) << 8) | (this.lsb & 0xFF);
    }

    // msb = 13
    // lsb = 0A
    // Returns "lsb msb" (e.g. "0A 13" for BIG_ENDIAN)
    // Returns "msb lsb" (e.g. "13 0A" for LITTLE_ENDIAN)
    public String toHex(int numBytes, boolean useLowerOnly, int encoding) {
        // Put bytes into an array for easy slicing/indexing
        byte[] bytes;

        if (encoding == BIG_ENDIAN) {
            bytes = new byte[]{ useLowerOnly ? (byte)(msb & 0x0F): msb, useLowerOnly ? (byte)(lsb & 0x0F): lsb };
        } else {
            bytes = new byte[]{ useLowerOnly ? (byte)(lsb & 0x0F): lsb, useLowerOnly ? (byte)(msb & 0x0F): msb };
        }

        // Ensure numBytes stays within valid bounds (0 to 2)
        int targetLength = Math.max(0, Math.min(numBytes, bytes.length));

        // Use HexFormat to format only the requested number of bytes
        return HexFormat.of().withUpperCase().withDelimiter(" ").formatHex(bytes, 0, targetLength);
    }

    public String toHex(int numBytes, int encoding) {
        return toHex(numBytes, false, encoding);
    }
}

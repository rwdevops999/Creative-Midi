package creative.scenes.sysexgen2.entity;

import lombok.Getter;

import java.util.HexFormat;

import static util.Constants.BIG_ENDIAN;
import static util.Constants.LITTLE_ENDIAN;

@Getter
public class DataBlock {
    private int endian = LITTLE_ENDIAN;
    private Byte b1;
    private Byte b2;

    public DataBlock() {
        this.b1 = null;
        this.b2 = null;
        this.endian = LITTLE_ENDIAN;
    }

    public DataBlock(boolean endian) {
        this();
        this.endian = endian ? LITTLE_ENDIAN : BIG_ENDIAN;
    }

    public DataBlock(Integer b1, Integer b2, boolean isLittleEndian) {
        this(isLittleEndian);

        if (b1 != null) {
            this.b1 = b1.byteValue();
        }

        if (b2 != null) {
            this.b2 = b2.byteValue();
        }
    }

    public DataBlock(Integer b1, Integer b2) {
        this(true);

        if (b1 != null) {
            this.b1 = b1.byteValue();
        }

        if (b2 != null) {
            this.b2 = b2.byteValue();
        }
    }

    public DataBlock(Integer i1) {
        this(true);

        this.b1 = (byte) ((i1 >> 8) & 0xFF);
        this.b2 = (byte) (i1 & 0xFF);;
    }

    public DataBlock(Integer i1, boolean isLittleEndian) {
        this(isLittleEndian);

        this.b1 = (byte) ((i1 >> 8) & 0xFF);
        this.b2 = (byte) (i1 & 0xFF);;
    }

    public Integer getValue() {
        return (this.b1 << 7) | this.b2;
    }

    public void setValue(Integer value) {
        b1 = (byte)(value >> 7);
        b2 = (byte)(value & 0x7F);
    }

    public void setValue(Integer value, boolean isLittleEndian) {
        this.endian = isLittleEndian ? LITTLE_ENDIAN : BIG_ENDIAN;

        b1 = (byte)(value >> 7);
        b2 = (byte)(value & 0x7F);
    }

    // Returns "lsb msb" (e.g. "0A 13"
    public String toHex(int numBytes, boolean useLowerOnly) {
        // Put bytes into an array for easy slicing/indexing
        byte[] bytes;

        if (endian == LITTLE_ENDIAN) {
            bytes = new byte[]{ useLowerOnly ? (byte)(b2 & 0xF): b2, useLowerOnly ? (byte)(b1 & 0xF): b1 };
        } else {
            bytes = new byte[]{useLowerOnly ? (byte) (b1 & 0xF) : b1, useLowerOnly ? (byte) (b2 & 0xF) : b2};
        }

        // Ensure numBytes stays within valid bounds (0 to 2)
        int targetLength = Math.max(0, Math.min(numBytes, bytes.length));

        // Use HexFormat to format only the requested number of bytes
        return HexFormat.of().withUpperCase().withDelimiter(" ").formatHex(bytes, 0, targetLength);
    }

    public String toHex(int numBytes) {
        return toHex(numBytes, false);
    }
}

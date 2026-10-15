package creative.scenes.sysexgen2.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.HexFormat;

import static util.Constants.BIG_ENDIAN;
import static util.Constants.LITTLE_ENDIAN;

@Getter
public class DataBlock {
    private int endian;
    private Byte b1;
    private Byte b2;

    public DataBlock() {
        this.endian = BIG_ENDIAN;
        this.b1 = null;
        this.b2 = null;
    }

    public DataBlock(int endian) {
        this();
        this.endian = endian;
    }

    public DataBlock(Integer msb, Integer lsb, int endian) {
        this(endian);

        if (msb != null) {
            this.b1 = msb.byteValue();
        } else {
            this.b1 = 0;
        }

        if (lsb != null) {
            this.b2 = lsb.byteValue();
        } else {
            this.b2 = 0;
        }
    }

    public DataBlock(Integer msb, Integer lsb) {
        this(BIG_ENDIAN);

        if (b1 != null) {
            this.b1 = msb.byteValue();
        } else {
            this.b1 = 0;
        }

        if (b2 != null) {
            this.b2 = lsb.byteValue();
        } else {
            this.b2 = 0;
        }
    }

    public DataBlock(Integer i1) {
        this(BIG_ENDIAN);

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

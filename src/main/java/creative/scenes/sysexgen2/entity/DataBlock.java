package creative.scenes.sysexgen2.entity;

import lombok.Getter;

import java.util.HexFormat;

import static util.Constants.BIG_ENDIAN;

public class DataBlock {
    private boolean encodingIsBigEndian;
    @Getter
    private byte first;
    @Getter
    private byte second;

    public DataBlock() {
        this.encodingIsBigEndian = true;
        this.first = 0;
        this.second = 0;
    }

    public DataBlock(boolean encodingIsBigEndian) {
        this();
        this.encodingIsBigEndian = encodingIsBigEndian;
    }

    public DataBlock(byte first, byte second) {
        this();
        this.first = first;
        this.second = second;
    }

    public DataBlock(byte first, byte second, boolean encodingIsBigEndian) {
        this(encodingIsBigEndian);
        this.first = first;
        this.second = second;
    }

    public DataBlock(Integer ib1, Integer ib2) {
        this();
        this.first = (byte)(ib2 & 0XFF);
        this.second = (byte)(ib1 & 0xFF);
    }

    public DataBlock(Integer b1, Integer b2, boolean encodingIsBigEndian) {
        this(encodingIsBigEndian);
        this.first = (byte)(b2 & 0xFF);
        this.second = (byte)(b1 & 0XFF);
    }

    public DataBlock(Integer ival) {
        this();
        if (this.encodingIsBigEndian) {
            this.first = (byte)((ival >> 8) & 0xFF);
            this.second = (byte)(ival & 0xFF);
        } else {
            this.first = (byte)(ival & 0xFF);
            this.second = (byte)((ival >> 8) & 0xFF);
        }
    }

    public DataBlock(Integer ival, boolean encodingIsBigEndian) {
        this(encodingIsBigEndian);

        if (this.encodingIsBigEndian) {
            this.first = (byte)((ival >> 8) & 0xFF);
            this.second = (byte)(ival & 0xFF);
        } else {
            this.first = (byte)(ival & 0xFF);
            this.second = (byte)((ival >> 8) & 0xFF);
        }
    }

    public void setValue(Integer ival) {
        if (this.encodingIsBigEndian) {
            this.first = (byte)((ival >> 8) & 0xFF);
            this.second = (byte)(ival & 0xFF);
        } else {
            this.first = (byte)(ival & 0xFF);
            this.second = (byte)((ival >> 8) & 0xFF);
        }
    }

    public Integer getValue() {
        if (this.encodingIsBigEndian) {
            return (this.second << 8) | this.first;
        } else {
            return (this.first << 8) | this.second;
        }
    }

    // msb = 13
    // lsb = 0A
    // Returns "lsb msb" (e.g. "0A 13" for BIG_ENDIAN)
    // Returns "msb lsb" (e.g. "13 0A" for LITTLE_ENDIAN)
    public String toHex(int numBytes, boolean useLowerOnly) {
        // Put bytes into an array for easy slicing/indexing
        byte[] bytes;

        bytes = new byte[]{ useLowerOnly ? (byte)(second & 0x0F): second, useLowerOnly ? (byte)(first & 0x0F): first };

        // Ensure numBytes stays within valid bounds (0 to 2)
        int targetLength = Math.max(0, Math.min(numBytes, bytes.length));

        // Use HexFormat to format only the requested number of bytes
        return HexFormat.of().withUpperCase().withDelimiter(" ").formatHex(bytes, 0, targetLength);
    }

    public String toHex(int numBytes) {
        return toHex(numBytes, false);
    }
}

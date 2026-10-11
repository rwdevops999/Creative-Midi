package entity.sysex;

import lombok.Getter;

@Getter
public class DataBlock {
    Byte b1;
    Byte b2;

    public DataBlock() {
        this.b1 = null;
        this.b2 = null;
    }

    public DataBlock(Integer b1, Integer b2) {
        this();

        if (b1 != null) {
            this.b1 = (byte) (b1 & 0x0F);
        }

        if (b2 != null) {
            this.b2 = (byte)(b2 & 0x0F);
        }
    }

    public int getValue() {
        int MSB = 0;
        int LSB = 0;

        if (this.b1 != null) {
            MSB = (this.b1 << 7);
        }

        if (this.b2 != null) {
            LSB = (this.b2 & 0xF0);
        }

        return MSB | LSB;
    }
}

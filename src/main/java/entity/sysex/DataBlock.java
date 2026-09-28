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
            this.b1 = b1.byteValue();
        }
        if (b2 != null) {
            this.b2 = b2.byteValue();
        }
    }

    public DataBlock(byte b1, byte b2) {
        this();

        this.b1 = b1;
        this.b2 = b2;
    }
}

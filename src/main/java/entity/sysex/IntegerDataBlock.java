package entity.sysex;

import lombok.Getter;

import java.util.HexFormat;

@Getter
public class IntegerDataBlock {
    Integer i1;
    Integer i2;

    public IntegerDataBlock() {
        this.i1 = null;
        this.i2 = null;
    }

    public IntegerDataBlock(Integer val1, Integer val2) {
        this();

        this.i1 = val1;
        this.i2 = val2;
    }
}

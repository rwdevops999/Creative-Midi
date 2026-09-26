package creative.scenes.midi.data;

import java.util.ArrayList;
import java.util.Arrays;

public enum ByteType {
    Continuous(3),
    ContinuousRange(2),
    Panning(2),
    ContinuousOnOff(2),
    OnOff(2),
    ContinuousMinusPlus(2),
    Key(3),
    NoValue(3),
    DefaultValue(2),
    FreeValue(1);

    private int value;

    ByteType(int byteValue) {
        this.value = byteValue;
    }

    public static ByteType[] fromValue(int value) {
        ArrayList<ByteType> list = new ArrayList<>();

        return Arrays.stream(values()).filter(t -> (t.value & value) == value).toList().toArray(ByteType[]::new);
    }
}

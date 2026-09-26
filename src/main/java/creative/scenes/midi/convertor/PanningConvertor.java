package creative.scenes.midi.convertor;

import javafx.util.StringConverter;

public class PanningConvertor extends StringConverter<Double> {
    public PanningConvertor() {
    }

    public PanningConvertor(int min, int max) {
    }

    @Override
    public String toString(Double d) {
        if (Math.abs(d - 63.0) < 0.01) return "C";
        if (Math.abs(d - 0.0) < 0.01) return "L";
        if (Math.abs(d - 127.0) < 0.01) return "R";
        return ""; // Empty for the other ticks
    }

    @Override
    public Double fromString(String s) {
        if ("C".equals(s)) return 63.0;
        if ("L".equals(s)) return 0.0;
        if ("R".equals(s)) return 127.0;
        return 0.0;
    }
}

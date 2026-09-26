package creative.scenes.midi.convertor;

import javafx.util.StringConverter;

public class MinPlusConvertor extends StringConverter<Double> {
    private Integer minValue = 0;
    private Integer maxValue = 127;
    private Integer middle = maxValue / 2;

    public MinPlusConvertor() {
    }

    public MinPlusConvertor(int min, int max) {
        minValue = min;
        maxValue = max;
        middle = maxValue / 2;
    }

    @Override
    public String toString(Double d) {
        if (d == -64) return "-64";
        if (d == -1) return "0";
        if (d == 63) return "+63";
        return ""; // Leeg voor andere ticks
    }

    @Override
    public Double fromString(String s) {
        if ("0".equals(s)) return middle.doubleValue();
        if ("-64".equals(s)) return -64.0;
        if ("+63".equals(s)) return 63.0;
        return 0.0;
    }
}

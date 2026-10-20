package creative.scenes.midi.convertor;

import javafx.util.StringConverter;

public class SplitConvertor extends StringConverter<Double> {
    private static double dMin = 0.0;
    private static double dMax = 127.0;

    public SplitConvertor(int min, int max) {
        dMin = min;
        dMax = max;
    }

    @Override
    public String toString(Double d) {
        if (Math.abs(d - dMin) < 0.01) return "OFF";
        if (Math.abs(d - dMax) < 0.01) return "ON";
        return ""; // Nothing for the other ticks
    }

    @Override
    public Double fromString(String s) {
        if ("OFF".equals(s)) return dMin;
        if ("ON".equals(s)) return dMax;
        return dMin;
    }
}

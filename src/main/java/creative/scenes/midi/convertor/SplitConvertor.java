package creative.scenes.midi.convertor;

import javafx.util.StringConverter;

public class SplitConvertor extends StringConverter<Double> {
    private static double dmin = 0.0;
    private static double dmax = 127.0;

    public SplitConvertor(int min, int max) {
        dmin = min;
        dmax = max;
    }

    public SplitConvertor() {
    }

    @Override
    public String toString(Double d) {
        if (Math.abs(d - dmin) < 0.01) return "OFF";
        if (Math.abs(d - dmax) < 0.01) return "ON";
        return ""; // Nothing for the other ticks
    }

    @Override
    public Double fromString(String s) {
        if ("OFF".equals(s)) return dmin;
        if ("ON".equals(s)) return dmax;
        return dmin;
    }
}

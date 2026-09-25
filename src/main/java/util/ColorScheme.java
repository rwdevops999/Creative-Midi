package util;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class ColorScheme {
    public record ColorKey(String arg1, String arg2, String arg3) {}

    private static final Map<ColorKey, Color> colors = new HashMap<>();

    static {
        // default color
        colors.put(new ColorKey(null, null, null), Color.DARKGRAY);

        // status color
        colors.put(new ColorKey("status", null, null), Color.BLUE);

        // test colors
        colors.put(new ColorKey("test", "red", null), Color.RED);
        colors.put(new ColorKey("test", "green", null), Color.GREEN);
        colors.put(new ColorKey("test", "blue", null), Color.BLUE);
        colors.put(new ColorKey("test", "orange", null), Color.ORANGE);
    }

    public static Color getColor(ColorKey key) {
        if (colors.containsKey(key)) {
            return colors.get(key);
        }

        ColorKey nextKey = new ColorKey(key.arg1, key.arg2, null);
        if (colors.containsKey(nextKey)) {
            return colors.get(nextKey);
        }

        nextKey = new ColorKey(key.arg1, null, null);
        if (colors.containsKey(nextKey)) {
            return colors.get(nextKey);
        }

        return colors.get(new ColorKey(null, null, null));
    }

    public static Color getColor(String... args) {
        ColorKey key = new ColorKey(null, null, null);
        if (args != null && args.length > 0) {
            if (args.length == 1) {
                key = new ColorKey(args[0], null, null);
            } else if (args.length == 2) {
                key = new ColorKey(args[0], args[1], null);
            } else if (args.length == 3) {
                key = new ColorKey(args[0], args[1], args[2]);
            }
        }

        return getColor(key);
    }
}

package util.properties;

import java.util.Properties;

public class PropertyContainer {
    public static String SHOW_ABOUT_DIALOG = "show.about";
    public static String ABOUT_TIMING= "about.seconds";
    public static String APP_VERSION = "version";
    public static String THEME = "theme";
    public static String PROPERTIES_PATH = "properties.path";

    // PATHS CONSTANTS
    public static String MIDI_PATH = "midi.path";
    public static String MIDI_EVENTS_FILE = "midi.events.file";

    private static Properties systemProperties = new Properties();
    private static Properties pathProperties = new Properties();

    public static void setProperties(PropertyType type, Properties props) {
        switch (type) {
            case System:
                systemProperties = props;
                break;
            case Path:
                pathProperties = props;
                break;
        }
    }

    public static boolean getPropertyAsBoolean (PropertyType type, String key, boolean defaultvalue) {
        String property = switch (type) {
            case System -> (String)systemProperties.get(key);
            default -> null;
        };

        if (property == null) {
            return defaultvalue;
        }

        return Boolean.parseBoolean(property);
    }

    public static Integer getPropertyAsInteger (PropertyType type, String key, int defaultvalue) {
        String property = switch (type) {
            case System -> (String)systemProperties.get(key);
            default -> null;
        };

        if (property == null) {
            return defaultvalue;
        }

        return Integer.parseInt(property);
    }

    public static String getPropertyAsString (PropertyType type, String key, String defaultvalue) {
        String property = switch (type) {
            case System -> (String)systemProperties.get(key);
            case Path -> (String)pathProperties.get(key);
            default -> null;
        };

        if (property == null) {
            return defaultvalue;
        }

        return property;
    }
}

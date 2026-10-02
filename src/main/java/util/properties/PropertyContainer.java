package util.properties;

import java.util.Properties;

public class PropertyContainer {
    // SYSTEM KEYS
    public static String SHOW_ABOUT_DIALOG = "show.about";
    public static String ABOUT_TIMING= "about.seconds";
    public static String APP_VERSION = "version";
    public static String THEME = "theme";
    public static String PROPERTIES_PATH = "properties.path";
    public static String AUTO_SELECT_DEVICE = "auto.select.device";
    public static String PROPERTIES_FILE = "keyboard.properties";
    public static String VENDOR_FILE = "vendor.properties";

    // PATHS KEYS
    public static String MIDI_PATH = "midi.path";
    public static String MIDI_EVENTS_FILE = "midi.events.file";
    public static String VOICES_PATH = "voices.path";
    public static String EXPORT_PATH = "export.path";
    public static String MIDI_DEMO_FILE = "midi.demo.file";
    public static String SYSEX_PATH = "sysex.path";
    public static String SYSEX_EVENTS_FILE = "sysex.events.file";
    public static String PLAYLIST_PATH = "playlist.path";
    public static String PLAYLIST_FILE = "playlist.file";

    // KEYBOARD KEYS
    public static String DEFAULT_KEYBOARD = "default.voice.file";
    public static String BASE_OCTAVE="base.octave";
    public static String DEVICE_NAME="device.name";

    // VENDOR KEYS
    public static String VENDOR = "target.vendor";
    public static String PPQ = "target.ppq";

    private static Properties systemProperties = new Properties();
    private static Properties pathProperties = new Properties();
    private static Properties keyboardProperties = new Properties();
    private static Properties generalKeyboardProperties = new Properties();
    private static Properties vendorProperties = new Properties();

    public static void setProperties(PropertyType type, Properties props) {
        switch (type) {
            case System:
                systemProperties = props;
                break;
            case Path:
                pathProperties = props;
                break;
            case GeneralKeyboard:
                generalKeyboardProperties = props;
                break;
            case Keyboard:
                keyboardProperties = props;
                break;
            case Vendor:
                vendorProperties = props;
                break;
        }
    }

    public static boolean getPropertyAsBoolean (PropertyType type, String key, boolean defaultvalue) {
        String property = getPropertyAsString(type, key, null);

        if (property == null) {
            return defaultvalue;
        }

        return Boolean.parseBoolean(property);
    }

    public static Integer getPropertyAsInteger (PropertyType type, String key, Integer defaultvalue) {
        String property = getPropertyAsString(type, key, null);

        if (property == null) {
            return defaultvalue;
        }

        return Integer.parseInt(property);
    }

    public static String getPropertyAsString (PropertyType type, String key, String defaultvalue) {
        String property = switch (type) {
            case System -> (String)systemProperties.get(key);
            case Path -> (String)pathProperties.get(key);
            case Keyboard -> (String)keyboardProperties.get(key);
            case GeneralKeyboard -> (String)generalKeyboardProperties.get(key);
            case Vendor -> (String)vendorProperties.get(key);
            default -> null;
        };

        if (property == null) {
            return defaultvalue;
        }

        return property;
    }
}

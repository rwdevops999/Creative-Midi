package util.properties;

import java.util.Properties;

public class PropertyContainer {
    public static String SHOW_ABOUT_DIALOG = "show.about";
    public static String ABOUT_TIMING= "about.seconds";
    public static String APP_VERSION = "version";
    public static String THEME = "theme";

    private static Properties systemProperties = new Properties();
    public static void setSystemProperties(Properties props) {
        systemProperties = props;
    }

    public static boolean getPropertyAsBoolean (PropertyType type, String key, boolean defaultvalue) {
        String property = switch (type) {
            case System -> (String)systemProperties.get(key);
        };

        if (property == null) {
            return defaultvalue;
        }

        return Boolean.parseBoolean(property);
    }

    public static Integer getPropertyAsInteger (PropertyType type, String key, int defaultvalue) {
        String property = switch (type) {
            case System -> (String)systemProperties.get(key);
        };

        if (property == null) {
            return defaultvalue;
        }

        return Integer.parseInt(property);
    }

    public static String getPropertyAsString (PropertyType type, String key, String defaultvalue) {
        String property = switch (type) {
            case System -> (String)systemProperties.get(key);
        };

        if (property == null) {
            return defaultvalue;
        }

        return property;
    }
}

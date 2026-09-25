package util;

import javafx.scene.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.enums.ThemeType;

public class Theme {
    private static final Logger logger = LoggerFactory.getLogger(Theme.class);

    private static ThemeType appTheme = ThemeType.LIGHT;
    public static void setThemeType(ThemeType themeType) {
        appTheme = themeType;
    }

    public static boolean isModeLight() {
        return appTheme == ThemeType.LIGHT;
    }

    public static String getThemeIcon() {
        if (isModeLight()) {
            return "icons/bulb-off.png";
        }

        return "icons/bulb-on.png";
    }

    public static void switchMode(boolean mode) {
        logger.debug("[CM_GLOBALS] Set theme (true = LIGHT, false = DARK) {}", mode);
        appTheme = ThemeType.DARK;

        if (mode) {
            appTheme = ThemeType.LIGHT;
        }
    }

    public static void switchTheme(Scene scene) {
        if (appTheme == ThemeType.LIGHT) {
            if (scene != null) {
                scene.getRoot().setStyle("");
            }
        } else {
            if (scene != null) {
                scene.getRoot().setStyle("-fx-base: #2d2d2d;");
            }
        }
    }

}

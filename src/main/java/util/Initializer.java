package util;

import communication.CommunicationModel;
import creative.panes.MainPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.enums.ThemeType;
import util.properties.PropertyContainer;
import util.properties.PropertyLoader;
import util.properties.PropertyType;

import java.util.Properties;

public class Initializer {
    private static final Logger logger = LoggerFactory.getLogger(Initializer.class);

    public void initApp() {
        logger.debug("[CM_INITIALIZER] Initializing Application");

        CommunicationModel.setStatus("Initializing Creative Midi");

        PropertyLoader propertyLoader = new PropertyLoader();

        // 1. Load system properties
        Properties props = propertyLoader.loadPropertiesFromResource("creative.properties");
        logger.debug("[CM_INITIALIZER] System Properties loaded");
        PropertyContainer.setSystemProperties(props);

        // 2. Set Theme
        ThemeType theme = ThemeType.valueOf(PropertyContainer.getPropertyAsString(PropertyType.System, PropertyContainer.THEME, "DARK"));
        Theme.setThemeType(theme);
        Theme.switchTheme(ApplicationInfo.getInstance().getRootScene());
    }
}

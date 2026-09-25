package util;

import communication.CommunicationModel;
import creative.panes.MainPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.enums.ThemeType;
import util.properties.PropertyContainer;
import util.properties.PropertyLoader;
import util.properties.PropertyType;

import java.nio.file.Path;
import java.nio.file.Paths;
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
        PropertyContainer.setProperties(PropertyType.System, props);

        // 2. Set Theme
        ThemeType theme = ThemeType.valueOf(PropertyContainer.getPropertyAsString(PropertyType.System, PropertyContainer.THEME, "DARK"));
        Theme.setThemeType(theme);
        Theme.switchTheme(ApplicationInfo.getInstance().getRootScene());

        // 3. load paths
        String propertiesDirectory = PropertyContainer.getPropertyAsString(PropertyType.System, PropertyContainer.PROPERTIES_PATH, "./properties");
        Path path = Paths.get(propertiesDirectory, "paths.properties"); // Use your actual path
        Properties pathProperties = propertyLoader.loadPropertiesFromPath(path);
        PropertyContainer.setProperties(PropertyType.Path, pathProperties);
    }
}

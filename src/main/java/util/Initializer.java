package util;

import creative.panes.MainPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.properties.PropertyContainer;
import util.properties.PropertyLoader;

import java.util.Properties;

public class Initializer {
    private static final Logger logger = LoggerFactory.getLogger(Initializer.class);

    public void initApp() {
        logger.debug("[CM_INITIALIZER] Initializing Application");
        PropertyLoader propertyLoader = new PropertyLoader();

        // 1. Load system properties
        Properties props = propertyLoader.loadPropertiesFromResource("creative.properties");
        logger.debug("[CM_INITIALIZER] System Properties loaded");
        PropertyContainer.setSystemProperties(props);
    }
}

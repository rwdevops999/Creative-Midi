package util.properties;

import creative.panes.MainPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {
    private static final Logger logger = LoggerFactory.getLogger(PropertyLoader.class);

    public Properties loadPropertiesFromResource(String filename) {
        Properties properties = new Properties();

        try (InputStream input = PropertyLoader.class.getClassLoader().getResourceAsStream(filename)) {
            if (input == null) {
                logger.error("[CM_PROPERTY_LOADER] ERROR. Can't read properties from file {}. File doesn't exist.", filename);
                return null;
            }

            // Load the properties file
            properties.load(input);

            if (ApplicationInfo.getInstance().isDebugging()) {
                properties.entrySet().stream().forEach(entry -> {
                    logger.debug("SYSTEM PROPERTY: " + entry.getKey() + ": " + entry.getValue());
                });
            }
            return properties;
        } catch (IOException ioe) {
            logger.error("[CM_PROPERTY_LOADER] EXCEPTION. Cause {}", ioe.getMessage());
        }

        return null;
    }
}

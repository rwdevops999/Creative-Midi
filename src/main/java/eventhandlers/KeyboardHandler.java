package eventhandlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.properties.PropertyContainer;
import util.properties.PropertyType;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class KeyboardHandler<T extends ActionEvent> implements EventHandler<T> {
    private static final Logger logger = LoggerFactory.getLogger(KeyboardHandler.class);

    @Override
    public void handle(T event) {
        logger.debug("[CM_KEYBOARD_HANDLER] Handling event {}", event.getClass().getSimpleName());

        ComboBox<String> combo = (ComboBox<String>)event.getTarget();

        String[] splitted = combo.getValue().split(" ");

        logger.debug("[CM_KEYBOARD_HANDLER] Handling selected keyboard {}", combo.getValue());
        String voicesDirectory = PropertyContainer.getPropertyAsString(PropertyType.Path, PropertyContainer.VOICES_PATH, "./voices");
        Path dirPath = Paths.get(voicesDirectory); // Use your actual path

        String provider = splitted[1].substring(1, splitted[1].length() - 1);
        String keyboard = splitted[0];
        String extension = provider.equalsIgnoreCase("Yamaha") ? ".txt" : ".ins";

        logger.debug("[CM_KEYBOARD_HANDLER] Handled event {}", event.getClass().getSimpleName());
    }
}

package eventhandlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyboardHandler<T extends ActionEvent> implements EventHandler<T> {
    private static final Logger logger = LoggerFactory.getLogger(KeyboardHandler.class);

    @Override
    public void handle(T event) {
        logger.debug("[CM_KEYBOARD_HANDLER] Handling event {}", event.getClass().getSimpleName());

        logger.debug("[CM_KEYBOARD_HANDLER] Handled event {}", event.getClass().getSimpleName());
    }
}

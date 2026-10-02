package creative.scenes.eventlist;

import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.border.Border;

public class EventlistPane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(EventlistPane.class);
    public EventlistPane() {
        super();

        setId("EventlistPane");

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_EVENT_LIST_PANE] Building {}", getId());

        logger.debug("[CM_EVENT_LIST_PANE] Built {}", getId());
    }
}

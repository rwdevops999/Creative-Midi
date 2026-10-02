package creative.scenes.eventlist;

import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventsDisplayPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(EventsDisplayPane.class);

    public EventsDisplayPane() {
        super();

        setId("EventsDisplayPane");
    }

    private EventsPane parent;
    public EventsDisplayPane(EventsPane owner) {
        this();

        parent = owner;

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_EVENTS_DISPLAY_PANE] Building {}", getId());

        logger.debug("[CM_EVENTS_DISPLAY_PANE] Built {}", getId());
    }

    // ACCESSORS
    public EventsPane getEventsPane() {
        return parent;
    }
}


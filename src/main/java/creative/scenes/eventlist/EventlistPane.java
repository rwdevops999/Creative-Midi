package creative.scenes.eventlist;

import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.border.Border;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;

public class EventlistPane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(EventlistPane.class);
    public EventlistPane() {
        super();

        setId("EventlistPane");

//        showPaneBorder(this, getColor("border", "red"));

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_EVENT_LIST_PANE] Building {}", getId());

        setTop(new FileSelectionPane(this));
        setCenter(new EventsPane(this));

        logger.debug("[CM_EVENT_LIST_PANE] Built {}", getId());
    }

    // ACCESSORS
    public FileSelectionPane getFileSelectPane() {
        return (FileSelectionPane) getTop();
    }

    public EventsPane getEventsPane() {
        return (EventsPane) getCenter();
    }
}

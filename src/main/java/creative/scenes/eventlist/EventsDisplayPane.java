package creative.scenes.eventlist;

import creative.scenes.eventlist.parser.entity.MidiEventInfo;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.Sequence;
import java.util.Queue;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;

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

//        showPaneBorder(this, getColor("border", "red"));
        buildPane();
    }

    private boolean[] selectedEvents = {true, true, true, false};
    private void buildPane() {
        logger.debug("[CM_EVENTS_DISPLAY_PANE] Building {}", getId());

        setStyle("-fx-font-size: 10px;");

        int[] columnSizes = {10,4,15,4,15,8,53};

        FilterPane filterPane = new FilterPane(this, selectedEvents);
        getChildren().add(filterPane);

        logger.debug("[CM_EVENTS_DISPLAY_PANE] Built {}", getId());
    }

    public void setEventInfo(Sequence sequence, Queue<MidiEventInfo> events) {
        // TODO
    }

    // ACCESSORS
    public EventsPane getEventsPane() {
        return parent;
    }
}


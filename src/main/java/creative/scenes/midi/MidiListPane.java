package creative.scenes.midi;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MidiListPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(MidiListPane.class);

    public MidiListPane() {
        super();

        setId("MidiListPane");
    }

    private MidiPane parent;
    public MidiListPane(MidiPane parent) {
        this();

        this.parent = parent;

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_MIDI_LIST_PANE] Building {}", getId());

        logger.debug("[CM_MIDI_LIST_PANE] Built {}", getId());
    }
}

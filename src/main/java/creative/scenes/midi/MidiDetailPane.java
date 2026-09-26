package creative.scenes.midi;

import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MidiDetailPane extends GridPane {
    private static final Logger logger = LoggerFactory.getLogger(MidiDetailPane.class);

    public MidiDetailPane() {
        super();

        setId("MidiDetailPane");
    }

    private MidiPane parent;
    public MidiDetailPane(MidiPane parent) {
        this.parent = parent;

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_MIDI_DETAIL_PANE] Building {}", getId());

        logger.debug("[CM_MIDI_DETAIL_PANE] Built {}", getId());
    }
}

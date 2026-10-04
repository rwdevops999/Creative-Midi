package creative.scenes.merge;

import creative.scenes.midi.MidiPane;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;

public class MergePane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(MergePane.class);

    public MergePane() {
        super();

        setId("MergePane");

        showPaneBorder(this, getColor("border", "red"));

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_MIDI_SCENE] Starting Merge Scene");

        logger.debug("[CM_MIDI_SCENE] Starting Merge Scene");
    }

    // ACCESSORS
}

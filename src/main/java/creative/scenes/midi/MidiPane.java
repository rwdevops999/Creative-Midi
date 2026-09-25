package creative.scenes.midi;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MidiPane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(MidiPane.class);

    public MidiPane() {
        super();

        setId("MidiMainPane");

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_MIDI_PANE] Building {}", getId());

        Label title = new Label("MIDI");
        title.setTextFill(Color.RED);

        setCenter(new HBox(title));

        logger.debug("[CM_MIDI_PANE] Built {}", getId());
    }
}

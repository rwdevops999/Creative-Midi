package creative.scenes.voice;

import communication.CommunicationModel;
import creative.scenes.midi.MidiPane;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoicePane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(VoicePane.class);

    public VoicePane() {
        super();

        setId("VoicePane");

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_VOICE_PANE] Building {}", getId());

        CommunicationModel.setStatus("Matching Voices");

        logger.debug("[CM_VOICE_PANE] Built {}", getId());
    }
}

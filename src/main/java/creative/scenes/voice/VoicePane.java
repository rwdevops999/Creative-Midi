package creative.scenes.voice;

import communication.CommunicationModel;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static util.Util.setPaneBackground;

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

        setPaneBackground(this);

        setCenter(new VoiceSearchResultsPane(this, new ArrayList<>()));
        setLeft(new VoiceSearchPane(this));

        logger.debug("[CM_VOICE_PANE] Built {}", getId());
    }

    // ACCESSORS
    public VoiceSearchPane getVoiceSearchPane() {
        return (VoiceSearchPane)getLeft();
    }

    public VoiceSearchResultsPane getVoiceSearchResultsPane() {
        return (VoiceSearchResultsPane)getCenter();
    }
}

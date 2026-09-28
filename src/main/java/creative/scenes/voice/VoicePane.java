package creative.scenes.voice;

import communication.CommunicationModel;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;

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

        setLeft(new VoiceSearchPane(this));
        setCenter(new VoiceSearchResultsPane(this, new ArrayList<>()));

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

package creative.scenes.voice;

import entity.voice.Patch;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Util;

import java.util.List;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneWidthAsPercentage;

public class VoiceSearchResultsPane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(VoiceSearchResultsPane.class);

    public VoiceSearchResultsPane() {
        super();

        setId("VoiceSearchResultsPane");
    }

    private VoicePane parent;
    public VoiceSearchResultsPane(VoicePane owner, List<Patch> data) {
        super();

        parent = owner;

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_VOICE_SEARCH_RESULT_PANE] Building {}", getId());

        setPaneWidthAsPercentage(this, parent, 30);

        logger.debug("[CM_VOICE_SEARCH_RESULT_PANE] Built {}", getId());
    }

    // ACCESSORS
    public VoicePane getVoicePane() {
        return parent;
    }
}

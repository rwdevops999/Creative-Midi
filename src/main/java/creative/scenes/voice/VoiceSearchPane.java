package creative.scenes.voice;

import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneWidthAsPercentage;

public class VoiceSearchPane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(VoiceSearchPane.class);

    public VoiceSearchPane() {
        super();

        setId("VoiceSearchPane");
    }

    private VoicePane parent;

    public VoiceSearchPane(VoicePane owner) {
        this();

        this.parent = owner;

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_VOICE_SEARCH_PANE] Building {}", getId());

        setPaneWidthAsPercentage(this, parent, 30);

        setTop(new PatchSearchPane(this));
        setCenter(new GroupSearchPane(this));
        setBottom(new ExcludesPane(this));

        logger.debug("[CM_VOICE_SEARCH_PANE] Built {}", getId());
    }

    // ACCESSORS
    public VoicePane getVoicePane() {
        return parent;
    }

    public PatchSearchPane getPatchSearchPane() {
        return (PatchSearchPane) getTop();
    }

    public GroupSearchPane getGroupSearchPane() {
        return (GroupSearchPane) getCenter();
    }

    public ExcludesPane getExcludesPane() {
        return (ExcludesPane) getBottom();
    }
}

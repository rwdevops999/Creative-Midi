package creative.scenes.voice;

import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneHeightAsPercentage;

public class ExcludesPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(ExcludesPane.class);

    public ExcludesPane() {
        super();

        setId("ExcludesPane");
    }

    VoiceSearchPane parent;

    public ExcludesPane(VoiceSearchPane owner) {
        this();

        parent = owner;

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_EXCLUDES_PANE] Building {}", getId());

        setPaneHeightAsPercentage(this, parent, 30);

        logger.debug("[CM_EXCLUDES_PANE] Built {}", getId());
    }

    // ACCESSORS
    public VoiceSearchPane getVoiceSearchPane() {
        return parent;
    }
}

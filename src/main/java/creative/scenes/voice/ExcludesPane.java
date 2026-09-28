package creative.scenes.voice;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Util;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;
import static util.Util.setPaneHeightAsPercentage;

public class ExcludesPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(ExcludesPane.class);

    public ExcludesPane() {
        super();

        setId("ExcludesPane");

        setPadding(new Insets(5));
    }

    VoiceSearchPane parent;

    public ExcludesPane(VoiceSearchPane owner) {
        this();

        showPaneBorder(this, getColor("test", "red"));

        parent = owner;

        setPaneHeightAsPercentage(this, owner, 30);
        setPaneBackground(this);

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

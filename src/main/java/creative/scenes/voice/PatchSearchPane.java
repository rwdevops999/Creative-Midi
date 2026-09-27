package creative.scenes.voice;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ColorScheme;
import util.Util;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;
import static util.Util.setPaneHeightAsPercentage;

public class PatchSearchPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(PatchSearchPane.class);

    public PatchSearchPane() {
        super();

        setId("PatchSearchPane");

        setSpacing(5);
        setPadding(new Insets(5));
        setAlignment(Pos.TOP_LEFT);
    }

    VoiceSearchPane parent;

    public PatchSearchPane(VoiceSearchPane owner) {
        this();

        parent = owner;

        showPaneBorder(this, getColor("test", "green"));
        setPaneHeightAsPercentage(this, parent, 55);
        setPaneBackground(this);

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_PATCH_SEARCH_PANE] Building {}", getId());

        logger.debug("[CM_PATCH_SEARCH_PANE] Built {}", getId());
    }

    // ACCESSORS
    public VoiceSearchPane getVoiceSearchPane() {
        return parent;
    }
}

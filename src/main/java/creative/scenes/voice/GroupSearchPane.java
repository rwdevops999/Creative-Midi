package creative.scenes.voice;

import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneHeightAsPercentage;

public class GroupSearchPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(GroupSearchPane.class);

    public GroupSearchPane() {
        super();

        setId("GroupSearchPane");
    }

    VoiceSearchPane parent;

    public GroupSearchPane(VoiceSearchPane owner) {
        this();

        parent = owner;

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_GROUP_SEARCH_PANE] Building {}", getId());

        setPaneHeightAsPercentage(this, parent, 15);

        logger.debug("[CM_GROUP_SEARCH_PANE] Built {}", getId());
    }

    // ACCESSORS
    public VoiceSearchPane getVoiceSearchPane() {
        return parent;
    }
}

package creative.scenes.voice;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

        setPaneWidthAsPercentage(this, parent, 30);

        buildPane();
    }

    private BooleanProperty visible =  new SimpleBooleanProperty(this, "visible", true);

    public void setSearchPanelsVisible(boolean visible) {
        this.visible.set(visible);
    }

    private void buildPane() {
        logger.debug("[CM_VOICE_SEARCH_PANE] Building {}", getId());

        GroupSearchPane groupsSearchPane = new GroupSearchPane(this);
        groupsSearchPane.visibleProperty().bind(visible);
        setCenter(groupsSearchPane);
        PatchSearchPane patchSearchPane = new PatchSearchPane(this);
        patchSearchPane.visibleProperty().bind(visible);
        setTop(patchSearchPane);
        ExcludesPane excludesPane = new ExcludesPane(this);
        excludesPane.visibleProperty().bind(visible);
        setBottom(excludesPane);

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

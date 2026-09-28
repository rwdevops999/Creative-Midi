package creative.scenes.voice;

import creative.scenes.voice.components.ExcludePane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
        setAlignment(Pos.TOP_LEFT);
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

        getChildren().clear();

        ExcludePane excludePane = new ExcludePane(this);
        getChildren().add(excludePane);

        logger.debug("[CM_EXCLUDES_PANE] Built {}", getId());
    }

    public void updateExcludes() {
        System.out.println("UPDATING EXCLUDES");
        buildPane();
    }

    // ACCESSORS
    public VoiceSearchPane getVoiceSearchPane() {
        return parent;
    }
}

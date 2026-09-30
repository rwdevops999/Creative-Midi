package creative.scenes.playlist;

import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;
import static util.Util.setPaneWidthAsPercentage;

public class PlaylistDetailsPane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(PlaylistDetailsPane.class);

    public PlaylistDetailsPane() {
        super();

        setId("PlaylistDetailsPane");
    }

    private PlaylistPane parent;
    public PlaylistDetailsPane(PlaylistPane owner) {
        this();

        parent = owner;

        showPaneBorder(this, getColor("border", "red", null));

        setPaneWidthAsPercentage(this, owner, 70);
        setPaneBackground(this);

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_PLAYLIST_DETAILS_PANE] Building {}", getId());

        logger.debug("[CM_PLAYLIST_DETAILS_PANE] Built {}", getId());
    }

    // ACCESSORS
    public PlaylistPane getPlaylistPane() {
        return parent;
    }
}

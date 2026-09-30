package creative.scenes.playlist;

import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

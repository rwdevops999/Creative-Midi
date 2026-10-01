package creative.scenes.playlist;

import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SongDetailsPane extends GridPane {
    private static final Logger logger = LoggerFactory.getLogger(SongDetailsPane.class);

    public SongDetailsPane() {
        super();

        setId("SongDetailsPane");
    }

    private PlaylistDetailsPane parent;
    public SongDetailsPane(PlaylistDetailsPane owner) {
        this();

        parent = owner;

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_SONG_DETAILS_PANE] Building {}", getId());

        logger.debug("[CM_SONG_DETAILS_PANE] Built {}", getId());
    }

    // ACCESSORS
    public PlaylistDetailsPane getPlaylistDetailsPane() {
        return parent;
    }
}

package creative.scenes.playlist;

import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SongMappingsPane extends StackPane {
    private static final Logger logger = LoggerFactory.getLogger(SongMappingsPane.class);

    public SongMappingsPane() {
        super();

        setId("SongMappingsPane");
    }

    private PlaylistDetailsPane parent;
    public SongMappingsPane(PlaylistDetailsPane owner) {
        this();

        parent = owner;

        buildPane();
    }

    public void buildPane() {
        logger.debug("[CM_SONG_MAPPINGS_PANE] Building {}", getId());

        logger.debug("[CM_SONG_MAPPINGS_PANE] Built {}", getId());
    }

    // ACCESSORS
    public PlaylistDetailsPane getPlaylistDetailsPane() {
        return parent;
    }
}

package creative.scenes.playlist;

import communication.CommunicationModel;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaylistPane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(PlaylistPane.class);

    public PlaylistPane() {
        super();

        setId("PlaylistPane");

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_PLAYLIST_PANE] Building {}", getId());

        CommunicationModel.setStatus("Let's play");

        logger.debug("[CM_PLAYLIST_PANE] Built {}", getId());
    }
}

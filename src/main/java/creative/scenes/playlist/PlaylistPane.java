package creative.scenes.playlist;

import communication.CommunicationModel;
import creative.scenes.SceneActionsPane;
import creative.scenes.playlist.consumer.*;
import entity.AEntity;
import entity.playlist.Song;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

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

        setLeft(new PlaylistListPane(this));
        setCenter(new PlaylistDetailsPane(this));

        BiConsumer<AEntity, Pane>[] consumers = new BiConsumer[]{
                new NewConsumer<Song, Pane>(),
                new AddConsumer<Song, Pane>(),
                new DeleteConsumer<Song, Pane>(),
                new UpdateConsumer<Song, Pane>(),
                new ExecuteConsumer<Song, Pane>(),
                new ExportConsumer<Song, Pane>()
        };

        SceneActionsPane actionsPane = new SceneActionsPane(this, "Execute", consumers, this::getEntity);
//        actionsPane.setDisabledButtons(new boolean[]{false, true, false, false, PlaylistContainer.containsSongs(), PlaylistContainer.containsSongs()});
        setRight(actionsPane);

        logger.debug("[CM_PLAYLIST_PANE] Built {}", getId());
    }

    public AEntity getEntity() {
        PlaylistListPane listPane = (PlaylistListPane)getLeft();
        return listPane.getSelectedSong();
    };

    // ACCESSORS
    public PlaylistListPane getPlaylistListPane() {
        return (PlaylistListPane) getLeft();
    }

    public PlaylistDetailsPane getPlaylistDetailsPane() {
        return (PlaylistDetailsPane) getCenter();
    }

    public SceneActionsPane getSceneActionsPane() {
        return (SceneActionsPane) getRight();
    }
}

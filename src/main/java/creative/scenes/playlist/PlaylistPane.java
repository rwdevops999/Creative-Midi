package creative.scenes.playlist;

import communication.CommunicationModel;
import creative.scenes.SceneActionsPane;
import creative.scenes.playlist.consumer.*;
import creative.scenes.playlist.statemachine.PlaylistStateMachine;
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

        if (! PlaylistContainer.isFileLoaded()) {
            PlaylistContainer.loadPlaylist();
        }

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_PLAYLIST_PANE] Building {}", getId());

        CommunicationModel.setStatus("Let's play");

        BiConsumer<AEntity, Pane>[] consumers = new BiConsumer[]{
                new NewConsumer<Song, Pane>(),
                new AddConsumer<Song, Pane>(),
                new DeleteConsumer<Song, Pane>(),
                new UpdateConsumer<Song, Pane>(),
                new ExecuteConsumer<Song, Pane>(),
                new ExportConsumer<Song, Pane>()
        };

        SceneActionsPane sceneActionsPane = new SceneActionsPane(this, "Execute", consumers, this::getEntity);
        setRight(sceneActionsPane);
        PlaylistStateMachine.setActionsPane(sceneActionsPane);

        setLeft(new PlaylistListPane(this));
        setCenter(new PlaylistDetailsPane(this));

        logger.debug("[CM_PLAYLIST_PANE] Built {}", getId());
    }

    public AEntity getEntity() {
        return getPlaylistDetailsPane().getSongDetailsPane().getSong();
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

    public SongDetailsPane getSongDetailsPane() {
        return getPlaylistDetailsPane().getSongDetailsPane();
    }

    public SongMappingsPane getSongMappingsPane() {
        return getPlaylistDetailsPane().getSongMappingsPane();
    }
}

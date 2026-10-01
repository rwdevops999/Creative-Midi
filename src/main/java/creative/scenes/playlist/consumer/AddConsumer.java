package creative.scenes.playlist.consumer;

import creative.scenes.SceneActionsPane;
import creative.scenes.playlist.PlaylistContainer;
import creative.scenes.playlist.PlaylistDetailsPane;
import creative.scenes.playlist.PlaylistPane;
import creative.scenes.playlist.statemachine.PlaylistState;
import entity.AEntity;
import entity.playlist.Song;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

public class AddConsumer<T extends AEntity, P extends Pane> implements BiConsumer<T, P> {
    private static final Logger logger = LoggerFactory.getLogger(AddConsumer.class);

    @Override
    public void accept(T entity, P pane) {
        logger.debug("[CM_ADD_CONSUMER<Playlist>] Handling Add Song");

        Song song = (Song)entity;

        PlaylistContainer.addSong(song);

        SceneActionsPane sceneActionsPane = (SceneActionsPane)pane;
        sceneActionsPane.getPlaylistPane().getPlaylistListPane().refreshList(song);

        sceneActionsPane.getPlaylistPane().getSongDetailsPane().setState(PlaylistState.FINISHED);
    }
}

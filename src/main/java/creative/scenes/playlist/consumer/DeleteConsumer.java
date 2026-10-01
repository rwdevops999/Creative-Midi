package creative.scenes.playlist.consumer;

import creative.scenes.SceneActionsPane;
import creative.scenes.playlist.PlaylistContainer;
import creative.scenes.playlist.PlaylistListPane;
import creative.scenes.playlist.PlaylistPane;
import creative.scenes.playlist.statemachine.PlaylistState;
import custom.dialog.DialogFactory;
import entity.AEntity;
import entity.playlist.Song;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

public class DeleteConsumer<T extends AEntity, P extends Pane> implements BiConsumer<T, P> {
    private static final Logger logger = LoggerFactory.getLogger(DeleteConsumer.class);

    @Override
    public void accept(T entity, P pane) {
        logger.debug("[CM_DELETE_CONSUMER<Playlist>] Handling Delete Song");

        Song song = (Song)entity;

        PlaylistContainer.deleteSongById(song.getSongId());

        SceneActionsPane sceneActionsPane = (SceneActionsPane)pane;
        sceneActionsPane.getPlaylistPane().getPlaylistListPane().refreshList(null);

        sceneActionsPane.getPlaylistPane().getSongDetailsPane().setSong(null);
        sceneActionsPane.getPlaylistPane().getSongDetailsPane().setState(PlaylistState.EMPTY);

        sceneActionsPane.getPlaylistPane().getSongMappingsPane().reset();
    }
}

package creative.scenes.playlist.consumer;

import creative.scenes.SceneActionsPane;
import creative.scenes.playlist.PlaylistContainer;
import creative.scenes.playlist.PlaylistDetailsPane;
import creative.scenes.playlist.PlaylistListPane;
import creative.scenes.playlist.PlaylistPane;
import creative.scenes.playlist.statemachine.PlaylistState;
import entity.AEntity;
import entity.playlist.Song;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

public class NewConsumer<T extends AEntity, P extends Pane> implements BiConsumer<T, P> {
    private static final Logger logger = LoggerFactory.getLogger(NewConsumer.class);

    @Override
    public void accept(T entity, P pane) {
        logger.debug("[CM_NEW_CONSUMER<Playlist>] Handling New Song");

        Song newSong = new Song(PlaylistContainer.getNextSongId());
        newSong.setSongName("Unknown");

        SceneActionsPane sceneActionsPane = (SceneActionsPane)pane;
        sceneActionsPane.getPlaylistPane().getSongDetailsPane().setSong(newSong);
        sceneActionsPane.getPlaylistPane().getSongDetailsPane().setState(PlaylistState.READY);
    }
}

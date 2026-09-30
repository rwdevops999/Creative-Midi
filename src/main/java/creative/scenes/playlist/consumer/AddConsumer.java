package creative.scenes.playlist.consumer;

import creative.scenes.playlist.PlaylistDetailsPane;
import creative.scenes.playlist.PlaylistPane;
import entity.AEntity;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

public class AddConsumer<T extends AEntity, P extends Pane> implements BiConsumer<T, P> {
    private static final Logger logger = LoggerFactory.getLogger(AddConsumer.class);

    @Override
    public void accept(T entity, P pane) {
        logger.debug("[CM_ADD_CONSUMER<Playlist>] Handling Add Song");

    }
}

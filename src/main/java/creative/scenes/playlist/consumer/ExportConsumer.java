package creative.scenes.playlist.consumer;

import custom.dialog.DialogFactory;
import entity.AEntity;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

public class ExportConsumer<T extends AEntity, P extends Pane> implements BiConsumer<T, P> {
    private static final Logger logger = LoggerFactory.getLogger(ExportConsumer.class);

    @Override
    public void accept(T entity, P pane) {
        logger.debug("[CM_EXPORT_CONSUMER<Playlist>] Handling Export Song");

    }
}

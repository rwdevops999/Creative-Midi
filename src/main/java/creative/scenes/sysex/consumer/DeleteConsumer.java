package creative.scenes.sysex.consumer;

import creative.scenes.SceneActionsPane;
import creative.scenes.sysex.SysexDetailsPane;
import creative.scenes.sysex.SysexPane;
import entity.AEntity;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

public class DeleteConsumer<T extends AEntity, P extends Pane> implements BiConsumer<T, P> {
    private static final Logger logger = LoggerFactory.getLogger(DeleteConsumer.class);
    @Override
    public void accept(T entity, P pane) {
        logger.debug("[CM_DELETE_CONSUMER<SysEx>] Handling Delete SysEx");

        SceneActionsPane sceneActionsPane = (SceneActionsPane) pane;
        SysexPane sysexPane = sceneActionsPane.getSysexPane();
    }
}

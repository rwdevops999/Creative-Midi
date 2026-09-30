package creative.scenes.sysex.consumer;

import creative.scenes.SceneActionsPane;
import creative.scenes.sysex.SysexContainer;
import creative.scenes.sysex.SysexPane;
import creative.scenes.sysex.data.SysexState;
import entity.AEntity;
import entity.sysex.Sysex;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

public class UpdateConsumer<T extends AEntity, P extends Pane> implements BiConsumer<T, P> {
    private static final Logger logger = LoggerFactory.getLogger(UpdateConsumer.class);
    @Override
    public void accept(T entity, P pane) {
        logger.debug("[CM_UPDATE_CONSUMER<SusEx>] Handling Update SysEx");

        SceneActionsPane sceneActionsPane = (SceneActionsPane) pane;
        SysexPane sysexPane = sceneActionsPane.getSysexPane();

        Sysex sysex = (Sysex) entity;
        SysexContainer.updateSysex(sysex);

        sysexPane.getSysexListPane().triggerReload(sysex);
        sysexPane.getSysexDetailsPane().getStateMachine().transitionTo(SysexState.UPDATED, true, sysexPane.getSysexDetailsPane().getSupplier());
    }
}

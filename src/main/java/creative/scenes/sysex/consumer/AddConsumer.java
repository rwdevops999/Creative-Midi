package creative.scenes.sysex.consumer;

import creative.scenes.SceneActionsPane;
import creative.scenes.sysex.SysexContainer;
import creative.scenes.sysex.SysexDetailsPane;
import creative.scenes.sysex.SysexListPane;
import creative.scenes.sysex.SysexPane;
import creative.scenes.sysex.data.SysexState;
import entity.AEntity;
import entity.sysex.Sysex;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

public class AddConsumer<T extends AEntity, P extends Pane> implements BiConsumer<T, P> {
    private static final Logger logger = LoggerFactory.getLogger(AddConsumer.class);

    @Override
    public void accept(T entity, P pane) {
        logger.debug("[CM_ADD_CONSUMER<SysEx>] Handling Add SysEx");

        SceneActionsPane sceneActionsPane = (SceneActionsPane) pane;
        SysexPane sysexPane = sceneActionsPane.getSysexPane();

        SysexDetailsPane sysexDetailsPane = sysexPane.getSysexDetailsPane();
        Sysex sysexToAdd = sysexDetailsPane.getSysex();

        SysexListPane sysexListPane = sysexPane.getSysexListPane();

        SysexContainer.addSysex(sysexToAdd);
        sysexListPane.triggerReload(sysexToAdd);
        sysexDetailsPane.getStateMachine().transitionTo(SysexState.ADDED, true, sysexDetailsPane.getSupplier());
    }
}

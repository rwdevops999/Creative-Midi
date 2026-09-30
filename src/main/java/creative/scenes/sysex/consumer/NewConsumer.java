package creative.scenes.sysex.consumer;

import creative.scenes.SceneActionsPane;
import creative.scenes.midi.MidiDetailPane;
import creative.scenes.sysex.SysexDetailsPane;
import creative.scenes.sysex.SysexPane;
import creative.scenes.sysex.SysexStateMachine;
import creative.scenes.sysex.data.SysexState;
import entity.AEntity;
import entity.midi.Midi;
import entity.sysex.Sysex;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

import java.util.function.BiConsumer;

public class NewConsumer<T extends AEntity, P extends Pane> implements BiConsumer<T, P> {
    private static final Logger logger = LoggerFactory.getLogger(NewConsumer.class);

    @Override
    public void accept(T entity, P pane) {
        logger.debug("[CM_NEW_CONSUMER<SysEx>] Handling New SysEx");

        SceneActionsPane sceneActionsPane = (SceneActionsPane) pane;
        SysexPane sysexPane = sceneActionsPane.getSysexPane();

        Sysex sysex = new Sysex("Unknown");

        ApplicationInfo.getInstance().setCurrentSysex(sysex);

        SysexDetailsPane sysexDetailsPane = new SysexDetailsPane(sysexPane, sysex);
        sysexPane.setCenter(sysexDetailsPane);

        SysexStateMachine stateMachine = sysexPane.getSysexDetailsPane().getStateMachine();
        stateMachine.transitionTo(SysexState.NEW, true, sysexPane.getSysexDetailsPane().getSupplier());
    }
}

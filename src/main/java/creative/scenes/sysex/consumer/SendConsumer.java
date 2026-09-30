package creative.scenes.sysex.consumer;

import creative.scenes.SceneActionsPane;
import creative.scenes.sysex.SysexPane;
import creative.scenes.sysex.util.SysexWriter;
import custom.dialog.DialogFactory;
import entity.AEntity;
import entity.sysex.Sysex;
import entity.sysex.SysexContent;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

public class SendConsumer<T extends AEntity, P extends Pane> implements BiConsumer<T, P> {
    private static final Logger logger = LoggerFactory.getLogger(SendConsumer.class);
    @Override
    public void accept(T entity, P pane) {
        logger.debug("[CM_SEND_CONSUMER<SysEx>] handling Send SysEx");

        SceneActionsPane sceneActionsPane = (SceneActionsPane) pane;
        SysexPane sysexPane = sceneActionsPane.getSysexPane();

        Sysex sysex = (Sysex)entity;

        for (SysexContent sysexContent : sysex.getList()) {
            if (sysexContent.getContent() == null || sysexContent.getContent().isEmpty()) {
                DialogFactory.renderErrorDialog("Can't send system exclusive. Content empty");
            } else {
                SysexWriter writer = new SysexWriter();
                writer.sendSysex(sysex);
            }
        }

    }
}

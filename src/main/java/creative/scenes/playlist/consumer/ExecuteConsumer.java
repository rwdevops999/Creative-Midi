package creative.scenes.playlist.consumer;

import communication.CommunicationModel;
import creative.scenes.SceneActionsPane;
import creative.scenes.playlist.entity.SharedEntity;
import creative.scenes.playlist.service.KeyboardService;
import custom.dialog.DialogFactory;
import entity.AEntity;
import javafx.concurrent.Service;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

import java.util.function.BiConsumer;

public class ExecuteConsumer<T extends AEntity, P extends Pane> implements BiConsumer<T, P> {
    private static final Logger logger = LoggerFactory.getLogger(ExecuteConsumer.class);
    @Override
    public void accept(T entity, P pane) {
        logger.debug("[CM_EXECUTE_CONSUMER<Playlist>] Handling Startup Playlist Thread");

        SceneActionsPane actionsPane = (SceneActionsPane) pane;

        if (ApplicationInfo.getInstance().getMidiInputDevice() == null) {
            DialogFactory.renderErrorDialog("Device is not selected");
        } else {
            Service<SharedEntity> service = ApplicationInfo.getInstance().getKeyboardService();
            if (service != null) {
                service.cancel();

                actionsPane.renameActionButtonName("Execute");

                ApplicationInfo.getInstance().setKeyboardService(null);
            } else {
                actionsPane.renameActionButtonName("Stop");

                service = new KeyboardService();
                ApplicationInfo.getInstance().setKeyboardService(service);
                service.start();

                // handle stuff from keyboard service
                service.valueProperty().addListener((observable, oldValue, newValue) -> {
                    // newValue = Shared Entity
                    if (newValue != null) {
                        CommunicationModel.monitorInbound(newValue.getValue());
                    }
                });
            }
        }
    }
}

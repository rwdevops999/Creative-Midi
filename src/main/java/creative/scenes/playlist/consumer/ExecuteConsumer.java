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

        if (ApplicationInfo.getInstance().getMidiInputDevice() == null) {
            DialogFactory.renderErrorDialog("Device is not selected");
        } else {
            logger.debug("[CM_EXECUTE_CONSUMER<Playlist>] Start keyboard service");
            Service<SharedEntity> service = new KeyboardService();
            service.start();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {

            }

            service.cancel();

            service.valueProperty().addListener((observable, oldValue, newValue) -> {
                // newValue = Shared Entity
                if (newValue != null) {
                    logger.debug("[CM_EXECUTE_CONSUMER<Playlist>] service received {}  -> FORWARD TO MONITOR",  newValue);

                }
            });
        }
    }
}

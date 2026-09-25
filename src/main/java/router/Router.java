package router;

import creative.scenes.IScene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

public class Router implements IRouter {
    private static final Logger logger = LoggerFactory.getLogger(Router.class);

    /**
     * Route from caller to called,
     *
     * @param cleanup: remove SPA
     */
    @Override
    public void routeTo(IScene callingScene, IScene calledScene, boolean cleanup) {
        logger.debug("[CM_ROUTER] Route To {}", calledScene.getClass().getName());

        Pane spa = ApplicationInfo.getInstance().getSpa();
        Pane spaOwner = (Pane)spa.getParent();

        if (cleanup)
        {
            if (spa != null) {
                spaOwner.getChildren().remove(spa);
            }
        }

        Pane pane = calledScene.render(callingScene);
        if (pane != null) {
            if (spaOwner instanceof BorderPane bp) {
                bp.setCenter(pane);
            } else {
                spaOwner.getChildren().add(pane);
            }
        }
    }
}

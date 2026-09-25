package creative.scenes.main;


import creative.scenes.IScene;
import creative.scenes.base.Base;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainScene implements IScene {
    private static final Logger logger = LoggerFactory.getLogger(MainScene.class);

    @Override
    public Pane render(IScene callingScene) {
        logger.debug("[CM_MAIN_SCENE] Starting MainScene.");

        try {
            return new Base("MainScene", new MainPane(this), callingScene, false);
        } catch (IllegalStateException e) {
            logger.error("[CM_MAIN_SCENE] EXCEPTION: In MainScene. CAUSE: {}", e.getMessage());
        }

        return null;
    }
}

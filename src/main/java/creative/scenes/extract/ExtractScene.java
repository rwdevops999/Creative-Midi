package creative.scenes.extract;

import creative.scenes.IScene;
import creative.scenes.base.Base;
import creative.scenes.merge.MergePane;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtractScene implements IScene {
    private static final Logger logger = LoggerFactory.getLogger(ExtractScene.class);

    @Override
    public Pane render(IScene callingScene) {
        logger.debug("[CM_EXTRACT_SCENE] Starting Extracting MIDI");

        return new Base("ExtractScene", new ExtractPane(), callingScene, true);
    }
}

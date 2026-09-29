package creative.scenes.sysexgen;

import creative.scenes.IScene;
import creative.scenes.base.Base;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysexGenScene implements IScene {
    private static final Logger logger = LoggerFactory.getLogger(SysexGenScene.class);

    @Override
    public Pane render(IScene callingScene) {
        logger.debug("[CM_SYSEX_GEN_SCENE] Starting Sysex Generator Scene");

        return new Base("SysexGenScene", new SysexGenPane(), callingScene, true);
    }
}

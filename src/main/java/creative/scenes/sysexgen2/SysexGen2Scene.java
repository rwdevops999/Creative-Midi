package creative.scenes.sysexgen2;

import creative.scenes.IScene;
import creative.scenes.base.Base;
import creative.scenes.sysexgen.SysexGenPane;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysexGen2Scene implements IScene {
    private static final Logger logger = LoggerFactory.getLogger(SysexGen2Scene.class);

    @Override
    public Pane render(IScene callingScene) {
        logger.debug("[CM_SYSEX_GEN2_SCENE] Starting Sysex Generator 2 Scene");

        return new Base("SysexGen2Scene", new SysexGen2Pane(), callingScene, true);
    }
}

package creative.scenes.sysex;

import creative.scenes.IScene;
import creative.scenes.base.Base;
import creative.scenes.midi.MidiPane;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysexScene implements IScene {
    private static final Logger logger = LoggerFactory.getLogger(SysexScene.class);

    @Override
    public Pane render(IScene callingScene) {
        logger.debug("[CM_SYSEX_SCENE] Starting Sysex Scene");

        return new Base("SysexScene", new SysexPane(), callingScene, true);
    }
}

package creative.scenes.test;

import creative.scenes.IScene;
import creative.scenes.base.Base;
import creative.scenes.midi.MidiPane;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestScene implements IScene {
    private static final Logger logger = LoggerFactory.getLogger(TestScene.class);

    @Override
    public Pane render(IScene callingScene) {
        logger.debug("[CM_TEST_SCENE] Starting Test Scene");

        return new Base("MidiScene", new MidiPane(), callingScene, true);
    }

    @Override
    public Pane renderScene(IScene callingScene, Pane pane) {
        logger.debug("[CM_TEST_SCENE] Starting Test Scene");

        return new Base("MidiScene", pane, callingScene, true);
    }
}

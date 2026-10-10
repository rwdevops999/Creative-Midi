package creative.scenes.merge;

import creative.scenes.IScene;
import creative.scenes.base.Base;
import creative.scenes.midi.MidiPane;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MergeScene implements IScene {
    private static final Logger logger = LoggerFactory.getLogger(MergeScene.class);

    @Override
    public Pane render(IScene callingScene) {
        logger.debug("[CM_MIDI_SCENE] Starting Merge Scene");

        return new Base("MergeScene", new MergePane(true), callingScene, true);
    }
}

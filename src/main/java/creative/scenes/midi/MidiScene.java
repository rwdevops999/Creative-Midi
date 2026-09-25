package creative.scenes.midi;

import creative.scenes.IScene;
import creative.scenes.base.Base;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MidiScene implements IScene {
    private static final Logger logger = LoggerFactory.getLogger(MidiScene.class);

    @Override
    public Pane render(IScene callingScene) {
        logger.debug("[CM_MIDI_SCENE] Starting Midi Scene");

        return new Base("MidiScene", new MidiPane(), callingScene, true);
    }
}

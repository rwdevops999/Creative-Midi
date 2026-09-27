package creative.scenes.voice;

import creative.scenes.IScene;
import creative.scenes.base.Base;
import creative.scenes.midi.MidiPane;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoiceScene implements IScene {
    private static final Logger logger = LoggerFactory.getLogger(VoiceScene.class);

    @Override
    public Pane render(IScene callingScene) {
        logger.debug("[CM_MIDI_SCENE] Starting Voice Scene");

        return new Base("VoiceScene", new VoicePane(), callingScene, true);
    }
}

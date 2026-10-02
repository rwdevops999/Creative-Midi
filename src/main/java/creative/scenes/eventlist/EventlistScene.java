package creative.scenes.eventlist;

import creative.scenes.IScene;
import creative.scenes.base.Base;
import creative.scenes.midi.MidiPane;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventlistScene implements IScene {
    private static final Logger logger = LoggerFactory.getLogger(EventlistScene.class);

    @Override
    public Pane render(IScene callingScene) {
        logger.debug("[CM_EVENT_LIST_SCENE] Starting Event List");

        return new Base("EventlistScene", new EventlistPane(), callingScene, true);
    }
}

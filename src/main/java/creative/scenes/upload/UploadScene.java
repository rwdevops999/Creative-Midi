package creative.scenes.upload;

import creative.scenes.IScene;
import creative.scenes.base.Base;
import creative.scenes.merge.MergePane;
import creative.scenes.midi.MidiPane;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadScene implements IScene {
    private static final Logger logger = LoggerFactory.getLogger(UploadScene.class);

    @Override
    public Pane render(IScene callingScene) {
        logger.debug("[CM_UPLOAD_SCENE] Starting Upload Scene");

        return new Base("UploadSene", new MergePane(false), callingScene, true);
    }
}

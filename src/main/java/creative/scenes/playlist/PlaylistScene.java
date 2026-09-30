package creative.scenes.playlist;

import creative.scenes.IScene;
import creative.scenes.base.Base;
import creative.scenes.midi.MidiPane;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaylistScene implements IScene {
    private static final Logger logger = LoggerFactory.getLogger(PlaylistScene.class);

    @Override
    public Pane render(IScene callingScene) {
        logger.debug("[CM_PLAYLIST_SCENE] Starting Playlist Scene");

        return new Base("PlaylistScene", new PlaylistPane(), callingScene, true);
    }
}

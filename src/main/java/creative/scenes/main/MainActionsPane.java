package creative.scenes.main;

import creative.scenes.IScene;
import creative.scenes.midi.MidiScene;
import custom.components.ActionButton;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActionsPane extends GridPane {
    private static final Logger logger = LoggerFactory.getLogger(MainActionsPane.class);

    public MainActionsPane() {
        super();

        setId("MainActionsPane");

        setHgap(10);
        setVgap(10);

        setPadding(new Insets(10));
    }

    public MainActionsPane(IScene ownerScene) {
        this();

        logger.debug("[CM_ACTIONS_PANE] Building {}", getId());

        add(new ActionButton(ownerScene, "midiButton", "handle midi", "midi_out", new MidiScene()), 0, 0);
/*        add(new ActionButton(ownerScene,"sysexButton", "handle sysex", "sysex_out", new SysExScene()), 1, 0);
        add(new ActionButton(ownerScene, "playlistButton", "handle playlist", "playlist", new PlaylistScene()), 2, 0);
        add(new ActionButton(ownerScene, "voiceButton", "handle voice", "voice_search", new VoiceScene()), 3, 0);
        add(new ActionButton(ownerScene, "sysexgen2Button", "Generate SysEx", "sysex-generator", new SysExGen3Scene()), 4, 0);
        add(new ActionButton(ownerScene, "convertButton", "handle convert", "convert", new ConvertScene()), 5, 0);
        add(new ActionButton(ownerScene, "setupButton", "handle setup", "setup", new SetupScene()), 6, 0);

        add(new ActionButton(ownerScene, "eventlistButton", "midi event list", "event-list", new EventListScene()), 0, 1);

        if (Globals.isTestMode()) {
            add(new ActionButton(ownerScene, "test1Button", "test 1", "test1", new Test1()), 3, 1);
            add(new ActionButton(ownerScene, "test2Button", "test 2", "test2", new Test2()), 4, 1);
            add(new ActionButton(ownerScene, "test3Button", "test 3", "test3", new Test3()), 5, 1);
            add(new ActionButton(ownerScene, "test4Button", "test 4", "test4", new Test4()), 6, 1);
        }
*/
        logger.debug("[CM_ACTIONS_PANE] Built {}", getId());
    }
}

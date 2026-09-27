package creative.scenes.main;

import creative.scenes.IScene;
import creative.scenes.midi.MidiScene;
import creative.scenes.voice.VoiceScene;
import custom.components.ActionButton;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

public class MainActionsPane extends GridPane {
    private static final Logger logger = LoggerFactory.getLogger(MainActionsPane.class);

    public MainActionsPane() {
        super();

        setId("MainActionsPane");

        int totalColumns = 10;
        double percentagePerColumn = 100.0 / totalColumns; // Dit is ~8.3333%

        for (int i = 0; i < totalColumns; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(percentagePerColumn);
            col.setHalignment(HPos.LEFT);
            getColumnConstraints().add(col);
        }

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.CENTER);
        getRowConstraints().add(rowConstraints);

        setHgap(10);
        setVgap(10);

        setPadding(new Insets(10));
    }

    private static final int SIZE=64;

    public MainActionsPane(IScene ownerScene) {
        this();

        logger.debug("[CM_ACTIONS_PANE] Building {}", getId());

        add(new ActionButton(ownerScene, SIZE, "midiButton", "handle midi", "midi_out", new MidiScene()), 0, 0);
        add(new ActionButton(ownerScene, SIZE, "voiceButton", "handle voice", "voice_search", new VoiceScene()), 1, 0);

/*        add(new ActionButton(ownerScene,"sysexButton", "handle sysex", "sysex_out", new SysExScene()), 1, 0);
        add(new ActionButton(ownerScene, "playlistButton", "handle playlist", "playlist", new PlaylistScene()), 2, 0);
        add(new ActionButton(ownerScene, "sysexgen2Button", "Generate SysEx", "sysex-generator", new SysExGen3Scene()), 4, 0);
        add(new ActionButton(ownerScene, "convertButton", "handle convert", "convert", new ConvertScene()), 5, 0);
        add(new ActionButton(ownerScene, "setupButton", "handle setup", "setup", new SetupScene()), 6, 0);

        add(new ActionButton(ownerScene, "eventlistButton", "midi event list", "event-list", new EventListScene()), 0, 1);
*/
        if (ApplicationInfo.getInstance().isTestMode()) {
            add(new ActionButton(ownerScene, SIZE, "test1Button", "test 1", "test1", "Test1"), 3, 1);
            add(new ActionButton(ownerScene, SIZE, "test2Button", "test 2", "test2", "Test2"), 4, 1);
            add(new ActionButton(ownerScene, SIZE, "test3Button", "test 3", "test3", "Test3"), 5, 1);
            add(new ActionButton(ownerScene, SIZE, "test4Button", "test 4", "test4", "Test4"), 6, 1);
        }

        logger.debug("[CM_ACTIONS_PANE] Built {}", getId());
    }
}

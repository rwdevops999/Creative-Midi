package creative.scenes.midi;

import creative.scenes.SceneActionsPane;
import creative.scenes.midi.provider.MidiProvider;
import entity.midi.Midi;
import entity.midi.NoteEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;
import util.Util;

import java.util.ArrayList;
import java.util.List;

public class MidiListPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(MidiListPane.class);

    private ListView<Midi> midiListView = null;

    public MidiListPane() {
        super();

        setId("MidiListPane");
    }

    private MidiPane parent;
    public MidiListPane(MidiPane parent) {
        this();

        this.parent = parent;

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_MIDI_LIST_PANE] Building {}", getId());

        midiListView = new ListView<Midi>();
        updateMidiList();
        midiListView.setCellFactory(param -> new ListCell<Midi>() {
            @Override
            protected void updateItem(Midi midi, boolean empty) {
                super.updateItem(midi, empty);

                if (empty || midi == null) {
                    setText(null);
                } else {
                    // Hier kies je welke variabele je wilt tonen
                    setText(midi.getName());
                }
            }
        });

        midiListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ApplicationInfo.getInstance().setCurrentMidi(newValue);
                MidiDetailPane midiDetailPane = new MidiDetailPane(parent);
                parent.setCenter(midiDetailPane);
                int enables = SceneActionsPane.BUTTON[SceneActionsPane.BUTTON_NEW] |
                        SceneActionsPane.BUTTON[SceneActionsPane.BUTTON_DELETE] |
                        SceneActionsPane.BUTTON[SceneActionsPane.BUTTON_ACTION];

                parent.getActionsPane().setEnables(enables);
            }
        });

        VBox.setVgrow(midiListView, Priority.ALWAYS);
        getChildren().add(midiListView);

        logger.debug("[CM_MIDI_LIST_PANE] Built {}", getId());
    }

    public void updateMidiList() {
        MidiProvider midiProvider = ApplicationInfo.getInstance().getMidiProvider();
        List<Midi> midis = new ArrayList<>();
        if (midiProvider != null) {
            midis = midiProvider.getMidis();
        }

        midiListView.setItems(FXCollections.observableList(midis));
    }

    // ACCESSOR
    public MidiPane getMidiPane() {
        return parent;
    }
}

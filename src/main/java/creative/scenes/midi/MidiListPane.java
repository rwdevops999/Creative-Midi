package creative.scenes.midi;

import creative.scenes.midi.provider.MidiProvider;
import entity.midi.Midi;
import entity.midi.NoteEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;
import util.Util;

import java.util.ArrayList;
import java.util.List;

public class MidiListPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(MidiListPane.class);

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

        ListView<Midi> midiListView = new ListView<Midi>();

        MidiProvider midiProvider = ApplicationInfo.getInstance().getMidiProvider();
        List<Midi> midis = new ArrayList<>();
        if (midiProvider != null) {
            midis = midiProvider.getMidis();
        }

        midiListView.setItems(FXCollections.observableList(midis));
        midiListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        });

        VBox.setVgrow(midiListView, Priority.ALWAYS);
        getChildren().add(midiListView);

        logger.debug("[CM_MIDI_LIST_PANE] Built {}", getId());
    }
}

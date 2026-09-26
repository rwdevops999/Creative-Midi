package creative.scenes.midi;

import creative.scenes.midi.data.MessageType;
import creative.scenes.midi.handler.ChangedHandler;
import custom.components.HexTextField;
import custom.components.midi.MidiEntrySelect;
import entity.midi.Midi;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;
import util.ColorScheme;

import java.util.concurrent.atomic.AtomicInteger;

import static util.Util.setPaneBackground;

public class MidiDetailPane extends GridPane {
    private static final Logger logger = LoggerFactory.getLogger(MidiDetailPane.class);

    private ObjectProperty<Integer> channel = new SimpleObjectProperty<>(0);

    private ChangedHandler changedHandler = new ChangedHandler();

    public MidiDetailPane() {
        super();

        setId("MidiDetailPane");

        int[] columnSizes = {10,10,10,10,10,10,10,10,10,10,10,10};
        for (int size: columnSizes) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(size);
            col.setHalignment(HPos.LEFT);
            getColumnConstraints().add(col);
        }

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.CENTER);
        getRowConstraints().add(rowConstraints);

        setPadding(new Insets(5));
        setVgap(10);
    }

    private MidiPane parent;
    public MidiDetailPane(MidiPane parent) {
        this();

        this.parent = parent;

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_MIDI_DETAIL_PANE] Building {}", getId());

        removeAllChildren();

        setPaneBackground(this);

        Midi currentMidi = ApplicationInfo.getInstance().getCurrentMidi();
        if (currentMidi != null) {
            AtomicInteger row = new AtomicInteger(0);

            // ROW0
            Label midiName = new Label("Name:");
            add(midiName, 0, row.get());

            TextField midiNameInputField = new TextField();
            midiNameInputField.textProperty().bindBidirectional(currentMidi.getNameProperty());
            midiNameInputField.setPromptText("name...");
            midiNameInputField.textProperty().addListener((observable, oldValue, newValue) -> {
               changedHandler.handle(new ActionEvent());
            });
            add(midiNameInputField, 1, row.get(), 5, 1);

            // ROW1
            row.getAndIncrement();
            Label statusLabel = new Label("Status:");
            add(statusLabel, 0, row.get());

            HexTextField hexInput = new HexTextField();
            hexInput.setPromptText("status");
            hexInput.textProperty().bindBidirectional(currentMidi.getStatusProperty());
            hexInput.textProperty().addListener((observable, oldValue, newValue) -> {
                changedHandler.handle(new ActionEvent());
            });
            add(hexInput, 1, row.get(), 2, 1);

            ToggleGroup group = new ToggleGroup();

            // ROW2
            row.getAndIncrement();
            RadioButton systemRadio = new RadioButton("System Message");
            systemRadio.setToggleGroup(group);
            systemRadio.setSelected(true);
            add(systemRadio, 1, row.get(), 3, 1);

            // ROW3
            row.getAndIncrement();
            RadioButton channelRadio = new RadioButton("Channel Message");
            channelRadio.setToggleGroup(group);
            channelRadio.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    handleChannelMessageSelected(row.get(), currentMidi);
                } else {
                    removeDeletables();
                }
            });
            add(channelRadio, 1, row.get(), 3, 1);

            if (currentMidi.getMessageType() != null) {
                if (currentMidi.getMessageType().name().equals("system")) {
                    systemRadio.setSelected(true);
                } else {
                    channelRadio.setSelected(true);
                }
            }
            group.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle != null) {
                    currentMidi.getMessageTypeProperty().set((MessageType)newToggle.getUserData());
                    changedHandler.handle(new ActionEvent());
                }
            });

            currentMidi.getMessageTypeProperty().addListener((obs, oldMode, newMode) -> {
                group.getToggles().stream()
                        .filter(t -> t.getUserData() == newMode)
                        .findFirst()
                        .ifPresent(group::selectToggle);
            });
/*
            if (currentMidi.getByte2() != null) {
                row.getAndAdd(7);
                renderByteEntry(2, currentMidi, row.get());
            } */
        }

        logger.debug("[CM_MIDI_DETAIL_PANE] Built {}", getId());
    }

    private void handleChannelMessageSelected(int currentRow, Midi currentMidi) {
        currentRow++;
        renderChannelSelection(currentRow, currentMidi);
//        currentRow++;
//        renderByteEntry(1, currentMidi, currentRow);
    }

    private void renderChannelSelection(int onRow, Midi currentMidi) {
        Label channelLabel = new Label("Channel:");
        channelLabel.setId("Deletable");
        add(channelLabel, 0, onRow, 2, 1);

        ComboBox<Integer> channelSelect = new ComboBox<>();
        channelSelect.setId("Deletable");
        for (int i = 0; i < 16; i++) {
            channelSelect.getItems().add(i);
        }

        channelSelect.valueProperty().bindBidirectional(currentMidi.getChannelProperty());
        add(channelSelect, 1, onRow, 2, 1);
    }

    private void renderByteEntry(int byteId, Midi currentMidi, int onRow) {
        MidiEntrySelect byteSelect = new MidiEntrySelect(byteId);
        if (byteId == 1) {
            byteSelect.byteTypeValue.bindBidirectional(currentMidi.getByte1TypeProperty());
            byteSelect.byteValue.bindBidirectional(currentMidi.getByte1Property());
            byteSelect.showRemoveButton(false);
            byteSelect.setAddHandler(e -> {
                renderByteEntry(byteId + 1, currentMidi, onRow + 5);
            });
        } else if (byteId == 2) {
            byteSelect.showAddButton(false);
            byteSelect.byteTypeValue.bindBidirectional(currentMidi.getByte2TypeProperty());
            byteSelect.byteValue.bindBidirectional(currentMidi.getByte2Property());
            byteSelect.setRemoveHandler(e -> {
                Button btn = (Button)e.getSource();
                MidiEntrySelect src = (MidiEntrySelect)btn.getParent();
                getChildren().remove(src);
            });
        }
        byteSelect.setId("Deletable");
        add(byteSelect, 0, onRow, 12, onRow);
    }

    private void removeDeletables() {
        getChildren().removeIf(node -> "Deletable".equals(node.getId()));
    }

    private void removeAllChildren() {
        getChildren().clear();
    }
}

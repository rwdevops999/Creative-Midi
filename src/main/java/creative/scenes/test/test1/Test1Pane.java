package creative.scenes.test.test1;

import communication.CommunicationModel;
import custom.components.HexTextField;
import custom.components.midi.MidiEntrySelect;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

import static util.ColorScheme.getColor;
import static util.Util.setPaneBackground;

public class Test1Pane extends GridPane {
    private static final Logger logger = LoggerFactory.getLogger(Test1Pane.class);
    public Test1Pane() {
        super();

        setId("Test1");
        CommunicationModel.setStatus("Running Test1");

        setupTest();
        runTest();
    }

    private void setupTest() {
        int[] columnSizes = {5,5,5,5,5,5,5,5,5,5,5,5};
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

    private void runTest() {
        logger.debug("[CM_TEST1_PANE] Executing {}", getId());

        setPaneBackground(this);

        AtomicInteger row = new AtomicInteger(0);

        // ROW0
        Label midiName = new Label("Name:");
        add(midiName, 0, row.get());

        TextField midiNameInputField = new TextField();
        add(midiNameInputField, 1, row.get(), 5, 1);

        // ROW1
        row.getAndIncrement();
        Label statusLabel = new Label("Status:");
        add(statusLabel, 0, row.get());

        HexTextField hexInput = new HexTextField();
        hexInput.setPromptText("status");
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
                handleChannelMessageSelected(row.get());
            } else {
                removeDeletables();
            }
        });
        add(channelRadio, 1, row.get(), 3, 1);

        logger.debug("[CM_TEST1_PANE] Executed {}", getId());
    }

    private void handleChannelMessageSelected(int currentRow) {
        currentRow++;
        renderChannelSelection(currentRow);
        currentRow++;
        renderByteEntry(1, currentRow);
    }

    private ObjectProperty<Integer> channel = new SimpleObjectProperty<>(0);
    public void setChannel(int channel) {
        this.channel.setValue(channel);
    }

    public int getChannel() {
        return this.channel.getValue();
    }

    private void renderChannelSelection(int onRow) {
        Label channelLabel = new Label("Channel:");
        channelLabel.setId("Deletable");
        add(channelLabel, 0, onRow, 2, 1);

        ComboBox<Integer> channelSelect = new ComboBox<>();
        channelSelect.setId("Deletable");
        for (int i = 0; i < 16; i++) {
            channelSelect.getItems().add(i);
        }

        channelSelect.valueProperty().bindBidirectional(channel);
        add(channelSelect, 2, onRow, 2, 1);
    }

    private void removeDeletables() {
        getChildren().removeIf(node -> "Deletable".equals(node.getId()));
    }

    private void renderByteEntry(int byteId, int onRow) {
        MidiEntrySelect byteSelect = new MidiEntrySelect(byteId);
        if (byteId == 1) {
            byteSelect.showRemoveButton(false);
            byteSelect.setAddHandler(e -> {
                renderByteEntry(byteId + 1, onRow + 5);
            });
        } else if (byteId == 2) {
            byteSelect.showAddButton(false);
            byteSelect.setRemoveHandler(e -> {
                Button btn = (Button)e.getSource();
                MidiEntrySelect src = (MidiEntrySelect)btn.getParent();
                getChildren().remove(src);
            });
        }
        byteSelect.setId("Deletable");
        add(byteSelect, 0, onRow, 12, onRow);
    }
}

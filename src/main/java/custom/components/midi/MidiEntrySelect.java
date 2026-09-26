package custom.components.midi;

import creative.scenes.midi.convertor.MinPlusConvertor;
import creative.scenes.midi.convertor.PanningConvertor;
import creative.scenes.midi.convertor.SplitConvertor;
import creative.scenes.midi.data.ByteType;
import custom.components.OnOffSwitch;
import entity.midi.NoteEntity;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.StringConverter;
import util.Util;
import util.properties.PropertyContainer;
import util.properties.PropertyType;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static util.ColorScheme.getColor;
import static util.Util.calcNote;

public class MidiEntrySelect extends GridPane {
    public ObjectProperty<Integer> byte1Value = new SimpleObjectProperty<>(0);

    public MidiEntrySelect() {
        super();

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
    }

    private Button addButton;
    private Button removeButton;

    private EventHandler<ActionEvent> addHandler = null;
    public void setAddHandler(EventHandler<ActionEvent> addHandler) {
        this.addHandler = addHandler;
        if (addButton != null) {
            addButton.setOnAction(addHandler);
        }
    }

    private EventHandler<ActionEvent> removeHandler = null;
    public void setRemoveHandler(EventHandler<ActionEvent> removeHandler) {
        this.removeHandler = removeHandler;
        if (removeButton != null) {
            removeButton.setOnAction(removeHandler);
        }
    }

    private BooleanProperty addButtonShow = new SimpleBooleanProperty(true);
    public void showAddButton(boolean show) {
        addButtonShow.set(show);
    }

    private BooleanProperty removeButtonShow = new SimpleBooleanProperty(true);
    public void showRemoveButton(boolean show) {
        removeButtonShow.set(show);
    }

    public MidiEntrySelect(int buttonId) {
        this();

        buildComponent(buttonId, null, null);
    }

    public MidiEntrySelect(int buttonId, EventHandler<ActionEvent> addHandler, EventHandler<ActionEvent> removeHandler) {
        this();

        buildComponent(buttonId, addHandler, removeHandler);
    }

    private void buildComponent(int buttonId, EventHandler<ActionEvent> addHandler, EventHandler<ActionEvent> removeHandler) {
        String flatButtonStyle =
                "-fx-background-color: #a9a9a9; " + // Gray background (or 'transparent')
                        "-fx-background-radius: 0; " +       // Straight corners
                        "-fx-text-fill: #333333; " +         // Text color
                        "-fx-font-size: 14px; " +            // Text size
                        "-fx-font-weight: bold; " +          // Bold text
                        "-fx-cursor: hand;";

        AtomicInteger row = new AtomicInteger(-1);

        // ROW0
        row.getAndIncrement();

        addButton = new Button("+");
        addButton.setStyle(flatButtonStyle);
        addButton.visibleProperty().bind(addButtonShow);
        add(addButton, 0, row.get());
        if (addHandler != null) {
            addButton.setOnAction(addHandler);
        }

        Label byteLabel = new Label("Type:");
        add(byteLabel, 1, row.get());

        ComboBox<String> byteTypeBox = new ComboBox<>();
        byteTypeBox.setMaxWidth(Double.MAX_VALUE);
        Arrays.stream(ByteType.values()).forEach(type -> byteTypeBox.getItems().add(type.name().replaceAll("(?<!^)(?=[A-Z])", " ")));
        byteTypeBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            removeDeletables();
            handleTypeChange(buttonId, row.get(), newValue);
        });
        add(byteTypeBox, 3, row.get(), 5, 1);

        removeButton = new Button("-");
        removeButton.setStyle(flatButtonStyle);
        removeButton.visibleProperty().bind(removeButtonShow);
        add(removeButton, 8, row.get());
        if (removeHandler != null) {
            removeButton.setOnAction(removeHandler);
        }
    }

    private void removeDeletables() {
        getChildren().removeIf(node -> "Deletable".equals(node.getId()));
    }

    private void handleTypeChange(int buttonId, int currentRow, String newType) {
        ByteType type = ByteType.valueOf(newType.replace(" ", ""));

        switch (type) {
            case Continuous -> handleContinuous(currentRow, 0, 127, buttonId, null);
            case ContinuousRange -> handleContinuous(currentRow, 0, 16, buttonId, null);
            case DefaultValue -> handleDefaultValue(currentRow, buttonId, null);
            case OnOff -> handleOnOff(currentRow, buttonId, null);
            case Panning -> handlePanning(currentRow, 0, 127, buttonId, null);
            case ContinuousMinusPlus -> handleMinPlus(currentRow, -64, 63, buttonId, null);
            case ContinuousOnOff -> handleContinousOnOff(currentRow, 0, 127, buttonId, null);
            case Key -> handleKey(currentRow, buttonId, "Note");
        };
    }

    private void handleContinuous(int currentRow, int min, int max, int buttonId, String label) {
        currentRow++;

        renderByteLabel(currentRow, buttonId, label);
        renderSpinner(currentRow, min, max);
        renderSlider(currentRow+1, min, max, null);
    }

    private void handleDefaultValue(int currentRow, int buttonId, String label) {
        currentRow++;

        renderByteLabel(currentRow, buttonId, label);
        renderSpinner(currentRow, 0, 127);
        renderSlider(currentRow, 0, 127, null);
    }

    private void handleOnOff(int currentRow, int buttonId, String label) {
        currentRow++;

        renderByteLabel(currentRow, buttonId, label);

        OnOffSwitch onoffSwitch = new OnOffSwitch();
        onoffSwitch.setId("Deletable");
        bindBidirectional(
                onoffSwitch.switchedOnProperty().asObject(), // Property<Boolean>
                byte1Value,                    // Property<Integer>
                boolVal -> boolVal ? 127 : 0,   // Boolean -> Integer
                intVal -> intVal == null ? false : (intVal.equals(0) ? false : true)        // Integer -> Boolean
        );

        add(onoffSwitch, 3, currentRow);
    }

    private void handlePanning(int currentRow, int min, int max, int buttonId, String label) {
        currentRow++;

        renderByteLabel(currentRow, buttonId, label);
        renderSpinner(currentRow, min, max);
        renderSlider(currentRow+1, min, max, "panning");
    }

    private void handleMinPlus(int currentRow, int min, int max, int buttonId, String label) {
        currentRow++;

        renderByteLabel(currentRow, buttonId, label);
        renderSpinner(currentRow, min, max);
        renderSlider(currentRow+1, min, max, "minplus");
    }

    private void handleContinousOnOff(int currentRow, int min, int max, int buttonId, String label) {
        currentRow++;

        renderByteLabel(currentRow, buttonId, label);
        renderSpinner(currentRow, min, max);
        renderSlider(currentRow+1, min, max, "splitted");
    }

    private void handleKey(int currentRow, int buttonId, String label) {
        currentRow++;

        renderByteLabel(currentRow, buttonId, label);
        renderNotesCombo(currentRow);
    }

    private void renderByteLabel(int currentRow, int buttonId, String label) {
        if (label == null)  {
            label = "Byte" + buttonId + ":";
        }

        Label byte1Label = new Label(label);
        byte1Label.setId("Deletable");
        add(byte1Label, 1, currentRow, 2, 1);
    }

    private void renderSpinner(int currentRow, int min, int max) {
        Spinner<Integer> spinner = new Spinner<>(min, max, 0);
        spinner.setId("Deletable");
        spinner.getValueFactory().valueProperty().bindBidirectional(byte1Value);
        add(spinner, 3, currentRow, 2, 1);
    }

    private void renderSlider(int currentRow, int min, int max, String subtype) {
        currentRow++;
        Slider slider = new Slider(min, max, 0);
        slider.setId("Deletable");
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(63);

        bindBidirectional(
                slider.valueProperty().asObject(), // Property<Double>
                byte1Value,                    // Property<Integer>
                doubleVal -> doubleVal == null ? 0 : doubleVal.intValue(),   // Double -> Integer
                intVal -> intVal == null ? 0.0 : intVal.doubleValue()        // Integer -> Double
        );

        if (subtype != null) {
            switch (subtype) {
                case "panning" -> slider.setLabelFormatter(new PanningConvertor());
                case "minplus" -> slider.setLabelFormatter(new MinPlusConvertor(min, max));
                case "splitted" -> {
                    slider.setLabelFormatter(new SplitConvertor(min, max));
                    String css = Objects.requireNonNull(getClass().getResource("/styles/gradient.css")).toExternalForm();
                    slider.getStylesheets().add(css);
                }
            }
        }

        add(slider, 1, currentRow, 7, 1);
    }

    private void renderNotesCombo(int currentRow) {
//        Integer baseOctave = PropertyContainer.getPropertyAsInteger(PropertyType.Keyboard, "base.octave", 0);
        Integer baseOctave = PropertyContainer.getPropertyAsInteger(PropertyType.System, "base.octave", -1);
        ObservableList<NoteEntity> notes = FXCollections.observableList(Util.calcNotes(baseOctave));

        ComboBox<NoteEntity> noteSelect = new ComboBox<>(notes);
        noteSelect.setId("Deletable");
        noteSelect.setConverter(new StringConverter<NoteEntity>() {
            @Override
            public String toString(NoteEntity entity) {
                // Return the name property if the item isn't null
                return (entity != null) ? entity.getName() : "";
            }

            @Override
            public NoteEntity fromString(String string) {
                // Leave this null because the ComboBox is read-only
                return null;
            }
        });

        noteSelect.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            byte1Value.set(newValue.getId());
        });

        noteSelect.getSelectionModel().select(calcNote(byte1Value.get(), baseOctave));

        add(noteSelect, 3, currentRow, 3, 1);
    }

    private <A, B> void bindBidirectional(Property<A> propertyA, Property<B> propertyB, java.util.function.Function<A, B> toB, java.util.function.Function<B, A> toA) {
        ChangeListener<A> listenerA = new ChangeListener<>() {
            private boolean updating = false;
            @Override
            public void changed(ObservableValue<? extends A> obs, A oldVal, A newVal) {
                if (!updating) {
                    try {
                        updating = true;
                        propertyB.setValue(toB.apply(newVal));
                    } finally {
                        updating = false;
                    }
                }
            }
        };

        ChangeListener<B> listenerB = new ChangeListener<>() {
            private boolean updating = false;
            @Override
            public void changed(ObservableValue<? extends B> obs, B oldVal, B newVal) {
                if (!updating) {
                    try {
                        updating = true;
                        propertyA.setValue(toA.apply(newVal));
                    } finally {
                        updating = false;
                    }
                }
            }
        };

        propertyA.addListener(listenerA);
        propertyB.addListener(listenerB);
    }
}

package creative.scenes.sysexgen2;

import creative.scenes.sysexgen2.entity.Event;
import creative.scenes.sysexgen2.entity.EventType;
import creative.scenes.sysexgen2.listener.SelectionHandler;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static util.ColorScheme.getColor;
import static util.Constants.APP_WIDTH;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;

public class SysexGenSelectionPane extends GridPane {
    private static final Logger logger = LoggerFactory.getLogger(SysexGenSelectionPane.class);

    public SysexGenSelectionPane() {
        super();

        setId("SysExGenSelectionPane");
        setHgap(6);
        setVgap(3);
        setPadding(new Insets(5));

        setMaxWidth(Double.MAX_VALUE);
    }

    private SysexGen2Pane parent;

    private double singleColumnPixelWidth;

    private static final int NUM_COLS=24;

    private Event rootEvent;
    public SysexGenSelectionPane(SysexGen2Pane owner, Event event) {
        this();

        this.parent = owner;
        this.rootEvent = event;

        this.singleColumnPixelWidth = (double)APP_WIDTH / NUM_COLS;

        // Put the grid width within the enclosing BorderPane
        this.setPrefWidth(APP_WIDTH);
        this.setMaxWidth(APP_WIDTH);

        // Create thee column-constraints based on percentages
        this.getColumnConstraints().clear();
        double percentagePerColumn = 100.0 / NUM_COLS;

        for (int i = 0; i < NUM_COLS; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(percentagePerColumn);
            col.setHalignment(HPos.LEFT);
            this.getColumnConstraints().add(col);
        }

        // 5. Add a basic row-constraint for each vertical row
        this.getRowConstraints().clear();
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.CENTER);
        this.getRowConstraints().add(rowConstraints);

        showPaneBorder(this, getColor("border", "green"));

        setPaneBackground(this);

        buildPane();
    }

    private int nextColumn = 0;
    private int nextRow = 0;

    private void buildPane() {
        logger.debug("[CM_SYSEX_GEN_SELECTION_PANE] Building {}", getId());

        getChildren().clear();

        nextColumn = 0;
        nextRow = 0;

        Event event = rootEvent;

        while (event != null) {
            final Event currentEvent = event;

            if ("XG Change Parameter".equals(currentEvent.getName())) {
                nextRow += 1;
                nextColumn = 0;
                    add(new javafx.scene.control.Separator(), nextColumn, nextRow, (24 - nextColumn), 1);

                nextRow += 1;
            }

            if (!currentEvent.hasChildren()) {
                handleLeafEvent(currentEvent, false);
                break;
            }

            // Handle if the event has children
            if ("Vendor".equals(currentEvent.getName())) {
                System.out.println("BREAK");
            }

            nextColumn = populateLabelAndComboBoxGeneric(
                    nextRow,
                    nextColumn,
                    currentEvent.getName(),                     // 1. Label Text
                    currentEvent.getChildren(),                 // 2. Items list
                    Event::getName,                             // 3. Name Extractor
                    currentEvent.getSelectedEvent(),            // 4. Selected item
                    newVal -> {                           // 5. callback
                        currentEvent.setSelectedEvent(newVal);
                        buildPane();                            // rebuild the pane
                    }
            );

            // continue with next selected event
            event = currentEvent.getSelectedEvent();
        }

        logger.debug("[CM_SYSEX_GEN_SELECTION_PANE] Built {}", getId());
    }

    private <T> int populateLabelAndComboBoxGeneric(
            int row, int column, String labelText, List<T> items,
            Function<T, String> nameExtractor, T selectedItem,
            SelectionHandler<T> selectionHandler) {

        // Create a Label
        Label label = new Label(labelText);
        label.setMinWidth(Control.USE_PREF_SIZE);

        Text labelHelper = new javafx.scene.text.Text(labelText);
        double labelWidth = labelHelper.getLayoutBounds().getWidth() + 8.0;

        // Calculate its colspan a Label
        int labelColspan = (int) Math.ceil(labelWidth / this.singleColumnPixelWidth);

        // Add, or replace position of, the label
        if (!getChildren().contains(label)) {
            add(label, column, row, labelColspan, 1);
        } else {
            GridPane.setColumnSpan(label, labelColspan);
        }

        // Lookup then longest string from the list
        String longestName = items.stream()
                .map(nameExtractor)
                .max(Comparator.comparingInt(String::length))
                .orElse("EMPTY");

        // Create the ComboBox
        ComboBox<T> comboBox = new ComboBox<>();
        comboBox.setItems(FXCollections.observableArrayList(items));

        comboBox.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(T object) { return object == null ? "" : nameExtractor.apply(object); }
            @Override public T fromString(String string) { return null; }
        });

        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(nameExtractor.apply(item));
                }
            }
        });

        if (selectedItem != null) {
            comboBox.setValue(selectedItem);
        }

        // The Listener calls the callback
        comboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectionHandler.handleSelection(newVal);
            }
        });

// Calculate the colspan of the ComboBox
        javafx.scene.text.Text comboHelper = new javafx.scene.text.Text(longestName);

// FIX: Verhoog de basis-buffer van 38.0 naar 50.0 pixels voor de pijl en interne styling
        double exactComboWidth = comboHelper.getLayoutBounds().getWidth() + 50.0;

// FIX: Verhoog de extra layout-marge van 12.0 naar 20.0 pixels om de afronding van singleColumnPixelWidth op te vangen
        int comboboxColspan = (int) Math.ceil((exactComboWidth + 20.0) / this.singleColumnPixelWidth);

// Zorg er daarnaast voor dat de ComboBox minimaal de berekende breedte aanneemt
        comboBox.setMinWidth(Control.USE_COMPUTED_SIZE);
        comboBox.setPrefWidth(exactComboWidth + 20.0); // Dwing de ComboBox in deze breedte
        comboBox.setMaxWidth(Double.MAX_VALUE);

        // add the combo or replace it
        if (!getChildren().contains(comboBox)) {
            add(comboBox, column + labelColspan, row, comboboxColspan, 1);
        } else {
            GridPane.setColumnIndex(comboBox, column + labelColspan);
            GridPane.setRowIndex(comboBox, row);
            GridPane.setColumnSpan(comboBox, comboboxColspan);
        }

// Zorg dat hij de beschikbare breedte in de kolommen ook echt mag vullen
        GridPane.setFillWidth(comboBox, true);
        GridPane.setHgrow(comboBox, Priority.ALWAYS);

//        comboBox.setMinWidth(Control.USE_COMPUTED_SIZE);
//        comboBox.setPrefWidth(Control.USE_COMPUTED_SIZE);
//        comboBox.setMaxWidth(Control.USE_COMPUTED_SIZE);
//        GridPane.setHalignment(comboBox, HPos.LEFT);
//        GridPane.setFillWidth(comboBox, false);
//        GridPane.setHgrow(comboBox, Priority.NEVER);

        GridPane.setMargin(comboBox, new javafx.geometry.Insets(0, 12, 0, 0));

        // Return the exact the column (include spans)
        return column + labelColspan + comboboxColspan;
    }

    private void handleLeafEvent(Event event, boolean skipTypes) {
        if (! skipTypes) {
            if (event.hasEventTypes()) {
                List<String> categories = event.getEventTypeCategories();
                EventType eventType = event.findEventTypeFromDefaultValue();

                nextColumn = populateLabelAndComboBoxGeneric(
                        nextRow,
                        nextColumn,
                        "Categories",                  // 1. Text for the label
                        categories,                             // 2. List of items
                        String::toString,                       // 3. Name Extractor
                        eventType.getCategory(),                // 4. the selected item
                        newVal -> {                       // 5. callback
                            handleNewCategory(event, newVal);
                        }
                );

                List<EventType> eventTypesOfCategory = event.getEventTypesOfCategory(eventType.getCategory());

                nextColumn = populateLabelAndComboBoxGeneric(
                        nextRow,
                        nextColumn,
                        "Type",                                        // 1. Text for the label
                        eventTypesOfCategory,                                   // 2. List of items
                        EventType::getName,                                     // 3. Name Extractor
                        eventType,                                              // 4. the selected item
                        newVal -> {                                   // 5. callback
                            event.setDefaultValue(newVal.getEventValue());
                            handleLeafEvent(event, true);
                        }
                );

                startProcessingLeafEvent(event);
                return;
            }
        }

        startProcessingLeafEvent(event);

    }

    private void handleNewCategory(Event event, String category) {
        EventType firstEventType = event.getFirstEventTypeOfCategory(category);
        event.setDefaultValue(firstEventType.getEventValue());

        buildPane();
        startProcessingLeafEvent(event);
    }

    private void startProcessingLeafEvent(Event event) {
        getSysexGen2Pane().getSysexGenParametersPane().setEvent(event);
    }

    // ACCESSORS
    public SysexGen2Pane getSysexGen2Pane() {
        return parent;
    }
}

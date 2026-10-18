package creative.scenes.sysexgen2;

import creative.scenes.sysexgen2.SysexGen2Pane;
import creative.scenes.sysexgen2.entity.Event;
import creative.scenes.sysexgen2.entity.EventType;
import creative.scenes.sysexgen2.listener.SelectionHandler;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;

import static util.ColorScheme.getColor;
import static util.Constants.APP_WIDTH;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;

public class SysexGenSelectionPane extends GridPane {
    private static final Logger logger = LoggerFactory.getLogger(SysexGenSelectionPane.class);

    double hgap = 6.0;                  // Horizontal gap between columns

    public SysexGenSelectionPane() {
        super();

        setId("SysExGenSelectionPane");
        setHgap(hgap);
        setVgap(3);
        setPadding(new Insets(5));
    }

    private SysexGen2Pane parent;

    private double singleColumnPixelWidth;

    private static final int NUM_COLS=24;
    double fixedWindowWidth = APP_WIDTH; // Put your exact fixed application width here
    double gridPadding = 20.0;          // Total left + right padding of the grid

    private Event rootEvent;
    public SysexGenSelectionPane(SysexGen2Pane owner, Event event) {
        this();

        this.parent = owner;
        this.rootEvent = event;

        // ⚡ BELANGRIJK: Als je een hgap gebruikt, zet die dan HIER (vóór de berekening)
        setHgap(6.0);

        // Bereken de exacte pixelgrootte per kolom
        this.singleColumnPixelWidth = (fixedWindowWidth - gridPadding) / NUM_COLS;

        for (int i = 0; i < NUM_COLS; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setMinWidth(singleColumnPixelWidth);
            col.setPrefWidth(singleColumnPixelWidth);
            col.setMaxWidth(singleColumnPixelWidth);
            col.setHalignment(HPos.LEFT);
            getColumnConstraints().add(col);
        }

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.CENTER);
        getRowConstraints().add(rowConstraints);

        showPaneBorder(this, getColor("border", "red"));

        setPaneBackground(this);

        buildPane();
    }

    private boolean debugAddCombo = true;

    private StringProperty dsSelectedSysexText = new SimpleStringProperty("");
    private BooleanProperty dsSelectedSysexVisibility = new SimpleBooleanProperty(false);

    private StringConverter<Event> eventToStringConvertor = new StringConverter<Event>() {
        @Override
        public String toString(Event event) {
            // Return the field to display
            return (event != null) ? event.getName() : "";
        }

        @Override
        public Event fromString(String string) {
            // Not needed because the ComboBox is not editable
            return null;
        }
    };

    private int nextColumn = 0;
    private int nextRow = 0;

    private void buildPane() {
        getChildren().clear();

        nextColumn = 0;
        nextRow = 0;

        Event event = rootEvent;

        while (event != null) {
            final Event currentEvent = event;

            // 1. OUDE LOGICA HERSTELD: Check op "XG Change Parameter" voor de Separator line
            if ("XG Change Parameter".equals(currentEvent.getName())) {
                nextRow += 1;
                nextColumn = 0;
                add(new javafx.scene.control.Separator(), nextColumn, nextRow, 24, 1);
                nextRow += 1;
            }

            // 2. OUDE LOGICA HERSTELD: Check of dit het eindstation (leaf) is
            if (!currentEvent.hasChildren()) {
                handleLeafEvent(currentEvent, false); // Voer je leaf-logica uit na de clear()
                break; // Stop de loop direct, een leaf heeft geen sub-events meer
            }

            // 3. Als het event kinderen heeft, roepen we de methode aan met exact 7 parameters
            nextColumn = populateLabelAndComboBoxGeneric(
                    nextRow,
                    nextColumn,
                    currentEvent.getName(),            // 1. LabelText
                    currentEvent.getChildren(),        // 2. Items lijst
                    Event::getName,                    // 3. NameExtractor
                    currentEvent.getSelectedEvent(),   // 4. Geselecteerde item
                    newVal -> {                        // 5. SelectionHandler callback
                        currentEvent.setSelectedEvent(newVal);
                        buildPane();           // Trigger de schone heropbouw
                    }
            );

            // 4. Verschuif de pointer naar het geselecteerde sub-event voor de volgende stap
            event = currentEvent.getSelectedEvent();
        }
    }

    private <T> int populateLabelAndComboBoxGeneric(
            int row, int column, String labelText, List<T> items,
            Function<T, String> nameExtractor, T selectedItem,
            SelectionHandler<T> selectionHandler) {

        // 1. Maak het Label aan en bereken zijn colspan
        Label label = new Label(labelText);
        label.setMinWidth(Control.USE_PREF_SIZE);

        javafx.scene.text.Text labelHelper = new javafx.scene.text.Text(labelText);
        double labelWidth = labelHelper.getLayoutBounds().getWidth() + 8.0;
        int labelColspan = (int) Math.ceil(labelWidth / this.singleColumnPixelWidth);

        if (!getChildren().contains(label)) {
            add(label, column, row, labelColspan, 1);
        } else {
            GridPane.setColumnSpan(label, labelColspan);
        }

        // 2. Zoek de langste tekst in de lijst via de nameExtractor
        String longestName = items.stream()
                .map(nameExtractor)
                .max((s1, s2) -> Integer.compare(s1.length(), s2.length()))
                .orElse("EMPTY");

        // 3. Maak de ComboBox aan
        ComboBox<T> comboBox = new ComboBox<>();
        comboBox.setItems(FXCollections.observableArrayList(items));

        comboBox.setConverter(new javafx.util.StringConverter<T>() {
            @Override public String toString(T object) { return object == null ? "" : nameExtractor.apply(object); }
            @Override public T fromString(String string) { return null; }
        });

        comboBox.setButtonCell(new ListCell<T>() {
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

        // 4. De Listener activeert de callback en bewaart de data
        comboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectionHandler.handleSelection(newVal);
            }
        });

        // 5. Bereken de colspan voor de ComboBox
        javafx.scene.text.Text comboHelper = new javafx.scene.text.Text(longestName);
        double exactComboWidth = comboHelper.getLayoutBounds().getWidth() + 38.0;
        int comboboxColspan = (int) Math.ceil((exactComboWidth + 12.0) / this.singleColumnPixelWidth);

        if (!getChildren().contains(comboBox)) {
            add(comboBox, column + labelColspan, row, comboboxColspan, 1);
        } else {
            GridPane.setColumnIndex(comboBox, column + labelColspan);
            GridPane.setRowIndex(comboBox, row);
            GridPane.setColumnSpan(comboBox, comboboxColspan);
        }

        // 6. Grid-constraints om de ComboBox compact te houden
        comboBox.setMinWidth(Control.USE_COMPUTED_SIZE);
        comboBox.setPrefWidth(Control.USE_COMPUTED_SIZE);
        comboBox.setMaxWidth(Control.USE_COMPUTED_SIZE);
        GridPane.setHalignment(comboBox, HPos.LEFT);
        GridPane.setFillWidth(comboBox, false);
        GridPane.setHgrow(comboBox, Priority.NEVER);

        GridPane.setMargin(comboBox, new javafx.geometry.Insets(0, 12, 0, 0));

        // 7. Geeft de exacte, pure eindkolom terug (vlijmscherpe aansluiting, geen overlap)
        return column + labelColspan + comboboxColspan;
    }

    public int calculateColSpan(Control control, String longestText) {
        if (longestText == null || longestText.isEmpty()) {
            return 1;
        }

        boolean isComboBox = control instanceof ComboBox;

        // Meet de exacte pixelbreedte van de letters op het scherm
        javafx.scene.text.Text helper = new javafx.scene.text.Text(longestText);
        double rawTextWidth = helper.getLayoutBounds().getWidth();

        // Voeg de minimale themamarges toe (34px voor de ComboBox pijl, 6px voor een Label)
        double totalRequiredWidth = rawTextWidth + (isComboBox ? 34.0 : 6.0);

        // Bereken de colspan op basis van de exacte pixelbreedte uit jouw constructor
        int colspan = (int) Math.ceil(totalRequiredWidth / this.singleColumnPixelWidth);

        // Blijf binnen de 24 kolommen
        return Math.max(1, Math.min(colspan, NUM_COLS));
    }

    private void handleLeafEvent(Event event, boolean skipTypes) {
        if (! skipTypes) {
            if (event.hasEventTypes()) {
                List<String> categories = event.getEventTypeCategories();
                EventType eventType = event.findEventTypeFromDefaultValue();

                nextColumn = populateLabelAndComboBoxGeneric(
                        nextRow,
                        nextColumn,
                        "Categories",            // 1. LabelText
                        categories,        // 2. Items lijst
                        String::toString,                    // 3. NameExtractor
                        eventType.getCategory(),   // 4. Geselecteerde item
                        newVal -> {                        // 5. SelectionHandler callback
                            handleNewCategory(event, newVal);
//                        currentEvent.setSelectedEvent(newVal);
//                        buildPane();           // Trigger de schone heropbouw
                        }
                );

                List<EventType> eventTypesOfCategory = event.getEventTypesOfCategory(eventType.getCategory());

                nextColumn = populateLabelAndComboBoxGeneric(
                        nextRow,
                        nextColumn,
                        "Type",            // 1. LabelText
                        eventTypesOfCategory,        // 2. Items lijst
                        EventType::getName,                    // 3. NameExtractor
                        eventType,   // 4. Geselecteerde item
                        newVal -> {                        // 5. SelectionHandler callback
                            System.out.println(("NEW EVENTTYPE: " + newVal.getName()));
                            event.setDefaultValue(newVal.getEventValue());
                            handleLeafEvent(event, true);
//                        currentEvent.setSelectedEvent(newVal);
//                        buildPane();           // Trigger de schone heropbouw
                        }
                );

                return;
            }
        }

        System.out.println("HANDLING LEAF " + event.getName());

    }

    private void handleNewCategory(Event event, String category) {
        System.out.println(("HANDLE NEW CATEGORY " + category + " FOR EVENT: " + event.getName()));
        EventType firstEventType = event.getFirstEventTypeOfCategory(category);
        event.setDefaultValue(firstEventType.getEventValue());

        buildPane();
    }

    // ACCESSORS
    public SysexGen2Pane getSysexGen2Pane() {
        return parent;
    }
}

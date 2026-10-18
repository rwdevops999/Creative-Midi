package creative.scenes.sysexgen2;

import creative.scenes.sysexgen2.entity.Event;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

//        setVgap(3);
//        setPadding(new Insets(5));
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

    private ObjectProperty<Event> dsSelectedEvent = new SimpleObjectProperty<>();

    private int nextpos = 0;

    private void buildPane() {
        getChildren().clear();

        // first we show the children op the root event
        int row = 0;

        nextpos = populateLabelAndComboBox(row, 0, rootEvent.getChildren(), rootEvent);

        setupDataSourceListener();

        Button testButton = new Button("Test");
        add(testButton, nextpos, row);

    }

    private void setupDataSourceListener() {
        dsSelectedEvent.addListener((observable, oldValue, newValue) -> {
            System.out.println("Selected new event: " + newValue.getName());
        });
    }

    private int populateLabelAndComboBox(int row, int column, List<Event> children, Event event) {
        // 1. Maak het Label aan en bereken zijn eigen pure colspan
        Label label = new Label(event.getName());
        label.setMinWidth(Control.USE_PREF_SIZE);

        // Meet de exacte pixels van de LABEL-tekst
        javafx.scene.text.Text labelHelper = new javafx.scene.text.Text(event.getName());
        double labelWidth = labelHelper.getLayoutBounds().getWidth() + 10.0; // 10px ademruimte voor het label
        int labelColspan = (int) Math.ceil(labelWidth / this.singleColumnPixelWidth);

        if (!getChildren().contains(label)) {
            add(label, column, row, labelColspan, 1);
        } else {
            GridPane.setColumnSpan(label, labelColspan);
        }

        // 2. Zoek de langste naam voor de ComboBox
        String longestName = event.getChildren().stream()
                .map(Event::getName)
                .max((s1, s2) -> Integer.compare(s1.length(), s2.length()))
                .orElse("EMPTY");

        // 3. Maak de ComboBox aan (Puur Java, rechtstreeks in de Grid)
        ComboBox<Event> comboBox = new ComboBox<>();
        comboBox.setItems(FXCollections.observableArrayList(event.getChildren()));
        comboBox.setConverter(eventToStringConvertor);

        comboBox.setButtonCell(new ListCell<Event>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        // 4. Meet de exacte pixels van de COMBOBOX-tekst
        javafx.scene.text.Text comboHelper = new javafx.scene.text.Text(longestName);

        // Bereken de exacte pixelbreedte (breedte van de tekst + 38px voor de pijlknop + 12px visuele marge naar rechts)
        double exactComboWidth = comboHelper.getLayoutBounds().getWidth() + 50.0;

        // Bereken exact hoeveel kolommen deze ComboBox NU nodig heeft
        int comboboxColspan = (int) Math.ceil(exactComboWidth / this.singleColumnPixelWidth);

        // 5. Voeg de ComboBox toe (kolom-positie schuift op met de breedte van het label)
        if (!getChildren().contains(comboBox)) {
            add(comboBox, column + labelColspan, row, comboboxColspan, 1);
        } else {
            GridPane.setColumnIndex(comboBox, column + labelColspan);
            GridPane.setRowIndex(comboBox, row);
            GridPane.setColumnSpan(comboBox, comboboxColspan);
        }

        // 6. ⚡ DE PURE GRIDPANE REGLER: Dwing de ComboBox in het berekende vak
        comboBox.setMinWidth(Control.USE_COMPUTED_SIZE);
        comboBox.setPrefWidth(Control.USE_COMPUTED_SIZE);
        comboBox.setMaxWidth(Double.MAX_VALUE); // Vul de colspan perfect op

        GridPane.setHalignment(comboBox, HPos.LEFT);
        GridPane.setFillWidth(comboBox, true);
        GridPane.setHgrow(comboBox, Priority.ALWAYS);

        // 7. Geef de perfect aansluitende volgende kolomindex terug
        return column + labelColspan + comboboxColspan;
    }
    
    /**
     * Estimate visual pixel width required by the text
     *
     * @param str the string of which to calculate the width
     * @return the estimated width in pixels
     */
    private int calculateTextWidthInPixels(String str, Font font, boolean isComboBox) {
        if (str == null || str.isEmpty()) {
            return isComboBox ? 45 : 10; // Minimum default fallbacks
        }

        // 1. Create a graphic text element to accurately measure font metrics
        Text helper = new Text(str);
        if (font != null) {
            helper.setFont(font);
        }

        // 2. Extract the true visual width
        double textWidth = helper.getLayoutBounds().getWidth();

        // 3. Add structural UI padding buffers
        if (isComboBox) {
            // Text width + left/right padding + width of the drop-down arrow graphic
            return (int) Math.ceil(textWidth + 35);
        } else {
            // Text width + subtle padding buffer for regular labels
            return (int) Math.ceil(textWidth + 8);
        }
    }

    public int calculateColSpan(Control control, String longestText) {
        if (longestText == null || longestText.isEmpty()) {
            return 1;
        }

        boolean isComboBox = control instanceof ComboBox;

        // 1. Meet de pure pixelbreedte van de letters op het scherm
        javafx.scene.text.Text helper = new javafx.scene.text.Text(longestText);
        double rawTextWidth = helper.getLayoutBounds().getWidth();

        // 2. STRETCH CORRECTIE: We gebruiken exact 34px voor de ComboBox pijl/marge en 4px voor het label
        // Dit is de absolute minimumruimte om '...' te voorkomen zonder een extra kolom te triggeren
        double totalRequiredWidth = rawTextWidth + (isComboBox ? 34.0 : 4.0);

        // 3. Bereken de colspan op basis van je zuivere kolombreedte
        int colspan = (int) Math.ceil(totalRequiredWidth / this.singleColumnPixelWidth);

        return Math.max(1, Math.min(colspan, NUM_COLS));
    }

    private void setRootChildren(ComboBox<Event> comboBox, int row, int column, int relativePosition, int colspan, String label, boolean visible, Event event) {
        this.getChildren().remove(comboBox);
        Node child = this.lookup("#selectedSysexLabel");
        if (child != null) {
            this.getChildren().remove(child);
        }

        Label selectedSysexLabel = new Label();
        selectedSysexLabel.setId("selectedSysexLabel");
        selectedSysexLabel.textProperty().bind(dsSelectedSysexText);
        selectedSysexLabel.visibleProperty().bind(dsSelectedSysexVisibility);
        add(selectedSysexLabel, column, row, 6, 1);

        if (event.hasChildren()) {
            List<Event> children = event.getChildren();
            comboBox.setItems(FXCollections.observableArrayList(children));

            add(comboBox, column + relativePosition, row);

            int maxLength = comboBox.getItems().stream()
                    .map(Event::getName)
                    .mapToInt(String::length)
                    .max()
                    .orElse(10);

            double estimatedTextWidth = (maxLength * 8.5) + 35;
            comboBox.setMaxWidth(estimatedTextWidth);
            comboBox.setPrefWidth(estimatedTextWidth);

            double gridWidth = getWidth() > 0 ? getWidth() : 800;
            double singleColumnWidth = gridWidth / 24.0;
            int estcolspan = (int) Math.ceil(estimatedTextWidth / singleColumnWidth);
            estcolspan = Math.max(1, Math.min(estcolspan, 24));


            GridPane.setColumnSpan(comboBox, estcolspan);
        }

        dsSelectedSysexText.set(label);
        dsSelectedSysexVisibility.set(visible);

    }

    // ACCESSORS
    public SysexGen2Pane getSysexGen2Pane() {
        return parent;
    }
}

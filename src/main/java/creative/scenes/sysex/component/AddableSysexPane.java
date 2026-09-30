package creative.scenes.sysex.component;

import creative.scenes.sysex.SysexDetailsPane;
import creative.scenes.voice.PatchSearchPane;
import custom.components.SimpleHexTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import lombok.Getter;
import lombok.Setter;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.resizePaneWidth;

public class AddableSysexPane extends GridPane {
    @Getter
    @Setter
    private int buttonId = 0;

    public AddableSysexPane() {
        super();

        int totalColumns = 10;
        double percentagePerColumn = 100.0 / totalColumns;

        for (int i = 0; i < totalColumns; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(percentagePerColumn);
            col.setHalignment(HPos.LEFT);
            getColumnConstraints().add(col);
        }

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.CENTER);
        getRowConstraints().add(rowConstraints);
    }

    private StringProperty inputValue = new SimpleStringProperty();

    private SimpleHexTextField textField;
    public TextField getInputField() {
        return textField;
    }

    public AddableSysexPane(Pane owner, int id) {
        this();

        this.buttonId = id;

        resizePaneWidth(this, owner, 90);

        String flatButtonStyle =
                "-fx-background-color: #a9a9a9; " + // Grijze achtergrond (of 'transparent')
                        "-fx-background-radius: 0; " +       // Rechte hoeken (gebruik bijv. 4px voor licht afgerond)
                        "-fx-text-fill: #333333; " +         // Tekstkleur
                        "-fx-font-size: 14px; " +            // Lettergrootte
                        "-fx-font-weight: bold; " +          // Dikgedrukte tekst
                        "-fx-cursor: hand;";

        int row = 0;

        // ROW (+, input, -)
        Button addButton = new Button("+");
        addButton.setStyle(flatButtonStyle);
        addButton.setOnAction(event -> {
            Button btn = (Button) event.getSource();
            btn.setDisable(true);
            SysexDetailsPane sysexDetailsPane = (SysexDetailsPane) owner;
            sysexDetailsPane.addPane(buttonId);
            btn.setDisable(false);
        });

        this.add(addButton, 0, row);

        textField = new SimpleHexTextField();
        this.add(textField, 1, row, 9, 1);

        Button removeButton = new Button("-");
        removeButton.setStyle(flatButtonStyle);
        removeButton.setOnAction(event -> {
            Button btn = (Button) event.getSource();
            btn.setDisable(true);
            SysexDetailsPane sysexDetailsPane = (SysexDetailsPane) owner;
            sysexDetailsPane.removePane(buttonId);
            btn.setDisable(false);
        });
        add(removeButton, 10, row);
    }
}

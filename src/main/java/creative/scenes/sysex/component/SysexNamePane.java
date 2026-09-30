package creative.scenes.sysex.component;

import creative.scenes.sysex.SysexStateMachine;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.resizePaneWidth;

public class SysexNamePane extends GridPane {
    @Getter
    @Setter
    private int buttonId = 0;

    public SysexNamePane() {
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

    public SysexNamePane(Pane owner, StringProperty dsProperty, Consumer<Integer> changedConsumer) {
        this();

        resizePaneWidth(this, owner, 90);

        Label nameLabel = new Label("Name");
        add(nameLabel, 0, 0, 3, 1);

        TextField nameTextField = new TextField();
        nameTextField.textProperty().bindBidirectional(dsProperty);
        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            changedConsumer.accept(SysexStateMachine.STATE_DIRTY);
        });
        add(nameTextField, 1, 0, 4, 1);
    }
}

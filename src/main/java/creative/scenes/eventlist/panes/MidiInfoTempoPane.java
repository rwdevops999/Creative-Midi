package creative.scenes.eventlist.panes;

import util.Util;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import static util.Util.setPaneMinWidthAsPercentage;

public class MidiInfoTempoPane extends HBox {
    private final StringProperty value = new SimpleStringProperty("0 bpm");
    public MidiInfoTempoPane() {
        super();
        setSpacing(5);
        setPadding(new Insets(0, 0, 0, 5));
    }

    public MidiInfoTempoPane(Pane owner) {
        this();

        setPaneMinWidthAsPercentage(this, owner, 12);

        buildPane();
    }

    private void buildPane() {
        setStyle("-fx-border-color: black;");

        Label tempovalueLabel = new Label();
        tempovalueLabel.textProperty().bind(value);
        getChildren().add(tempovalueLabel);
    }

    public void setValue(String tempo) {
        value.set(tempo + " bpm");
    }
}

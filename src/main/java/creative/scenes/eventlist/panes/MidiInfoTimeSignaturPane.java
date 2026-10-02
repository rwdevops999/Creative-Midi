package creative.scenes.eventlist.panes;

import util.Util;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import static util.Util.setPaneMinWidthAsPercentage;

public class MidiInfoTimeSignaturPane extends HBox {
    private final StringProperty value = new SimpleStringProperty("4/4");
    public MidiInfoTimeSignaturPane() {
        super();

        setSpacing(5);
        setPadding(new Insets(0, 0, 0, 5));
    }

    public MidiInfoTimeSignaturPane(Pane owner) {
        this();

        setPaneMinWidthAsPercentage(this, owner, 13);

        buildPane();
    }

    private void buildPane() {
        setStyle("-fx-border-color: black;");

        Label signatureValueLabel = new Label();
        signatureValueLabel.textProperty().bind(value);
        getChildren().add(signatureValueLabel);
    }

    public void setValue(String signature) {
        value.setValue(signature);
    }
}

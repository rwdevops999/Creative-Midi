package creative.scenes.eventlist.panes;

import util.Util;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import static util.Util.setPaneMinWidthAsPercentage;
import static util.Util.setPaneWidthAsPercentage;

public class MidiInfoFilenamePane extends HBox {
    private final StringProperty value = new SimpleStringProperty("<no song selected>");
    public MidiInfoFilenamePane() {
        super();

        setSpacing(5);
        setPadding(new Insets(0, 0, 0, 5));
    }

    public MidiInfoFilenamePane(Pane owner) {
        this();

        setPaneMinWidthAsPercentage(this, owner, 48);

        buildPane();
    }

    private void buildPane() {
        setStyle("-fx-border-color: black;");

        Label songnameLabel = new Label();
        songnameLabel.textProperty().bind(value);
        getChildren().add(songnameLabel);
    }

    public void setValue (String name) {
        value.set(name);
    }
}

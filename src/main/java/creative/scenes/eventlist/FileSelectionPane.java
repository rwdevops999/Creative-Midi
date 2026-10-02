package creative.scenes.eventlist;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;
import util.Util;

import java.io.File;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;
import static util.Util.setPaneHeightAsPercentage;

public class FileSelectionPane extends HBox {
    private static final Logger logger = LoggerFactory.getLogger(FileSelectionPane.class);

    public FileSelectionPane() {
        super();

        setId("FileSelectionPane");

        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(5));
    }

    private EventlistPane parent;
    public FileSelectionPane(EventlistPane owner) {
        this();

        parent = owner;

//        showPaneBorder(this, getColor("border", "red"));
        setPaneHeightAsPercentage(this, parent, 10);
        setPaneBackground(this);

        buildPane();
    }

    private final StringProperty fileName = new SimpleStringProperty("<no file>");

    private File selectedFile = null;
    private void buildPane() {
        logger.debug("[CM_FILE_SELECTION_PANE] Building {}", getId());

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a MIDI file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MIDI", "*.mid")
        );

        Button fileSelectButton = new Button("...");
        fileSelectButton.setOnAction(e -> {
            selectedFile = fileChooser.showOpenDialog(ApplicationInfo.getInstance().getPrimaryStage());

            if (selectedFile != null) {
                fileName.set(selectedFile.getName());

                getEventlistPane().getEventsPane().processFile(selectedFile);
            }
        });
        getChildren().add(fileSelectButton);

        Label selectedFileLabel = new Label();
        selectedFileLabel.textProperty().bind(fileName);
        getChildren().add(selectedFileLabel);

        logger.debug("[CM_FILE_SELECTION_PANE] Built {}", getId());
    }

    // ACCESSORS
    public EventlistPane getEventlistPane() {
        return parent;
    }
}

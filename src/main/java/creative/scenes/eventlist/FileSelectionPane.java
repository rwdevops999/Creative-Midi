package creative.scenes.eventlist;

import creative.scenes.eventlist.parser.entity.MidiEventInfo;
import creative.scenes.eventlist.util.MidiFileWriter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.apache.commons.io.FilenameUtils;
import org.controlsfx.control.Notifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;
import util.Util;
import util.properties.PropertyContainer;
import util.properties.PropertyType;

import javax.sound.midi.Sequence;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

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

    private LinkedList<MidiEventInfo> currentEvents = null;
    private Sequence currentSequence = null;

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

        Button fileSaveButton = new Button("Save");
        fileSaveButton.disableProperty().bind(saveDisable);
        fileSaveButton.setOnAction(e -> {
            handleSaveFile();
        });
        getChildren().add(fileSaveButton);

        logger.debug("[CM_FILE_SELECTION_PANE] Built {}", getId());
    }

    public void setEventInfo(Sequence sequence, Queue<MidiEventInfo> events) {
        currentEvents = new LinkedList<>(events);
        currentSequence = sequence;
    }

    public void updateSequence(Sequence sequence) {
        currentSequence = sequence;
    }

    public void handleSaveFile() {
        // TODO
        if (selectedFile != null) {
            String midiPath = PropertyContainer.getPropertyAsString(PropertyType.Path, PropertyContainer.MIDI_PATH, "./midi");
            String newFilename = FilenameUtils.removeExtension(selectedFile.getName()) + ".mid";

            String newMidiFilename = midiPath + "/" + newFilename;

            fileName.set(newFilename);

            File file = new File(newMidiFilename);
            try {
                MidiFileWriter writer = new MidiFileWriter();
                writer.writeMidi(currentSequence, file);
                Notifications.create()
                        .title("File Saved")
                        .text("Saving the midi file to '" +  newMidiFilename + "'")
                        .hideAfter(Duration.seconds(3)) // Automatically hides after 5 seconds
                        .position(Pos.TOP_LEFT)     // Set corner position on screen
                        .showInformation();
                ApplicationInfo.getInstance().setMidiChanged(false);
            } catch (Exception e) {
                logger.error("[CM_FILE_SELECTION_PANE] Exception. CAUSE {}", e.getMessage());
            }
        }
    }

    private final BooleanProperty saveDisable = new SimpleBooleanProperty(true);
    public void fileHasChanged() {
        saveDisable.set(false);
    }

    // ACCESSORS
    public EventlistPane getEventlistPane() {
        return parent;
    }
}

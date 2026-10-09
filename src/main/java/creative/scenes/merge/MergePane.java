package creative.scenes.merge;

import creative.scenes.merge.util.YamahaStyleMerger;
import custom.components.ActionButton;
import custom.dialog.DialogFactory;
import custom.dialog.NameDialog;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;
import util.Registry;
import util.properties.PropertyContainer;
import util.properties.PropertyType;
import vendor.yamaha.YamahaStyleUploader;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;

public class MergePane extends GridPane {
    private static final Logger logger = LoggerFactory.getLogger(MergePane.class);

    private static YamahaStyleUploader yamaha;
    private static final ObjectProperty<ObservableList<String>> dsDeviceFiles =
            new SimpleObjectProperty<>(FXCollections.observableArrayList());

    static {
         try {
             yamaha = new YamahaStyleUploader();
         } catch (Exception e) {
             logger.error("[CM_MERGE_PANE]: Exception. CAUSE: " + e.getMessage());
         }
    }

    public MergePane() {
        super();

        setId("MergePane");
        setPadding(new Insets(5));
        setVgap(10);

        int totalColumns = 10;
        double percentagePerColumn = 100.0 / totalColumns; // Dit is ~8.3333%

        for (int i = 0; i < totalColumns; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(percentagePerColumn);
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

    private final StringProperty dsStyleFilename = new SimpleStringProperty("");
    private final StringProperty dsMidiFilename = new SimpleStringProperty("");
    private final StringProperty dsNewStyleFilename = new SimpleStringProperty("");

    private final BooleanProperty dsDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty dsVisible = new SimpleBooleanProperty(false);
    private final BooleanProperty dsDisabledUpload = new SimpleBooleanProperty(false);

    private final EventHandler<ActionEvent> uploadHandler = new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent event) {
            NameDialog nameDialog = new NameDialog("song");
            Optional<String> songName = nameDialog.showAndWait();

            if (songName.isPresent() && (! dsDeviceFiles.get().isEmpty())) {
                if (dsDeviceFiles.get().contains(songName.get())) {
                    System.out.println("File Exists");
                    if (DialogFactory.renderConfirmationDialog("File exists!", "Overwrite?")) {
                        System.out.println("Overwriting " + songName.get());
                    }
                }
            }

//            System.out.println("UPLOADING: " + dsNewStyleFilename.get());
            // TODO Render a name dialog
//            File file = new File("C:\\Users\\SX600\\Idea\\Creative Midi 3\\testmidi\\send\\aaa.sty");
//            YamahaStyleTransfer.sendStyleFile(file.toPath(), ApplicationInfo.getInstance().getMidiOutputDevice(), );

//            StyleTransfer st = new StyleTransfer();
            try {
                // Here we can retrieve de keyboard style files
//                YamahaStyleUploader yamaha = new YamahaStyleUploader();
//                List<String> styles = yamaha.getStyleFiles();



  /*              Path styleFile = Path.of("C:\\Users\\SX600\\Idea\\Creative Midi 3\\testmidi\\send\\WRAB.sty");

                try (YamahaStyleUploader uploader =
                             new YamahaStyleUploader()) {

                    uploader.upload(styleFile);
                }

                System.out.println("Loaded"); */
//                st.firstTest();
//                YamahaTest.YamahaTransferHandshake();
//                YamahaTest.usbTest();
//                YamahaTest.identityTest();
//                YamahaTest.sysexTest();
//                YamahaTest.YamahaNoteTest();
//                YamahaTest.sequenceTest();
//                YamahaTest.syexTest();
//                YamahaTest.sendTest();
//                YamahaTest.ListMidiDevices();
//                YamahaTest.send();
            } catch (Exception e) {
                System.out.println("EXCEPTION: " + e.getMessage());
            }
        }
    };

    private void buildPane() {
        logger.debug("[CM_MIDI_SCENE] Starting Merge Scene");

        Registry.register("DeviceSelector", this, (node, data) -> {
            if (node instanceof MergePane) {
                if (ApplicationInfo.getInstance().getMidiOutputDevice() != null) {
                    try {
                        if (yamaha == null) {
                            yamaha = new YamahaStyleUploader();
                            List<String> styles = yamaha.getStyleFiles().stream().sorted(String.CASE_INSENSITIVE_ORDER).toList();

                            dsDeviceFiles.set(FXCollections.observableArrayList(styles));
                        }
                    } catch (Exception e) {
                        logger.error("[CM_MIDI_SCENE] Exception. CAUSE: " + e.getMessage());
                    }
                } else {
                    dsDeviceFiles.set(FXCollections.observableArrayList(new ArrayList<>()));
                }

/*                if (ApplicationInfo.getInstance().getMidiOutputDevice() != null) {
                    try {
                        List<String> styles = yamaha.getStyleFiles().stream().sorted(String.CASE_INSENSITIVE_ORDER).toList();
                        dsDeviceFiles.set(FXCollections.observableArrayList(styles));
                    } catch (Exception e) {
                        logger.error("[CM_MIDI_SCENE] Exception. CAUSE: " + e.getMessage());
                    }
                } */
            }
        });

        FileChooser fileChooser = new FileChooser();

        // Row 0 (Style file selection)
        int row = 0;
        Label stylefileLabel = new Label("Style file");
        add (stylefileLabel, 0, row);

        Label stylefileValue = new Label();
        stylefileValue.textProperty().bind(dsStyleFilename);
        add (stylefileValue, 1, row, 5, 1);

        Button uploadStyleFileButton = new Button("Upload Style File");
        uploadStyleFileButton.setOnAction(event -> {
            fileChooser.setTitle("Select your style file");
            fileChooser.getExtensionFilters().clear();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("STYLE", "*.sty")
            );

            File selectedFile = fileChooser.showOpenDialog(ApplicationInfo.getInstance().getPrimaryStage());

            if (selectedFile != null) {
                dsStyleFilename.set(selectedFile.toString());
            }
        });
        add (uploadStyleFileButton, 8, row, 2, 1);

        // Row 1 (Midi file selection)
        row++;
        Label midifileLabel = new Label("Midi file");
        add (midifileLabel, 0, row);

        Label midifileValue = new Label();
        midifileValue.textProperty().bind(dsMidiFilename);
        add (midifileValue, 1, row, 5, 1);

        Button uploadMidiFileButton = new Button("Upload Midi File");
        uploadMidiFileButton.setOnAction(event -> {
            fileChooser.setTitle("Select your MIDI file");
            fileChooser.getExtensionFilters().clear();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("MIDI", "*.mid")
            );

            File selectedFile = fileChooser.showOpenDialog(ApplicationInfo.getInstance().getPrimaryStage());

            if (selectedFile != null) {
                dsMidiFilename.set(selectedFile.toString());
            }
        });
        add (uploadMidiFileButton, 8, row, 2, 1);

        // Row 2 (Merge button)
        row++;
        Button mergeButton = new Button("Merge");
        mergeButton.disableProperty().bind(dsDisabled);
        mergeButton.setOnAction(event -> {
            YamahaStyleMerger styleMerger = new YamahaStyleMerger();
            try {
                styleMerger.mergeStyleAndMidi(new File(dsStyleFilename.get()), new File(dsMidiFilename.get()), new File(dsNewStyleFilename.get()));
            } catch (Exception e) {
                System.out.println("MERGE EXCEPTION");
            }
            dsDisabledUpload.set(false);
        });
        add (mergeButton, 0, row);

        Label mergeFilenameLabel = new Label("to");
        mergeFilenameLabel.visibleProperty().bind(dsVisible);
        add (mergeFilenameLabel, 1, row);

        Label mergeFilenameValue = new Label();
        mergeFilenameValue.textProperty().bind(dsNewStyleFilename);
        mergeFilenameValue.visibleProperty().bind(dsVisible);
        add (mergeFilenameValue, 2, row, 8, 1);

        ActionButton uploadButton = new ActionButton(24, "uploadButton", "upload to keyboard", "upload", uploadHandler);
        uploadButton.disableProperty().bind(dsDisabledUpload);
        add(uploadButton, 8, row);

        // Row 3 (Instruction label)
        row++;
        Label instructionsLabel = new Label("Attention");
        add (instructionsLabel, 0, row);

        // Row 4 (Instructions)
        row++;
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        // Set the height to fit exactly 5 rows of text
        textArea.setPrefRowCount(5);
        add (textArea, 0, row, 10, 1);

        // Files on device
        row++;
        Label deviceLabel = new Label("Device files");
        add (deviceLabel, 0, row);

        double ROW_HEIGHT = 24.0;
        int VISIBLE_ROWS = 7;

        row++;
        ListView<String> deviceFiles = new ListView<>();
        deviceFiles.itemsProperty().bind(dsDeviceFiles);
        deviceFiles.setPrefHeight((VISIBLE_ROWS * ROW_HEIGHT) + 2);
        add (deviceFiles, 0, row, 10, 1);

        // listeners
        dsStyleFilename.addListener((observable, oldValue, newValue) -> {
            checkFiles();
        });
        dsMidiFilename.addListener((observable, oldValue, newValue) -> {
            checkFiles();
        });

        logger.debug("[CM_MIDI_SCENE] Starting Merge Scene");
    }

    private void checkFiles() {
        boolean isComplete = ! dsStyleFilename.get().isEmpty() && ! dsMidiFilename.get().isEmpty();

        if (isComplete) {
            String midiPath = PropertyContainer.getPropertyAsString(PropertyType.Path, PropertyContainer.STYLE_PATH, "./style");
            String filename = FilenameUtils.getBaseName(dsStyleFilename.get()) + ".sty";

            dsNewStyleFilename.set(midiPath + "/" + filename);
            dsDisabled.set(false);
            dsVisible.set(true);
        } else {
            dsNewStyleFilename.set("");
            dsDisabled.set(true);
            dsVisible.set(false);
        }
    }

    // ACCESSORS
}

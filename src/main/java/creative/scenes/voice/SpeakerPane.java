package creative.scenes.voice;

import custom.components.voice.ChannelIndicator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Util;

import java.io.File;
import java.util.Objects;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;
import static util.Util.setPaneWidthAsPercentage;

public class SpeakerPane extends HBox {
    private static final Logger logger = LoggerFactory.getLogger(SpeakerPane.class);
    public SpeakerPane() {
        super();

        setId("SpeakerPane");

        setSpacing(10);
        setAlignment(Pos.BASELINE_LEFT);
        setPadding(new Insets(2));
    }

    private VoiceSearchResultsPane parent;
    public SpeakerPane(VoiceSearchResultsPane owner) {
        this();

        parent = owner;

        showPaneBorder(this, getColor("test", "blue"));
        setPaneBackground(this);

        buildPane();
    }

    private static final int ICON_SIZE=24;
    private final StringProperty dsMidiName = new SimpleStringProperty("creative.mid");

    private void buildPane() {
        logger.debug("[CM_SPEAKER_PANE] Building {}", getId());

        ToggleButton speaker = new ToggleButton();
        speaker.setAlignment(Pos.CENTER);
        speaker.setStyle("-fx-background-color: darkgray;");

        Image icon = new Image(Objects.requireNonNull(VoiceSearchResultsPane.class.getClassLoader().getResourceAsStream("icons/speaker.png")));
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(ICON_SIZE);
        imageView.setFitHeight(ICON_SIZE);
        speaker.setPrefSize(ICON_SIZE,ICON_SIZE);
        speaker.setPadding(new Insets(1));
        speaker.setGraphic(imageView);
        speaker.setOnAction((e) -> {
/*            if (speaker.isSelected()) {
                Patch patch = table.getSelectionModel().getSelectedItem();
                if (patch == null) {
                    patch = new Patch(null, "Live! Grand Piano", "0", "115", "0", "Yamaha");
                    DirectSingleton.getInstance().setSelectedPatch(patch);
                }

                sendAsMidi(0, patch);
                speaker.setStyle("-fx-background-color: #0096C9;");
                playDemoFile((ToggleButton)(e.getSource()), midiChannelIndicator); */
/*                } else {

                    DialogFactory.renderWarningDialog("No Patch selected for demo");
                    speaker.setStyle("-fx-background-color: darkgray;");
                    speaker.setSelected(false);
                } */
/*            } else {
                MidiDemo.stopDemo(midiChannelIndicator);
                speaker.setSelected(false);
                speaker.setStyle("-fx-background-color: darkgray;");
            } */
        });
        getChildren().add(speaker);

        // Midi file name
        Label midiName = new Label();
        midiName.textProperty().bindBidirectional(dsMidiName);
        getChildren().add(midiName);

        // upload button
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select midi to play");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Midi", "*.mid")
        );

        Button uploadButton = new Button("Upload...");
        uploadButton.setOnAction(e -> {
/*            File selectedFile = fileChooser.showOpenDialog(Globals.getPrimaryStage());

            if (selectedFile != null) {
                dsMidiName.set(selectedFile.getName());
                Globals.setMidiFile(selectedFile);
            } */
        });
        getChildren().add(uploadButton);

        ChannelIndicator midiChannelIndicator = new ChannelIndicator();
        getChildren().add(midiChannelIndicator);

        logger.debug("[CM_SPEAKER_PANE] Building {}", getId());
    }

    // ACCESSORS
    public VoiceSearchResultsPane getVoiceSearchResultsPane() {
        return parent;
    }
}

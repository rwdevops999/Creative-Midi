package creative.scenes.voice;

import creative.scenes.base.Base;
import creative.scenes.base.BaseClosePane;
import creative.scenes.midi.data.ByteType;
import creative.scenes.midi.util.MidiDemo;
import creative.scenes.midi.util.MidiWriter;
import custom.components.voice.ChannelIndicator;
import custom.dialog.DialogFactory;
import entity.midi.Midi;
import entity.voice.Patch;
import eventhandlers.IThreadEventHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
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
import util.ApplicationInfo;
import util.Util;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.ShortMessage;
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
    private ToggleButton speaker;

    public SpeakerPane(VoiceSearchResultsPane owner) {
        this();

        parent = owner;

//        showPaneBorder(this, getColor("test", "red"));

        setPaneBackground(this);

        buildPane();
    }

    private static final int ICON_SIZE=24;
    private final StringProperty dsMidiName = new SimpleStringProperty(ApplicationInfo.getInstance().getMidiToTry().getName());

    EventHandler stopMidiHandler = (EventHandler) event -> {
        if (MidiDemo.isPlaying()) {
            MidiDemo.stopDemo(null);
        }
    };

    private void buildPane() {
        logger.debug("[CM_SPEAKER_PANE] Building {}", getId());

        ChannelIndicator midiChannelIndicator = new ChannelIndicator();

        speaker = new ToggleButton();
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
            if (speaker.isSelected()) {
                Base base = (Base) ApplicationInfo.getInstance().getSpa();
                BaseClosePane closePane = base.getClosePane();

                Patch selectedPatch = getVoiceSearchResultsPane().getSearchResultsPane().getSelectedPatch();
                if (selectedPatch == null) {
                    selectedPatch = new Patch(null, "Live! Grand Piano", "0", "115", "0", "Yamaha");
                }

                ApplicationInfo.getInstance().setSelectedPatch(selectedPatch);
                if (sendAsMidi(0, selectedPatch)) {
                    speaker.setStyle("-fx-background-color: #0096C9;");
                    playDemoFile((ToggleButton)(e.getSource()), midiChannelIndicator);
                    closePane.setActionHandler(stopMidiHandler);
                } else {
                    speaker.setSelected(false);
                    closePane.setActionHandler(null);
                }
            } else {
                stopDemoFile((ToggleButton)(e.getSource()), midiChannelIndicator);
                speaker.setStyle("-fx-background-color: darkgray;");
            }
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
            File selectedFile = fileChooser.showOpenDialog(ApplicationInfo.getInstance().getPrimaryStage());

            if (selectedFile != null) {
                dsMidiName.set(selectedFile.getName());
                ApplicationInfo.getInstance().setMidiToTry(selectedFile);
            }
        });
        getChildren().add(uploadButton);

        getChildren().add(midiChannelIndicator);

        logger.debug("[CM_SPEAKER_PANE] Building {}", getId());
    }

    public boolean sendAsMidi(int channel, Patch patch) {
        boolean result = true;

        MidiWriter midiWriter = new MidiWriter();

        Midi midi = new Midi();
        midi.setMessageType("channel");
        midi.setChannel(channel);
        midi.setByte1Type(ByteType.FreeValue);
        midi.setByte2Type(ByteType.FreeValue);

        midi.setStatus(String.format("%02X", ShortMessage.CONTROL_CHANGE));
        midi.setByte1(0);
        midi.setByte2(patch.getMsb());
        if (midiWriter.sendMidiMessageAsString(midi.toString())) {
            midi.setByte1(32);
            midi.setByte2(patch.getLsb());
            if (midiWriter.sendMidiMessageAsString(midi.toString())) {
                midi.setStatus(String.format("%02X", ShortMessage.PROGRAM_CHANGE));
                midi.setByte1(patch.getPc());
                midi.setByte2(0);
                midiWriter.sendMidiMessageAsString(midi.toString());
            } else {
                result = false;
            }
        } else {
            result = false;
        }

        return result;
    }

    public IThreadEventHandler anyHandler = new IThreadEventHandler() {
        @Override
        public void handle() {
            speaker.setSelected(false);
            speaker.setStyle("-fx-background-color: darkgray;");
        }
    };

    private void playDemoFile (ToggleButton button, ChannelIndicator midiChannelIndicator) {
        MidiDevice outputDevice = ApplicationInfo.getInstance().getMidiOutputDevice();
        if (outputDevice == null) {
            DialogFactory.renderWarningDialog("No device selected");
            button.setSelected(false);
        } else {
            Patch patch = ApplicationInfo.getInstance().getSelectedPatch();
            if (patch != null) {
                System.out.println("PLAY MIDI DEMO");
                MidiDemo.playDemo(anyHandler, anyHandler, midiChannelIndicator, 0);
            }
        }
    }

    private void stopDemoFile (ToggleButton button, ChannelIndicator midiChannelIndicator) {
        MidiDemo.stopDemo(midiChannelIndicator);
        System.out.println("STOP MIDI DEMO");
        button.setSelected(false);
    }

        // ACCESSORS
    public VoiceSearchResultsPane getVoiceSearchResultsPane() {
        return parent;
    }
}

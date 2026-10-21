package creative.scenes.voice;

import creative.scenes.base.Base;
import creative.scenes.base.BaseClosePane;
import creative.scenes.midi.data.ByteType;
import creative.scenes.midi.util.MidiDemo;
import creative.scenes.midi.util.MidiWriter;
import creative.scenes.voice.components.MultiChannelMidiReceiver;
import custom.components.LedToggleButton;
import custom.components.SpeakerToggle;
import custom.components.voice.ChannelIndicator;
import custom.dialog.DialogFactory;
import entity.midi.Midi;
import entity.voice.Patch;
import eventhandlers.IThreadEventHandler;
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
import system.SysData;
import util.ApplicationInfo;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.ShortMessage;
import java.io.File;
import java.util.*;

import static util.ColorScheme.getColor;
import static util.Constants.*;
import static util.Util.setPaneBackground;

public class SpeakerPane extends HBox {
    private static final Logger logger = LoggerFactory.getLogger(SpeakerPane.class);
    public SpeakerPane() {
        super();

        setId("SpeakerPane");

        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(0, 0, 2, 0));
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

    private void buildPane() {
        logger.debug("[CM_SPEAKER_PANE] Building {}", getId());

        Map<Integer, ChannelIndicator> ledMap = new HashMap<>();
        Map<Integer, Boolean> listen = new HashMap<>();

        ledMap.put(CHANNEL_RIGHT1, new ChannelIndicator("Ch 1:", getColor("voice", "ledRed", "on"), getColor("voice", "ledRed", "off")));
        ledMap.put(CHANNEL_LEFT, new ChannelIndicator("Ch 2 (⇧):", getColor("voice", "ledGreen", "on"), getColor("voice", "ledGreen", "off")));
        ledMap.put(CHANNEL_RIGHT2, new ChannelIndicator("Ch 3 (⌃):", getColor("voice", "ledBlue", "on"), getColor("voice", "ledBlue", "off")));

        listen.put(CHANNEL_RIGHT1, true);
        listen.put(CHANNEL_LEFT, true);
        listen.put(CHANNEL_RIGHT2, true);

        try (MultiChannelMidiReceiver multiChannelMidiReceiver = new MultiChannelMidiReceiver(ledMap, listen)) {
            speaker = new ToggleButton();
            speaker.setAlignment(Pos.CENTER);
            speaker.setStyle("-fx-background-color: darkgray;");

            Image icon = new Image(Objects.requireNonNull(VoiceSearchResultsPane.class.getClassLoader().getResourceAsStream("icons/speaker.png")));
            ImageView imageView = new ImageView(icon);
            imageView.setFitWidth(ICON_SIZE);
            imageView.setFitHeight(ICON_SIZE);
            speaker.setPrefSize(ICON_SIZE, ICON_SIZE);
            speaker.setPadding(new Insets(1));
            speaker.setGraphic(imageView);
            speaker.setOnAction((e) -> {
                Base base = (Base) ApplicationInfo.getInstance().getSpa();
                BaseClosePane closePane = base.getClosePane();
                if (speaker.isSelected()) {
                    Map<Integer, Patch> patches = getVoiceSearchResultsPane().getSearchResultsPane().getChannelPatches();
                    for (Integer channel : patches.keySet()) {
                        Patch selectedPatch = patches.get(channel);
                        ApplicationInfo.getInstance().setSelectedPatch(selectedPatch);
                        sendAsMidi(channel, selectedPatch);
                    }

                    speaker.setStyle("-fx-background-color: #0096C9;");
                    playDemoFile((ToggleButton) (e.getSource()), multiChannelMidiReceiver);
                    closePane.setActionHandler(event -> {
                        if (MidiDemo.isPlaying()) {
                            MidiDemo.stopDemo(null);
                        }
                    });
                } else {
                    speaker.setSelected(false);
                    closePane.setActionHandler(null);
                    stopDemoFile((ToggleButton) (e.getSource()), ledMap);
                    speaker.setStyle("-fx-background-color: darkgray;");
                }
            });
        }
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

        for (Integer channel : ledMap.keySet()) {
            SpeakerToggle toggle = new SpeakerToggle(listen.get(channel));
            toggle.setOnAction(event -> {
                listen.put(channel, toggle.isSelected());
            });
            getChildren().add(toggle);

            ChannelIndicator channelIndicator = ledMap.get(channel);
            getChildren().add(channelIndicator);
        }

        LedToggleButton favorites = new LedToggleButton("Favorites");
        favorites.setOnAction(e -> {
            VoicePane voicePane = getVoiceSearchResultsPane().getVoicePane();
            voicePane.getVoiceSearchPane().setSearchPanelsVisible(! favorites.isSelected());
            VoiceSearchResultsPane resultsPane = voicePane.getVoiceSearchResultsPane();

            if (favorites.isSelected()) {
                resultsPane.showResults(new ArrayList<>(SysData.getFavorites()));
            } else {
                resultsPane.showResults(null);
            }
        });

        getChildren().add(favorites);

        logger.debug("[CM_SPEAKER_PANE] Building {}", getId());
    }

    public boolean sendAsMidi(int channel, Patch patch) {
        boolean result = true;

        System.out.println("SEND AS MIDI to Channel " + channel + " Patch " + patch.getPatch());
/*        MidiWriter midiWriter = new MidiWriter();

        Midi midi = new Midi();
        midi.setMessageType("channel");
        midi.setChannel(channel);
        midi.setByte1Type(ByteType.FreeValue);
        midi.setByte2Type(ByteType.FreeValue);

        midi.setStatus(String.format("%02X", ShortMessage.CONTROL_CHANGE));
        midi.setByte1(0);
        midi.setByte2(patch.getMsb());
        if (midiWriter.sendMidiAsString(midi.toString())) {
            midi.setByte1(32);
            midi.setByte2(patch.getLsb());
            if (midiWriter.sendMidiAsString(midi.toString())) {
                midi.setStatus(String.format("%02X", ShortMessage.PROGRAM_CHANGE));
                midi.setByte1(patch.getPc());
                midi.setByte2(0);
                midiWriter.sendMidiAsString(midi.toString());
            } else {
                result = false;
            }
        } else {
            result = false;
        }
*/
        return result;
    }

    public IThreadEventHandler anyHandler = new IThreadEventHandler() {
        @Override
        public void handle() {
            speaker.setSelected(false);
            speaker.setStyle("-fx-background-color: darkgray;");
        }
    };

    private void playDemoFile (ToggleButton button, MultiChannelMidiReceiver multiChannelMidiReceiver) {
        MidiDevice outputDevice = ApplicationInfo.getInstance().getMidiOutputDevice();
        if (outputDevice == null) {
            DialogFactory.renderWarningDialog("No device selected");
            button.setSelected(false);
        } else {
            Patch patch = ApplicationInfo.getInstance().getSelectedPatch();
            if (patch != null) {
                MidiDemo.playDemo(anyHandler, anyHandler, multiChannelMidiReceiver);
            }
        }
    }

    private void stopDemoFile (ToggleButton button, Map<Integer, ChannelIndicator> midiChannelIndicators) {
        MidiDemo.stopDemo(midiChannelIndicators);
        button.setSelected(false);
    }

        // ACCESSORS
    public VoiceSearchResultsPane getVoiceSearchResultsPane() {
        return parent;
    }
}

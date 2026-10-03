package util;

import creative.scenes.midi.provider.MidiProvider;
import creative.scenes.playlist.entity.SharedEntity;
import creative.scenes.voice.provider.InstrumentProvider;
import entity.midi.Midi;
import entity.playlist.Song;
import entity.sysex.Sysex;
import entity.voice.Patch;
import javafx.concurrent.Service;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import javax.sound.midi.MidiDevice;
import java.io.File;
import java.util.List;

@Setter
@Getter
public class ApplicationInfo {
    private static final ApplicationInfo INSTANCE = new ApplicationInfo();

    private ApplicationInfo() {}

    public static ApplicationInfo getInstance() {
        return INSTANCE;
    }

    // Debugging
    private boolean debugging = false;

    // Primary Stage
    private Stage primaryStage;

    // Root Scene
    private Scene rootScene;

    /**
     * This is the SPA pane. It is also the rootPane for all spa content
     */
    private Pane spaOwner;

    /**
     * This is the SPA pane. It is also the rootPane for all spa content
     */
    private Pane spa;

    /**
     * Test Mode On/Off switch
     */
    private boolean testMode = false;

    /**
     * MIDI Provider with loaded midis from file
     */
    private MidiProvider midiProvider;

    /**
     * Current MIDI
     */
    private Midi currentMidi;

    /**
     * Dirty MIDI
     */
    private Midi dirtyMidi;

    /**
     * Global Dirty : when something is removed from list or added to list => for export
     */
    private boolean globalDirty;

    /**
     * The Selected output device
     */
    private String selectedDevice;

    /**
     * devices
     */
    private MidiDevice midiInputDevice;
    private MidiDevice midiOutputDevice;

    /**
     * Filename of available voice names
     */
    private List<String> voiceFilenames;

    /**
     * the instrument provider
     */
    private InstrumentProvider instrumentProvider;

    /**
     * The patch to be used for playing a midi
     */
    private Patch selectedPatch;

    /** The Midi File to play in voices
     *
     */
    private File midiToTry;

    /**
     * Current Sysex and DirtySysex
     */
    private Sysex currentSysex;
//    private Sysex dirtySysex = null;

    /**
     * Device Scanner Thread
     */
    private Thread deviceScannerThread;

    /**
     * Indiciation which keyboard file is loaded
     */
    private String keyboardProperties = null;

    /**
     * Current Song and DirtySong
     */
    private Song currentSong;
//    private Song dirtySong = null;

    /**
     * Keeping track of the keyboard (play) service
     */
    private Service<SharedEntity> keyboardService;

    /**
     * The midi events list has changed (and so also the midi file)
     */
    private boolean midiChanged = false;
}

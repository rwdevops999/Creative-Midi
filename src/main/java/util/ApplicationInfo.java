package util;

import creative.scenes.midi.MidiListPane;
import creative.scenes.midi.provider.MidiProvider;
import entity.midi.Midi;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

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
}

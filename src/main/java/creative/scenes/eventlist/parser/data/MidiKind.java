package creative.scenes.eventlist.parser.data;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Kind of Midi Events
 */
public enum MidiKind {
    CONTROL("Control"),
    NOTE("Note"),
    PITCH("Pitch"),
    PATCH("Program Change"),
    POLY_PRESSURE("Key Aftertouch"),
    CHANNEL_PRESSURE("Key Aftertouch"),
    UNKNOWN_META("Unknown Meta");

    private static final Map<String, MidiKind> BY_VALUE = new HashMap<>();

    static {
        for (MidiKind midiKind : values()) {
            BY_VALUE.put(midiKind.kindValue, midiKind);
        }
    }

    @Getter
    private String kindValue;

    public static MidiKind ofKindValue(String kindValue) {
            return BY_VALUE.get(kindValue); // Returns null if code doesn't exist
    }

    MidiKind(String value) {
        kindValue = value;
    }
}

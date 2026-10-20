package creative.scenes.eventlist.parser.data;

import lombok.Getter;

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

    @Getter
    private final String kindValue;

    MidiKind(String value) {
        kindValue = value;
    }
}

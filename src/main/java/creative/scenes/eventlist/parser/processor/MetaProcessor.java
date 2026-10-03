package creative.scenes.eventlist.parser.processor;

import creative.scenes.eventlist.parser.data.EventType;
import creative.scenes.eventlist.parser.entity.MidiEventInfo;
import creative.scenes.sysex.convertor.SysexToHexStringConvertor;
import javafx.scene.paint.Color;
import util.ColorScheme;

import javax.sound.midi.MetaMessage;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static util.ColorScheme.getColor;

public class MetaProcessor {
    private static final Map<ColorScheme.ColorKey, Color> colors = new HashMap<>();
    private static final Map<Integer, String> REGISTRY = new HashMap<>();

    static {
        colors.put(new ColorScheme.ColorKey("meta"), Color.INDIGO);

        colors.put(new ColorScheme.ColorKey("meta", "tempo"), Color.GREENYELLOW);
        colors.put(new ColorScheme.ColorKey("meta", "marker"), Color.AQUAMARINE);

//       colors.put(new ColorScheme.ColorKey("meta", "", ""), Color.);
        ColorScheme.registerColors(colors);

        // --- 1-Byte Standard IDs ---
        REGISTRY.put(0x01, "Sequential Circuits");
        REGISTRY.put(0x04, "Moog Music");
        REGISTRY.put(0x06, "Lexicon");
        REGISTRY.put(0x41, "Roland Corporation");
        REGISTRY.put(0x42, "Korg Inc.");
        REGISTRY.put(0x43, "Yamaha Corporation");
        REGISTRY.put(0x47, "Akai Professional");

        // --- 3-Byte Extended IDs (0x00XXXX) ---
        REGISTRY.put(0x00000E, "Apple Inc.");
        REGISTRY.put(0x000041, "Cakewalk / Twelve Tone Systems");
        REGISTRY.put(0x00010B, "Propellerhead Software");
        REGISTRY.put(0x00015B, "Steinberg Media Technologies");
        REGISTRY.put(0x002011, "Gibson Guitar");
        REGISTRY.put(0x00206B, "Arturia");

        // --- Special IDs ---
        REGISTRY.put(0x7D, "Educational / Research Use (Non-Commercial)");
    }

    /**
     * Process Meta event messages
     *
     * @param message the meta message
     * @param midiEventInfo the collectiong midiEvent
     * @return true, if the event should be visible, false if the event shouldn't be visible
     */
    public static boolean processMessage(MetaMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        String messageInfo = SysexToHexStringConvertor.convertToHexString(message.getData());
        midiEventInfo.setEventType(EventType.META);
        midiEventInfo.setColor(getColor("meta"));

        int type = message.getType();
        byte[] data = message.getData();

        switch (type) {
//            case 0x00 -> handleSequencenumber(data, midiEventInfo);
            case 0x01 -> handleText(data, midiEventInfo);
            case 0x02 -> handleCopyright(data, midiEventInfo);
            case 0x03 -> handleTrackname(data, midiEventInfo);
            case 0x04 -> handleInstrumentname(data, midiEventInfo);
            case 0x05 -> handleLyric(data, midiEventInfo);
            case 0x06 -> handleMarker(data, midiEventInfo);
            case 0x07 -> handleCuepoint(data, midiEventInfo);
//            case 0x08 -> handleProgramname(data, midiEventInfo);
//            case 0x09 -> handleDevicename(data, midiEventInfo);
            case 0x21 -> handleMIDIport(data, midiEventInfo);
            case 0x2F -> result = false; // We don't want this in the events list, so KEEP this
            case 0x51 -> handleTempo(data, midiEventInfo);
            case 0x54 -> handleSMPTEoffset(data, midiEventInfo);
            case 0x58 -> handleTimesignature(data, midiEventInfo);
            case 0x59 -> handleKeysignature(data, midiEventInfo);
            case 0x7F -> {
                handleSequencer(data, midiEventInfo);
                result = false;
            }
            default -> handleUnknown(type, midiEventInfo);
        }

        String typeString = String.format("%02X", type & 0xFF);
        midiEventInfo.setMessage("FF " + typeString + " " + messageInfo);

//        return result;
        return false; // TODO remove this because at development time I don't want to see META messages
    }

    private static void handleText(byte[] data, MidiEventInfo midiEventInfo) {
        midiEventInfo.setColor(getColor("meta", "text"));

        midiEventInfo.setDescription("Text");
        midiEventInfo.setComment(new String(data, StandardCharsets.ISO_8859_1));
    }

    /**
     * The Text specifies the title of the track or sequence
     *
     * @param data
     * @param midiEventInfo
     */
    private static void handleTrackname(byte[] data, MidiEventInfo midiEventInfo) {
        midiEventInfo.setColor(getColor("meta", "trackname"));

        midiEventInfo.setDescription("Track Name");
        midiEventInfo.setComment(new String(data, StandardCharsets.ISO_8859_1));
    }

    private static void handleCopyright(byte[] data, MidiEventInfo midiEventInfo) {
        midiEventInfo.setColor(getColor("meta", "copyright"));

        midiEventInfo.setDescription("Copyright");
        midiEventInfo.setComment(new String(data, StandardCharsets.ISO_8859_1));
    }

    private static void handleInstrumentname(byte[] data, MidiEventInfo midiEventInfo) {
        midiEventInfo.setColor(getColor("meta", "instrument"));

        midiEventInfo.setDescription("Instrument");
        midiEventInfo.setComment(new String(data, StandardCharsets.ISO_8859_1));
    }

    private static void handleLyric(byte[] data, MidiEventInfo midiEventInfo) {
        midiEventInfo.setColor(getColor("meta", "lyric"));

        midiEventInfo.setDescription("Lyric");
        midiEventInfo.setComment(new String(data, StandardCharsets.ISO_8859_1));
    }

    private static void handleMarker(byte[] data, MidiEventInfo midiEventInfo) {
        midiEventInfo.setColor(getColor("meta", "marker"));

        midiEventInfo.setDescription("Marker");
        midiEventInfo.setComment(new String(data, StandardCharsets.ISO_8859_1));
    }

    private static void handleCuepoint(byte[] data, MidiEventInfo midiEventInfo) {
        midiEventInfo.setColor(getColor("meta", "cue point"));

        midiEventInfo.setDescription("Cue Point");
        midiEventInfo.setComment(new String(data, StandardCharsets.ISO_8859_1));
    }

    private static void handleMIDIport(byte[] data, MidiEventInfo midiEventInfo) {
        midiEventInfo.setColor(getColor("meta", "midi port"));

        midiEventInfo.setDescription("Midi Port");

        int portNumber = data[0] & 0xFF;

        midiEventInfo.setComment("" + portNumber);
    }

    private static void handleEndOfTrack(byte[] data, MidiEventInfo midiEventInfo) {
    }

    private static void handleTempo(byte[] data, MidiEventInfo midiEventInfo) {
        midiEventInfo.setColor(getColor("meta", "tempo"));

        midiEventInfo.setDescription("Tempo");

        int mspq = ((data[0] & 0xFF) << 16)
                | ((data[1] & 0xFF) << 8)
                | (data[2] & 0xFF);

        double bpm = 60_000_000.0 / mspq;
        int bpmDisplay = (int) Math.round(bpm);

        midiEventInfo.setComment(String.format("%d bpm", bpmDisplay));
    }

    private static void handleSMPTEoffset(byte[] data, MidiEventInfo midiEventInfo) {
        midiEventInfo.setColor(getColor("meta", "smpte offset"));

        midiEventInfo.setDescription("SMPTE Offset");

        int firstByte = data[0] & 0xFF;
        int minutes   = data[1] & 0xFF;
        int seconds   = data[2] & 0xFF;
        int frames    = data[3] & 0xFF;
        int subFrames = data[4] & 0xFF;

        // Extract Frame Rate from the top 2 bits (bits 5 and 6)
        int fpsCode = (firstByte & 0x60) >> 5;
        double fps = 24.0;
        String fpsLabel = "24 FPS";

        switch (fpsCode) {
            case 0 -> { fps = 24.0; fpsLabel = "24 FPS"; }
            case 1 -> { fps = 25.0; fpsLabel = "25 FPS"; }
            case 2 -> { fps = 29.97; fpsLabel = "30 FPS (Drop Frame)"; }
            case 3 -> { fps = 30.0; fpsLabel = "30 FPS (Non-Drop)"; }
        }

        // Extract Hours from the lower 5 bits
        int hours = firstByte & 0x1F;

        String smpte = String.format("%02d:%02d:%02d:%02d.%02d (%s)",
                hours, minutes, seconds, frames, subFrames, fpsLabel);
        midiEventInfo.setComment(smpte);
    }

    /**
     * Time signature is expressed as 4 numbers. nn and dd represent the "numerator" and "denominator" of the signature as notated on sheet music. The denominator is a negative power of 2: 2 = quarter note, 3 = eighth, etc.
     *
     * @param data
     * @param midiEventInfo
     */
    private static void handleTimesignature(byte[] data, MidiEventInfo midiEventInfo) {
        midiEventInfo.setColor(getColor("meta", "timesignature"));

        midiEventInfo.setDescription("Time Signature");

        int numerator = data[0] & 0xFF;
        int denominatorExponent = data[1] & 0xFF;
        int denominator = 1 << denominatorExponent;

        midiEventInfo.setComment(String.format("%d/%d", numerator, denominator));
    }

    private static void handleKeysignature(byte[] data, MidiEventInfo midiEventInfo) {
        midiEventInfo.setColor(getColor("meta", "keysignature"));

        midiEventInfo.setDescription("Key Signature");

        int sharpsFlats = data[0];
        int isMinor = data[1];

        String scaleType = (isMinor == 1) ? "Minor" : "Major";
        String keyName = getMusicalKeyName(sharpsFlats, isMinor == 1);

        midiEventInfo.setComment(String.format("%s (%s)", keyName, scaleType));
    }

    private static void handleSequencer(byte[] data, MidiEventInfo midiEventInfo) {
        midiEventInfo.setColor(getColor("meta", "sequencer"));

        midiEventInfo.setDescription("Sequencer Data");

        int manufacturerId;

        // Determine Manufacturer ID (Standard MIDI Sysex/Meta Rules)
        if (data[0] != 0x00) {
            // 1-byte Manufacturer ID
            manufacturerId = data[0] & 0xFF;
        } else if (data.length >= 3) {
            // 3-byte Manufacturer ID (starts with 0x00)
            manufacturerId = ((data[0] & 0xFF) << 16) | ((data[1] & 0xFF) << 8) | (data[2] & 0xFF);
        } else {
            // Malformed data
            return;
        }

        // Process the data based on the specific software/hardware manufacturer
        String vendorId = REGISTRY.getOrDefault(manufacturerId, "Unknown Manufacturer");
        midiEventInfo.setComment(vendorId);
    }

    private static void handleUnknown(int type, MidiEventInfo midiEventInfo) {
        midiEventInfo.setColor(getColor("meta", "unknown"));
        midiEventInfo.setDescription("Unknown META event");
        midiEventInfo.setComment(String.format("%X2", type));
    }

        // Helper Methods
    private static String getMusicalKeyName(int sharpsFlats, boolean isMinor) {
        if (!isMinor) {
            switch (sharpsFlats) {
                case -7: return "Cb"; case -6: return "Gb"; case -5: return "Db";
                case -4: return "Ab"; case -3: return "Eb"; case -2: return "Bb";
                case -1: return "F";  case 0: return "C";   case 1: return "G";
                case 2: return "D";   case 3: return "A";   case 4: return "E";
                case 5: return "B";   case 6: return "F#";  case 7: return "C#";
                default: return "Unknown";
            }
        } else {
            switch (sharpsFlats) {
                case -7: return "Ab"; case -6: return "Eb"; case -5: return "Bb";
                case -4: return "F";  case -3: return "C";  case -2: return "G";
                case -1: return "D";  case 0: return "A";   case 1: return "E";
                case 2: return "B";   case 3: return "F#";  case 4: return "C#";
                case 5: return "G#";  case 6: return "D#";  case 7: return "A#";
                default: return "Unknown";
            }
        }
    }
}

package creative.scenes.eventlist.parser.processor;

import creative.scenes.eventlist.parser.data.EventType;
import creative.scenes.eventlist.parser.entity.MidiEventInfo;
import creative.scenes.sysex.convertor.SysexToHexStringConvertor;
import javafx.scene.paint.Color;
import util.ColorScheme;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.ShortMessage;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static util.ColorScheme.getColor;

public class MetaProcessor {
    private static final Map<ColorScheme.ColorKey, Color> colors = new HashMap<>();

    static {
        colors.put(new ColorScheme.ColorKey("meta"), Color.INDIGO);

        colors.put(new ColorScheme.ColorKey("meta", "tempo"), Color.GREENYELLOW);
        colors.put(new ColorScheme.ColorKey("meta", "marker"), Color.AQUAMARINE);

//       colors.put(new ColorScheme.ColorKey("meta", "", ""), Color.);
        ColorScheme.registerColors(colors);
    }

    /**
     * Process Meta event messages
     *
     * @param message the meta message
     * @param midiEventInfo the collectiong midiEvent
     * @return true, if the event should be visible, false if the event shouldn't be visible
     */
    public static boolean processMessage(MetaMessage message, MidiEventInfo midiEventInfo) {
        boolean result = false;

        String messageInfo = SysexToHexStringConvertor.convertToHexString(message.getData());
        midiEventInfo.setEventType(EventType.META);
        midiEventInfo.setColor(getColor("meta"));

        System.out.println("PROCESS THE EVENT");
        int type = message.getType();
        byte[] data = message.getData();

        switch (type) {
            case 0x00 -> handleSequencenumber(data, midiEventInfo);
            case 0x01 -> handleText(data, midiEventInfo);
            case 0x02 -> handleCopyright(data, midiEventInfo);
            case 0x03 -> {
                messageInfo = "FF 03 " + messageInfo;
                handleTrackname(data, midiEventInfo);
                result = true;
            }
            case 0x04 -> handleInstrumentname(data, midiEventInfo);
            case 0x05 -> handleLyric(data, midiEventInfo);
            case 0x06 -> handleMarker(data, midiEventInfo);
            case 0x07 -> handleCuepoint(data, midiEventInfo);
            case 0x08 -> handleProgramname(data, midiEventInfo);
            case 0x09 -> handleDevicename(data, midiEventInfo);
            case 0x2F -> {
                handleEndOfTrack(data, midiEventInfo);
                result = false;
            }
            case 0x51 -> handleTempo(data, midiEventInfo);
            case 0x54 -> handleSMPTEoffset(data, midiEventInfo);
            case 0x58 -> handleTimesignature(data, midiEventInfo);
            case 0x59 -> handleKeysignature(data, midiEventInfo);
            case 0x7F -> handleSequencer(data, midiEventInfo);
            default -> result = false;
        }

        midiEventInfo.setMessage(messageInfo);

        return result;
    }

    private static void handleSequencenumber(byte[] data, MidiEventInfo midiEventInfo) {
        System.out.println("SEQUENCENUMBER");
    }

    private static void handleText(byte[] data, MidiEventInfo midiEventInfo) {
        System.out.println("TEXT");
    }

    private static void handleTrackname(byte[] data, MidiEventInfo midiEventInfo) {
        midiEventInfo.setColor(getColor("meta", "trackname"));

        midiEventInfo.setDescription("Track Name");
        midiEventInfo.setComment(new String(data, StandardCharsets.ISO_8859_1));
    }

    private static void handleCopyright(byte[] data, MidiEventInfo midiEventInfo) {
        System.out.println("COPYRIGHT");
    }

    private static void handleInstrumentname(byte[] data, MidiEventInfo midiEventInfo) {
        System.out.println("INSTRUMENTNAME");
    }

    private static void handleLyric(byte[] data, MidiEventInfo midiEventInfo) {
        System.out.println("LYRIC");
    }

    private static void handleMarker(byte[] data, MidiEventInfo midiEventInfo) {
        System.out.println("MARKER");
    }

    private static void handleCuepoint(byte[] data, MidiEventInfo midiEventInfo) {
        System.out.println("CUEPOINT");
    }

    private static void handleProgramname(byte[] data, MidiEventInfo midiEventInfo) {
        System.out.println("PROGRAMNAME");
    }

    private static void handleDevicename(byte[] data, MidiEventInfo midiEventInfo) {
        System.out.println("DEVICENAME");
    }

    private static void handleEndOfTrack(byte[] data, MidiEventInfo midiEventInfo) {
        System.out.println("END OF TRACK");
    }

    private static void handleTempo(byte[] data, MidiEventInfo midiEventInfo) {
        System.out.println("TEMPO");
    }

    private static void handleSMPTEoffset(byte[] data, MidiEventInfo midiEventInfo) {
        System.out.println("SMPTEOFFSET");
    }

    private static void handleTimesignature(byte[] data, MidiEventInfo midiEventInfo) {
        System.out.println("TIMESIGNATURE");
    }

    private static void handleKeysignature(byte[] data, MidiEventInfo midiEventInfo) {
        System.out.println("KEYSIGNATURE");
    }

    private static void handleSequencer(byte[] data, MidiEventInfo midiEventInfo) {
        System.out.println("SEQUENCER");
    }
}

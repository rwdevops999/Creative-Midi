package creative.scenes.eventlist.parser.processor;

import creative.scenes.eventlist.parser.data.EventType;
import creative.scenes.eventlist.parser.entity.MidiEventInfo;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.ShortMessage;

import static util.ColorScheme.getColor;

public class MetaProcessor {
    /**
     * Process Meta event messages
     *
     * @param message the meta message
     * @param midiEventInfo the collectiong midiEvent
     * @return true, if the event should be visible, false if the event shouldn't be visible
     */
    public static boolean processMessage(MetaMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setEventType(EventType.META);
        midiEventInfo.setColor(getColor("meta"));

        System.out.println("PROCESS THE EVENT");
        int type = message.getType();
        byte[] data = message.getData();

        switch (type) {
            case 0x00 -> handleSequencenumber(data, midiEventInfo);
            case 0x01 -> handleText(data, midiEventInfo);
            case 0x02 -> handleCopyright(data, midiEventInfo);
            case 0x03 -> handleTrackname(data, midiEventInfo);
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

        return result;
    }

    private static void handleSequencenumber(byte[] data, MidiEventInfo midiEventInfo) {

    }

    private static void handleText(byte[] data, MidiEventInfo midiEventInfo) {

    }

    private static void handleTrackname(byte[] data, MidiEventInfo midiEventInfo) {

    }

    private static void handleCopyright(byte[] data, MidiEventInfo midiEventInfo) {

    }

    private static void handleInstrumentname(byte[] data, MidiEventInfo midiEventInfo) {

    }

    private static void handleLyric(byte[] data, MidiEventInfo midiEventInfo) {

    }

    private static void handleMarker(byte[] data, MidiEventInfo midiEventInfo) {

    }

    private static void handleCuepoint(byte[] data, MidiEventInfo midiEventInfo) {

    }

    private static void handleProgramname(byte[] data, MidiEventInfo midiEventInfo) {

    }

    private static void handleDevicename(byte[] data, MidiEventInfo midiEventInfo) {

    }

    private static void handleEndOfTrack(byte[] data, MidiEventInfo midiEventInfo) {

    }

    private static void handleTempo(byte[] data, MidiEventInfo midiEventInfo) {

    }

    private static void handleSMPTEoffset(byte[] data, MidiEventInfo midiEventInfo) {

    }

    private static void handleTimesignature(byte[] data, MidiEventInfo midiEventInfo) {

    }

    private static void handleKeysignature(byte[] data, MidiEventInfo midiEventInfo) {

    }

    private static void handleSequencer(byte[] data, MidiEventInfo midiEventInfo) {

    }
}

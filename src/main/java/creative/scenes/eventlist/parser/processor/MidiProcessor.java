package creative.scenes.eventlist.parser.processor;

import creative.scenes.eventlist.parser.data.EventType;
import creative.scenes.eventlist.parser.entity.MidiEventInfo;
import creative.scenes.sysex.convertor.SysexToHexStringConvertor;
import javafx.scene.paint.Color;
import util.ColorScheme;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.util.HashMap;
import java.util.Map;

import static util.ColorScheme.getColor;

public class MidiProcessor {
    private static final Map<ColorScheme.ColorKey, Color> colors = new HashMap<>();

    static {
        colors.put(new ColorScheme.ColorKey("midi"), Color.BLUE);

        ColorScheme.registerColors(colors);
    }

    public static boolean processMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        midiEventInfo.setEventType(EventType.MIDI);
        midiEventInfo.setColor(getColor("midi"));
        midiEventInfo.setMessage(SysexToHexStringConvertor.convertToHexString(message.getMessage()));

        int status = message.getStatus() & 0xF0;
        int channelOS = message.getStatus() & 0x0F;
        int channelOM = message.getChannel();
        System.out.println(String.format("%2X (BASE = %2X, OS = %2d, OM = %2d)", message.getStatus(), status, channelOS, channelOM));

        switch (status) {
            case 0x80,0x90,0xA0,0xB0,0xC0,0xD0,0xE0 -> handleChannelMessage(message, midiEventInfo);
            case 0xF0 -> handleSystemMessage(message, midiEventInfo);
            default -> handleUnknownMidiMessage(message, midiEventInfo);
        }

        return true;
    }

    private static void handleChannelMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        byte[] rawBytes = message.getMessage();

        int command = rawBytes[0] & 0xF0;
        int data1 = rawBytes[1] & 0xFF;
        int data2 = 0;

        if (rawBytes.length == 3) {
            data2 = rawBytes[2] & 0xFF;
        }

        switch (command) {
            case 0x80 -> handleNoteOff(message, midiEventInfo);
            case 0x90 -> handleNoteOn(message, midiEventInfo);
            case 0xB0 -> handleControlOrModeChange(message, midiEventInfo);
            case 0xC0 -> handleProgramChange(message, midiEventInfo);
            case 0xD0 -> handleChannelAfterTouch(message, midiEventInfo);
            case 0xA0 -> handlePolyAfterTouch(message, midiEventInfo);
            case 0xE0 -> handlePitchBend(message, midiEventInfo);
            default -> handleUnknownChannelMessage(message, midiEventInfo);
        }
    }

    private static void handleNoteOn(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("Note On");
    }

    private static void handleNoteOff(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("Note Off");
    }

    private static void handleControlOrModeChange(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("Control Or Mode Change");
    }

    private static void handleProgramChange(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("Program Change");
    }

    private static void handleChannelAfterTouch(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("Channel After Touch");
    }

    private static void handlePolyAfterTouch(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("PolyAfter Touch");
    }

    private static void handlePitchBend(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("Pitch Bend");
    }

    private static void handleUnknownChannelMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("Unknown Channel Message");
    }

    private static void handleSystemMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        byte[] rawBytes = message.getMessage();

        int command = rawBytes[0] & 0xFF;

        switch (command) {
            case 0xF8 -> handleMIDIClock(message, midiEventInfo);
            case 0xFA -> handleStart(message, midiEventInfo);
            case 0xFB -> handleContinue(message, midiEventInfo);
            case 0xFC -> handleStop(message, midiEventInfo);
            case 0xFE -> handleActiveSense(message, midiEventInfo);
            case 0xFF -> handleSystemReset(message, midiEventInfo);
            default -> handleUnknownSystemMessage(message, midiEventInfo);
        }
    }

    private static void handleMIDIClock(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("MIDIClock");
    }

    private static void handleStart(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("Start");
    }

    private static void handleContinue(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("Continue");
    }

    private static void handleStop(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("Stop");
    }

    private static void handleActiveSense(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("ActiveSense");
    }

    private static void handleSystemReset(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("System Reset");
    }

    private static void handleUnknownSystemMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("Unknown System Message");
    }

    private static void handleUnknownMidiMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("Unknown midi message");
    }
}

package creative.scenes.eventlist.parser.processor;

import creative.scenes.eventlist.parser.data.EventType;
import creative.scenes.eventlist.parser.entity.MidiEventInfo;
import creative.scenes.eventlist.parser.processor.matcher.MidiNoteMatcher;
import creative.scenes.sysex.convertor.SysexToHexStringConvertor;
import javafx.scene.paint.Color;
import util.ColorScheme;
import util.Util;
import util.properties.PropertyContainer;
import util.properties.PropertyType;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.util.HashMap;
import java.util.Map;

import static util.ColorScheme.getColor;
import static util.Util.calcNote;

public class MidiProcessor {
    private static final Map<ColorScheme.ColorKey, Color> colors = new HashMap<>();

    static {
        colors.put(new ColorScheme.ColorKey("midi"), Color.BLUE);

        ColorScheme.registerColors(colors);
    }

    public static boolean processMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setEventType(EventType.MIDI);
        midiEventInfo.setColor(getColor("midi"));
        midiEventInfo.setMessage(SysexToHexStringConvertor.convertToHexString(message.getMessage()));

        int status = message.getStatus() & 0xF0;

        result = switch (status) {
            case
                    0x80,
                    0x90,
                    0xA0,
                    0xB0,
                    0xC0,
                    0xD0,
                    0xE0 -> handleChannelMessage(message, midiEventInfo);
            case
                    0xF0 -> handleSystemMessage(message, midiEventInfo);
            default -> handleUnknownMidiMessage(message, midiEventInfo);
        };

        return result;
    }

    private static boolean handleChannelMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        byte[] rawBytes = message.getMessage();

        int command = rawBytes[0] & 0xF0;
        int data1 = rawBytes[1] & 0xFF;
        int data2 = 0;

        if (rawBytes.length == 3) {
            data2 = rawBytes[2] & 0xFF;
        }

        result = switch (command) {
            case 0x80 -> handleNoteOff(message, midiEventInfo);
            case 0x90 -> handleNoteOn(message, midiEventInfo);
            case 0xB0 -> handleControlOrModeChange(message, midiEventInfo);
            case 0xC0 -> handleProgramChange(message, midiEventInfo);
            case 0xD0 -> handleChannelAfterTouch(message, midiEventInfo);
            case 0xA0 -> handlePolyAfterTouch(message, midiEventInfo);
            case 0xE0 -> handlePitchBend(message, midiEventInfo);
            default -> handleUnknownChannelMessage(message, midiEventInfo);
        };

        return result;
    }

    private static MidiNoteMatcher midiNoteMatcher = new MidiNoteMatcher();
    private static boolean handleNoteOn(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "note on"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Note");

        int data1 = message.getData1();
        int data2 = message.getData2();

        int baseOctave = PropertyContainer.getPropertyAsInteger(PropertyType.Keyboard, PropertyContainer.BASE_OCTAVE, 0);

        midiEventInfo.setComment(calcNote(data1, baseOctave).getName());
        midiEventInfo.setData2(data2);

        result = midiNoteMatcher.processEvent(message, midiEventInfo);

        return result;
    }

    private static boolean handleNoteOff(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "note off"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Note");

        int data1 = message.getData1();
        int data2 = message.getData2();

        int baseOctave = PropertyContainer.getPropertyAsInteger(PropertyType.Keyboard, PropertyContainer.BASE_OCTAVE, 0);

        midiEventInfo.setComment(calcNote(data1, baseOctave).getName());
        midiEventInfo.setData2(data2);

        result = midiNoteMatcher.processEvent(message, midiEventInfo);

        return result;
    }

    private static boolean handleControlOrModeChange(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = false;

        byte[] rawbytes = message.getMessage();

        int byte1 = rawbytes[1] & 0xFF;
        int byte2 = rawbytes[2] & 0xFF;

        switch (byte1) {
            case
                    0x00,
                    0x01,
                    0x05,
                    0x06,
                    0x07,
                    0x0A,
                    0x0B,
                    0x10,
                    0x20,
                    0x26,
                    0x40,
                    0x41,
                    0x42,
                    0x43,
                    0x47,
                    0x48,
                    0x49,
                    0x4A,
                    0x4B,
                    0x4C,
                    0x4D,
                    0x4E,
                    0x50,
                    0x51,
                    0x52,
                    0x54,
                    0x5B,
                    0x5D,
                    0x5E,
                    0x60,
                    0x61,
                    0x62,
                    0x63,
                    0x64,
                    0x65 -> handleControlChangeMessage(message, midiEventInfo);
            case
                    0x78,
                    0x79,
                    0x7A,
                    0x7B,
                    0x7C,
                    0x7D,
                    0x7E,
                    0x7F -> handleModeChangeMessage(message, midiEventInfo);
            default -> handleUnknownControlOrModeMessage(message, midiEventInfo);
        };

        return result;
    }

    private static void handleControlChangeMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        byte[] rawbytes = message.getMessage();

        int byte1 = rawbytes[1] & 0xFF;

        switch (byte1) {
            case 0x00 -> handleBankSelectMSB(message, midiEventInfo);
            case 0x01 -> handleModulation(message, midiEventInfo);
            case 0x05 -> handlePortamento(message, midiEventInfo);
            case 0x06 -> handleDataEntryMSB(message, midiEventInfo);
            case 0x07 -> handleMainVolume(message, midiEventInfo);
            case 0x0A -> handlePanpot(message, midiEventInfo);
            case 0x0B -> handleExpression(message, midiEventInfo);
            case 0x10 -> handleGeneralPurposeController(message, midiEventInfo);
            case 0x20 -> handleBankSelectLSB(message, midiEventInfo);
            case 0x26 -> handleDataEntryLSB(message, midiEventInfo);
            case 0x40 -> handleSustainDamper(message, midiEventInfo);
            case 0x41 -> handlePortamento(message, midiEventInfo);
            case 0x42 -> handleSostenuto(message, midiEventInfo);
            case 0x43 -> handleSoftPedal(message, midiEventInfo);
            case 0x47 -> handleResonance(message, midiEventInfo);
            case 0x48 -> handleReleaseTime(message, midiEventInfo);
            case 0x49 -> handleAttackTime(message, midiEventInfo);
            case 0x4A -> handleCutoff(message, midiEventInfo);
            case 0x4B -> handleDecayTime(message, midiEventInfo);
            case 0x4C -> handleVibratoRange(message, midiEventInfo);
            case 0x4D -> handleVibratoDepth(message, midiEventInfo);
            case 0x4E -> handleVibratoDelay(message, midiEventInfo);
            case 0x50 -> handleArticulation1(message, midiEventInfo);
            case 0x51 -> handleArticulation2(message, midiEventInfo);
            case 0x52 -> handleArticulation3(message, midiEventInfo);
            case 0x54 -> handlePortamentoControl(message, midiEventInfo);
            case 0x5B -> handleReverbSendLevel(message, midiEventInfo);
            case 0x5D -> handleChorusSendLevel(message, midiEventInfo);
            case 0x5E -> handleVariationSendLevel(message, midiEventInfo);

            case 0x60 -> handleRPNIncrement(message, midiEventInfo);
            case 0x61 -> handleRPNDecrement(message, midiEventInfo);
            case 0x62 -> handleNRPNLsb(message, midiEventInfo);
            case 0x63 -> handleNRPNMsb(message, midiEventInfo);
            case 0x64 -> handleRPNLsb(message, midiEventInfo);
            case 0x65 -> handleRPNMsb(message, midiEventInfo);
            default -> handleUnknownControlChangeMessage(message, midiEventInfo);
        }
    }

    private static void handleBankSelectMSB(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleModulation(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handlePortamento(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleDataEntryMSB(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleMainVolume(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handlePanpot(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleExpression (ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleGeneralPurposeController(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleBankSelectLSB(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleDataEntryLSB(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleSustainDamper(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleVibratoRange(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleArticulation1(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleArticulation2(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleSostenuto(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleSoftPedal(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleResonance(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleReleaseTime(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleAttackTime(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleCutoff(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleDecayTime(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleVibratoDepth(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleVibratoDelay(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleChorusSendLevel(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleRPNIncrement(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleArticulation3(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handlePortamentoControl(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleReverbSendLevel(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleVariationSendLevel(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleRPNDecrement(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleNRPNLsb(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleNRPNMsb(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleRPNLsb(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleRPNMsb(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleUnknownControlChangeMessage(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleModeChangeMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        byte[] rawbytes = message.getMessage();

        int byte1 = rawbytes[1] & 0xFF;

        switch (byte1) {
            case 0x78 -> handleAllSoundOff(message, midiEventInfo);
            case 0x79 -> handleResetAllControllers(message, midiEventInfo);
            case 0x7A -> handleLocalControl(message, midiEventInfo);
            case 0x7B -> handleAllNoteOff(message, midiEventInfo);
            case 0x7C -> handleOmniOff(message, midiEventInfo);
            case 0x7D -> handleOmniOn(message, midiEventInfo);
            case 0x7E -> handleMono(message, midiEventInfo);
            case 0x7F -> handlePoly(message, midiEventInfo);
            default -> handleUnknownModeChangeMessage(message, midiEventInfo);
        }
    }

    private static void handleAllSoundOff(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleLocalControl(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleOmniOff(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleOmniOn(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleMono(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handlePoly(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleAllNoteOff(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleAllNoteOn(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleResetAllControllers(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleUnknownModeChangeMessage(ShortMessage message, MidiEventInfo midiEventInfo) {

    }

    private static void handleUnknownControlOrModeMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("Unknown Control Or Mode Change Message");
    }

    private static boolean handleProgramChange(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("Program Change");
        return false;
    }

    private static boolean handleChannelAfterTouch(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("Channel After Touch");
        return false;
    }

    private static boolean handlePolyAfterTouch(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("PolyAfter Touch");
        return false;
    }

    private static boolean handlePitchBend(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("Pitch Bend");
        return false;
    }

    private static boolean handleUnknownChannelMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("Unknown Channel Message");
        return false;
    }

    private static boolean handleSystemMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
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
        };

        return false;
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

    private static boolean handleUnknownMidiMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        System.out.println("Unknown midi message");

        return false;
    }
}

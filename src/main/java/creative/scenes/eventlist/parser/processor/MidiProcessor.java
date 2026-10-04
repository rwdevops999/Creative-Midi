package creative.scenes.eventlist.parser.processor;

import creative.scenes.eventlist.parser.data.EventType;
import creative.scenes.eventlist.parser.entity.MidiEventInfo;
import creative.scenes.eventlist.parser.processor.matcher.MidiNoteMatcher;
import creative.scenes.sysex.convertor.SysexToHexStringConvertor;
import creative.scenes.voice.provider.InstrumentProvider;
import entity.voice.Patch;
import javafx.scene.paint.Color;
import util.ApplicationInfo;
import util.ColorScheme;
import util.properties.PropertyContainer;
import util.properties.PropertyType;

import javax.sound.midi.ShortMessage;
import java.util.HashMap;
import java.util.Map;

import static util.ColorScheme.getColor;
import static util.Util.calcNote;

public class MidiProcessor {
    private static final Map<ColorScheme.ColorKey, Color> colors = new HashMap<>();

    private static final int[] currentMSB = new int[16];
    private static final int[] currentLSB = new int[16];

    static {
        colors.put(new ColorScheme.ColorKey("midi"), Color.BLUE);

        colors.put(new ColorScheme.ColorKey("midi", "note"), Color.WHITE);

        colors.put(new ColorScheme.ColorKey("midi", "bank select msb"), Color.GOLD);
        colors.put(new ColorScheme.ColorKey("midi", "bank select lsb"), Color.GOLD);
        colors.put(new ColorScheme.ColorKey("midi", "program change"), Color.GOLD);
        colors.put(new ColorScheme.ColorKey("midi", "pitch bend"), Color.MEDIUMSPRINGGREEN);

        ColorScheme.registerColors(colors);

        // Default MIDI bank is usually 0/0
        java.util.Arrays.fill(currentMSB, 0);
        java.util.Arrays.fill(currentLSB, 0);
    }

    private static int getMSB(int channel) { return currentMSB[channel]; }
    private static int getLSB(int channel) { return currentLSB[channel]; }

    public static boolean processMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result;

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
        boolean result;

        byte[] rawBytes = message.getMessage();

        int command = rawBytes[0] & 0xF0;

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

    private static final MidiNoteMatcher midiNoteMatcher = new MidiNoteMatcher();
    private static boolean handleNoteOn(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result;

        midiEventInfo.setColor(getColor("midi", "note"));
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
        boolean result;

        midiEventInfo.setColor(getColor("midi", "note"));
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
        boolean result;

        byte[] rawbytes = message.getMessage();

        int byte1 = rawbytes[1] & 0xFF;

        result = switch (byte1) {
            case
                    0x00,
                    0x01,
                    0x02,
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

    private static boolean handleControlChangeMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result;

        byte[] rawbytes = message.getMessage();

        int byte1 = rawbytes[1] & 0xFF;

        result = switch (byte1) {
            case 0x00 -> handleBankSelectMSB(message, midiEventInfo);
            case 0x01 -> handleModulation(message, midiEventInfo);
            case 0x02 -> handleBreath(message, midiEventInfo);
            case 0x05 -> handlePortamentoTime(message, midiEventInfo);
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
            case 0x4C -> handleVibratoRate(message, midiEventInfo);
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
        };

        return result;
    }

    private static boolean handleBankSelectMSB(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "bank select msb"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Bank select MSB");

        byte[] rawbytes = message.getMessage();

        int data2 = rawbytes[2] & 0xFF;
        midiEventInfo.setData2(data2);

        currentMSB[message.getChannel()] = data2;

        return result;
    }

    private static boolean handleModulation(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "modulation"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Modulation");

        byte[] rawbytes = message.getMessage();

        int data2 = rawbytes[2] & 0xFF;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleBreath(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "breath"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Breath");

        byte[] rawbytes = message.getMessage();

        int data2 = rawbytes[2] & 0xFF;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handlePortamentoTime(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "portamento time"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Portamento time");

        byte[] rawbytes = message.getMessage();

        int data2 = rawbytes[2] & 0xFF;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleDataEntryMSB(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "data entry msb"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Data entry MSB");

        byte[] rawbytes = message.getMessage();

        int data2 = rawbytes[2] & 0xFF;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleMainVolume(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "main volume"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Main Volume");

        byte[] rawbytes = message.getMessage();

        int data2 = rawbytes[2] & 0xFF;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handlePanpot(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "panpot"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Panpot");

        byte[] rawbytes = message.getMessage();

        int data2 = (rawbytes[2] & 0xFF) - 63;

        String display;
        if (data2 == 0) {
            display = "C(0)";
        } else if (data2 < 0) {
            display = "L("+Math.abs(data2) + ")";
        } else {
            display = "R("+data2 + ")";
        }
        midiEventInfo.setComment(display);

        return result;
    }

    private static boolean handleExpression (ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "expression"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Expression");

        byte[] rawbytes = message.getMessage();

        int data2 = rawbytes[2] & 0xFF;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleGeneralPurposeController(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "general purpose controller"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("General purpose controller");

        byte[] rawbytes = message.getMessage();

        int data2 = rawbytes[2] & 0xFF;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleBankSelectLSB(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "bank select lsb"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Bank Select LSB");

        byte[] rawbytes = message.getMessage();

        int data2 = rawbytes[2] & 0xFF;
        midiEventInfo.setData2(data2);

        currentLSB[message.getChannel()] = data2;

        return result;
    }

    private static boolean handleDataEntryLSB(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "data entry lsb"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Data entry LSB");

        byte[] rawbytes = message.getMessage();

        int data2 = rawbytes[2] & 0xFF;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleSustainDamper(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "sustain damper"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Sustain damper");

        byte[] rawbytes = message.getMessage();

        int data2 = rawbytes[2] & 0xFF;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handlePortamento(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "portamento"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Portamento");

        byte[] rawbytes = message.getMessage();

        int data2 = rawbytes[2] & 0xFF;
        midiEventInfo.setComment(data2 < 64 ? "OFF" : "ON");

        return result;
    }

    private static boolean handleVibratoRate(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "vibrato rate"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Vibrato rate");

        byte[] rawbytes = message.getMessage();

        int data2 = (rawbytes[2] & 0xFF) - 64;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleArticulation1(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "articulation1"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Articulation 1");

        byte[] rawbytes = message.getMessage();
        int data2 = rawbytes[2];

        midiEventInfo.setComment(data2 == 0 ? "OFF" : data2 == 127 ? "ON" : "UNKNOWN");

        return result;
    }

    private static boolean handleArticulation2(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "articulation2"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Articulation 2");

        byte[] rawbytes = message.getMessage();
        int data2 = rawbytes[2];

        midiEventInfo.setComment(data2 == 0 ? "OFF" : data2 == 127 ? "ON" : "UNKNOWN");

        return result;
    }

    private static boolean handleSostenuto(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "sostenuto"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Sostenuto");

        byte[] rawbytes = message.getMessage();
        int byte1 = rawbytes[2];

        midiEventInfo.setComment(byte1 < 64 ? "OFF" : "ON");

        return result;
    }

    private static boolean handleSoftPedal(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "soft pedal"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Soft pedal");

        byte[] rawbytes = message.getMessage();
        int byte1 = rawbytes[2];

        midiEventInfo.setComment(byte1 < 64 ? "OFF" : "ON");

        return result;
    }

    private static boolean handleResonance(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "resonance"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Resonance");

        byte[] rawbytes = message.getMessage();

        int data2 = (rawbytes[2] & 0xFF) - 64;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleReleaseTime(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "release time"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Release time");

        byte[] rawbytes = message.getMessage();

        int data2 = (rawbytes[2] & 0xFF) - 64;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleAttackTime(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "attack time"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Attack time");

        byte[] rawbytes = message.getMessage();

        int data2 = (rawbytes[2] & 0xFF) - 64;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleCutoff(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "cutoff"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Cutoff");

        byte[] rawbytes = message.getMessage();

        int data2 = (rawbytes[2] & 0xFF) - 64;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleDecayTime(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "decay time"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Decay time");

        byte[] rawbytes = message.getMessage();
        int data2 = (rawbytes[2] & 0xFF) - 64;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleVibratoDepth(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "vibrato depth"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("vibrato depth");

        byte[] rawbytes = message.getMessage();
        int data2 = (rawbytes[2] & 0xFF) - 64;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleVibratoDelay(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "vibrato delay"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Vibrato delay");

        byte[] rawbytes = message.getMessage();
        int data2 = (rawbytes[2] & 0xFF) - 64;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleChorusSendLevel(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "chorus send level"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Chorus send level");

        byte[] rawbytes = message.getMessage();

        int data2 = rawbytes[2] & 0xFF;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleRPNIncrement(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "rpn increment"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("RPN increment");

        return result;
    }

    private static boolean handleArticulation3(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "articulation3"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Articulation 3");

        byte[] rawbytes = message.getMessage();
        int data2 = rawbytes[2];
        midiEventInfo.setComment(data2 == 0 ? "OFF" : data2 == 127 ? "ON" : "UNKNOWN");

        return result;
    }

    private static boolean handlePortamentoControl(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "portamento control"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Portamento control");

        byte[] rawbytes = message.getMessage();

        int data2 = rawbytes[2] & 0xFF;

        int baseOctave = PropertyContainer.getPropertyAsInteger(PropertyType.Keyboard, PropertyContainer.BASE_OCTAVE, 0);
        midiEventInfo.setComment(calcNote(data2, baseOctave).getName());

        return result;
    }

    private static boolean handleReverbSendLevel(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "reverb send level"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Reverb send level");

        byte[] rawbytes = message.getMessage();

        int data2 = rawbytes[2] & 0xFF;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleVariationSendLevel(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "variation send level"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("Variation send level");

        byte[] rawbytes = message.getMessage();

        int data2 = rawbytes[2] & 0xFF;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleRPNDecrement(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "rpn decrement"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("RPN decrement");

        return result;
    }

    private static boolean handleNRPNLsb(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "nrpn lsb"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("NRPN LSB");

        byte[] rawbytes = message.getMessage();

        int data2 = rawbytes[2] & 0xFF;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleNRPNMsb(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "nrpn msb"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("NRPN MSB");

        byte[] rawbytes = message.getMessage();

        int data2 = rawbytes[2] & 0xFF;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleRPNLsb(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "rpn lsb"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("RPN LSB");

        byte[] rawbytes = message.getMessage();

        int data2 = rawbytes[2] & 0xFF;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleRPNMsb(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "rpn msb"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("RPN MSB");

        byte[] rawbytes = message.getMessage();

        int data2 = rawbytes[2] & 0xFF;
        midiEventInfo.setData2(data2);

        return result;
    }

    private static boolean handleUnknownControlChangeMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "unknown"));
        midiEventInfo.setChannel(message.getChannel()+1);
        midiEventInfo.setDescription("UNKNOWN (1)");

        return result;
    }

    private static boolean handleModeChangeMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result;

        byte[] rawbytes = message.getMessage();

        int byte1 = rawbytes[1] & 0xFF;

        result = switch (byte1) {
            case 0x78 -> handleAllSoundOff(message, midiEventInfo);
            case 0x79 -> handleResetAllControllers(message, midiEventInfo);
            case 0x7A -> handleLocalControl(message, midiEventInfo);
            case 0x7B -> handleAllNoteOff(message, midiEventInfo);
            case 0x7C -> handleOmniOff(message, midiEventInfo);
            case 0x7D -> handleOmniOn(message, midiEventInfo);
            case 0x7E -> handleMono(message, midiEventInfo);
            case 0x7F -> handlePoly(message, midiEventInfo);
            default -> handleUnknownModeChangeMessage(message, midiEventInfo);
        };

        return result;
    }

    private static boolean handleAllSoundOff(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "all sound off"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("All sound off");

        return result;
    }

    private static boolean handleLocalControl(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "local control"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("Local control");

        return result;
    }

    private static boolean handleOmniOff(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "omni off"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("Omni off");

        return result;
    }

    private static boolean handleOmniOn(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "omni on"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("Omni on");

        return result;
    }

    private static boolean handleMono(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "mono"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("Mono");

        return result;
    }

    private static boolean handlePoly(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "poly"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("Poly");

        return result;
    }

    private static boolean handleAllNoteOff(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "all note off"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("All note off");

        return result;
    }

    private static boolean handleResetAllControllers(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "reset all controllers"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("Reset all controllers");

        return result;
    }

    private static boolean handleUnknownModeChangeMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "unknown"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("UNKNOWN (2)");

        return result;
    }

    private static boolean handleUnknownControlOrModeMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "unknown"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("UNKNOWN (3)");

        return result;
    }

    private static boolean handleProgramChange(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "program change"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("Program change");

        byte[] rawbytes = message.getMessage();
        int byte1 = rawbytes[1] & 0xFF;

        midiEventInfo.setData2(byte1);

        int msb = getMSB(message.getChannel());
        int lsb = getLSB(message.getChannel());
        String voice = searchVoice(msb, lsb, byte1);

        midiEventInfo.setComment(voice);

        return result;
    }

    private static boolean handleChannelAfterTouch(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "channel after touch"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("Channel aftertouch");

        byte[] rawbytes = message.getMessage();
        int byte1 = rawbytes[1] & 0xFF;
        midiEventInfo.setData2(byte1);

        return result;
    }

    private static boolean handlePolyAfterTouch(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "polyphonic after touch"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("Key aftertouch");

        byte[] rawbytes = message.getMessage();
        int byte1 = rawbytes[1] & 0xFF;

        int baseOctave = PropertyContainer.getPropertyAsInteger(PropertyType.Keyboard, PropertyContainer.BASE_OCTAVE, 0);
        midiEventInfo.setComment(calcNote(byte1, baseOctave).getName());

        return result;
    }

    private static boolean handlePitchBend(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "pitch bend"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("Pitch bend");

        byte[] rawbytes = message.getMessage();
        int lsb = rawbytes[1] & 0xFF;
        int msb = rawbytes[2] & 0xFF;

        midiEventInfo.setData2((msb << 7) | lsb);

        return result;
    }

    private static boolean handleUnknownChannelMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "unknown"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("UNKNOWN (4)");

        return result;
    }

    private static boolean handleSystemMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result;

        byte[] rawBytes = message.getMessage();

        int command = rawBytes[0] & 0xFF;

        result = switch (command) {
            case 0xF8 -> handleMIDIClock(message, midiEventInfo);
            case 0xFA -> handleStart(message, midiEventInfo);
            case 0xFB -> handleContinue(message, midiEventInfo);
            case 0xFC -> handleStop(message, midiEventInfo);
            case 0xFE -> handleActiveSense(message, midiEventInfo);
            case 0xFF -> handleSystemReset(message, midiEventInfo);
            default -> handleUnknownSystemMessage(message, midiEventInfo);
        };

        return result;
    }

    private static boolean handleMIDIClock(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "clock"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("clock");

        return result;
    }

    private static boolean handleStart(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "start"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("Start");

        return result;
    }

    private static boolean handleContinue(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "continue"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("Continue");

        return result;
    }

    private static boolean handleStop(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "stop"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("Stop");

        return result;
    }

    private static boolean handleActiveSense(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "active sense"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("Active sense");

        return result;
    }

    private static boolean handleSystemReset(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "system reset"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("System reset");

        return result;
    }

    private static boolean handleUnknownSystemMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "unknown"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("UNKNOWN (5)");

        return result;
    }

    private static boolean handleUnknownMidiMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("midi", "unknown"));
        midiEventInfo.setChannel(message.getChannel() + 1);
        midiEventInfo.setDescription("UNKNOWN (6)");

        return result;
    }

    private static String searchVoice(int msb, int lsb, int pc) {
        String voice = "Unknown voice";

        InstrumentProvider instrumentProvider = ApplicationInfo.getInstance().getInstrumentProvider();

        if (instrumentProvider != null) {
            Patch patch = instrumentProvider.findPatch(msb, lsb, pc);
            if (patch != null) {
                voice = patch.getPatch();
            }
        }

        return voice;
    }
}

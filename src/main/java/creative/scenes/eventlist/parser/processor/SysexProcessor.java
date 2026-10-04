package creative.scenes.eventlist.parser.processor;

import creative.scenes.eventlist.data.EventKey;
import creative.scenes.eventlist.parser.entity.MidiEventInfo;
import creative.scenes.eventlist.parser.processor.helper.ByteHelper;
import creative.scenes.sysex.convertor.SysexToHexStringConvertor;
import entity.midi.NoteEntity;
import javafx.scene.paint.Color;
import util.ColorScheme;
import util.Util;
import util.properties.PropertyContainer;
import util.properties.PropertyType;

import javax.sound.midi.SysexMessage;
import java.util.HashMap;
import java.util.Map;

import static util.ColorScheme.getColor;

public class SysexProcessor {
    private static final Map<ColorScheme.ColorKey, Color> colors = new HashMap<>();

    static {
        colors.put(new ColorScheme.ColorKey("sysex"), Color.IVORY);

        ColorScheme.registerColors(colors);
    }

    //
    public static boolean processMessage(SysexMessage message, MidiEventInfo midiEventInfo) {
        boolean result;

        midiEventInfo.setEventKey(new EventKey(EventKey.KEY_SYSEX));
        midiEventInfo.setColor(getColor("sysex"));
        StringBuilder sysex = SysexToHexStringConvertor.convertToHexStringbuilder(message.getMessage());
        midiEventInfo.setMessage(sysex.toString());

        String selector = ByteHelper.getAndStrip(sysex, 1);

        result = switch(selector) {
            case "F0" -> handleUniversalMessages(sysex, midiEventInfo);
            default -> handleUnknownSysex(selector, midiEventInfo);
        };

        return result;
    }

    // F0
    private static boolean handleUniversalMessages(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = false;

        String selector = ByteHelper.getAndStrip(sysex, 1);

        result = switch(selector) {
            case "7F" -> handleUniversalRealTimeMessage(sysex, midiEventInfo);
            case "7E" -> handleUniversalNonRealTimeMessage(sysex, midiEventInfo);
            case "43" -> handleYamahaMessage(sysex, midiEventInfo);
            default -> handleUnknownUniversalMessage(selector, midiEventInfo);
        };

        return result;
    }

    // F0 43
    private static boolean handleYamahaMessage(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = false;

        midiEventInfo.setColor(getColor("sysex", "vendor"));

        String selector = ByteHelper.getAndStrip(sysex, 1);

        result = switch (selector) {
            case "00" -> handleXGBulkDump(sysex, midiEventInfo);
            case "10" -> handleXGParameterChange(sysex, midiEventInfo);
            case "20" -> handleXGDumpRequest(sysex, midiEventInfo);
            case "30" -> handleXGParameterRequest(sysex, midiEventInfo);
            case "7E" -> handleStyle(sysex, midiEventInfo);
            default -> handleUnknownYamahaMessage(sysex, midiEventInfo);
        };
        return  result;
    }

    // F0 43 ??
    private static boolean handleUnknownYamahaMessage(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("UNKNOWN VENDOR MESSAGE");
        midiEventInfo.setComment(sysex.toString());

        return result;
    }

    // FO 7F --
    private static boolean handleUniversalRealTimeMessage(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "universal real time"));

        ByteHelper.getAndStrip(sysex, 1);
        String selector = ByteHelper.getAndStrip(sysex, 1);

        result = switch(selector) {
            case "04" -> handleMasterMessage(sysex, midiEventInfo);
            case "09" -> handleChannelMessage(sysex, midiEventInfo);
            case "0A" -> handleInstrumentControl(sysex, midiEventInfo);
            default -> handleUnknownUniversalRealTimeMessage(sysex, midiEventInfo);
        };

        return result;
    }

    // F0 7F -- 04
    private static boolean handleMasterMessage(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = false;

        String selector = ByteHelper.getAndStrip(sysex, 1);

        result = switch (selector) {
            case "01" -> handleMasterVolume(sysex, midiEventInfo);
            case "03" -> handleMasterFineTuning(sysex, midiEventInfo);
            case "04" -> handleMasterCoarseTuning(sysex, midiEventInfo);
            case "05" -> handleEffectParameter(sysex, midiEventInfo);
            default -> handleUnknownMasterMessage(sysex, midiEventInfo);
        };

        return result;
    }

    // F0 7F -- 04 01
    private static boolean handleMasterVolume(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = false;

        midiEventInfo.setColor(getColor("sysex", "master volume"));
        midiEventInfo.setDescription("Master volume");

        String szLSB = ByteHelper.getAndStrip(sysex, 1);
        String szMSB = ByteHelper.getAndStrip(sysex, 1);

        int lsb = Integer.parseInt(szLSB, 16);
        int msb = Integer.parseInt(szMSB, 16);

        midiEventInfo.setData2((msb << 7) | lsb);

        return result;
    }

    // F0 7F -- 04 03
    private static boolean handleMasterFineTuning(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = false;

        midiEventInfo.setColor(getColor("sysex", "master fine tuning"));
        midiEventInfo.setDescription("Master fine tuning");

        String szLSB = ByteHelper.getAndStrip(sysex, 1);
        String szMSB = ByteHelper.getAndStrip(sysex, 1);

        int lsb = Integer.parseInt(szLSB, 16);
        int msb = Integer.parseInt(szMSB, 16);

        midiEventInfo.setData2((msb << 7) | lsb);

        return result;
    }

    // F0 7F -- 04 04
    private static boolean handleMasterCoarseTuning(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = false;

        midiEventInfo.setColor(getColor("sysex", "master coarse tuning"));
        midiEventInfo.setDescription("Master coarse tuning");

        ByteHelper.getAndStrip(sysex, 1);
        String szMSB = ByteHelper.getAndStrip(sysex, 1);

        int msb = Integer.parseInt(szMSB, 16);

        midiEventInfo.setData2(msb << 7);

        return result;
    }

    // F0 7F -- 04 05 -- -- -- --
    private static boolean handleEffectParameter(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = false;

        midiEventInfo.setColor(getColor("sysex", "parameter"));
        midiEventInfo.setDescription("Effect parameter");

        ByteHelper.getAndStrip(sysex, 4);
        String selector = ByteHelper.getAndStrip(sysex, 1);

        result = switch(selector) {
            case "01" -> handleReverbParameter(sysex, midiEventInfo);
            case "02" -> handleChorusParameter(sysex, midiEventInfo);
            default -> handleUnknownEffectParameter(sysex, midiEventInfo);
        };

        return result;
    }

    // F0 7F -- 04 05 -- -- -- -- 01
    private static boolean handleReverbParameter(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = false;

        midiEventInfo.setColor(getColor("sysex", "parameter"));
        midiEventInfo.setDescription("Reverb parameter");

        String pp = ByteHelper.getAndStrip(sysex, 1);
        String vv = ByteHelper.getAndStrip(sysex, 1);

        if (pp.equals("00")) {
            String type = switch(vv) {
                case "00" -> "RoomS";
                case "01" -> "RoomM";
                case "02" -> "RoomL";
                case "03" -> "HallM";
                case "04" -> "HallL";
                case "08" -> "GMPlate";
                default -> "Invalid";
            };
            midiEventInfo.setComment("Reverb type: " + type);

        } else if (pp.equals("01")) {
            midiEventInfo.setComment("Reverb time: " + Integer.parseInt(vv, 16));
        }

        return result;
    }

    // F0 7F -- 04 05 -- -- -- -- 02
    private static boolean handleChorusParameter(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = false;

        midiEventInfo.setColor(getColor("sysex", "parameter"));
        midiEventInfo.setDescription("Chorus parameter");

        String pp = ByteHelper.getAndStrip(sysex, 1);
        String vv = ByteHelper.getAndStrip(sysex, 1);

        if (pp.equals("00")) {
            String type = switch(vv) {
                case "00" -> "GM Chorus 1";
                case "01" -> "GM Chorus 2";
                case "02" -> "GM Chorus 3";
                case "03" -> "GM Chorus 4";
                case "04" -> "FB Chorus";
                case "05" -> "GM Flanger";
                default -> "Invalid";
            };
            midiEventInfo.setComment("Reverb type: " + type);

        } else if (pp.equals("01")) {
            midiEventInfo.setComment("Mod rate: " + Integer.parseInt(vv, 16));
        } else if (pp.equals("02")) {
            midiEventInfo.setComment("Mod depth: " + Integer.parseInt(vv, 16));
        } else if (pp.equals("03")) {
            midiEventInfo.setComment("Feedback: " + Integer.parseInt(vv, 16));
        } else if (pp.equals("04")) {
            midiEventInfo.setComment("Send to reverb: " + Integer.parseInt(vv, 16));
        } else {
            midiEventInfo.setComment("Invalid: " + Integer.parseInt(vv, 16));
        }

        return result;
    }

    // F0 7F -- 04 05 -- -- -- -- ??
    private static boolean handleUnknownEffectParameter(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = false;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("UNKNOWN effect parameter");

        return result;
    }

    // F0 7F -- 04 ??
    private static boolean handleUnknownMasterMessage(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = false;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("Unknown master message");

        return result;
    }

    // F0 7F -- 09
    private static boolean handleChannelMessage(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = false;

        midiEventInfo.setColor(getColor("sysex", "channel message"));
        midiEventInfo.setDescription("Channel message");

        String selector = ByteHelper.getAndStrip(sysex, 1);

        result = switch(selector) {
            case "01" -> handleChannelPressure(sysex, midiEventInfo);
            case "03" -> handleControlChange(sysex, midiEventInfo);
            default -> handleUnknowChannelMessage(sysex, midiEventInfo);
        };

        return result;
    }

    // F0 7F -- 01
    private static boolean handleChannelPressure(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = false;

        midiEventInfo.setDescription("Aftertouch");

        int channel = Integer.parseInt(ByteHelper.getAndStrip(sysex, 1), 16);
        String pp = ByteHelper.getAndStrip(sysex, 1);
        int rr = Integer.parseInt(ByteHelper.getAndStrip(sysex, 1), 16);

        String parameter = switch (pp) {
            case "00" -> "Pitch control";
            case "01" -> "Filter cutoff";
            case "02" -> "Amplitude";
            case "03" -> "Pitch depth";
            case "04" -> "Filter depth";
            case "05" -> "Amplitude depth";
            default -> "Invalid";
        };

        midiEventInfo.setComment("Channel: " + channel + ", " + parameter);
        midiEventInfo.setData2(rr);

        return result;
    }

    // F0 7F -- 03
    private static boolean handleControlChange(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = false;

        midiEventInfo.setDescription("control Change");

        int channel = Integer.parseInt(ByteHelper.getAndStrip(sysex, 1), 16);
        String pp = ByteHelper.getAndStrip(sysex, 1);
        int rr = Integer.parseInt(ByteHelper.getAndStrip(sysex, 1), 16);

        String parameter = switch (pp) {
            case "00" -> "Pitch control";
            case "01" -> "Filter cutoff";
            case "02" -> "Amplitude";
            case "03" -> "Pitch depth";
            case "04" -> "Filter depth";
            case "05" -> "Amplitude depth";
            default -> "Invalid";
        };

        midiEventInfo.setComment("Channel: " + channel + ", " + parameter);
        midiEventInfo.setData2(rr);

        return result;
    }

    // F0 7F -- ??
    private static boolean handleUnknowChannelMessage(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = false;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("Unknown channel message");

        return result;
    }
    // F0 7F -- 0A
    private static boolean handleInstrumentControl(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = false;

        ByteHelper.getAndStrip(sysex, 1);

        int channel = Integer.parseInt(ByteHelper.getAndStrip(sysex, 1), 16);
        int baseOctave = PropertyContainer.getPropertyAsInteger(PropertyType.Keyboard, PropertyContainer.BASE_OCTAVE, 0);
        NoteEntity note = Util.calcNote(Integer.parseInt(ByteHelper.getAndStrip(sysex, 1), 16), baseOctave);

        String cc = ByteHelper.getAndStrip(sysex, 1);
        int vv = Integer.parseInt(ByteHelper.getAndStrip(sysex, 1), 16);

        String control = switch(cc) {
            case "07" -> "Volume";
            case "0A" -> "Pan";
            case "5B" -> "Reverb send level";
            case "5D" -> "Chorus send level";
            default -> "Invalid";
        };

        midiEventInfo.setComment("channel: " + channel + ", note: " + note.getName() + ", " + control);
        midiEventInfo.setData2(vv);

        return result;
    }

    // F0 7F -- ??
    private static boolean handleUnknownUniversalRealTimeMessage(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = false;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("UNKNOWN Universal real time");

        return result;
    }

    // F0 7E
    private static boolean handleUniversalNonRealTimeMessage(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        // Skip 1 byte
        String check = ByteHelper.getAndStrip(sysex, 1);

        String selector = ByteHelper.getAndStrip(sysex, 2);

        result = switch (selector) {
            case "08 08" -> handleScaleOctaveTuning(midiEventInfo);
            case "09 01" -> handleGM1SystemOn(midiEventInfo);
            case "09 02" -> handleGeneralMidiSystemOff(midiEventInfo);
            case "09 03" -> handleGM2SystemOn(midiEventInfo);
            default -> handleUnknownUniversaleNonRealTimeMessage(midiEventInfo);
        };

        return result;
    }

    // FO 7E 08 08
    private static boolean handleScaleOctaveTuning(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "universal", "tuning"));
        midiEventInfo.setDescription("Scale/Octave tuning");

        return result;
    }

    // F0 7E 09 01
    private static boolean handleGM1SystemOn(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "universal", "gm1 system on"));
        midiEventInfo.setDescription("GM1 system on");

        return result;
    }

    // F0 7E 09 02
    private static boolean handleGeneralMidiSystemOff(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "universal", "general midi off"));
        midiEventInfo.setDescription("General MIDI system off");

        return result;
    }

    // F0 7E 09 03
    private static boolean handleGM2SystemOn(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "universal", "gm2 system on"));
        midiEventInfo.setDescription("GM2 system on");

        return result;
    }

    private static boolean handleUnknownUniversaleNonRealTimeMessage(MidiEventInfo midiEventInfo) {
        boolean result = true;

        return result;
    }

    // F0 43 00
    private static boolean handleXGBulkDump(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "vendor", "xg bulk dump"));
        midiEventInfo.setDescription("XG bulk dump");

        return result;
    }

    // F0 43 10
    private static boolean handleXGParameterChange(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "vendor", "xg parameter change"));

        // Skip 1 byte
        ByteHelper.getAndStrip(sysex, 1);

        String address = ByteHelper.getAndStrip(sysex, 2);

        result = switch (address) {
            case "00 00" -> handleXGParameterChange0000(sysex, midiEventInfo);
            case "02 01" -> handleEffect1(sysex, midiEventInfo);
            case
                    "03 00",
                    "03 02" -> handleEffect2(sysex, midiEventInfo);
            case
                "04 00" -> handleVocalHarmony(sysex, midiEventInfo);
            case "0A 00",
                 "0A 02" -> handleXGMultiPart(address, midiEventInfo);
            case
                    "08 00",
                    "08 01",
                    "08 02",
                    "08 03",
                    "08 04",
                    "08 05",
                    "08 06",
                    "08 07",
                    "08 08",
                    "08 09",
                    "08 0A",
                    "08 0B",
                    "08 0C",
                    "08 0D",
                    "08 0E" -> handleMultiPartMessage(address, midiEventInfo);
            case "30 23",
                 "30 2A",
                 "30 2C",
                 "30 2E" -> handleDrumSetup(address, midiEventInfo);
            default -> handleUnknownXGParameterChange(midiEventInfo);
        };

        return result;
    }

    // F0 43 10 -- 02 01
    private static boolean handleEffect1(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        String selector = ByteHelper.getAndStrip(sysex, 1);
        result = switch (selector) {
            case "00" -> handleReverb(midiEventInfo);
            case "20" -> handleChorus(midiEventInfo);
            case "40", "5A" -> handleVariation(midiEventInfo);
            default -> handleUnknownEffect1(midiEventInfo);
        };

        return result;
    }

    // F0 43 10 -- 03 nn
    private static boolean handleEffect2(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        String selector = ByteHelper.getAndStrip(sysex, 1);
        result = switch (selector) {
            case "00", "02", "03", "0C", "0B" -> handleInsertion(midiEventInfo);
            default -> handleUnknownEffect2(midiEventInfo);
        };

        return result;
    }

    // F0 43 10 -- 04 nn
    private static boolean handleVocalHarmony(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "vocal harmony"));
        midiEventInfo.setDescription("Vocal Harmony");

        return result;
    }

    // F0 43 10 -- 03 nn 00
    private static boolean handleInsertion(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "effect", "insertion"));
        midiEventInfo.setDescription("Insertion effect");

        return result;
    }

    // F0 43 10 -- 02 01 00
    private static boolean handleReverb(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "effect", "reverb"));
        midiEventInfo.setDescription("Reverb effect");

        return result;
    }

    // F0 43 10 -- 02 01 20
    private static boolean handleChorus(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "effect", "chorus"));
        midiEventInfo.setDescription("Chorus effect");

        return result;
    }

    // F0 43 10 -- 02 01 40
    private static boolean handleVariation(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "effect", "variation"));
        midiEventInfo.setDescription("Variation effect");

        return result;
    }

    // F0 43 10 -- 02 01 ??
    private static boolean handleUnknownEffect1(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("UNKNOWN effect1");

        return result;
    }

    // F0 43 10 -- 03 nn ??
    private static boolean handleUnknownEffect2(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("UNKNOWN effect2");

        return result;
    }

    // F0 43 10 -- 0A nn
    private static boolean handleXGMultiPart(String address, MidiEventInfo midiEventInfo) {
        boolean result = true;

        StringBuilder newAddress = new StringBuilder(address);

        ByteHelper.getAndStrip(newAddress, 1);

        String selector = ByteHelper.getAndStrip(newAddress, 1);

        midiEventInfo.setColor(getColor("sysex", "xg multipart"));
        midiEventInfo.setDescription("XG Multipart part mode");

        int receiveChannel = Integer.parseInt(selector, 16);
        midiEventInfo.setComment("receive channel: " + receiveChannel);

        return result;
    }

    // F0 43 10 -- 08 nn
    private static boolean handleMultiPartMessage(String address, MidiEventInfo midiEventInfo) {
        boolean result = true;

        StringBuilder newAddress = new StringBuilder(address);

        ByteHelper.getAndStrip(newAddress, 1);

        String selector = ByteHelper.getAndStrip(newAddress, 1);

        midiEventInfo.setColor(getColor("sysex","xg multipart"));
        midiEventInfo.setDescription("XG Multipart part mode");

        int receiveChannel = Integer.parseInt(selector, 16);
        midiEventInfo.setComment("receive channel: " + receiveChannel);

        return result;
    }

    // F0 43 10 -- 3n rr
    private static boolean handleDrumSetup(String address, MidiEventInfo midiEventInfo) {
        boolean result = true;

        StringBuilder newAddress = new StringBuilder(address);

        ByteHelper.getAndStrip(newAddress, 1);

        String selector = ByteHelper.getAndStrip(newAddress, 1);

        midiEventInfo.setColor(getColor("sysex", "drum setup"));
        midiEventInfo.setDescription("Drum setup");

        return result;
    }

    // F0 43 10 -- 00 00
    private static boolean handleXGParameterChange0000(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        String selector = ByteHelper.getAndStrip(sysex, 1);
        result = switch (selector) {
            case "7E" -> handleXGSystemOn(midiEventInfo);
            default -> handleUnknowXGParamemeterChange("0000", midiEventInfo);
        };

        return  result;
    }

    // F0 43 10 -- 00 00 ??
    private static boolean handleUnknowXGParamemeterChange(String address, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex","unknown"));
        midiEventInfo.setDescription("XG parameter change unknown");
        midiEventInfo.setComment(address + " UNKNOWN");

        return result;
    }

    // F0 43 10 -- 00 00 7E
    private static boolean handleXGSystemOn(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex","xg system on"));
        midiEventInfo.setDescription("XG system on");

        return result;
    }

    // F0 43 10 -- ?? ??
    private static boolean handleUnknownXGParameterChange(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex","unknown"));
        midiEventInfo.setDescription("XG parameter change");

        return result;
    }

    // F0 43 20
    private static boolean handleXGDumpRequest (StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "vendor", "XG dump request"));
        midiEventInfo.setDescription("XG dump request");

        return result;
    }

    // F0 43 30
    private static boolean handleXGParameterRequest (StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "vendor", "XG parameter request"));
        midiEventInfo.setDescription("XG parameter request");

        return result;
    }

    // F0 43 7E
    private static boolean handleStyle (StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        String selector = ByteHelper.getAndStrip(sysex, 1);
        result = switch (selector) {
            case "00" -> handleSectionControl(sysex, midiEventInfo);
            default -> handleUnknownStyle(sysex, midiEventInfo);
        };

        return result;
    }

    // F0 43 7E 00
    private static boolean handleSectionControl (StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "vendor", "section control"));
        midiEventInfo.setDescription("Section control");

        String section = ByteHelper.getAndStrip(sysex, 1);
        String onoff = ByteHelper.getAndStrip(sysex, 1);

        String sectionName = switch(section) {
            case "00" -> "INTRO 1";
            case "01" -> "INTRO 2";
            case "02" -> "INTRO 3";
            case "03" -> "INTRO 4";
            case "08" -> "MAIN A";
            case "09" -> "MAIN B";
            case "0A" -> "MAIN C";
            case "0B" -> "MAIN D";
            case "10" -> "FILL IN AA";
            case "11" -> "FILL IN BB";
            case "12" -> "FILL IN CC";
            case "13" -> "FILL IN DD";
            case "18" -> "BREAK FILL";
            case "20" -> "ENDING 1";
            case "21" -> "ENDING 2";
            case "22" -> "ENDING 3";
            case "23" -> "ENDING 4";
            default -> "INVALID";
        };

        String status = switch(onoff) {
            case "00" -> "OFF";
            case "7F" -> "ON";
            default -> "INVALID";
        };

        midiEventInfo.setComment(sectionName + " : " + status);

        return result;
    }

    // F0 43 7E ??
    private static boolean handleUnknownStyle (StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("UNKNOWN STYLE");

        return result;
    }

    private static boolean handleUnknownVendorMessage(String vendorId, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("UNKNOWN VENDOR");
        midiEventInfo.setComment(vendorId);

        return result;
    }

    // ??
    private static boolean handleUnknownSysex(String selector, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("UNKNOWN SYSEX");
        midiEventInfo.setComment(selector);

        return result;
    }

    private static boolean handleUnknownUniversalMessage(String sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("UNKNOWN UNIVERSAL MESSAGE");
        midiEventInfo.setComment(sysex);

        return result;
    }
}

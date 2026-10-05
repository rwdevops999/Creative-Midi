package creative.scenes.eventlist.parser.processor;

import creative.scenes.eventlist.parser.entity.MidiEventInfo;
import creative.scenes.eventlist.parser.processor.data.ChorusTypes;
import creative.scenes.eventlist.parser.processor.data.ReverbTypes;
import creative.scenes.eventlist.parser.processor.data.VariationTypes;
import creative.scenes.eventlist.parser.processor.helper.ByteHelper;
import creative.scenes.sysex.convertor.SysexToHexStringConvertor;
import javafx.scene.paint.Color;
import util.ColorScheme;

import javax.sound.midi.SysexMessage;
import java.util.HashMap;
import java.util.Map;

import static creative.scenes.eventlist.data.EventKeyValue.*;
import static util.ColorScheme.getColor;

public class SysexProcessor1 {
    private static final Map<ColorScheme.ColorKey, Color> colors = new HashMap<>();
    private static final Map<String, String> REGISTRY = new HashMap<>();

    static {
        colors.put(new ColorScheme.ColorKey("sysex"), Color.IVORY);

        ColorScheme.registerColors(colors);

        REGISTRY.put("01", "Sequential Circuits");
        REGISTRY.put("04", "Moog Music");
        REGISTRY.put("06", "Lexicon");
        REGISTRY.put("41", "Roland Corporation");
        REGISTRY.put("42", "Korg Inc.");
        REGISTRY.put("43", "Yamaha Corporation");
        REGISTRY.put("47", "Akai Professional");
    }

    //
    public static boolean processMessage(SysexMessage message, MidiEventInfo midiEventInfo) {
        boolean result = true;

        StringBuilder sysex = SysexToHexStringConvertor.convertToHexStringbuilder(message.getMessage());
        midiEventInfo.setMessage(sysex.toString());

        String selector = ByteHelper.getAndStrip(sysex, 1);

        result = switch (selector) {
            case "F0" -> handleSysexMessage(sysex, midiEventInfo);
            default -> handleUnknownSysexMessage(selector, midiEventInfo);
        };

        return result;
    }

    // FO ...
    private static boolean handleSysexMessage(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex"));
        midiEventInfo.setDescription("Sysex");

        String selector = ByteHelper.getAndStrip(sysex, 1);

        result = switch(selector) {
            case "7E" -> handleUniversalNonRealTimeMessage(sysex, midiEventInfo);
            case
                    "41",
                    "43" -> handleVendorMessage(selector, sysex, midiEventInfo);
            default -> handleUnknownSysexMessage("F0 " + selector, midiEventInfo);
        };

        return result;
    }

    // F0 7E ...
    private static boolean handleUniversalNonRealTimeMessage(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.getEventKey().addValue(SYSEX_UNIVERSAL_NON_REAL_TIME_MESSAGE);

        // skip the XN byte
        ByteHelper.getAndStrip(sysex, 1);

        String selector = ByteHelper.getAndStrip(sysex, 1);
        System.out.println("Handling Selector: " + selector);

        result = switch (selector) {
            case "09" -> handleGMMessage(sysex, midiEventInfo);
            default -> handleUnknownUniversalRealTimeMessage("FO 7E XN " + selector, midiEventInfo);
        };

        return result;
    }

    // F0 nn ...
    private static boolean handleVendorMessage(String vendorId, StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.getEventKey().addValue(SYSEX_VENDOR_MESSAGE);
        midiEventInfo.setDescription("Vendor message");
        midiEventInfo.setComment(vendorId);

        result = switch (vendorId) {
            case "43" -> handleYamahaMessage(sysex, midiEventInfo);
            default -> handleUnknowVendorMessage(vendorId, midiEventInfo);
        };

        return result;
    }

    //F0 43 ...
    private static boolean handleYamahaMessage(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setDescription("Yamaha message");
        midiEventInfo.setComment(null);

        String selector = ByteHelper.getAndStrip(sysex, 1);

        result = switch(selector) {
            case "10" -> handleXGParameterChange(sysex, midiEventInfo);
            default -> handleUnknownYamahaMessage("F0 43 " + selector, midiEventInfo);
        };

        return result;
    }

    // FO 43 10 ...
    private static boolean handleXGParameterChange(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.getEventKey().addValue(SYSEX_XG_PARAMETER_CHANGE_MESSAGE);
        midiEventInfo.setDescription("XG Parameter change");

        // skip byte 4C
        ByteHelper.getAndStrip(sysex, 1);

        String selector = ByteHelper.getAndStrip(sysex, 2);
        result = switch (selector) {
            case "00 00" -> handleSetupMessage(sysex, midiEventInfo);
            case "02 01" -> handleEffect1(sysex, midiEventInfo);
            case "03 00" -> handleEffect2(sysex, midiEventInfo);
            case
                    "08 00",
                    "08 02",
                    "08 03",
                    "08 09",
                    "08 0A",
                    "08 0B",
                    "08 0C",
                    "08 0D",
                    "08 0E" -> handleMultipart(sysex, midiEventInfo);
            default -> handleUnknownXGParameterChange("F0 43 10 -- " + selector, midiEventInfo);
        };

        return result;
    }

    // F0 43 10 -- 00 00
    private static boolean handleSetupMessage(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.getEventKey().addValue(SYSEX_XG_SETUP_MESSAGE);
        midiEventInfo.setDescription("XG setup");

        String selector = ByteHelper.getAndStrip(sysex, 1);

        result = switch (selector) {
            case "7E" -> handleXGSystemOn(sysex, midiEventInfo);
            default -> handleUnknownSetupMessage("F0 43 10 -- 00 00 " + selector, midiEventInfo);
        };

        return result;
    }

    // F0 43 10 -- 02 01
    private static boolean handleEffect1(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.getEventKey().addValue(SYSEX_XG_EFFECT1_MESSAGE);
        midiEventInfo.setDescription("XG effect 1");

        String selector = ByteHelper.getAndStrip(sysex, 1);
        result = switch(selector) {
            case "00" -> handleReverb(sysex, midiEventInfo);
            case
                    "02" ->handleReverbParameter(sysex, midiEventInfo);
            case "20" -> handleChorus(sysex, midiEventInfo);
            case "40" -> handleVariation(sysex, midiEventInfo);
            case
                    "5A" -> handleVariationParameterConnection(sysex, midiEventInfo);
            default -> handleUnknownEffect1("FO 43 10 --02 01 " + selector, midiEventInfo);
        };

        return result;
    }

    // F0 43 10 -- 03 00
    private static boolean handleEffect2(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.getEventKey().addValue(SYSEX_XG_EFFECT2_MESSAGE);
        midiEventInfo.setDescription("XG effect 2");

        String selector = ByteHelper.getAndStrip(sysex, 1);
        result = switch(selector) {
            case "00" -> handleInsertion(sysex, midiEventInfo);
            case
                    "0B",
                    "0C" -> handleInsertionParameter(selector, midiEventInfo);
            default -> handleUnknownEffect2("FO 43 10 -- 03 00 " + selector, midiEventInfo);
        };

        return result;
    }

    // F0 43 10 -- 02 01 00 ...
    private static boolean handleReverb(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.getEventKey().addValue(SYSEX_XG_REVERB);
        midiEventInfo.setDescription("XG reverb");

        int msb = Integer.parseInt(ByteHelper.getAndStrip(sysex,1), 16);
        int lsb = Integer.parseInt(ByteHelper.getAndStrip(sysex,1), 16);

        String reverbType = ReverbTypes.getReverbType(msb, lsb);
        midiEventInfo.setComment(reverbType);

        return result;
    }

    // F0 43 10 -- 03 00 00 ...
    private static boolean handleInsertion(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.getEventKey().addValue(SYSEX_XG_INSERTION);
        midiEventInfo.setDescription("XG insertion");

        int msb = Integer.parseInt(ByteHelper.getAndStrip(sysex,1), 16);
        int lsb = Integer.parseInt(ByteHelper.getAndStrip(sysex,1), 16);

        // We can use Variation type here because of the manual Insertion Block is the
        // same as Variation Block
        String insertionType = VariationTypes.getVariationType(msb, lsb);
        midiEventInfo.setComment(insertionType);

        return result;
    }

    // F0 43 10 -- 02 01 20 ...
    private static boolean handleChorus(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.getEventKey().addValue(SYSEX_XG_CHORUS);
        midiEventInfo.setDescription("XG chorus");

        int msb = Integer.parseInt(ByteHelper.getAndStrip(sysex,1), 16);
        int lsb = Integer.parseInt(ByteHelper.getAndStrip(sysex,1), 16);

        String chorusType = ChorusTypes.getChorusType(msb, lsb);
        midiEventInfo.setComment(chorusType);

        return result;
    }

    // F0 43 10 -- 02 01 40 ...
    private static boolean handleVariation(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.getEventKey().addValue(SYSEX_XG_VARIATION);
        midiEventInfo.setDescription("XG variation");

        int msb = Integer.parseInt(ByteHelper.getAndStrip(sysex,1), 16);
        int lsb = Integer.parseInt(ByteHelper.getAndStrip(sysex,1), 16);

        String variationType = VariationTypes.getVariationType(msb, lsb);
        midiEventInfo.setComment(variationType);

        return result;
    }

    // F0 43 10 -- 02 01 {02}  ...
    private static boolean handleReverbParameter(StringBuilder selector, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.getEventKey().addValue(SYSEX_XG_REVERB_PARAMETER);
        midiEventInfo.setDescription("XG insertion parameter");


        return result;
    }

    // F0 43 10 -- 03 00 {0C}  ...
    private static boolean handleInsertionParameter(String parameterId, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.getEventKey().addValue(SYSEX_XG_INSERTION_PARAMETER);
        midiEventInfo.setDescription("XG insertion parameter");

        result = switch (parameterId) {
            case "0B" -> {
                midiEventInfo.setComment(parameterId);
                yield true;
            }
            case "0C" -> {
                midiEventInfo.getEventKey().addValue(SYSEX_XG_INSERTION_EFFECT_PART);
                midiEventInfo.setComment("Insertion effect part");
                yield true;
                }

            default -> handleUnknownInsertionParameter("F0 43 10 -- 30 00 " + parameterId, midiEventInfo);
        };

        return result;
    }

    // F0 43 10 -- 02 01 {5A}  ...
    private static boolean handleVariationParameterConnection(StringBuilder selector, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.getEventKey().addValue(SYSEX_XG_VARIATION_PARAMETER_CONNECTION);
        midiEventInfo.setDescription("XG variation connection");

        String address = ByteHelper.getAndStrip(selector, 1);
        midiEventInfo.setComment("Insertion system");

        return result;
    }

    // F0 43 10 -- 08 00
    private static boolean handleMultipart(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.getEventKey().addValue(SYSEX_MULTIPART);
        midiEventInfo.setDescription("XG multipart");
        String selector = ByteHelper.getAndStrip(sysex, 1);
        midiEventInfo.setComment(selector);


        return result;
    }

    // F0 43 10 -- 00 00 7E
    private static boolean handleXGSystemOn(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.getEventKey().addValue(SYSEX_XG_SYSTEM_ON);
        midiEventInfo.setDescription("XG system on");

        String selector = ByteHelper.getAndStrip(sysex, 1);

        if ("00".equals(selector)) {
            midiEventInfo.setComment("ON");
        } else {
            midiEventInfo.setComment("INVALID");
        }

        return result;
    }

    // F0 7E XN 09 ...
    private static boolean handleGMMessage(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.getEventKey().addValue(SYSEX_GM_MESSAGE);

        String selector = ByteHelper.getAndStrip(sysex, 1);
        System.out.println("Handling Selector: " + selector);

        result = switch (selector) {
            case "01" -> handleGM1SystemOn(midiEventInfo);
            case "02" -> handleGeneralMidSystemOff(midiEventInfo);
            default -> handleUnknownGMMessage("F7 F0 SN 09 " + selector, midiEventInfo);
        };

        return result;
    }

    //  F0 7E XN 09 01 ...
    private static boolean handleGM1SystemOn(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.getEventKey().addValue(SYSEX_GM1_SYSTEM_ON);
        midiEventInfo.setDescription("GM1 system on");

        return result;
    }

    //  F0 7E XN 09 02 ...
    private static boolean handleGeneralMidSystemOff(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.getEventKey().addValue(SYSEX_GENERAL_MIDI_OFF);
        midiEventInfo.setDescription("General MIDI off");

        return result;
    }

    // UNKNOWNS
    private static boolean handleUnknownSysexMessage(String sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("Unknown sysex");
        midiEventInfo.setComment(sysex);

        return result;
    }

    private static boolean handleUnknownUniversalRealTimeMessage(String sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("Unknown universal read time sysex");
        midiEventInfo.setComment(sysex);

        return result;
    }

    private static boolean handleUnknownGMMessage(String sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("Unknown GM message");
        midiEventInfo.setComment(sysex);

        return result;
    }

    private static boolean handleUnknowVendorMessage(String vendorId, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("Vendor not handled");

        String vendor = REGISTRY.get(vendorId);
        if (vendor == null) {
            vendor = "UNKNOWN";
        }
        midiEventInfo.setComment(vendor);

        return result;
    }

    private static boolean handleUnknownYamahaMessage(String sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("Unknown Yamaha message");
        midiEventInfo.setComment(sysex);

        return result;
    }

    private static boolean handleUnknownXGParameterChange(String sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("Unknown XG parameter change message");
        midiEventInfo.setComment(sysex);

        return result;
    }

    private static boolean handleUnknownSetupMessage(String sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("Unknown setup message");
        midiEventInfo.setComment(sysex);

        return result;
    }

    private static boolean handleUnknownEffect1(String sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("Unknown effect1 message");
        midiEventInfo.setComment(sysex);

        return result;
    }

    private static boolean handleUnknownEffect2(String sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("Unknown effect2 message");
        midiEventInfo.setComment(sysex);

        return result;
    }

    private static boolean handleUnknownInsertionParameter(String sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("Unknown insertion parameter");
        midiEventInfo.setComment(sysex);

        return result;
    }
}

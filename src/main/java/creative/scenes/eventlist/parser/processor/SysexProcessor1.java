package creative.scenes.eventlist.parser.processor;

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

import static creative.scenes.eventlist.data.EventKeyValue.*;
import static util.ColorScheme.getColor;

public class SysexProcessor1 {
    private static final Map<ColorScheme.ColorKey, Color> colors = new HashMap<>();

    static {
        colors.put(new ColorScheme.ColorKey("sysex"), Color.IVORY);

        ColorScheme.registerColors(colors);
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
            case "43" -> handleVendorMessage(selector, sysex, midiEventInfo);
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

    // F0 XN 09 ...
    private static boolean handleGMMessage(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.getEventKey().addValue(SYSEX_GM_MESSAGE);

        String selector = ByteHelper.getAndStrip(sysex, 1);
        System.out.println("Handling Selector: " + selector);

        result = switch (selector) {
            case "01" -> handleGM1SystemOn(midiEventInfo);
            default -> handleUnknownGMMessage("F7 F0 SN 09 " + selector, midiEventInfo);
        };

        return result;
    }

    // F0 XN 09 01
    private static boolean handleGM1SystemOn(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.getEventKey().addValue(SYSEX_GM1_SYSTEM_ON);
        midiEventInfo.setDescription("GM1 system on");

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
        midiEventInfo.setDescription("Unknown vendor");
        midiEventInfo.setComment(vendorId);

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
}

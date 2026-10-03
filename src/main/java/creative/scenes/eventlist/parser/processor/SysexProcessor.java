package creative.scenes.eventlist.parser.processor;

import creative.scenes.eventlist.parser.data.EventType;
import creative.scenes.eventlist.parser.entity.MidiEventInfo;
import creative.scenes.eventlist.parser.processor.helper.ByteHelper;
import creative.scenes.sysex.convertor.SysexToHexStringConvertor;
import entity.midi.Midi;
import javafx.scene.paint.Color;
import util.ColorScheme;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.SysexMessage;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import static util.ColorScheme.getColor;

public class SysexProcessor {
    private static final Map<ColorScheme.ColorKey, Color> colors = new HashMap<>();

    static {
        colors.put(new ColorScheme.ColorKey("sysex"), Color.CRIMSON);

        ColorScheme.registerColors(colors);
    }

    public static boolean processMessage(SysexMessage message, MidiEventInfo midiEventInfo) {
        boolean result;

        midiEventInfo.setEventType(EventType.SYSEX);
        midiEventInfo.setColor(getColor("sysex"));
        StringBuilder sysex = SysexToHexStringConvertor.convertToHexStringbuilder(message.getMessage());
        midiEventInfo.setMessage(sysex.toString());

        String selector = ByteHelper.getAndStrip(sysex, 2);

        result = switch(selector) {
            case "F0 7F" -> handleUniversalRealTimeMessage(sysex, midiEventInfo);
            case "F0 7E" -> handleUniversalNonRealTimeMessage(sysex, midiEventInfo);
            case "F0 43" -> handleVendorMessage(sysex, midiEventInfo);
            default -> handleUnknownSysex(midiEventInfo);
        };

        return result;
    }

    private static boolean handleUniversalRealTimeMessage(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        return result;
    }

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

    private static boolean handleScaleOctaveTuning(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unrt", "scale octave tuning"));
        midiEventInfo.setDescription("Scale/Octave tuning");

//        return result;
        return false;
    }

    private static boolean handleGM1SystemOn(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unrt", "gm1 system on"));
        midiEventInfo.setDescription("GM1 system on");

//        return result;
        return false;
    }

    private static boolean handleGeneralMidiSystemOff(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unrt", "general midi system off"));
        midiEventInfo.setDescription("General MIDI system off");

//        return result;
        return false;
    }

    private static boolean handleGM2SystemOn(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unrt", "gm2 system on"));
        midiEventInfo.setDescription("GM2 system on");

//        return result;
        return false;
    }

    private static boolean handleUnknownUniversaleNonRealTimeMessage(MidiEventInfo midiEventInfo) {
        boolean result = true;

        return result;
    }

    private static boolean handleVendorMessage(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        String selector = ByteHelper.getAndStrip(sysex, 1);
        int iselector = Integer.parseInt(selector, 16) & 0xF0;

        result = switch (iselector) {
            case 0x00 -> handleXGBulkDump(sysex, midiEventInfo);
            case 0x10 -> handleXGParameterChange(sysex, midiEventInfo);
            case 0x20 -> handleXGDumpRequest(sysex, midiEventInfo);
            case 0x30 -> handleXGParameterRequest(sysex, midiEventInfo);
            default -> handleUnknownVendorMessage(sysex, midiEventInfo);
        };

        return result;
    }

    private static boolean handleXGBulkDump(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "vendor", "xg bulk dump"));
        midiEventInfo.setDescription("XG bulk dump");

        return result;
    }

    private static boolean handleXGParameterChange(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "vendor", "xg parameter change"));
        midiEventInfo.setDescription("XG parameter change");

        // Skip 1 byte
        ByteHelper.getAndStrip(sysex, 1);

        String address = ByteHelper.getAndStrip(sysex, 3);

        result = switch (address) {
            case "00 00 7E" -> handleXGSystemOn(midiEventInfo);
            default -> handleUnknownXGParameterChange(midiEventInfo);
        };

        return result;
    }

    private static boolean handleXGSystemOn(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "vendor", "xg system on"));
        midiEventInfo.setDescription("XG system on");

        return result;
    }

    private static boolean handleUnknownXGParameterChange(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "vendor", "unknown"));
        midiEventInfo.setDescription("UNKNOWN");

        return result;
    }

    private static boolean handleXGDumpRequest (StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "vendor", "xg dump request"));
        midiEventInfo.setDescription("XG dump request");

        return result;
    }

    private static boolean handleXGParameterRequest (StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "vendor", "xg parameter request"));
        midiEventInfo.setDescription("XG parameter request");

        return result;
    }

    private static boolean handleUnknownVendorMessage(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "vendor", "unknown"));
        midiEventInfo.setDescription("UNKNOWN");

        return result;
    }

    private static boolean handleUnknownSysex(MidiEventInfo midiEventInfo) {
        boolean result = true;

        return result;
    }
}

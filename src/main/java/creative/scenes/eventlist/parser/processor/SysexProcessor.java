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

    //
    public static boolean processMessage(SysexMessage message, MidiEventInfo midiEventInfo) {
        boolean result;

        midiEventInfo.setEventType(EventType.SYSEX);
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
            default -> handleUnknownVendorMessage(selector, midiEventInfo);
        };

        return result;
    }

    // F0 43
    private static boolean handleYamahaMessage(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = false;

        midiEventInfo.setColor(getColor("sysex", "vendor"));
        midiEventInfo.setDescription("Yamaha");

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

        midiEventInfo.setColor(getColor("sysex", "yamaha", "unknown"));
        midiEventInfo.setDescription("UNKNOWN");
        midiEventInfo.setComment(sysex.toString());

        return result;
    }

    // FO 7F
    private static boolean handleUniversalRealTimeMessage(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

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

        midiEventInfo.setColor(getColor("sysex", "unrt", "scale octave tuning"));
        midiEventInfo.setDescription("Scale/Octave tuning");

//        return result;
        return false;
    }

    // F0 7E 09 01
    private static boolean handleGM1SystemOn(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unrt", "gm1 system on"));
        midiEventInfo.setDescription("GM1 system on");

//        return result;
        return false;
    }

    // F0 7E 09 02
    private static boolean handleGeneralMidiSystemOff(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unrt", "general midi system off"));
        midiEventInfo.setDescription("General MIDI system off");

//        return result;
        return false;
    }

    // F0 7E 09 03
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

        midiEventInfo.setColor(getColor("sysex", "yamaha", "xg parameter change"));
        midiEventInfo.setDescription("XG parameter change");

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
            case "0A 02" -> handleXGMultiPart(address, midiEventInfo);
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
            default -> handleUnknownXGParameterChange(midiEventInfo);
        };

        return result;
    }

    // F0 43 10 -- 02 01
    private static boolean handleEffect1(StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        System.out.println("Sysex");
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

        System.out.println("Sysex");
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

        midiEventInfo.setColor(getColor("sysex", "insertion effect"));
        midiEventInfo.setDescription("Insertion effect");

        return result;
    }

    // F0 43 10 -- 02 01 00
    private static boolean handleReverb(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "reverb effect"));
        midiEventInfo.setDescription("Reverb effect");

//        return result;
        return false;
    }

    // F0 43 10 -- 02 01 20
    private static boolean handleChorus(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "chorus effect"));
        midiEventInfo.setDescription("Chorus effect");

//        return result;
        return false;
    }

    // F0 43 10 -- 02 01 40
    private static boolean handleVariation(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "variation effect"));
        midiEventInfo.setDescription("Variation effect");

//        return result;
        return false;
    }

    // F0 43 10 -- 02 01 ??
    private static boolean handleUnknownEffect1(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("Unknown effect1");

        return result;
    }

    // F0 43 10 -- 03 nn ??
    private static boolean handleUnknownEffect2(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("Unknown effect1");

        return result;
    }

    // F0 43 10 -- 0A nn
    private static boolean handleXGMultiPart(String address, MidiEventInfo midiEventInfo) {
        boolean result = true;

        StringBuilder newAddress = new StringBuilder(address);

        ByteHelper.getAndStrip(newAddress, 1);

        String selector = ByteHelper.getAndStrip(newAddress, 1);

        midiEventInfo.setColor(getColor("sysex", "partmode"));
        midiEventInfo.setDescription("XG Multipart part mode");

        int receiveChannel = Integer.parseInt(selector, 16);
        midiEventInfo.setComment("receive channel: " + receiveChannel);

//        return result;
        return false;
    }

    // F0 43 10 -- 08 nn
    private static boolean handleMultiPartMessage(String address, MidiEventInfo midiEventInfo) {
        boolean result = true;

        StringBuilder newAddress = new StringBuilder(address);

        ByteHelper.getAndStrip(newAddress, 1);

        String selector = ByteHelper.getAndStrip(newAddress, 1);

        midiEventInfo.setColor(getColor("sysex", "partmode"));
        midiEventInfo.setDescription("XG Multipart part mode");

        int receiveChannel = Integer.parseInt(selector, 16);
        midiEventInfo.setComment("receive channel: " + receiveChannel);

//        return result;
        return false;
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

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("XG parameter change unknown");
        midiEventInfo.setComment(address + " UNKNOWN");

        return result;
    }

    // F0 43 10 -- 00 00 7E
    private static boolean handleXGSystemOn(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "vendor", "xg system on"));
        midiEventInfo.setDescription("XG system on");

//        return result;
        return false;
    }

    // F0 43 10 -- ?? ??
    private static boolean handleUnknownXGParameterChange(MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "vendor", "unknown"));
        midiEventInfo.setDescription("UNKNOWN");

        return result;
    }

    // F0 43 20
    private static boolean handleXGDumpRequest (StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "vendor", "xg dump request"));
        midiEventInfo.setDescription("XG dump request");

        return result;
    }

    // F0 43 30
    private static boolean handleXGParameterRequest (StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "vendor", "xg parameter request"));
        midiEventInfo.setDescription("XG parameter request");

        return result;
    }

    // F0 43 7E
    private static boolean handleStyle (StringBuilder sysex, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "vendor", "xg parameter request"));
        midiEventInfo.setDescription("XG parameter request");

        return result;
    }

    private static boolean handleUnknownVendorMessage(String vendorId, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "vendor", "unknown"));
        midiEventInfo.setDescription("UNKNOWN VENDOR");
        midiEventInfo.setComment(vendorId);

        return result;
    }

    // ??
    private static boolean handleUnknownSysex(String selector, MidiEventInfo midiEventInfo) {
        boolean result = true;

        midiEventInfo.setColor(getColor("sysex", "unknown"));
        midiEventInfo.setDescription("UNKNOWN");
        midiEventInfo.setComment(selector + "= UNKNOWN");

        return result;
    }
}

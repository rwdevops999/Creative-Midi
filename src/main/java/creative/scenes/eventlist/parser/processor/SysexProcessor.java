package creative.scenes.eventlist.parser.processor;

import creative.scenes.eventlist.parser.data.EventType;
import creative.scenes.eventlist.parser.entity.MidiEventInfo;
import creative.scenes.eventlist.parser.processor.helper.ByteHelper;
import creative.scenes.sysex.convertor.SysexToHexStringConvertor;
import javafx.scene.paint.Color;
import util.ColorScheme;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.SysexMessage;
import java.util.HashMap;
import java.util.Map;

import static util.ColorScheme.getColor;

public class SysexProcessor {
    private static final Map<ColorScheme.ColorKey, Color> colors = new HashMap<>();

    static {
        colors.put(new ColorScheme.ColorKey("meta"), Color.CRIMSON);

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
            case "F0 7F" -> handleUniversalRealTimeMessage(sysex);
            case "F0 7E" -> handleUniversalNonRealTimeMessage(sysex);
            case "F0 43" -> handleVendorMessage(sysex);
            default -> handleUnknownSysex();
        };

        return result;
    }

    private static boolean handleUniversalRealTimeMessage(StringBuilder sysex) {
        boolean result = true;

        return result;
    }

    private static boolean handleUniversalNonRealTimeMessage(StringBuilder sysex) {
        boolean result = true;

        return result;
    }

    private static boolean handleVendorMessage(StringBuilder sysex) {
        boolean result = true;

        return result;
    }
    
    private static boolean handleUnknownSysex() {
        boolean result = true;

        return result;
    }
}

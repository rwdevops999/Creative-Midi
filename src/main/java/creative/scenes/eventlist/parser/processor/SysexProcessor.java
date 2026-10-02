package creative.scenes.eventlist.parser.processor;

import creative.scenes.eventlist.parser.data.EventType;
import creative.scenes.eventlist.parser.entity.MidiEventInfo;
import javafx.scene.paint.Color;
import util.ColorScheme;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.SysexMessage;
import java.util.HashMap;
import java.util.Map;

public class SysexProcessor {
    private static final Map<ColorScheme.ColorKey, Color> colors = new HashMap<>();

    static {
        colors.put(new ColorScheme.ColorKey("meta"), Color.CRIMSON);

        ColorScheme.registerColors(colors);
    }

    public static boolean processMessage(SysexMessage message, MidiEventInfo midiEventInfo) {
        midiEventInfo.setEventType(EventType.SYSEX);

        // TODO
        return true;
    }
}

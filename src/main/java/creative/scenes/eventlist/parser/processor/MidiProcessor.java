package creative.scenes.eventlist.parser.processor;

import creative.scenes.eventlist.parser.data.EventType;
import creative.scenes.eventlist.parser.entity.MidiEventInfo;
import javafx.scene.paint.Color;
import util.ColorScheme;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.util.HashMap;
import java.util.Map;

public class MidiProcessor {
    private static final Map<ColorScheme.ColorKey, Color> colors = new HashMap<>();

    static {
        colors.put(new ColorScheme.ColorKey("midi"), Color.BLUE);

        ColorScheme.registerColors(colors);
    }

    public static boolean processMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        midiEventInfo.setEventType(EventType.MIDI);
        // TODO
        return false;
    }
}

package creative.scenes.eventlist.parser.processor;

import creative.scenes.eventlist.parser.data.EventType;
import creative.scenes.eventlist.parser.entity.MidiEventInfo;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.ShortMessage;

public class MetaProcessor {
    public static boolean processMessage(MetaMessage message, MidiEventInfo midiEventInfo) {
        midiEventInfo.setEventType(EventType.META);
        // TODO
        return true;
    }
}

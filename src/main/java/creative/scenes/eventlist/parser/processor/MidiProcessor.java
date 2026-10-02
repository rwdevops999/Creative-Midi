package creative.scenes.eventlist.parser.processor;

import creative.scenes.eventlist.parser.data.EventType;
import creative.scenes.eventlist.parser.entity.MidiEventInfo;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

public class MidiProcessor {
    public static boolean processMessage(ShortMessage message, MidiEventInfo midiEventInfo) {
        midiEventInfo.setEventType(EventType.MIDI);
        // TODO
        return true;
    }
}

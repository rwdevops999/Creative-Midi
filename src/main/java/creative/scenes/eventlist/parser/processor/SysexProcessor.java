package creative.scenes.eventlist.parser.processor;

import creative.scenes.eventlist.parser.data.EventType;
import creative.scenes.eventlist.parser.entity.MidiEventInfo;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.SysexMessage;

public class SysexProcessor {
    public static boolean processMessage(SysexMessage message, MidiEventInfo midiEventInfo) {
        midiEventInfo.setEventType(EventType.SYSEX);

        // TODO
        return true;
    }
}

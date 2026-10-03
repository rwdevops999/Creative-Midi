package creative.scenes.eventlist.parser.processor.matcher;

import creative.scenes.eventlist.parser.MBTCalculator;
import creative.scenes.eventlist.parser.data.MidiKind;
import creative.scenes.eventlist.parser.entity.MidiEventInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.ShortMessage;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class MidiNoteMatcher {
    private static final Logger logger = LoggerFactory.getLogger(MidiNoteMatcher.class);

    private record NoteKey(int channel, int noteNumber) {}

    private final Map<NoteKey, Queue<MidiEventInfo>> activeNotes = new HashMap<>();

    public boolean processEvent(ShortMessage message, MidiEventInfo midiEventInfo) {
        boolean isNoteOn = (message.getStatus() & 0xF0) == 0x90;
        boolean isNoteOff = (message.getStatus() & 0xF0) == 0x80;

        NoteKey key = new NoteKey(message.getChannel(), message.getData1());

        if (isNoteOn && message.getData2() > 0) {
            // Handle NOTE ON
            activeNotes.computeIfAbsent(key, k -> new LinkedList<>()).add(midiEventInfo);
        } else if (isNoteOff || (isNoteOn && message.getData2() == 0)) {
            // Handle NOTE OFF
            Queue<MidiEventInfo> onsetQueue = activeNotes.get(key);
            if (onsetQueue != null && !onsetQueue.isEmpty()) {
                MidiEventInfo noteOnEvent = onsetQueue.poll();

                triggerNotePairFound(noteOnEvent, midiEventInfo);

                if (onsetQueue.isEmpty()) {
                    activeNotes.remove(key);
                }

                copyEvents(noteOnEvent, midiEventInfo);
                return true;
            } else {
                logger.error("[CM_MIDI_NOTE_MATCHER] Orphaned Note Off detected (No matching Note On)");
            }
        }

        return false;
    }

    private void triggerNotePairFound(MidiEventInfo noteOn, MidiEventInfo noteOff) {
        long durationTicks = noteOff.getOriginalEvent().getTick() - noteOn.getOriginalEvent().getTick();
//        noteOn.setDuration(durationTicks);
//        long durationTicks = MBTCalculator.convertTick(noteOff.getMidiEvent().getTick() - noteOn.getMidiEvent().getTick());
        double seconds = MBTCalculator.convertToSeconds(durationTicks);
//        noteOn.setInternalDuration(durationTicks);
        noteOn.setDuration(formatDuration(seconds));
    }

    private static String formatDuration(double seconds) {
        Duration duration = Duration.ofNanos((long) (seconds * 1_000_000_000));

        long lhours = duration.toHours();
        long lminutes = duration.toMinutesPart();
        long lseconds = duration.toSecondsPart();

        // Extract milliseconds and scale down to 2 digits (.mm)
        long millis2Digits = duration.toMillisPart() / 10;

        return String.format("%02d:%02d:%02d.%02d", lhours, lminutes, lseconds, millis2Digits);
    }

    private void copyEvents(MidiEventInfo src, MidiEventInfo dst) {
        dst.setTick(src.getTick());
        dst.setMbtPosition(src.getMbtPosition());
        dst.setOriginalEvent(src.getOriginalEvent());
        dst.setData2(src.getData2());
        dst.setMessage(src.getMessage());
        dst.setDuration(src.getDuration());
    }
}

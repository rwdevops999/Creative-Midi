package creative.scenes.eventlist.parser.processor;

import creative.scenes.eventlist.parser.MBTCalculator;
import creative.scenes.eventlist.parser.entity.MidiEventInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.*;
import java.util.LinkedList;
import java.util.Queue;

public class MidiFileProcessor {
    private static final Logger logger = LoggerFactory.getLogger(MidiFileProcessor.class);

    private static Queue<MidiEventInfo> events = new LinkedList<>();

    public static void processMidi(Sequence sequence) {
        events = new LinkedList<>();

        // Track doesn't keep the tracknumber, so we do it ourselves
        int trackNumber = 0;

        // we run through each track
        for (Track track : sequence.getTracks()) {
            trackNumber++;

            // loop though all events in the track (track.size == number of events in track)
            for (int i = 0; i < track.size(); i++) {
                boolean isProcessed = false;

                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();

                long tick = MBTCalculator.convertTick(event.getTick());
                MidiEventInfo midiEventInfo = new MidiEventInfo(tick, MBTCalculator.calculateMBT(tick), trackNumber, event);

                if (message instanceof ShortMessage midiMessage) {
                    // The message is a MIDI
                    isProcessed = MidiProcessor.processMessage(midiMessage, midiEventInfo);
                } else if (message instanceof MetaMessage metaMessage) {
                    // The message is a META
                    isProcessed = MetaProcessor.processMessage(metaMessage, midiEventInfo);
                } else if (message instanceof SysexMessage sysexMessage) {
                    // The message is a SYSEX
                    isProcessed = SysexProcessor.processMessage(sysexMessage, midiEventInfo);
                } else {
                    logger.error("[CM_MIDI_FILE_PROCESSOR] ERROR: Unknown message type: " + message.getClass().getName());
                }

                if (isProcessed) {
                    events.add(midiEventInfo);
                }
            }
        }
    }

    public static Queue<MidiEventInfo> getEvents() {
        return events;
    }
}

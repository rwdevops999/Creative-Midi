package creative.scenes.eventlist.parser;

import creative.scenes.eventlist.parser.entity.MidifileEventInfo;

import javax.sound.midi.*;
import java.util.List;
import java.util.Map;

public class MidiSequenceConverter {
    public static Sequence convertToStandardSequence(MidiSequence customSeq) throws Exception {
        // Create a new empty sequence with PPQ filled in.
        Sequence standardSequence = new Sequence(Sequence.PPQ, customSeq.getPpq());

        // Loop through the tracks
        for (Map.Entry<Integer, List<MidifileEventInfo>> entry : customSeq.getTracks().entrySet()) {
            // get all events on this track
            List<MidifileEventInfo> customEvents = entry.getValue();

            // create a empty standard track (!!! strandardTrack is a reference to this new track
            Track standardTrack = standardSequence.createTrack();

            // add all events to this track
            for (MidifileEventInfo customEvent : customEvents) {
                // create a midi message
                MidiMessage message = createMidiMessage(customEvent);

                if (message != null) {
                    // add the official event to the track
                    MidiEvent standardEvent = new MidiEvent(message, customEvent.tick);
                    standardTrack.add(standardEvent);
                }
            }

        }

        return standardSequence;
    }

    // Helper for converting event (raw status and byte-arrays) to javax.sound.midi objects
    private static MidiMessage createMidiMessage(MidifileEventInfo customEvent) {
        try {
            int status = customEvent.status;
            byte[] data = customEvent.data != null ? customEvent.data : new byte[0];

            // Process Meta events
            if (status == 0xFF) {
                MetaMessage metaMessage = new MetaMessage();
                metaMessage.setMessage(
                        customEvent.getMetaType(),
                        customEvent.getData(),
                        customEvent.getData().length
                );
                return metaMessage;
            }

            // Processs Sysex events
            if (status == 0xF0 || status == 0xF7) {
                SysexMessage sysex = new SysexMessage();
                sysex.setMessage(status, data, data.length);
                return sysex;
            }

            // Process Midi events
            if (status >= 0x80 && status < 0xF0) {
                ShortMessage shortMsg = new ShortMessage();
                int data1 = (data.length > 0) ? (data[0] & 0xFF) : 0;
                int data2 = (data.length > 1) ? (data[1] & 0xFF) : 0;

                shortMsg.setMessage(status, data1, data2);
                return shortMsg;
            }

        } catch (InvalidMidiDataException imde) {
            System.err.println("Exception: invalid midi data" + imde.getMessage());
        }

        // Here we come if the event can't be processed
        return null;
    }
}
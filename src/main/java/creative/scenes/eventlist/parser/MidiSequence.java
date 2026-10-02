package creative.scenes.eventlist.parser;

import creative.scenes.eventlist.parser.entity.MidifileEventInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains information about the midi file
 */
public class MidiSequence {
    @Getter
    @Setter
    private int fileType;

    @Getter
    @Setter
    private int ppq;

    @Getter
    @Setter
    private int trackCount;

    @Getter
    private final Map<Integer, List<MidifileEventInfo>> tracks;

    public MidiSequence() {
        this.fileType = -1;
        this.ppq = 480;
        this.trackCount = 0;
        this.tracks = new HashMap<>();
    }

    public double getMidiBpm(MidiSequence sequence) {
        for (List<MidifileEventInfo> trackEvents : sequence.getTracks().values()) {
            for (MidifileEventInfo event : trackEvents) {
                // Check on a 3-byte data of the tempo event (0x51)
                if (event.status == 0xFF && event.data != null && event.data.length == 3) {
                    long mpqn = ((event.data[0] & 0xFF) << 16)
                            | ((event.data[1] & 0xFF) << 8)
                            | (event.data[2] & 0xFF);

                    if (mpqn > 0) {
                        return 60_000_000.0 / mpqn; // Convert MPQN to BPM
                    }
                }
            }
        }

        // If not found handle a default of 120 BPM
        return 120.0;
    }

    // The maximal number of ticks in the midi
    public long getTickLength() {
        long maxTicks = 0;

        // Run through all tracks
        for (List<MidifileEventInfo> trackEvents : tracks.values()) {
            if (!trackEvents.isEmpty()) {
                MidifileEventInfo lastEvent = trackEvents.get(trackEvents.size() - 1);

                if (lastEvent.tick > maxTicks) {
                    maxTicks = lastEvent.tick;
                }
            }
        }

        return maxTicks;
    }
}

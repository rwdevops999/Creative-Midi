package creative.scenes.eventlist.parser.entity;

import creative.scenes.eventlist.parser.MBTCalculator;
import creative.scenes.eventlist.parser.data.EventType;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import javax.sound.midi.MidiEvent;

public class MidiEventInfo {

    // the event tick converted (to targetPPQ)
    private long tick;

    // the MBT Position
    private MbtPosition mbtPosition;

    // The track id it belongs to
    private int trackId;

    // The original event
    private MidiEvent originalEvent;

    public MidiEventInfo(long tick, MbtPosition mbtPosition, int trackId, MidiEvent originalEvent) {
        this.tick = tick;
        this.mbtPosition = mbtPosition;
        this.trackId = trackId;
        this.originalEvent = originalEvent;
    }

    @Setter
    private EventType eventType;

    @Getter
    @Setter
    private Color color;

    @Getter
    private String description;
}

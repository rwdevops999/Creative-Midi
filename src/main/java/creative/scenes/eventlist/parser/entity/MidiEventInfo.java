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
    @Getter
    private String mbtPosition;

    // The track id it belongs to
    private int trackId;

    // The original event
    private MidiEvent originalEvent;

    public MidiEventInfo(long tick, MbtPosition mbtPosition, int trackId, MidiEvent originalEvent) {
        this.tick = tick;
        this.mbtPosition = mbtPosition.toString();
        this.trackId = trackId;
        this.originalEvent = originalEvent;
    }

    @Setter
    @Getter
    private EventType eventType;

    @Getter
    @Setter
    private Color color;

    @Setter
    private String description;

    @Setter
    private String comment;  // this can contain integers or strings

    private void stringifyData(int data1) {
        comment = ""+data1;
    }

    private int data1;
    public void setData1(int data1) {
        this.data1 = data1;
        stringifyData(data1);
    }

    @Setter
    private int data2;

    @Setter
    @Getter
    private Integer channel = null;

    @Setter
    private String message;
}

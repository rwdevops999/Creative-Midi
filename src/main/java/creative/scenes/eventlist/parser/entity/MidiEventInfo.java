package creative.scenes.eventlist.parser.entity;

import creative.scenes.eventlist.parser.MBTCalculator;
import creative.scenes.eventlist.parser.data.EventType;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import javax.sound.midi.MidiEvent;

public class MidiEventInfo {

    // the event tick converted (to targetPPQ)
    @Getter
    @Setter
    private long tick;

    // the MBT Position
    @Getter
    @Setter
    private String mbtPosition;

    // The track id it belongs to
    @Getter
    @Setter
    private int trackId;

    // The original event
    @Getter
    @Setter
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

    @Setter
    @Getter
    private long colorAsLong;

    public void setColor(Color color) {
        this.colorAsLong = colorToLong(color);
    }

    @Setter
    @Getter
    private String description;

    @Setter
    @Getter
    private String comment;  // this can contain integers or strings

    private void stringifyData(int data1) {
        comment = ""+data1;
    }

    @Getter
    private int data1;
    public void setData1(int data1) {
        this.data1 = data1;
        stringifyData(data1);
    }

    @Setter
    @Getter
    private Integer data2 = null;

    @Setter
    @Getter
    private Integer channel = null;

    @Setter
    @Getter
    private String message;

    private long colorToLong(Color color) {
        // Convert the 0.0-1.0 double values to 0-255 integers
        long r = Math.round(color.getRed() * 255);
        long g = Math.round(color.getGreen() * 255);
        long b = Math.round(color.getBlue() * 255);
        long a = Math.round(color.getOpacity() * 255);

        // Shift bits to pack them into a single value (RGBA format)
        return ((r << 24) | (g << 16) | (b << 8) | a)  & 0xFFFFFFFFL;
    }

    @Setter
    @Getter
    private String duration = null;

}

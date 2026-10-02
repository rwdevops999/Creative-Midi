package creative.scenes.eventlist.parser.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MidifileEventInfo {
    public long tick;       // The added rime in ticks from the beginning
    public int status;      // The event type (e.g. 0xFF for Meta)
    public byte[] data;     // De raw data of the event
    public int metaType;    // the meta kind

    private int track;      // track id

    public MidifileEventInfo(long tick, int status, byte[] data, int metaType) {
        this.tick = tick;
        this.status = status;
        this.data = data;
        this.metaType = metaType;
    }

    public MidifileEventInfo(long tick, int status, byte[] data) {
        this(tick, status, data, -1);
    }
}

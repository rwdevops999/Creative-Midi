package creative.scenes.eventlist.parser.entity;

import creative.scenes.eventlist.parser.data.EventType;
import creative.scenes.eventlist.parser.data.MidiKind;
import javafx.beans.property.*;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;

import static util.ColorScheme.getColor;

public class MidiEventInfo_bu {
    @Getter
    private long tick;

    @Getter
    @Setter
    private long internalDuration;

    private final StringProperty duration = new SimpleStringProperty();
    public String getDuration() {
        return duration.get();
    }

    public void setDuration(String data) {
        this.duration.set(data);
    }

    @Getter
    @Setter
    private int itemColor;

    private final IntegerProperty track = new SimpleIntegerProperty();
    public Integer getTrack() {
        return track.get();
    }
    public void setTrack(Integer track) {
        this.track.set(track);
    }

    @Getter
    private EventType eventTypeInternal;

    public void setEventType(EventType eventType) {
        eventTypeInternal = eventType;
        if (eventType.equals(EventType.MIDI)) {
            this.itemColor = setColor(getColor("midi", null, null));
        } else if (eventType.equals(EventType.META)) {
            this.itemColor = setColor(getColor("meta", null, null));
        } else if (eventType.equals(EventType.SYSEX)) {
            this.itemColor = setColor(getColor("sysex", null, null));
        }
    }

    private final StringProperty kind = new SimpleStringProperty();
    public String getKind() {
        return kind.get();
    }
    public void setKindAsString(String value) {
        kind.set(value);
    }

    public void setKind(MidiKind kind) {
        switch (kind) {
            case CONTROL:
                this.itemColor = setColor(getColor("midi", "controller", null));
                break;
            case NOTE:
                this.itemColor = setColor(getColor("midi", "note", null));
                break;
            case PATCH:
                this.itemColor = setColor(getColor("midi", "program change", null));
                break;
            case PITCH:
                this.itemColor = setColor(getColor("midi", "pitch", null));
                break;
            default:
                this.itemColor = setColor(getColor("midi"));
                break;
        };

        this.kind.set(kind.getKindValue());
    }

    @Getter
    @Setter
    MidiEvent midiEvent;

    public int getStatus() {
        MidiMessage message = midiEvent.getMessage();

        return message.getStatus();
    }

    @Getter
    @Setter
    private MbtPosition mbtInternal;

    private StringProperty mbt = new SimpleStringProperty();
    public void setMbt(MbtPosition mbt) {
        this.mbtInternal = mbt;
        if (mbt != null) {
            this.mbt.set(mbt.toString());
        }
    }
    public String getMbt() {
        return mbt.get();

    }

    private final ObjectProperty<Integer> channel = new SimpleObjectProperty<>();
    public Integer getChannel() {
        return channel.get();
    }

    public void setChannel(Integer channel) {
        this.channel.set(channel);
    }

    private final StringProperty data = new SimpleStringProperty();
    public String getData() {
        return data.get();
    }

    public void setData(String data) {
        this.data.set(data);
    }

    private final ObjectProperty<Integer> data2 = new SimpleObjectProperty<>();
    public Integer getData2() {
        return data2.get();
    }

    public void setData2(Integer data) {
        this.data2.set(data);
    }

    private final StringProperty message = new SimpleStringProperty();
    public String getMessage() {
        return message.get();
    }

    public void setMessage(String data) {
        this.message.set(data);
    }

    public MidiEventInfo_bu(long tick, MbtPosition mbt, int track, MidiEvent midiEvent) {
        this.tick = tick;
        setMbt(mbt);
        setTrack(track);
        this.midiEvent = midiEvent;
        itemColor = setColor(Color.GRAY);
    }

    public static int setColor(Color color) {
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);
        int a = (int) (color.getOpacity() * 255);

//        int argb = (a << 24) | (r << 16) | (g << 8) | b;

//        return argb;

        int rgba = (r << 24) | (g << 16) | (b << 8) | a;

        return rgba;
    }
}

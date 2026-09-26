package entity.midi;

import creative.scenes.midi.data.ByteType;
import creative.scenes.midi.data.MessageType;
import entity.AEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Midi extends AEntity {
    private String name;
    int statusByte;
    MessageType messageType;
    int channel;
    ByteType byte1Type;
    int byte1;
    ByteType byte2Type;
    int byte2;

    public Midi() {
        this.name = "";
    }
}

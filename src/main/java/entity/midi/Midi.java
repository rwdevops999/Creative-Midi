package entity.midi;

import creative.scenes.midi.data.Byte1Type;
import creative.scenes.midi.data.ByteType;
import creative.scenes.midi.data.MessageType;
import entity.AEntity;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Midi extends AEntity {
    private StringProperty nameProperty = new SimpleStringProperty();
    private String name;

    private StringProperty statusProperty = new SimpleStringProperty();
    private String status;

    private ObjectProperty<MessageType> messageTypeProperty = new SimpleObjectProperty<>();
    MessageType messageType;

    private ObjectProperty<Integer> channelProperty = new SimpleObjectProperty<>();
    int channel;

    private ObjectProperty<ByteType> byte1TypeProperty = new SimpleObjectProperty<>();
    ByteType byte1Type;

    private ObjectProperty<Integer> byte1Property = new SimpleObjectProperty<>();
    int byte1;

    private ObjectProperty<ByteType> byte2TypeProperty = new SimpleObjectProperty<>();
    ByteType byte2Type;

    private ObjectProperty<Integer> byte2Property = new SimpleObjectProperty<>();
    int byte2;

    public Midi() {
        this.name = "";
    }

    public void setName(String name) {
        nameProperty.set(name);
        this.name = name;
    }

    public String getName() {
        return nameProperty.get();
    }

    public void setStatus(String status) {
        statusProperty.set(status);
        this.status = status;
    }

    public String getStatus() {
        return statusProperty.get();
    }

    public void setMessageType(String messageType) {
        messageTypeProperty.set(MessageType.valueOf(messageType));
        this.messageType = MessageType.valueOf(messageType);
    }

    public MessageType getMessageType() {
        return messageTypeProperty.get();
    }

    public void setChannel(int channel) {
        channelProperty.set(channel);
        this.channel = channel;
    }

    public int getChannel() {
        return channelProperty.get();
    }

    public void setByte1Type(ByteType type) {
        byte1TypeProperty.set(type);
        this.byte1Type = type;
    }

    public ByteType getByte1Type() {
        return byte1TypeProperty.get();
    }

    public void setByte1(int byte1) {
        byte1Property.set(byte1);
        this.byte1 = byte1;
    }

    public Integer getByte1() {
        return byte1Property.get();
    }

    public void setByte2Type(ByteType type) {
        byte2TypeProperty.set(type);
        this.byte2Type = type;
    }

    public ByteType getByte2Type() {
        return byte2TypeProperty.get();
    }

    public void setByte2(int byte2) {
        byte2Property.set(byte2);
        this.byte2 = byte2;
    }

    public Integer getByte2() {
        return byte2Property.get();
    }
}

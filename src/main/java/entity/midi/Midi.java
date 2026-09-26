package entity.midi;

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

    ByteType byte1Type;
    private ObjectProperty<Integer> byte1Property = new SimpleObjectProperty<>();
    int byte1;

    ByteType byte2Type;
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

    public void setByte1(int byte1) {
        byte1Property.set(byte1);
        this.byte1 = byte1;
    }

    public int getByte1() {
        return byte1Property.get();
    }
}

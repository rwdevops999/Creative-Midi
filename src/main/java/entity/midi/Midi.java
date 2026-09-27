package entity.midi;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    public StringProperty getNameProperty() {
        return nameProperty;
    }

    private String name;

    private StringProperty statusProperty = new SimpleStringProperty();
    @JsonIgnore
    public StringProperty getStatusProperty() {
        return statusProperty;
    }

    private String status;

    private ObjectProperty<MessageType> messageTypeProperty = new SimpleObjectProperty<>();
    @JsonIgnore
    public ObjectProperty<MessageType> getMessageTypeProperty() {
        return messageTypeProperty;
    }

    MessageType messageType;

    private ObjectProperty<Integer> channelProperty = new SimpleObjectProperty<>(0);
    @JsonIgnore
    public ObjectProperty<Integer> getChannelProperty() {
        return channelProperty;
    }
    int channel;

    private ObjectProperty<ByteType> byte1TypeProperty = new SimpleObjectProperty<>();
    @JsonIgnore
    public ObjectProperty<ByteType> getByte1TypeProperty() {
        return byte1TypeProperty;
    }

    ByteType byte1Type;

    private ObjectProperty<Integer> byte1Property = new SimpleObjectProperty<>(0);
    @JsonIgnore
    public ObjectProperty<Integer> getByte1Property() {
        return byte1Property;
    }

    int byte1;

    private ObjectProperty<ByteType> byte2TypeProperty = new SimpleObjectProperty<>();
    @JsonIgnore
    public ObjectProperty<ByteType> getByte2TypeProperty() {
        return byte2TypeProperty;
    }

    ByteType byte2Type;

    private ObjectProperty<Integer> byte2Property = new SimpleObjectProperty<>(0);
    @JsonIgnore
    public ObjectProperty<Integer> getByte2Property() {
        return byte2Property;
    }

    Integer byte2;

    public Midi() {
        setName("Unknown");
        setStatus("00");
        setMessageType("system");
    }

    public Midi(Midi other) {
        if (other == null) return;

        this.setName(other.getName());
        this.setStatus(other.getStatus());
        this.setMessageType(other.getMessageType().name());
        this.setChannel(other.getChannel());
        this.setByte1Type(other.getByte1Type());
        this.setByte1(other.getByte1());
        this.setByte2Type(other.getByte2Type());
        this.setByte2(other.getByte2());
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

    public Integer getChannel() {
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
        return byte1Property.get() & 0xFF;
    }

    public void setByte2Type(ByteType type) {
        byte2TypeProperty.set(type);
        this.byte2Type = type;
    }

    public ByteType getByte2Type() {
        return byte2TypeProperty.get();
    }

    public void setByte2(Integer byte2) {
        byte2Property.set(byte2);
        this.byte2 = byte2;
    }

    public Integer getByte2() {
        return byte2Property.get() & 0xFF;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        // Replace 'Note' with your actual class name
        Midi other = (Midi)obj;

        boolean result = this.getName().equals(other.getName());

        if (this.getStatus() != null) {
            result = result && this.getStatus().equals(other.getStatus());
        }

        if (this.getMessageType() != null) {
            result = result && this.getMessageType().equals(other.getMessageType());
        }

        if (this.getChannel() != null) {
            result = result && this.getChannel().equals(other.getChannel());
        }

        if (this.getByte1Type() != null) {
            result = result && this.getByte1Type().equals(other.getByte1Type());
        }

        if (this.getByte1() != null) {
            result = result && this.getByte1().equals(other.getByte1());
        }

        if (this.getByte2Type() != null) {
            result = result && this.getByte2Type().equals(other.getByte2Type());
        }

        if (this.getByte2() != null) {
            result = result && this.getByte2().equals(other.getByte2());
        }

        // Replace 'id' with your object's unique identifier (e.g., getId())
        return result;
    }

    @Override
    public int hashCode() {
        // Keeps hashCode consistent with equals
        return java.util.Objects.hash(getName());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (! getStatus().isEmpty()) {
            int status = Integer.parseInt(getStatus(), 16);
            if (getMessageType().equals(MessageType.channel)) {
                status += getChannel();
                sb.append(String.format("%02X", status));
                if (getByte1() != null) {
                    sb.append(" ").append(String.format("%02X", getByte1()));
                    if (getByte2() != null) {
                        sb.append(" ").append(String.format("%02X", getByte2()));
                    }
                }
            } else {
                sb.append(String.format("%02X", status));
            }
        }

        return sb.toString();
    }
}

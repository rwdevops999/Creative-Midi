package communication;

import creative.panes.monitor.data.CommunicationType;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

public class CommunicationModel {
    // For the application status
    @Getter
    private static final StringProperty statusProperty = new SimpleStringProperty();

    public static void setStatus(String status) {
        statusProperty.setValue(status);
    }
    public static void clearStatus() {
        statusProperty.setValue("");
    }

    // For monitoring

    // - Type of monitoring
    @Getter
    private static ObjectProperty<CommunicationType> typeProperty = new SimpleObjectProperty<>(CommunicationType.INCOMING);

    // - monitoring message
    @Getter
    private static StringProperty messageProperty = new SimpleStringProperty("");

    public static void monitorInfo (String message) {
        typeProperty.setValue(CommunicationType.INFO);
        messageProperty.set(message);
    }

    public static void monitorWarning (String message) {
        typeProperty.setValue(CommunicationType.WARNING);
        messageProperty.set(message);
    }

    public static void monitorError (String message) {
        typeProperty.setValue(CommunicationType.ERROR);
        messageProperty.set(message);
    }

    public static void monitorOutbound (String message) {
        typeProperty.setValue(CommunicationType.OUTGOING);
        messageProperty.set(message);
    }

    public static void monitorInbound (String message) {
        typeProperty.setValue(CommunicationType.INCOMING);
        messageProperty.set(message);
    }

    public static void monitorSongSelect (String message) {
        typeProperty.setValue(CommunicationType.SONG_SELECT);
        messageProperty.set(message);
    }
}

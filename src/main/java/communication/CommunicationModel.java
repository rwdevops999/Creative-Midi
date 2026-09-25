package communication;

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
}

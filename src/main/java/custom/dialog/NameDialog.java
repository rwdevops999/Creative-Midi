package custom.dialog;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class NameDialog extends Dialog<String> {
    public StringProperty nameProperty = new SimpleStringProperty();

    public NameDialog() {
        super();

        setTitle("Enter sysex name");

        ButtonType useButtonType = new ButtonType("Use", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(useButtonType, ButtonType.CANCEL);

        HBox content = new HBox();
        content.setAlignment(Pos.CENTER);
        content.setSpacing(10);

        Label sysexNameLabel = new Label("Sysex Name");
        content.getChildren().add(sysexNameLabel);

        TextField sysexNameTextField = new TextField();
        sysexNameTextField.setPromptText("Sysex name ...");
        sysexNameTextField.textProperty().bindBidirectional(nameProperty);
        content.getChildren().add(sysexNameTextField);

        getDialogPane().setContent(content);

        setResultConverter(dialogButton -> {
            if (dialogButton == useButtonType) {
                return nameProperty.get();
            }

            return null; // Geeft null terug bij annuleren
        });
    }

}

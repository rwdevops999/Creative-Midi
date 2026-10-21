package custom.components;

import javafx.beans.binding.Bindings;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SpeakerToggle extends ToggleButton {
    private final Image onImage = new Image(getClass().getResourceAsStream("/icons/speaker-on.png"));
    private final Image offImage = new Image(getClass().getResourceAsStream("/icons/speaker-off.png"));

    private static final int SIZE=12;

    public SpeakerToggle(boolean selected) {
        ImageView imageView = new ImageView();

        // Optional: Bind sizes to keep them consistent
        imageView.setFitWidth(SIZE);
        imageView.setFitHeight(SIZE);
        imageView.setPreserveRatio(true);

        // Dynamic binding: swaps image instantly based on selected state
        imageView.imageProperty().bind(Bindings
                .when(this.selectedProperty())
                .then(onImage)
                .otherwise(offImage)
        );

        // Apply graphic to the ToggleButton
        this.setGraphic(imageView);

        // Optional: Remove default button styling if you want ONLY the images to show
        this.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

        this.setSelected(selected);
    }

    public SpeakerToggle() {
        this(true);
    }
}

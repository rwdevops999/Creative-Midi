package creative.panes;

import creative.CreativeApp;
import creative.panes.header.HeaderPane;
import custom.dialog.AboutDialog;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.properties.PropertyContainer;
import util.properties.PropertyType;

import java.util.Objects;

public class MainPane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(MainPane.class);

    public MainPane() {
        super();

        setId("MainPane");
    }

    private RootPane parent;
    public MainPane(RootPane parent) {
        this();

        this.parent = parent;

        createPane();
    }

    private void createPane() {
        logger.debug("[CM_MAIN_PANE] building Pane {}", getId());

        Image image = new Image(Objects.requireNonNull(CreativeApp.class.getClassLoader().getResourceAsStream("backgrounds/background.png")));
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,  // Repeat X
                BackgroundRepeat.NO_REPEAT,  // Repeat Y
                BackgroundPosition.CENTER,   // Position
                new BackgroundSize(          // Size
                        BackgroundSize.AUTO,
                        BackgroundSize.AUTO,
                        false,
                        false,
                        true,                    // Contain (preserve aspect ratio)
                        false                    // Cover (fill the entire pane)
                )
        );
        setBackground(new Background(backgroundImage));


        if (PropertyContainer.getPropertyAsBoolean(PropertyType.System, PropertyContainer.SHOW_ABOUT_DIALOG, false)) {
            AboutDialog aboutDialog = new AboutDialog(PropertyContainer.getPropertyAsString(PropertyType.System, PropertyContainer.APP_VERSION, "0.0"));

            int seconds = PropertyContainer.getPropertyAsInteger(PropertyType.System, PropertyContainer.ABOUT_TIMING, 3);
            PauseTransition delay = new PauseTransition(Duration.seconds(seconds));

            // 3. Define the action when the timer finishes
            delay.setOnFinished(event -> {
                aboutDialog.getDialogPane().getScene().getWindow().hide();
                continueApplication();
            });

            // 4. Start the timer
            delay.play();

            // 5. Display the dialog and wait
            aboutDialog.showAndWait();
        } else {
            continueApplication();
        }

        logger.debug("[CM_MAIN_PANE] built Pane {}", getId());
    }

    private void continueApplication() {
        // Set Header
        setTop(new HeaderPane());

        // Set SPA

        // Set Footer

    }


        // ACCESSORS
    public RootPane getRootPane() {
        return parent;
    }

    public HeaderPane getHeaderPane() {
        return (HeaderPane)getTop();
    }
}

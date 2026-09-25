package creative.panes;

import creative.CreativeApp;
import creative.panes.footer.FooterPane;
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

import static util.ColorScheme.getColor;

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

        BackgroundFill backgroundColor = new BackgroundFill(getColor(), CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY);

        Image image = new Image(Objects.requireNonNull(CreativeApp.class.getClassLoader().getResourceAsStream("backgrounds/background.png")));

        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,   // Niet herhalen
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,    // Centreren
                new BackgroundSize(
                        BackgroundSize.AUTO,
                        BackgroundSize.AUTO,
                        false,
                        false,
                        true,
                        false
                )
        );
        setBackground(new Background(new BackgroundFill[]{backgroundColor}, new BackgroundImage[]{backgroundImage}));

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
        setBottom(new FooterPane());
    }


        // ACCESSORS
    public RootPane getRootPane() {
        return parent;
    }

    public HeaderPane getHeaderPane() {
        return (HeaderPane)getTop();
    }
    public FooterPane getFooterPane() {
        return (FooterPane)getBottom();
    }
}

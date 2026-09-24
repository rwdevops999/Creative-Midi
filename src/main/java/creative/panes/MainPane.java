package creative.panes;

import creative.CreativeApp;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        logger.debug("[CM_MAIN_PANE] built Pane {}", getId());
    }

    // ACCESSORS
    public RootPane getRootPane() {
        return parent;
    }
}

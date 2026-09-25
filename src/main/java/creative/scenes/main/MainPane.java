package creative.scenes.main;

import creative.scenes.IScene;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainPane extends StackPane {
    private static final Logger logger = LoggerFactory.getLogger(MainPane.class);

    public MainPane() {
        super();
    }

    public MainPane(IScene ownerScene) {
        this();

        setId("MainPane");

        logger.debug("[CM_MAIN_PANE] Building {}", getId());

        getChildren().add(new MainActionsPane(ownerScene));

        logger.debug("[CM_MAIN_PANE] Built {}", getId());
    }
}

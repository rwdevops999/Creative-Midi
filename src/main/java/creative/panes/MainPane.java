package creative.panes;

import creative.CreativeApp;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        logger.debug("[CM_MAIN_PANE] built Pane {}", getId());
    }
}

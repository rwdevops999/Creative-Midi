package creative.panes;

import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.border.Border;

import static util.constants.*;

public class RootPane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(RootPane.class);

    public RootPane() {
        setId("RootPane");

        setPrefSize(APP_WIDTH, APP_HEIGHT+MONITOR_HEIGHT);

        createPane();
    }

    private void createPane() {
        logger.debug("[CM_ROOT_PANE] building Pane {}", getId());

        setCenter(new MainPane(this));
//        setBottom(new MonitorPane(rootScenePane));

        logger.debug("[CM_ROOT_PANE] built Pane {}", getId());
    }

    // ACCESSORS
    public MainPane getMainPane() {
        return (MainPane)getCenter();
    }
}

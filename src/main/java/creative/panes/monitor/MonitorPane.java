package creative.panes.monitor;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.Util.setPaneHeightAsPercentage;

public class MonitorPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(MonitorPane.class);

    public MonitorPane() {
        super();

        setId("MonitorPane");
    }

    public MonitorPane(Pane owner) {
        this();

        buildPane(owner);
    }

    private void buildPane(Pane owner) {
        logger.debug("[CM_MONITOR_PANE] Building {}", getId());

        setPaneHeightAsPercentage(this, owner, 20);

        logger.debug("[CM_MONITOR_PANE] Built {}", getId());
    }
}

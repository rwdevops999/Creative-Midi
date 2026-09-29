package creative.scenes.sysex;

import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysexDetailsPane extends GridPane {
    private static final Logger logger = LoggerFactory.getLogger(SysexDetailsPane.class);

    public SysexDetailsPane() {
        super();

        setId("SysexDetailsPane");
    }

    private SysexPane parent;
    public SysexDetailsPane(SysexPane owner) {
        this();

        parent = owner;

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_SYSEX_DETAILS_PANE] Building {}", getId());

        logger.debug("[CM_SYSEX_DETAILS_PANE] Built {}", getId());
    }

    // ACCESSORS
    public SysexPane getSysexPane() {
        return parent;
    }
}

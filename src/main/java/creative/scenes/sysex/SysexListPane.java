package creative.scenes.sysex;

import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysexListPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(SysexListPane.class);

    public SysexListPane() {
        super();

        setId("SysexListPane");
    }

    private SysexPane parent;
    public SysexListPane(SysexPane owner) {
        this();

        parent = owner;

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_SYSEX_LIST_PANE] Building {}", getId());

        logger.debug("[CM_SYSEX_LIST_PANE] Built {}", getId());
    }

    // ACCESSORS
    public SysexPane getSysexPane() {
        return parent;
    }
}

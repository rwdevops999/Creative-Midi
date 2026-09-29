package creative.scenes.sysex;

import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;

public class SysexPane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(SysexPane.class);

    public SysexPane() {
        super();

        setId("SysexPane");

        showPaneBorder(this, getColor("border", "red", null));

        buildPane();
    }

    public void buildPane() {
        logger.debug("[CM_SYSEX_PANE] Building {}", getId());

        logger.debug("[CM_SYSEX_PANE] Built {}", getId());
    }
}

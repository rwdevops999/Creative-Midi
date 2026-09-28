package creative.scenes.sysex;

import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Util;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;
import static util.Util.setPaneHeightAsPercentage;

public class SysexResultPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(SysexResultPane.class);

    public SysexResultPane() {
        super();

        setId("SysexResultPane");
    }

    private SysexPane parent;
    public SysexResultPane(SysexPane owner) {
        this();

        this.parent = owner;

//        showPaneBorder(this, getColor("border", "orange", null));
        setPaneHeightAsPercentage(this, owner, 21);
        setPaneBackground(this);

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_SYSEX_RESULT_PANE] Building {}", getId());

        logger.debug("[CM_SYSEX_RESULT_PANE] Built {}", getId());
    }

    // ACCESSORS
    public SysexPane getSysexPane() {
        return parent;
    }
}

package creative.scenes.sysex;

import communication.CommunicationModel;
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

//        showPaneBorder(this, getColor("border", "red"));
        SysexGenHelper.setup();

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_SYSEX_PANE] Building {}", getId());

        CommunicationModel.setStatus("Defining Sysex");

        this.setTop(new SysexSelectionPane(this));
        this.setCenter(new SysexParametersPane(this));
        this.setBottom(new SysexResultPane(this));

        logger.debug("[CM_SYSEX_PANE] Built {}", getId());
    }

    // ACCESSORS
    public SysexSelectionPane getSysexSelectionPane() {
        return (SysexSelectionPane) this.getTop();
    }

    public SysexParametersPane getSysexParametersPane() {
        return (SysexParametersPane) this.getCenter();
    }

    public SysexResultPane getSysexResultPane() {
        return (SysexResultPane) this.getBottom();
    }
}

package creative.scenes.sysexgen2;

import communication.CommunicationModel;
import creative.scenes.sysexgen2.definition.SysexGen2Definitions;
import creative.scenes.sysexgen2.entity.Event;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;

public class SysexGen2Pane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(SysexGen2Pane.class);

    private Event rootEvent = null;

    public SysexGen2Pane() {
        super();

        setId("SysexGenPane");

//        showPaneBorder(this, getColor("border", "red"));
        setPaneBackground(this);

        rootEvent = SysexGen2Definitions.getRootEvent();
        if (! rootEvent.hasChildren()) {
            rootEvent = SysexGen2Definitions.setup();
        }

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_SYSEX_GEN2_PANE] Building {}", getId());

        CommunicationModel.setStatus("Starting up the Sysex Generator");

        this.setTop(new SysexGenSelectionPane(this, rootEvent));
        this.setCenter(new SysexGenParametersPane(this));
        this.setBottom(new SysexGenResultPane(this));

        logger.debug("[CM_SYSEX_GEN2_PANE] Built {}", getId());
    }

    // ACCESSORS
    public SysexGenSelectionPane getSysexGenSelectionPane() {
        return (SysexGenSelectionPane) this.getTop();
    }

   public SysexGenParametersPane getSysexGenParametersPane() {
        return (SysexGenParametersPane) this.getCenter();
    }

    public SysexGenResultPane getSysexGenResultPane() {
        return (SysexGenResultPane) this.getBottom();
    }
}

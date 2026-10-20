package creative.scenes.sysexgen;

import communication.CommunicationModel;
import creative.scenes.sysexgen.definition.SysexGenDefinitions;
import creative.scenes.sysexgen.entity.Event;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;

public class SysexGenPane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(SysexGenPane.class);

    private Event rootEvent = null;

    public SysexGenPane() {
        super();

        setId("SysexGenPane");

        showPaneBorder(this, getColor("border", "red"));
        setPaneBackground(this);

        rootEvent = SysexGenDefinitions.getRootEvent();
        if (! rootEvent.hasChildren()) {
            rootEvent = SysexGenDefinitions.setup();
        }

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_SYSEX_GEN_PANE] Building {}", getId());

        CommunicationModel.setStatus("Starting up the Sysex Generator");

        this.setTop(new SysexGenSelectionPane(this, rootEvent));
        this.setCenter(new SysexGenParametersPane(this));
        this.setBottom(new SysexGenResultPane(this));

        logger.debug("[CM_SYSEX_GEN_PANE] Built {}", getId());
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

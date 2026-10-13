package creative.scenes.sysexgen2;

import creative.scenes.sysexgen2.definition.SysexGen2Definitions;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;

public class SysexGen2Pane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(SysexGen2Pane.class);

    public SysexGen2Pane() {
        super();

        setId("SysexGenPane");

        showPaneBorder(this, getColor("border", "red"));
        setPaneBackground(this);

        SysexGen2Definitions.setup();

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_SYSEX_GEN2_PANE] Building {}", getId());

//        CommunicationModel.setStatus("Defining Sysex Generator");

//        this.setTop(new SysexGenSelectionPane(this));
//        this.setCenter(new SysexGenParametersPane(this));
//        this.setBottom(new SysexGenResultPane(this));

        logger.debug("[CM_SYSEX_GEN2_PANE] Built {}", getId());
    }

    // ACCESSORS
/*    public SysexGenSelectionPane getSysexGenSelectionPane() {
        return (SysexGenSelectionPane) this.getTop();
    }

   public SysexGenParametersPane getSysexGenParametersPane() {
        return (SysexGenParametersPane) this.getCenter();
    }

    public SysexGenResultPane getSysexGenResultPane() {
        return (SysexGenResultPane) this.getBottom();
    }
    */
}

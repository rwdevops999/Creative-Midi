package creative.scenes.sysex;

import communication.CommunicationModel;
import creative.scenes.main.MainActionsPane;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.border.Border;

public class SysexPane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(SysexPane.class);

    public SysexPane() {
        super();

        setId("SysexPane");

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_SYSEX_PANE] Building {}", getId());

        CommunicationModel.setStatus("Defining Sysex");

        logger.debug("[CM_SYSEX_PANE] Built {}", getId());
    }
}

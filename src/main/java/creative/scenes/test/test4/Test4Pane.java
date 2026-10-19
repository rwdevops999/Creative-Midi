package creative.scenes.test.test4;

import communication.CommunicationModel;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test4Pane extends HBox {
    private static final Logger logger = LoggerFactory.getLogger(Test4Pane.class);
    public Test4Pane() {
        super();

        setId("Test4");
        CommunicationModel.setStatus("Running Test4");

        setupTest();
        runTest();
    }

    private int Volume;
    private int Msb;
    private int Lsb;

    private void setupTest() {
        logger.debug("[CM_TEST4_PANE] Setting up {}", getId());

        Msb = 127;
        Lsb = 127;

        logger.debug("[CM_TEST4_PANE] Set up {}", getId());
    }

    private void runTest() {
        logger.debug("[CM_TEST4_PANE] Executing {}", getId());

        Volume = 15221;

        int nMsb = 0;
        int nLsb = 0;
        nMsb = Volume >> 7;
        nLsb = Volume & 0x7F;

        logger.debug("[CM_TEST4_PANE] Executed {}", getId());
    }
}

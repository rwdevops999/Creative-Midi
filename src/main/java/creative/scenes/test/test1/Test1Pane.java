package creative.scenes.test.test1;

import communication.CommunicationModel;
import creative.scenes.sysexgen2.entity.DataBlock;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.ColorScheme.getColor;
import static util.Constants.BIG_ENDIAN;
import static util.Constants.LITTLE_ENDIAN;

public class Test1Pane extends GridPane {
    private static final Logger logger = LoggerFactory.getLogger(Test1Pane.class);
    public Test1Pane() {
        super();

        setId("Test1");
        CommunicationModel.setStatus("Running Test1");

        setupTest();
        runTest();
    }

    private byte[] data;
    private void setupTest() {
    }

    private void runTest() {
        logger.debug("[CM_TEST1_PANE] Executing {}", getId());

        logger.debug("[CM_TEST1_PANE] Executed {}", getId());
    }
}

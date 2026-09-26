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

        runTest();
    }

    private void runTest() {
        logger.debug("[CM_TEST1_PANE] Executing {}", getId());

        logger.debug("[CM_TEST1_PANE] Executed {}", getId());
    }
}

package creative.scenes.test.test3;

import communication.CommunicationModel;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test3Pane extends HBox {
    private static final Logger logger = LoggerFactory.getLogger(Test3Pane.class);
    public Test3Pane() {
        super();

        setId("Test3");
        CommunicationModel.setStatus("Running Test3");

        runTest();
    }

    private void runTest() {
        logger.debug("[CM_TEST1_PANE] Executing {}", getId());

        logger.debug("[CM_TEST1_PANE] Executed {}", getId());
    }
}

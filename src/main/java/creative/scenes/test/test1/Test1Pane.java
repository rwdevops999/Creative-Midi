package creative.scenes.test.test1;

import communication.CommunicationModel;
import creative.scenes.test.TestScene;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test1Pane extends HBox {
    private static final Logger logger = LoggerFactory.getLogger(Test1Pane.class);
    public Test1Pane() {
        super();

        setId("Test1");
        CommunicationModel.setStatus("Running Test1");

        runTest();
    }

    private void runTest() {
        logger.debug("[CM_TEST1_PANE] Executing {}", getId());

        logger.debug("[CM_TEST1_PANE] Executed {}", getId());
    }
}

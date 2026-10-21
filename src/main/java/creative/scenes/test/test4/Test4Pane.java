package creative.scenes.test.test4;

import communication.CommunicationModel;
import custom.components.DiscreteSpinner;
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

    private void setupTest() {
        logger.debug("[CM_TEST4_PANE] Setting up {}", getId());

        logger.debug("[CM_TEST4_PANE] Set up {}", getId());
    }

    private void runTest() {
        logger.debug("[CM_TEST4_PANE] Executing {}", getId());

        DiscreteSpinner spinner = new DiscreteSpinner(0, 127,0);
        getChildren().add(spinner);

        logger.debug("[CM_TEST4_PANE] Executed {}", getId());
    }
}

package creative.scenes.test.test3;

import communication.CommunicationModel;
import creative.scenes.sysex.component.AddableSysexPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test3Pane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(Test3Pane.class);
    public Test3Pane() {
        super();

        setId("Test3");
        CommunicationModel.setStatus("Running Test3");

        runTest();
    }

    private StringProperty ds = new SimpleStringProperty("");

    private void runTest() {
        logger.debug("[CM_TEST1_PANE] Executing {}", getId());

        AddableSysexPane pane = new AddableSysexPane(this, 0, ds);
        getChildren().add(pane);

        logger.debug("[CM_TEST1_PANE] Executed {}", getId());
    }
}

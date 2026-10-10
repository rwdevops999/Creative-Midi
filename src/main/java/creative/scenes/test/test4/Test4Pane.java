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

    private int Bank;
    private int Msb;
    private int Lsb;

    private void setupTest() {
        logger.debug("[CM_TEST4_PANE] Setting up {}", getId());

        Msb = 131;
        Lsb = 25;

        logger.debug("[CM_TEST4_PANE] Set up {}", getId());
    }

    private void runTest() {
        logger.debug("[CM_TEST4_PANE] Executing {}", getId());

        Bank = (Msb << 7) | Lsb;
        System.out.println("Bank = " + Bank);

        int nMsb = 0;
        int nLsb = 0;
        nMsb = Bank >> 7;
        nLsb = Bank & 0x7F;
        System.out.println("n MSB = " + nMsb);
        System.out.println("n LSB = " + nLsb);

        logger.debug("[CM_TEST4_PANE] Executed {}", getId());
    }
}

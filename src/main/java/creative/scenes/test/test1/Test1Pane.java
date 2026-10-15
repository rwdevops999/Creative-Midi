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

        System.out.println("TEST 1");
        DataBlock tdb = new DataBlock(0x12, 0X55);
        System.out.println(tdb.toHex(2));

        System.out.println("TEST 2");
        tdb = new DataBlock(0x12, 0X55, true);
        System.out.println(tdb.toHex(2));

        System.out.println("TEST 3");
        tdb = new DataBlock(0x12, 0X55, false);
        System.out.println(tdb.toHex(2));

        System.out.println("TEST 4 (LITTLE)");
        tdb = new DataBlock(false);
        tdb.setValue(12345);
        System.out.println(tdb.toHex(2));

        System.out.println("TEST 5 (BIG)");
        tdb = new DataBlock(true);
        tdb.setValue(12345);
        System.out.println(tdb.toHex(2));

        logger.debug("[CM_TEST1_PANE] Executed {}", getId());
    }
}

package creative.scenes.test.test1;

import communication.CommunicationModel;
import creative.scenes.eventlist.parser.processor.helper.ByteHelper;
import creative.scenes.sysex.convertor.SysexToHexStringConvertor;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.ColorScheme.getColor;
import static util.Util.setPaneBackground;

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
        data = new byte[]{ -16, 126, 127, 9, 1, -9 };
    }

    private void runTest() {
        logger.debug("[CM_TEST1_PANE] Executing {}", getId());

        setPaneBackground(this);

        StringBuilder sysex = SysexToHexStringConvertor.convertToHexStringbuilder(data);
        System.out.println("Sysex: " + sysex);


        String xxx = ByteHelper.getAndStrip(sysex, 1);
        sysex = SysexToHexStringConvertor.convertToHexStringbuilder(data);
        String xxx2 = ByteHelper.getAndStrip(sysex, 2);
        sysex = SysexToHexStringConvertor.convertToHexStringbuilder(data);
        String xxx3 = ByteHelper.getAndStrip(sysex, 3);

        sysex = SysexToHexStringConvertor.convertToHexStringbuilder(data);
        String xxxm = ByteHelper.getAndStrip(sysex, 6);
        sysex = SysexToHexStringConvertor.convertToHexStringbuilder(data);
        String xxxn = ByteHelper.getAndStrip(sysex, 10);

        logger.debug("[CM_TEST1_PANE] Executed {}", getId());
    }
}

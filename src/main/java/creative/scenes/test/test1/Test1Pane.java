package creative.scenes.test.test1;

import communication.CommunicationModel;
import creative.scenes.eventlist.dialog.ChannelSelectionDialog;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class Test1Pane extends GridPane {
    private static final Logger logger = LoggerFactory.getLogger(Test1Pane.class);
    public Test1Pane() {
        super();

        setId("Test1");
        CommunicationModel.setStatus("Running Test1");

        setupTest();
        runTest();
    }

    private static char input;

    private static final char CHANNEL_1 = 1;
    private static final char CHANNEL_2 = 1 << 1;
    private static final char CHANNEL_3 = 1 << 2;
    private static final char CHANNEL_4 = 1 << 3;
    private static final char CHANNEL_5 = 1 << 4;
    private static final char CHANNEL_6 = 1 << 5;
    private static final char CHANNEL_7 = 1 << 6;
    private static final char CHANNEL_8 = 1 << 7;
    private static final char CHANNEL_9 = 1 << 8;
    private static final char CHANNEL_10 = 1 << 9;
    private static final char CHANNEL_11 = 1 << 10;
    private static final char CHANNEL_12 = 1 << 11;
    private static final char CHANNEL_13 = 1 << 12;
    private static final char CHANNEL_14 = 1 << 13;
    private static final char CHANNEL_15 = 1 << 14;
    private static final char CHANNEL_16 = 1 << 15;

    private void setupTest() {
        input = CHANNEL_2 | CHANNEL_5 | CHANNEL_7 | CHANNEL_10 | CHANNEL_15;
    }

    private void runTest() {
        logger.debug("[CM_TEST1_PANE] Executing {}", getId());

        ChannelSelectionDialog channelDialog = new ChannelSelectionDialog(input);
        Optional<Character> resultingChannel = channelDialog.showAndWait();

        logger.debug("[CM_TEST1_PANE] Executed {}", getId());
    }
}

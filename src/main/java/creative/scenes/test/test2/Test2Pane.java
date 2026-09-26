package creative.scenes.test.test2;

import communication.CommunicationModel;
import entity.midi.NoteEntity;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.Util.calcNote;

public class Test2Pane extends HBox {
    private static final Logger logger = LoggerFactory.getLogger(Test2Pane.class);
    public Test2Pane() {
        super();

        setId("Test2");
        CommunicationModel.setStatus("Running Test2");

        runTest();
    }

    private void runTest() {
        logger.debug("[CM_TEST1_PANE] Executing {}", getId());

        int value = 60;
        int baseOctave = -1;
        NoteEntity noteEntity = calcNote(value, baseOctave);
        System.out.println("SELECTED NOTE = " + noteEntity.getName());

        logger.debug("[CM_TEST1_PANE] Executed {}", getId());
    }
}

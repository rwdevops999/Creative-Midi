package creative.scenes.test.test3;

import communication.CommunicationModel;
import creative.scenes.sysexgen2.definition.SysexGen2Definitions;
import creative.scenes.sysexgen2.entity.Event;
import creative.scenes.sysexgen2.entity.EventParameter;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static util.Util.setPaneBackground;

public class Test3Pane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(Test3Pane.class);
    public Test3Pane() {
        super();

        setId("Test3");
        CommunicationModel.setStatus("Running Test3");

        setupTest();
        runTest();
    }

    private void setEventValue(String eventName, String parameterName, Integer value, Integer b1, Integer b2) {
        Event event = SysexGen2Definitions.getRootEvent().findEvent(eventName);
        if (event != null) {
            EventParameter eventParameter = event.findEventParameterByName(parameterName);
            if (eventParameter != null) {
                if (value != null) {
                    eventParameter.setValue(value); // 51730 = CA 12 or 12 CA
                } else {
                    eventParameter.setValue(b1, b2);
                }
            }
        }

    }

    private void setupTest() {
        logger.debug("[CM_TEST1_PANE] Setting up {}", getId());

        setPaneBackground(this);

        SysexGen2Definitions.setup();

        logger.debug("[CM_TEST1_PANE] Set up {}", getId());
    }

    private void handleEvent (String name) {
        Event rootEvent = SysexGen2Definitions.getRootEvent();
        Event event = rootEvent.findEvent(name);

        if (event != null) {
            List<String> sysexs = event.complete(event.createSysexStrings());

            System.out.println("SYSEXS OF: " + name);
            sysexs.forEach(System.out::println);
        } else {
            System.out.println("EVENT:" + name + " NOT FOUND");
        }
    }

    private void runTest() {
        logger.debug("[CM_TEST1_PANE] Executing {}", getId());

//        handleEvent("GM1 System On");
//        handleEvent("GM2 System On");
//        handleEvent("General MIDI System Off");
//        handleEvent("Internal Clock");
//        handleEvent("External Clock");

//        setEventValue("Master Volume", "Parameter 1", 51730, null, null);
//        handleEvent("Master Volume");

//        setEventValue("Master Fine Tuning", "Parameter 1", 51730, null, null);
//        handleEvent("Master Fine Tuning");

//        setEventValue("Master Coarse Tuning", "Parameter 1", null, 0, 123);
//        handleEvent("Master Coarse Tuning");

        final int START=0;
        final int STOP=1;
        final int PAUSE =2;

        setEventValue("Audio Song Control", "Parameter 1", null, 0, START);
        handleEvent("Audio Song Control");
        setEventValue("Audio Song Control", "Parameter 1", null, 0, STOP);
        handleEvent("Audio Song Control");
        setEventValue("Audio Song Control", "Parameter 1", null, 0, PAUSE);
        handleEvent("Audio Song Control");

        logger.debug("[CM_TEST1_PANE] Executed {}", getId());
    }
}

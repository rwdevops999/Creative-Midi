package creative.scenes.test.test3;

import communication.CommunicationModel;
import creative.scenes.sysexgen2.data.ParameterType;
import creative.scenes.sysexgen2.definition.SysexGen2Definitions;
import creative.scenes.sysexgen2.entity.Event;
import creative.scenes.sysexgen2.entity.EventParameter;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static util.Constants.BIG_ENDIAN;
import static util.Constants.LITTLE_ENDIAN;
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


    private void setEventValue(String eventName, ParameterType parameterType, Object identifier, Integer ival, Integer b1val, Integer b2val) {
        boolean isLittleEndian = true;
        Event event = SysexGen2Definitions.getRootEvent().findEvent(eventName);
        if (event != null) {
            EventParameter eventParameter;
            if (parameterType == ParameterType.PARAMETER) {
                eventParameter = event.findEventParameterById((Integer)identifier);
            } else {
                isLittleEndian = false;
                eventParameter = event.findEventParameterByLabel((String)identifier);
            }

            if (eventParameter != null) {
                if (ival == null && b1val == null && b2val == null) {
                    eventParameter.clearValue();
                } else {
                    if (ival != null) {
                        eventParameter.setValue(ival, isLittleEndian);
                    } else {
                        eventParameter.setValue(b1val, b2val, isLittleEndian);
                    }
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

    private int normalize_minus_63(int value) {
        return value + 63;
    }

    private void handleEvent (String name, Integer b1, Integer b2 ) {
        Event rootEvent = SysexGen2Definitions.getRootEvent();

        Event event = rootEvent.findEvent(name);
        if (! (b1 == null && b2 == null)) {
            event.setDefaultValues(b1, b2);
        }

        if (event != null) {
            List<String> sysexs = event.complete(new ArrayList<>(event.createSysexStrings()));

            System.out.println("SYSEXS OF: " + name);
            sysexs.forEach(System.out::println);
        } else {
            System.out.println("EVENT:" + name + " NOT FOUND");
        }
    }

    private void runTest() {
        logger.debug("[CM_TEST1_PANE] Executing {}", getId());

        final int OFF = 0x00;
        final int ON = 0x7F;

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

//        setEventValue("Audio Song Control", "Parameter 1", null, 0, START);
//        handleEvent("Audio Song Control");
//        setEventValue("Audio Song Control", "Parameter 1", null, 0, STOP);
//        handleEvent("Audio Song Control");
//        setEventValue("Audio Song Control", "Parameter 1", null, 0, PAUSE);
//        handleEvent("Audio Song Control");

        final int INTRO_1 = 0x00;
        final int INTRO_2 = 0x01;
        final int INTRO_3 = 0x02;
        final int INTRO_4 = 0x03;

        final int MAIN_A = 0x08;
        final int MAIN_B = 0x09;
        final int MAIN_C = 0x0A;
        final int MAIN_D = 0x0B;

        final int FILL_IN_AA = 0x10;
        final int FILL_IN_BB = 0x11;
        final int FILL_IN_CC = 0x12;
        final int FILL_IN_DD = 0x13;

        final int BREAK_FILL = 0x18;

        final int ENDING_1 = 0x20;
        final int ENDING_2 = 0x21;
        final int ENDING_3 = 0x22;
        final int ENDING_4 = 0x23;

//        setEventValue("Section Control", "Parameter 1", null, 0, INTRO_1);
//        setEventValue("Section Control", "Parameter 2", null, 0, ON);
//        handleEvent("Section Control");
//        setEventValue("Section Control", "Parameter 1", null, 0, MAIN_B);
//        setEventValue("Section Control", "Parameter 2", null, 0, OFF);
//        handleEvent("Section Control");

        final int MAIN=0x00;
        final int SUB=0x01;

        final int LEFT=0x4F;
        final int STYLE=0x50;
        final int MULTI_PAD=0x51;
        final int RIGHT1=0x52;
        final int RIGHT2=0x53;

//        setEventValue("Scale Tuning Part", "Parameter 1", null, 0, MAIN);
//        setEventValue("Scale Tuning Part", "Parameter 2", null, 0, LEFT);
//        setEventValue("Scale Tuning Part", "Parameter 3", null, 0, ON);
//        handleEvent("Scale Tuning Part");
//        setEventValue("Scale Tuning Part", "Parameter 1", null, 0, SUB);
//        setEventValue("Scale Tuning Part", "Parameter 2", null, 0, MULTI_PAD);
//        setEventValue("Scale Tuning Part", "Parameter 3", null, 0, OFF);
//        handleEvent("Scale Tuning Part");

        final int REG_1=0;
        final int REG_2=1;
        final int REG_3=2;
        final int REG_4=3;
        final int REG_5=4;
        final int REG_6=5;
        final int REG_7=6;
        final int REG_8=7;

//        setEventValue("Registration", "Parameter 1", null, 0, REG_3);
//        handleEvent("Registration");
//        setEventValue("Registration", "Parameter 1", null, 0, REG_7);
//        handleEvent("Registration");

        // TODO Test From Here
        final int DUET_1=0;
        final int BLOCK=8;
        final int OCTAVE=17;

//        setEventValue("Harmony", "Parameter 1", null, 0, DUET_1);
//        handleEvent("Harmony");
//        setEventValue("Harmony", "Parameter 1", null, 0, BLOCK);
//        handleEvent("Harmony");
//        setEventValue("Harmony", "Parameter 1", null, 0, OCTAVE);
//        handleEvent("Harmony");

        final int ECHO_6=21;

//        setEventValue("Echo", "Parameter 1", null, 0, ECHO_6);
//        handleEvent("Echo");

        final int TREMOLO_32=27;

//        setEventValue("Tremolo", "Parameter 1", null, 0, TREMOLO_32);
//        handleEvent("Tremolo");

        final int TRILL_12=28;

//        setEventValue("Trill", "Parameter 1", null, 0, TRILL_12);
//        handleEvent("Trill");

        final int TRACK1_OFF_TRACK2_ON = 1;
        final int KARAOKE_KEY=3;

//        setEventValue("Guide", "Parameter 1", null, 0, TRACK1_OFF_TRACK2_ON);
//        setEventValue("Guide", "Parameter 2", null, 0, KARAOKE_KEY);
//        handleEvent("Guide");

//        setEventValue("MIDI Master Tuning", "Parameter 1", null, 0, 123);
//        setEventValue("MIDI Master Tuning", "Parameter 2", null, 0, 98);
//        setEventValue("MIDI Master Tuning", "Parameter 3", null, 0, 100);
//        handleEvent("MIDI Master Tuning");

//        setEventValue("Scale Tuning", "Parameter 1", SUB, null, null);
////        setEventValue("Scale Tuning", "Parameter 2", -40, null, null); IS FIXED
//        setEventValue("Scale Tuning", "Parameter 3", normalize_minus_63(-35), null, null);
//        setEventValue("Scale Tuning", "Parameter 4", normalize_minus_63(-30), null, null);
//        setEventValue("Scale Tuning", "Parameter 5", normalize_minus_63(-25), null, null);
//        setEventValue("Scale Tuning", "Parameter 6", normalize_minus_63(-20), null, null);
//        setEventValue("Scale Tuning", "Parameter 7", normalize_minus_63(-15), null, null);
//        setEventValue("Scale Tuning", "Parameter 8", normalize_minus_63(-10), null, null);
//        setEventValue("Scale Tuning", "Parameter 9", normalize_minus_63(0), null, null);
//        setEventValue("Scale Tuning", "Parameter 10", normalize_minus_63( 10), null, null);
//        setEventValue("Scale Tuning", "Parameter 11", normalize_minus_63(15), null, null);
//        setEventValue("Scale Tuning", "Parameter 12", normalize_minus_63(20), null, null);
//        setEventValue("Scale Tuning", "Parameter 13", normalize_minus_63(30), null, null);
//        setEventValue("Scale Tuning", "Parameter 14", normalize_minus_63(40), null, null);
//        handleEvent("Scale Tuning");

        final int BASE_NOTE_D=2;
        final int BASE_NOTE_MODE_ONLY_BASE_NOTE=0;

//        setEventValue("Scale Tuning Base Note", "Parameter 1", null, 0, BASE_NOTE_D);
//        setEventValue("Scale Tuning Base Note", "Parameter 2", null, 0, BASE_NOTE_MODE_ONLY_BASE_NOTE);
//        handleEvent("Scale Tuning Base Note");

        final int BASE_NOTE_G_SHARP=9;
        final int BASE_NOTE_MODE_BASE_AND_SCALE=1;

 //       setEventValue("Scale Tuning Base Note", "Parameter 1", BASE_NOTE_G_SHARP, 0, null);
 //       setEventValue("Scale Tuning Base Note", "Parameter 2", BASE_NOTE_MODE_BASE_AND_SCALE, 0, null);
 //       handleEvent("Scale Tuning Base Note");

        final int SMALL=0x00;
        final int LARGE=0x01;

        final int CHANNEL_3=2;
        final int CHANNEL_AUTO=127;

        final int QUARTER=0;
        final int EIGHTH=1;
        final int SIXTEENTH=2;
        final int THIRTY_SECOND=3;

        final int FIXED_DO=1;

//        setEventValue("Score", "Parameter 1", ON, null, null);
//        setEventValue("Score", "Parameter 2", OFF, null, null);
//        setEventValue("Score", "Parameter 3", ON, null, null);
//        setEventValue("Score", "Parameter 6", LARGE, null, null);
//        setEventValue("Score", "Parameter 7", CHANNEL_3, null, null);
//        setEventValue("Score", "Parameter 8", CHANNEL_AUTO, null, null);
//        setEventValue("Score", "Parameter 9", ON, null, null);
//        setEventValue("Score", "Parameter 10", THIRTY_SECOND, null, null);
//        setEventValue("Score", "Parameter 11", FIXED_DO, null, null);
//        handleEvent("Score");

//        setEventValue("Reverb", ParameterType.PARAMETER, 1,10, null, null); // #1
//        setEventValue("Reverb", ParameterType.PARAMETER, 2, 3, null, null);
//        setEventValue("Reverb", ParameterType.PARAMETER, 3, 100, null, null); // #2
//        setEventValue("Reverb", ParameterType.PARAMETER, 4, 30, null, null); // #3
//        setEventValue("Reverb", ParameterType.PARAMETER, 5, 47, null, null); // #3
//        setEventValue("Reverb", ParameterType.PARAMETER, 10, 21, null, null);
//        setEventValue("Reverb", ParameterType.PARAMETER, 11, 90, null, null); // #2
//        setEventValue("Reverb", ParameterType.PARAMETER, 12, 2, null, null);
//        setEventValue("Reverb", ParameterType.PARAMETER, 13, 63, null, null);
//        setEventValue("Reverb", ParameterType.PARAMETER, 14, 5, null, null);
//        setEventValue("Reverb", ParameterType.PARAMETER, 15, 13, null, null);

//        setEventValue("Reverb", ParameterType.FREE, "0C", 13, null, null);
//        setEventValue("Reverb", ParameterType.FREE, "0D", 13, null, null);

//        handleEvent("Reverb", 1, 0); // (1,0) = Hall1

//        setEventValue("Split Point", ParameterType.FREE, "Split point", 93, null, null);
//        handleEvent("Split Point", null, null);

//        setEventValue("Select Style", ParameterType.FREE, "Style", 12345, null, null);
//        handleEvent("Select Style", null, null);

//        setEventValue("Tempo", ParameterType.FREE, "Tempo", 5, null, null);
//        handleEvent("Tempo", null, null);
//        setEventValue("Tempo", ParameterType.FREE, "Tempo", 112, null, null);
//        handleEvent("Tempo", null, null);


        setEventValue("Scale/Octave Tuning", ParameterType.FREE, "Channel", 16, null, null);
        setEventValue("Scale/Octave Tuning", ParameterType.FREE, "Tune E", null, 0x0E, null);
        setEventValue("Scale/Octave Tuning", ParameterType.FREE, "Tune B", null, 0x0E, null);
        handleEvent("Scale/Octave Tuning", null, null);

        logger.debug("[CM_TEST1_PANE] Executed {}", getId());
    }
}

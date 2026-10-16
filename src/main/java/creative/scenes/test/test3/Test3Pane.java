package creative.scenes.test.test3;

import communication.CommunicationModel;
import creative.scenes.sysexgen2.data.ParameterType;
import creative.scenes.sysexgen2.definition.SysexGen2Definitions;
import creative.scenes.sysexgen2.entity.Event;
import creative.scenes.sysexgen2.entity.EventParameter;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

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

    private EventParameter setEventValue(String eventName, String eventParameterName, Integer ival, Integer b1val, Integer b2val) {
        EventParameter eventParameter = null;
        Event event = SysexGen2Definitions.getRootEvent().findEvent(eventName);
        if (event != null) {
            eventParameter = event.findEventParameterByName(eventParameterName);

            if (eventParameter != null) {
                if (ival == null && b1val == null && b2val == null) {
                    eventParameter.clearValue();
                } else {
                    if (ival != null) {
                        eventParameter.setValue(ival);
                    } else {
                        eventParameter.setValue(b1val, b2val);
                    }
                }
            }
        }

        return eventParameter;
    }

    private void setLinkedEventValue(String eventName, String eventParameterName, String linkedEventName, Integer ival, Integer b1val, Integer b2val) {
        Event event = SysexGen2Definitions.getRootEvent().findEvent(eventName);
        if (event != null) {
            EventParameter eventParameter = event.findEventParameterByName(eventParameterName);

            if (eventParameter != null) {
                EventParameter linkedEventParameter = eventParameter.findLinkedEventParameterByName(linkedEventName);

                if (linkedEventParameter != null) {
                    if (ival == null && b1val == null && b2val == null) {
                        linkedEventParameter.clearValue();
                    } else {
                        if (ival != null) {
                            linkedEventParameter.setValue(ival);
                        } else {
                            linkedEventParameter.setValue(b1val, b2val);
                        }
                    }
                }
            } else {
                System.out.println("WE SHOULDN'T COME HERE");
            }
        }
    }

    private void setEventValue(EventParameter parentParameter, String linkedEventName, Integer ival, Integer b1val, Integer b2val) {
        if (parentParameter != null) {
            EventParameter linkedEventParameter = parentParameter.findLinkedEventParameterByName(linkedEventName);
            if (linkedEventParameter != null) {
                if (ival != null) {
                    linkedEventParameter.setValue(ival);
                } else {
                    linkedEventParameter.setValue(b1val, b2val);
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

    private void handleEvent (String name, Integer b1, Integer b2, List<String> results ) {
        Event rootEvent = SysexGen2Definitions.getRootEvent();

        Event event = rootEvent.findEvent(name);
        if (! (b1 == null && b2 == null)) {
            event.setDefaultValues(b1, b2);
        }

        if (event != null) {
            List<String> sysexs = event.complete(new ArrayList<>(event.createSysexStrings()));

            System.out.println("---=== " + name + " ===---");
            sysexs.forEach(System.out::println);

            for (String result: results) {
                assertThat(sysexs).contains(result);
            }
        } else {
            System.out.println("EVENT:" + name + " NOT FOUND");
        }
    }

    private void handleEvent (String name, List<String> result ) {
        handleEvent(name, null, null, result);
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

//        setEventValue("Master Volume", "Volume", 51730, null, null);
//        handleEvent("Master Volume", List.of("F0 7F 7F 04 01 12 CA F7"));

//        setEventValue("Master Fine-Tuning", "FineTuning", 51730, null, null);
//        handleEvent("Master Fine-Tuning", List.of("F0 7F 7F 04 03 12 CA F7"));

//        setEventValue("Master Coarse Tuning", "CoarseTuning", null, 0, 123);
//        handleEvent("Master Coarse Tuning", List.of("F0 7F 7F 04 04 00 7B F7"));

        final int START=0;
        final int STOP=1;
        final int PAUSE=2;

//        setEventValue("Audio Song Control", "Control", null, 0, START);
//        handleEvent("Audio Song Control", List.of("F0 43 73 01 50 19 00 00 00 F7"));
//        setEventValue("Audio Song Control", "Control", null, 0, STOP);
//        handleEvent("Audio Song Control", List.of("F0 43 73 01 50 19 00 00 01 F7"));
//        setEventValue("Audio Song Control", "Control", null, 0, PAUSE);
//        handleEvent("Audio Song Control", List.of("F0 43 73 01 50 19 00 00 02 F7"));

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

//        setEventValue("Section Control", "Section", null, 0, INTRO_1);
//        setEventValue("Section Control", "Switch", null, 0, ON);
//        handleEvent("Section Control", List.of("F0 43 7E 00 00 7F F7"));
//        setEventValue("Section Control", "Section", null, 0, MAIN_B);
//        setEventValue("Section Control", "Switch", null, 0, OFF);
//        handleEvent("Section Control", List.of("F0 43 7E 00 09 00 F7"));

        final int MAIN=0x00;
        final int SUB=0x01;

        final int LEFT=0x4F;
        final int STYLE=0x50;
        final int MULTI_PAD=0x51;
        final int RIGHT1=0x52;
        final int RIGHT2=0x53;

//        setEventValue("Scale Tuning Part", "Type", null, 0, MAIN);
//        setEventValue("Scale Tuning Part", "Parameter ID", null, 0, LEFT);
//        setEventValue("Scale Tuning Part", "Switch", null, 0, ON);
//        handleEvent("Scale Tuning Part", List.of("F0 43 73 01 52 2F 02 00 4F 7F F7"));

//        setEventValue("Scale Tuning Part", "Type", null, 0, SUB);
//        setEventValue("Scale Tuning Part", "Parameter ID", null, 0, MULTI_PAD);
//        setEventValue("Scale Tuning Part", "Switch", null, 0, OFF);
//        handleEvent("Scale Tuning Part", List.of("F0 43 73 01 52 2F 02 01 51 00 F7"));

        final int REG_1=0;
        final int REG_2=1;
        final int REG_3=2;
        final int REG_4=3;
        final int REG_5=4;
        final int REG_6=5;
        final int REG_7=6;
        final int REG_8=7;

//        setEventValue("Registration", "Number", null, 0, REG_3);
//        handleEvent("Registration", List.of("F0 43 73 01 52 25 11 00 02 00 02 F7"));
//        setEventValue("Registration", "Number", null, 0, REG_7);
//        handleEvent("Registration", List.of("F0 43 73 01 52 25 11 00 02 00 06 F7"));

        final int STANDARD_DUET_1=0;
        final int BLOCK=8;
        final int OCTAVE=17;

//        setEventValue("Harmony", "Type", null, 0, STANDARD_DUET_1);
//        handleEvent("Harmony", List.of("F0 43 73 01 02 00 24 00 F7"));
//        setEventValue("Harmony", "Type", null, 0, BLOCK);
//        handleEvent("Harmony", List.of("F0 43 73 01 02 00 24 08 F7"));
//        setEventValue("Harmony", "Type", null, 0, OCTAVE);
//        handleEvent("Harmony", List.of("F0 43 73 01 02 00 24 11 F7"));

        final int ECHO_6=21;

//        setEventValue("Echo", "Type", null, 0, ECHO_6);
//        handleEvent("Echo", List.of("F0 43 73 01 02 00 24 15 F7"));

        final int TREMOLO_32=27;

//        setEventValue("Tremolo", "Type", null, 0, TREMOLO_32);
//        handleEvent("Tremolo", List.of("F0 43 73 01 02 00 24 1B F7"));

        final int TRILL_12=28;

//        setEventValue("Trill", "Parameter 1", null, 0, TRILL_12);
//        handleEvent("Trill", List.of("F0 43 73 01 02 00 24 1C F7"));

        final int TRACK1_OFF_TRACK2_ON = 1;
        final int KARAOKE_KEY=3;

//        setEventValue("Guide", "Part", null, 0, TRACK1_OFF_TRACK2_ON);
//        setEventValue("Guide", "Mode", null, 0, KARAOKE_KEY);
//        handleEvent("Guide", List.of("F0 43 73 01 1F 00 01 03 F7"));

//       setEventValue("MIDI Master Tuning", "MSB", null, 0, 123);
//       setEventValue("MIDI Master Tuning", "LSB", null, 0, 98);
//       setEventValue("MIDI Master Tuning", "DONT CARE", null, 0, 100);
//       handleEvent("MIDI Master Tuning", List.of("F0 43 10 27 30 00 00 0B 02 64 F7"));

//        setEventValue("Scale Tuning", "Type", SUB, null, null);
//        setEventValue("Scale Tuning", "Fixed", 0x40, null, null); // IS FIXED
//        setEventValue("Scale Tuning", "C", normalize_minus_63(-38), null, null);
//        setEventValue("Scale Tuning", "C#", normalize_minus_63(-35), null, null);
//        setEventValue("Scale Tuning", "D", normalize_minus_63(-30), null, null);
//        setEventValue("Scale Tuning", "D#", normalize_minus_63(-25), null, null);
//        setEventValue("Scale Tuning", "E", normalize_minus_63(-20), null, null);
//        setEventValue("Scale Tuning", "F", normalize_minus_63(-15), null, null);
//        setEventValue("Scale Tuning", "F#", normalize_minus_63(-10), null, null);
//        setEventValue("Scale Tuning", "G", normalize_minus_63(0), null, null);
//        setEventValue("Scale Tuning", "G#", normalize_minus_63( 10), null, null);
//        setEventValue("Scale Tuning", "A", normalize_minus_63(15), null, null);
//        setEventValue("Scale Tuning", "A#", normalize_minus_63(20), null, null);
//        setEventValue("Scale Tuning", "B", normalize_minus_63(30), null, null);
//        handleEvent("Scale Tuning", List.of("F0 43 73 01 52 2F 02 01 40 19 1C 21 26 2B 30 35 3F 49 4E 53 5D F7"));

        final int BASE_NOTE_D=2;
        final int BASE_NOTE_MODE_ONLY_BASE_NOTE=0;

//        setEventValue("Scale Tuning Base Note", "Base note", null, 0, BASE_NOTE_D);
//        setEventValue("Scale Tuning Base Note", "Mode", null, 0, BASE_NOTE_MODE_ONLY_BASE_NOTE);
//        handleEvent("Scale Tuning Base Note", List.of("F0 43 73 01 52 2F 02 00 4D 02 00 F7"));

        final int BASE_NOTE_G_SHARP=8;
        final int BASE_NOTE_MODE_BASE_AND_SCALE=1;

//        setEventValue("Scale Tuning Base Note", "Base note", null, 0, BASE_NOTE_G_SHARP);
//        setEventValue("Scale Tuning Base Note", "Mode", null, 0, BASE_NOTE_MODE_BASE_AND_SCALE);
//        handleEvent("Scale Tuning Base Note", List.of("F0 43 73 01 52 2F 02 00 4D 08 01 F7"));

        // TODO Continue from here
        final int SMALL=0x00;
        final int LARGE=0x01;

        final int CHANNEL_1 = 0;
        final int CHANNEL_2 = CHANNEL_1 + 1;
        final int CHANNEL_3 = CHANNEL_2 + 1;
        final int CHANNEL_4 = CHANNEL_3 + 1;
        final int CHANNEL_5 = CHANNEL_4 + 1;
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


//        setEventValue("Scale/Octave Tuning", ParameterType.FREE, "Channel", 16, null, null);
//        setEventValue("Scale/Octave Tuning", ParameterType.FREE, "Tune E", null, 0x0E, null);
//        setEventValue("Scale/Octave Tuning", ParameterType.FREE, "Tune B", null, 0x0E, null);
//        handleEvent("Scale/Octave Tuning", null, null);

        final int ROOM_S=0;
        final int GM_PLATE=8;

//        EventParameter ep = setEventValue("Reverb Parameter", ParameterType.PARAMETER, 1, ROOM_S, null, null);
//        setLinkedEventValue("Reverb Parameter", 1, 2, 123, null, null);
//        handleEvent("Reverb Parameter", null, null);

        final int FB_CHORUS=4;

//        EventParameter ep = setEventValue("Chorus Parameter", ParameterType.PARAMETER, 1, FB_CHORUS, null, null);
//        setLinkedEventValue("Chorus Parameter", 1, 0x01, 123, null, null);
//        setLinkedEventValue("Chorus Parameter", 1, 0X02, 125, null, null);
//        setLinkedEventValue("Chorus Parameter", 1, 0x04, 126, null, null);
//        setEventValue("Scale/Octave Tuning", ParameterType.FREE, "Tune B", null, 0x0E, null);
//        ep.setValue(GM_PLATE, true);
//        setEventValue(ep, 101, 123, null, null);
//        setLinkedEventValue("Chorus Parameter", ParameterType.PARAMETER, 1, 1, 111, null, null);
//        handleEvent("Chorus Parameter", null, null);

        // THIS IS THE LATEST ONE
//        setEventValue("Aftertouch", "ChannelSelect", CHANNEL_5, null, null);
//        setEventValue("Aftertouch", "PitchControl", 0x31, null, null);
//        setLinkedEventValue("Aftertouch", "PitchControl", "AmplitudeControl", 123, null, null);
//        setLinkedEventValue("Aftertouch", "PitchControl", "LFOAmplitudeDepth", 33, null, null);
//        handleEvent("Aftertouch", null, null);

        logger.debug("[CM_TEST1_PANE] Executed {}", getId());
    }
}

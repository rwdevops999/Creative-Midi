package creative.scenes.sysexgen;

import creative.scenes.sysexgen.data.ParameterType;
import creative.scenes.sysexgen.data.RangeType;
import creative.scenes.sysexgen.entity.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SysexGenDefinitions {
    private static Map<String, ParameterTable> parameterTables = new HashMap<>();

    private static final String PARAMETER_TABLE_ON_OFF="ON_OFF";
    private static final String PARAMETER_TABLE_REAL_REVERB="REAL_REVERB";
    private static final String PARAMETER_TABLE_REVERB1="REVERB1";
    private static final String PARAMETER_TABLE_REVERB2="REVERB2";
    private static final String PARAMETER_TABLE_REVERB3="REVERB3";
    private static final String PARAMETER_TABLE_HARMONY="HARMONY";
    private static final String PARAMETER_TABLE_SECTION="SECTION";
    private static final String PARAMETER_TABLE_REVERB_TYPES="REVERB_TYPES";

    private static final String PARAMETER_TABLE_PARTS="PARTS";
    private static final String PARAMETER_TABLE_MODE="MODE";
    private static final String PARAMETER_TABLE_AUDIO_CONTROL="AUDIO_CONTROL";

    @Getter
    private static List<SysexRoot> sysexs = new ArrayList<>();

    public static void setup() {
        Map<String, ParameterDataTable> parameterDatatables = new HashMap<>();

        ParameterDataTable.ParameterDataTableBuilder parameterDataTableBuilder = new ParameterDataTable.ParameterDataTableBuilder();

        // Parameter Data Tables
        parameterDatatables.put("Table#1", parameterDataTableBuilder
                .clear()
                .withName("Table1")
                .withDescription("Reverb Time [s]")
                .withSize(70)
                .withEntries(
                        "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1.0", "1.1", "1.2",
                        "1.3", "1.4", "1.5", "1.6", "1.7", "1.8", "1.9", "2.0", "2.1", "2.2",
                        "2.3", "2.4", "2.5", "2.6", "2.7", "2.8", "2.9", "3.0", "3.1", "3.2",
                        "3.3", "3.4", "3.5", "3.6", "3.7", "3.8", "3.9", "4.0", "4.1", "4.2",
                        "4.3", "4.4", "4.5", "4.6", "4.7", "4.8", "4.9", "5.0", "5.5", "6.0",
                        "6.5", "7.0", "7.5", "8.0", "8.5", "9.0", "9.5", "10.0", "11.0", "12.0",
                        "13.0", "14.0", "15.0", "16.0", "17.0", "18.0", "19.0", "20.0", "25.0", "30.0"
                )
                .build());

        parameterDatatables.put("Table#2", parameterDataTableBuilder
                .clear()
                .withName("Table2")
                .withDescription("Delay  Time (0.1 - 200.0 [ms])")
                .withSize(128)
                .withEntries(
                        "0.1", "1.7", "3.2", "4.8", "6.4", "8.0", "9.5", "11.1", "12.7", "14.3",
                        "15.8", "17.4", "19.0", "20.6", "22.1", "23.7", "25.3", "26.9", "28.4", "30.0",
                        "31.6", "33.2", "34.7", "36.3", "37.9", "39.5", "41.0", "42.6", "44.2", "45.7",
                        "47.3", "48.9",

                        "50.5", "52.0", "53.6", "55.2", "56.8", "58.3", "59.9", "61.5", "63.1", "64.6",
                        "66.2", "67.8", "69.4", "70.9", "72.5", "74.1", "75.7", "77.2", "78.8", "80.4",
                        "81.9", "83.5", "85.1", "86.7", "88.2", "89.8", "91.4", "93.0", "94.5", "96.1",
                        "97.7", "99.3",

                        "100.8", "102.4", "104.0", "105.6", "107.1", "108.7", "110.3", "111.9", "113,4", "115.0",
                        "116.6", "118.2", "119.7", "121.3", "122.9", "124.4", "126.0", "127.6", "129.2", "130.7",
                        "132.3", "133.9", "135.5", "137.0", "138.6", "140.2", "141.8", "143.3", "144.9", "146.5",
                        "148.1", "149.6",

                        "151.2", "152.8", "154.4", "155.9", "157.5", "159.1", "160.6", "162.2", "163.8", "165.4",
                        "166.9", "168.5", "170.1", "171.7", "173.2", "174.8", "176.4", "178.0", "179.5", "181.1",
                        "182.7", "184.3", "185.8", "187.4", "189.0", "190.6", "192.1", "193.7", "195.3", "196.9",
                        "198.4", "200.0"
                )
                .build());

        parameterDatatables.put("Table#3", parameterDataTableBuilder
                .clear()
                .withName("Table3")
                .withDescription("EQ Frequency [Hz]")
                .withSize(61)
                .withEntries(
                        "THRU(20)", "22", "25", "28", "32", "36", "40", "45", "50", "56",
                        "63", "70", "80", "90", "100", "110", "125", "140", "160", "180",
                        "200", "225", "250", "280", "315", "355", "400", "450", "500", "560",
                        "630", "700", "800", "900", "1.0k", "1.1k", "1.2k", "1.4k", "1.6k", "1.8k",
                        "2.0k", "2.2k", "2.5k", "2.8k", "3.2k", "3.6k", "4.0k", "4.5k", "5.0k", "5.6K",
                        "6.3k", "7.0k", "8.0k", "9.0k", "10k", "11k", "12k", "14k", "16k", "18k",
                        "THRU(20)"
                )
                .build());

        parameterDatatables.put("Table#4", parameterDataTableBuilder
                .clear()
                .withName("Table4")
                .withDescription("Reverb Width; Depth; Height [m]")
                .withSize(105)
                .withEntries(
                        "0.5","0.8","1.0","1.3","1.5","1.8","2.0","2.3","2.6","2.8",
                        "3.1","3.3","3.6","3.9","4.1","4.4","4.6","4.9","5.2","5.4",
                        "5.7","5.9","6.2","6.5","6.7","7.0","7.2","7.5","7.8","8.0",
                        "8.3","8.6","8.8","9.1","9.4","9.6","9.9","10.2","10.4","10.7",
                        "11.0","11.2","11.5","11.8","12.1","12.3","12.6","12.9","13.1","13.4",
                        "13.7","14.0","14.2","14.5","14.8","15.1","15.4","15.6","15.9","16.2",
                        "16.5","16.8","17.1","17.3","17.6","17.9","18.2","18.5","18.8","19.1",
                        "19.4","19.7","20.0","20.2","20.5","20.8","21.1","21.4","21.7","22.0",
                        "22.4","22.7","23.0","23.3","23.6","23.9","24.2","24.5","24.9","25.2",
                        "25.5","25.8","26.1","26.5","26.8","27.1","27.5","27.8","28.1","28.5",
                        "28.8","29.2","29.5","29.9","30.2"
                )
                .build());

        parameterDatatables.put("Table#99", parameterDataTableBuilder
                .clear()
                .withName("Table99")
                .withDescription("Harmony Types")
                .withSize(20)
                .withEntries(
                        "Duet 1", "Duet 2", "Trio", "Full Chord", "Rock Duet", "Country Duet 1", "Country Duet 2", "Country Trio", "Block", "4-Way Close 1", "4-Way Close 2", "4-Way Close 3", "4-Way Close 4",
                        "4-Way Open 1", "4-Way Open 2", "4-Way Open 3", "1+5", "Octave", "Strum", "Multi Assign"
                )
                .build());

        parameterDatatables.put("Table#98", parameterDataTableBuilder
                .clear()
                .withName("Table98")
                .withDescription("Sections")
                .withSize(36)
                .withEntries(
                        "Intro 1", "Intro 2", "Intro 3", "Intro 4", "", "", "", "",
                        "Main A", "Main B", "Main C", "Main D", "", "", "", "",
                        "Fill-In AA", "Fill-In BB", "Fill-In CC", "Fill-In DD", "", "", "", "",
                        "Break Fill", "", "", "", "", "", "", "",
                        "Ending 1", "Ending 2", "Ending 3", "Ending 4"
                )
                .build());

        parameterDatatables.put("Table#90", parameterDataTableBuilder
                .clear()
                .withName("Table90")
                .withDescription("On-Off")
                .withSize(128)
                .withEntry(0, "OFF")
                .withEntry(127, "ON")
                .build());

        parameterDatatables.put("Table#Parts", parameterDataTableBuilder
                .clear()
                .withName("TableParts")
                .withDescription("Right/Left)")
                .withSize(4)
                .withEntries(
                        "Right = ON, Left = ON",
                        "Right = OFF, Left = ON",
                        "Right = ON, Left = OFF",
                        "Right = OFF, Left = OFF")
                .build());

        parameterDatatables.put("Table#Mode", parameterDataTableBuilder
                .clear()
                .withName("TableMore")
                .withDescription("(0 - 6)")
                .withSize(7)
                .withEntries(
                        "Off", "Follow Lights", "Any Key", "Karaoke Key", "Vocal Cue Time", "Your Tempo")
                .build());

        parameterDatatables.put("Table#AControl", parameterDataTableBuilder
                .clear()
                .withName("TableAControl")
                .withDescription("(0 - 2)")
                .withSize(3)
                .withEntries(
                        "Start", "Stop", "Pause")
                .build());

        parameterDatatables.put("Table#ReverbType", parameterDataTableBuilder
                .clear()
                .withName("TableAReverbType")
                .withDescription("(0 - 8)")
                .withSize(9)
                .withEntries(
                        "RoomS", "RoomM", "RoomL",
                        "HallM", "HallL", "",
                        "", "", "GM PLate"
                )
                .build());

        // Parameter Tables
        ParameterTable.ParameterTableBuilder parameterTableBuilder = new ParameterTable.ParameterTableBuilder();

        parameterTables.put(PARAMETER_TABLE_REAL_REVERB, parameterTableBuilder
                .clear()
                .withName(PARAMETER_TABLE_REAL_REVERB)
                .withParameterTableEntry(1, "Reverb Time", "0.3s - 30.0s", 0, 69, parameterDatatables.get("Table#1"))
                .withParameterTableEntry(3, "Initial Delay Time", "0.1ms - 200.0ms", 0, 127, parameterDatatables.get("Table#2"))
                .withParameterTableEntry(4, "High Damp Frequency", "1.0kHz - 18kHr, Thru", 34, 60, parameterDatatables.get("Table#3"))
                .withParameterTableEntry(6, "High Ratio", "0.0 - 1.0", 0, 10)
                .withParameterTableEntry(13, "EQ Low Frequency", "22Hz - 1.0kHz", 1, 34, parameterDatatables.get("Table#3"))
                .withParameterTableEntry(14, "EQ Low Gain", "-12db - 0db - +12db", 52, 76)
                .withParameterTableEntry(15, "EQ High Frequency", "500Hz - 18kHz", 28, 59, parameterDatatables.get("Table#3"))
                .withParameterTableEntry(16, "EQ High Gain", "-12db - 0db - +12db", 52, 76)
                .build());

        parameterTables.put(PARAMETER_TABLE_REVERB1, parameterTableBuilder
                .clear()
                .withName(PARAMETER_TABLE_REVERB1)
                .withParameterTableEntry(1, "Reverb Time", "0.3s - 30.0s", 0, 69, parameterDatatables.get("Table#1)"))
                .withParameterTableEntry(2, "Diffusion", "0 - 10", 0, 10)
                .withParameterTableEntry(3, "Initial Delay Time", "0.1ms - 200.0ms", 0, 127, parameterDatatables.get("Table#2"))
                .withParameterTableEntry(4, "HPF Cutoff Frequency", "Thru, 22Hz - 8.0kHz", 0, 52, parameterDatatables.get("Table#3"))
                .withParameterTableEntry(5, "LPF Cutoff Frequency", "1.0kHz - 18.0kHz, Thru", 34, 60, parameterDatatables.get("Table#3"))
                .withParameterTableEntry(10, "Dry/Wet", "D63>W - D=W - D<W63", 1, 127)
                .withParameterTableEntry(11, "Reverb Delay Time", "0.1ms - 200ms", 0, 127, parameterDatatables.get("Table#2"))
                .withParameterTableEntry(12, "Density", "0 - 4", 0, 4)
                .withParameterTableEntry(13, "ER/Reverb Balance", "E63>R - R=R - E<R63", 1, 127)
                .withParameterTableEntry(14, "High Damp", "0.1 - 1.0", 1, 10)
                .withParameterTableEntry(15, "Feedback Level", "-63 - 0 - +63", 1, 127)
                .build());

        parameterTables.put(PARAMETER_TABLE_REVERB2, parameterTableBuilder
                .clear()
                .withName(PARAMETER_TABLE_REVERB2)
                .withParameterTableEntry(1, "Reverb Time", "0.3s - 30.0s", 0, 69, parameterDatatables.get("Table#1)"))
                .withParameterTableEntry(2, "Diffusion", "0 - 10", 0, 10)
                .withParameterTableEntry(3, "Initial Delay Time", "0.1ms - 200.0ms", 0, 127, parameterDatatables.get("Table#2"))
                .withParameterTableEntry(4, "HPF Cutoff Frequency", "Thru, 22Hz - 8.0kHz", 0, 52, parameterDatatables.get("Table#3"))
                .withParameterTableEntry(5, "LPF Cutoff Frequency", "1.0kHz - 18.0kHz, Thru", 34, 60, parameterDatatables.get("Table#3"))
                .withParameterTableEntry(14, "High Damp", "0.1 - 1.0", 1, 10)
                .build());

        parameterTables.put(PARAMETER_TABLE_REVERB3, parameterTableBuilder
                .clear()
                .withName(PARAMETER_TABLE_REVERB3)
                .withParameterTableEntry(1, "Reverb Time", "0.3s - 30.0s", 0, 69, parameterDatatables.get("Table#1)"))
                .withParameterTableEntry(2, "Diffusion", "0 - 10", 0, 10)
                .withParameterTableEntry(3, "Initial Delay Time", "0.1ms - 200.0ms", 0, 127, parameterDatatables.get("Table#2"))
                .withParameterTableEntry(4, "HPF Cutoff Frequency", "Thru, 22Hz - 8.0kHz", 0, 52, parameterDatatables.get("Table#3"))
                .withParameterTableEntry(5, "LPF Cutoff Frequency", "1.0kHz - 18.0kHz, Thru", 34, 60, parameterDatatables.get("Table#3"))
                .withParameterTableEntry(6, "Width", "0.5m - 30.2m", 0, 104, parameterDatatables.get("Table#4"))
                .withParameterTableEntry(7, "Height", "0.5m - 30.2m", 0, 104, parameterDatatables.get("Table#4"))
                .withParameterTableEntry(8, "Depth", "0.5m - 30.2m", 0, 104, parameterDatatables.get("Table#4"))
                .withParameterTableEntry(9, "Wall Vary", "0 - 30", 0, 30)
                .withParameterTableEntry(10, "Dry/Wet", "D63>W - D=W - D<W63", 1, 127)
                .withParameterTableEntry(11, "Reverb Delay Time", "0.1ms - 200ms", 0, 127, parameterDatatables.get("Table#2"))
                .withParameterTableEntry(12, "Density", "0 - 4", 0, 4)
                .withParameterTableEntry(13, "ER/Reverb Balance", "E63>R - R=R - E<R63", 1, 127)
                .withParameterTableEntry(14, "High Damp", "0.1 - 1.0", 1, 10)
                .withParameterTableEntry(15, "Feedback Level", "-63 - 0 - +63", 1, 127)
                .build());

        parameterTables.put(PARAMETER_TABLE_HARMONY, parameterTableBuilder
                .clear()
                .withName(PARAMETER_TABLE_HARMONY)
                .withParameterTableEntry(1, "Harmony", "0 - 19", 0, 19, parameterDatatables.get("Table#99"))
                .build());

        parameterTables.put(PARAMETER_TABLE_SECTION, parameterTableBuilder
                .clear()
                .withName(PARAMETER_TABLE_SECTION)
                .withParameterTableEntry(1, "Section", "0 - 35", 0, 35, parameterDatatables.get("Table#98"))
                .build());

        parameterTables.put(PARAMETER_TABLE_ON_OFF, parameterTableBuilder
                .clear()
                .withName(PARAMETER_TABLE_ON_OFF)
                .withParameterTableEntry(1, "On/Off", "0 - 127", 0, 127, parameterDatatables.get("Table#90"))
                .build());

        parameterTables.put(PARAMETER_TABLE_PARTS, parameterTableBuilder
                .clear()
                .withName(PARAMETER_TABLE_PARTS)
                .withParameterTableEntry(1, "Part", "0 - 3", 0, 3, parameterDatatables.get("Table#Parts"))
                .build());

        parameterTables.put(PARAMETER_TABLE_MODE, parameterTableBuilder
                .clear()
                .withName(PARAMETER_TABLE_MODE)
                .withParameterTableEntry(1, "Mode", "0 - 6", 0, 6, parameterDatatables.get("Table#Mode"))
                .build());

        parameterTables.put(PARAMETER_TABLE_AUDIO_CONTROL, parameterTableBuilder
                .clear()
                .withName(PARAMETER_TABLE_AUDIO_CONTROL)
                .withParameterTableEntry(1, "Audio Control", "0 - 2", 0, 2, parameterDatatables.get("Table#AControl"))
                .build());

        parameterTables.put(PARAMETER_TABLE_REVERB_TYPES, parameterTableBuilder
                .clear()
                .withName(PARAMETER_TABLE_REVERB_TYPES)
                .withParameterTableEntry(1, "Reverb Type", "0 - 8", 0, 8, parameterDatatables.get("Table#ReverbType"))
                .build());

        // SubEvents
        SubEvent.SubEventBuilder subEventBuilder = new SubEvent.SubEventBuilder();
        Parameter.ParameterBuilder parameterBuilder = new Parameter.ParameterBuilder();
        EventType.EventTypeBuilder eventTypeBuilder = new EventType.EventTypeBuilder();

        // And SysexRoots
        SysexRoot.SysexRootBuilder sysexRootBuilder = new SysexRoot.SysexRootBuilder();

        SubEvent masterVolume = subEventBuilder
                .clear()
                .withName("Master Volume")
                .withSysex("10 04 01")
                .withParameter(
                        parameterBuilder
                                .clear()
                                .withType(ParameterType.INLINE)
                                .withName("Volume")
                                .withDescription("0 - 16383")
                                .withDefaultValue(0)
                                .withRangeValues(0, 16383)
                                .withNumBytes(2)
                                .build())
                .build();

        SubEvent masterFineTuning = subEventBuilder
                .clear()
                .withName("Master Fine Tuning")
                .withSysex("10 04 03")
                .withParameter(
                        parameterBuilder
                                .clear()
                                .withType(ParameterType.INLINE)
                                .withName("Fine tuning")
                                .withDescription("0 - 16383")
                                .withDefaultValue(0)
                                .withRangeValues(0, 16383)
                                .withNumBytes(2)
                                .build())
                .build();

        SubEvent masterCoarseTuning = subEventBuilder
                .clear()
                .withName("Master Coarse Tuning")
                .withSysex("10 04 04 00")
                .withParameter(
                        parameterBuilder
                                .clear()
                                .withType(ParameterType.INLINE)
                                .withName("Coarse Tuning")
                                .withDescription("0 - 127")
                                .withDefaultValue(0)
                                .withRangeValues(0, 127)
                                .withNumBytes(1)
                                .build())
                .build();

        SubEvent reverbParameter = subEventBuilder
                .clear()
                .withName("Reverb Parameter")
                .withSysex("10 04 05 01 01 01 01 01")
                .withParameter(
                        parameterBuilder
                                .clear()
                                .withType(ParameterType.INLINE)
                                .withName("Reverb Type")
                                .withDescription("0 - 8")
                                .withIdentifier("1")
                                .withDefaultValue(0)
                                .withRangeValues(0, 8)
                                .withNumBytes(1)
                                .withEventValue(0)
                                .build())
                .withParameter(
                        parameterBuilder
                                .clear()
                                .withType(ParameterType.INLINE)
                                .withName("Reverb Time")
                                .withDescription("0 - 11.0s")
                                .withDefaultValue(0)
                                .withRangeValues(0, 127)
                                .withNumBytes(1)
                                .build())
                .withEventType(eventTypeBuilder.clear().withName("Reverb Type").withValue(0).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB_TYPES)).build())
                .build();

        SubEvent chorusParameter = subEventBuilder
                .clear()
                .withName("Chorus Parameter")
                .withSysex("10 04 05 01 01 01 02")
                .build();

        SubEvent channelAftertouch = subEventBuilder
                .clear()
                .withName("Channel Aftertouch")
                .withSysex("10 09 01")
                .build();

        SubEvent controlChange = subEventBuilder
                .clear()
                .withName("Control Change")
                .withSysex("10 09 03")
                .build();

        SubEvent instrumentControl = subEventBuilder
                .clear()
                .withName("Instrument Change")
                .withSysex("10 0A 0A")
                .build();

        SysexRoot universalRealTime = sysexRootBuilder
                .clear()
                .withName("Real Time")
                .withRootSysex("7F")
//                .add(masterVolume)
//                .add(masterFineTuning)
//                .add(masterCoarseTuning)
                .add(reverbParameter)
                .add(chorusParameter)
                .add(channelAftertouch)
                .add(controlChange)
                .add(instrumentControl)
                .build();
        sysexs.add(universalRealTime);

        SubEvent GM1SystemOn = subEventBuilder
                .clear()
                .withName("GM1 System On")
                .withSysex("7F 09 01")
                .build();

        SubEvent GM2SystemOn = subEventBuilder
                .clear()
                .withName("GM2 System On")
                .withSysex("7F 09 03")
                .build();

        SubEvent MIDISystemOff = subEventBuilder
                .clear()
                .withName("General MIDI System Off")
                .withSysex("7F 09 02")
                .build();

        SubEvent ScaleOctaveTuning = subEventBuilder
                .clear()
                .withName("Scale/Octave Tuning")
                .withSysex("10 08 08")
                .build();

        SysexRoot universalNonRealTime = sysexRootBuilder
                .clear()
                .withName("Non-Real Time")
                .withRootSysex("7E")
                .add(GM1SystemOn)
                .add(GM2SystemOn)
                .add(MIDISystemOff)
                .add(ScaleOctaveTuning)
                .build();
        sysexs.add(universalNonRealTime);

        SubEvent SectionControl = subEventBuilder
                .clear()
                .withName("Section Control")
                .withSysex("7E 00")
                .withParameter(
                        parameterBuilder
                                .clear()
                                .withName("Section")
                                .withType(ParameterType.INLINE)
                                .withNumBytes(1)
                                .withIdentifier("1")
                                .withDefaultValue(0)
                                .withEventValue(0)
                                .build()
                )
                .withParameter(
                        parameterBuilder
                                .clear()
                                .withName("On/Off")
                                .withDescription("0 = 0FF, 127 = ON")
                                .withType(ParameterType.INLINE)
                                .withIdentifier("1")
                                .withNumBytes(1)
                                .withEventValue(1)
                                .withDefaultValue(127)
                                .withRangeValues(0, 127)
                                .withRangeType(RangeType.DISCRETE)
                                .build()
                )
                .withEventType(eventTypeBuilder.clear().withName("Section").withValue(0).withParameterTable(parameterTables.get(PARAMETER_TABLE_SECTION)).build())
                .withEventType(eventTypeBuilder.clear().withName("OnOff").withValue(1).withParameterTable(parameterTables.get(PARAMETER_TABLE_ON_OFF)).build())
                .build();

        // TODO This one we do later as this requires calculation
        SubEvent TempoControl = subEventBuilder
                .clear()
                .withName("Tempo Control")
                .withSysex("7E 01")
                .build();

        // TODO For this we have to think about
        SubEvent ChordControlType1 = subEventBuilder
                .clear()
                .withName("Chord Control (Type1)")
                .withSysex("7E 01 02")
                .build();

        // TODO For this we have to think
        SubEvent ChordControlType2 = subEventBuilder
                .clear()
                .withName("Chord Control (Type2)")
                .withSysex("7E 01 03")
                .build();

        // This Event has children
        SubEvent Reverb = subEventBuilder
                .clear()
                .withName("Reverb")
                .withSysex("02 01")
                .withParameter(parameterBuilder.clear().withSubAddress(0X00).withNumBytes(2).withType(ParameterType.TYPE).withDescription("MSB/LSB").withDefaultValues(1,0).build())
                .withParameter(parameterBuilder.clear().withSubAddress(0X02).withNumBytes(1).withType(ParameterType.PARAMETER).withIdentifier("1").build())
                .withParameter(parameterBuilder.clear().withSubAddress(0X03).withNumBytes(1).withType(ParameterType.PARAMETER).withIdentifier("2").build())
                .withParameter(parameterBuilder.clear().withSubAddress(0X04).withNumBytes(1).withType(ParameterType.PARAMETER).withIdentifier("3").build())
                .withParameter(parameterBuilder.clear().withSubAddress(0X05).withNumBytes(1).withType(ParameterType.PARAMETER).withIdentifier("4").build())
                .withParameter(parameterBuilder.clear().withSubAddress(0X06).withNumBytes(1).withType(ParameterType.PARAMETER).withIdentifier("5").build())
                .withParameter(parameterBuilder.clear().withSubAddress(0X07).withNumBytes(1).withType(ParameterType.PARAMETER).withIdentifier("6").build())
                .withParameter(parameterBuilder.clear().withSubAddress(0X08).withNumBytes(1).withType(ParameterType.PARAMETER).withIdentifier("7").build())
                .withParameter(parameterBuilder.clear().withSubAddress(0X09).withNumBytes(1).withType(ParameterType.PARAMETER).withIdentifier("8").build())
                .withParameter(parameterBuilder.clear().withSubAddress(0X0A).withNumBytes(1).withType(ParameterType.PARAMETER).withIdentifier("9").build())
                .withParameter(parameterBuilder.clear().withSubAddress(0X0B).withNumBytes(1).withType(ParameterType.PARAMETER).withIdentifier("10").build())
                .withParameter(parameterBuilder.clear().withSubAddress(0X0C).withNumBytes(1).withType(ParameterType.GLOBAL).withIdentifier("Reverb Return").withDescription("-\u221Edb ... 0dB...+6dB").withDefaultValue(40).build())
                .withParameter(parameterBuilder.clear().withSubAddress(0X0D).withNumBytes(1).withType(ParameterType.GLOBAL).withIdentifier("Reverb Pan").withRangeValues(0X1,0x7F).withDescription("L63...C...R63").withDefaultValue(40).build())
                .withParameter(parameterBuilder.clear().withSubAddress(0X10).withNumBytes(1).withType(ParameterType.PARAMETER).withIdentifier("11").build())
                .withParameter(parameterBuilder.clear().withSubAddress(0X11).withNumBytes(1).withType(ParameterType.PARAMETER).withIdentifier("12").build())
                .withParameter(parameterBuilder.clear().withSubAddress(0X12).withNumBytes(1).withType(ParameterType.PARAMETER).withIdentifier("13").build())
                .withParameter(parameterBuilder.clear().withSubAddress(0X13).withNumBytes(1).withType(ParameterType.PARAMETER).withIdentifier("14").build())
                .withParameter(parameterBuilder.clear().withSubAddress(0X14).withNumBytes(1).withType(ParameterType.PARAMETER).withIdentifier("15").build())
                .withParameter(parameterBuilder.clear().withSubAddress(0X15).withNumBytes(1).withType(ParameterType.PARAMETER).withIdentifier("16").build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("RealLrgHall").withDescription("").withValues(1,32).withParameterTable(parameterTables.get(PARAMETER_TABLE_REAL_REVERB)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("RealMedHall").withDescription("").withValues(1,33).withParameterTable(parameterTables.get(PARAMETER_TABLE_REAL_REVERB)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("RealBrtHall").withDescription("").withValues(1,34).withParameterTable(parameterTables.get(PARAMETER_TABLE_REAL_REVERB)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("BasicHall").withDescription("").withValues(1,21).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("LightHall").withDescription("").withValues(1,22).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("BalladHall").withDescription("").withValues(1,19).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB2)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("PianoHall").withDescription("").withValues(1,20).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB2)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("Hall1").withDescription("").withValues(1,0).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("Hall2").withDescription("").withValues(1,16).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("Hall3").withDescription("").withValues(1,17).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("Hall4").withDescription("").withValues(1,18).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("Hall5").withDescription("").withValues(1,1).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("VocalHall1").withDescription("").withValues(1,27).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("VocalHall2").withDescription("").withValues(1,28).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("RealRoom").withDescription("").withValues(2,32).withParameterTable(parameterTables.get(PARAMETER_TABLE_REAL_REVERB)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("RealPwrRoom").withDescription("").withValues(2,33).withParameterTable(parameterTables.get(PARAMETER_TABLE_REAL_REVERB)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("AcousticRoom").withDescription("").withValues(2,20).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("DrumsRoom").withDescription("").withValues(2,21).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("Stage1").withDescription("").withValues(3,16).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("RealLrgPlate").withDescription("").withValues(4,32).withParameterTable(parameterTables.get(PARAMETER_TABLE_REAL_REVERB)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("RealMedPlate").withDescription("").withValues(4,33).withParameterTable(parameterTables.get(PARAMETER_TABLE_REAL_REVERB)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("RealRtlPlate").withDescription("").withValues(4,34).withParameterTable(parameterTables.get(PARAMETER_TABLE_REAL_REVERB)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("Plate1").withDescription("").withValues(4,16).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())

                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("HallM").withValues(1,6).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("HallL").withValues(1,7).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("AtmoHall").withValues(1,23).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("LargeHall").withValues(1,2).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB2)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("MediumHall").withValues(1,3).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB2)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("PercRoom").withValues(2,22).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Room1").withValues(2,16).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Room2").withValues(2,17).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Room3").withValues(2,18).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Room4").withValues(2,19).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Room5").withValues(2,0).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Room6").withValues(2,1).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Room7").withValues(2,2).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("RoomS").withValues(2,5).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("RoomM").withValues(2,6).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("RoomL").withValues(2,7).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("WarmRoom").withValues(2,3).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB2)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("WhiteRoom").withValues(16,0).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB3)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("WoodyRoom").withValues(2,4).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB2)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Stage2").withValues(3,17).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Stage3").withValues(3,0).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Stage4").withValues(3,1).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Plate2").withValues(4,17).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Plate3").withValues(4,0).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("GM Plate").withValues(4,7).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB1)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("RichPlate").withValues(4,1).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB2)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Tunnel").withValues(17,0).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB3)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Canyon").withValues(18,0).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB3)).build())
                .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Basement").withValues(19,0).withParameterTable(parameterTables.get(PARAMETER_TABLE_REVERB3)).build())

                .withEventType(eventTypeBuilder.clear().withCategory("None").withName("NoEffect").withValues(0,0).build())

                .build();

        SubEvent XGParameterChange = subEventBuilder
                .clear()
                .withName("XG Parameter Change")
                .withSysex("10 4C")
                .addChild(Reverb)
                .build();

        // TODO Think
        SubEvent ScaleTuning = subEventBuilder
                .clear()
                .withName("Scale Tuning")
                .withSysex("73 01 52 2F 02 cc 40")
                .build();

        // TODO Think
        SubEvent ScaleTuningPart = subEventBuilder
                .clear()
                .withName("Scale Tuning Part")
                .withSysex("73 01 52 2F 02 cc")
                .build();

        // TODO Think
        SubEvent ScaleTuningBaseNote = subEventBuilder
                .clear()
                .withName("Scale Tuning base Note")
                .withSysex("73 01 52 2F 02 00 4D")
                .build();

        SubEvent InternalClock = subEventBuilder
                .clear()
                .withName("Internal Clock")
                .withSysex("73 01 02")
                .build();

        SubEvent ExternalClock = subEventBuilder
                .clear()
                .withName("External Clock")
                .withSysex("73 01 03")
                .build();

        // TODO To be examined
        SubEvent Style = subEventBuilder
                .clear()
                .withName("Style")
                .withSysex("73 01 51 00 00")
                .build();

        SubEvent MIDIMasterTuning = subEventBuilder
                .clear()
                .withName("MIDI Master Tuning")
                .withSysex("10 27 30 00 00")
                .withParameter(
                        parameterBuilder
                                .clear()
                                .withType(ParameterType.INLINE)
                                .withName("Master Tune MSB")
                                .withDescription("0 - 127")
                                .withDefaultValue(0)
                                .withRangeValues(0, 127)
                                .build()
                )
                .withParameter(
                        parameterBuilder
                                .clear()
                                .withType(ParameterType.INLINE)
                                .withName("Master Tune LSB")
                                .withDescription("0 - 127")
                                .withDefaultValue(0)
                                .withRangeValues(0, 127)
                                .build()
                )
                .withParameter(
                        parameterBuilder
                                .clear()
                                .withType(ParameterType.INLINE)
                                .withDefaultValue(0)
                                .ignore()
                                .build()
                )
                .build();

        SubEvent GuideMode = subEventBuilder
                .clear()
                .withName("Guide Mode")
                .withSysex("73 01 1F 00")
                .withParameter(
                        parameterBuilder
                                .clear()
                                .withType(ParameterType.INLINE)
                                .withName("Part Select")
                                .withDescription("0 - 4")
                                .withIdentifier("1")
                                .withDefaultValue(0)
                                .withRangeValues(0, 4)
                                .withEventValue(0)
                                .build()
                )
                .withParameter(
                        parameterBuilder
                                .clear()
                                .withType(ParameterType.INLINE)
                                .withName("Mode")
                                .withDescription("0 - 6")
                                .withIdentifier("1")
                                .withDefaultValue(0)
                                .withRangeValues(0, 6)
                                .withEventValue(1)
                                .build()
                )
                .withEventType(eventTypeBuilder.clear().withName("Part Select").withValue(0).withParameterTable(parameterTables.get(PARAMETER_TABLE_PARTS)).build())
                .withEventType(eventTypeBuilder.clear().withName("Mode").withValue(1).withParameterTable(parameterTables.get(PARAMETER_TABLE_MODE)).build())
                .build();

        SubEvent AudioSongControl = subEventBuilder
                .clear()
                .withName("Audio Song Control")
                .withSysex("73 01 50 19 00 00")
                .withParameter(
                        parameterBuilder
                                .clear()
                                .withType(ParameterType.INLINE)
                                .withName("AudioControl")
                                .withDescription("0 - 2")
                                .withIdentifier("1")
                                .withDefaultValue(0)
                                .withRangeValues(0, 2)
                                .withEventValue(0)
                                .build()
                )
                .withEventType(eventTypeBuilder.clear().withName("Audio Control").withValue(0).withParameterTable(parameterTables.get(PARAMETER_TABLE_AUDIO_CONTROL)).build())
                .build();

        // TODO think about
        SubEvent Score = subEventBuilder
                .clear()
                .withName("Score")
                .withSysex("73 01 50 12 00")
                .build();

        SubEvent Registrations = subEventBuilder
                .clear()
                .withName("Registration")
                .withSysex("73 01 52 25 11 00 02 00")
                .withParameter(
                        parameterBuilder
                                .clear()
                                .withType(ParameterType.INLINE)
                                .withName("Registration number")
                                .withDescription("0 - 7")
                                .withEventValue(0)
                                .withDefaultValue(0)
                                .withRangeValues(0, 7)
                                .build())
                .build();

        SubEvent Harmony = subEventBuilder
                        .clear()
                        .withName("Harmony")
                        .withSysex("73 01 51 04 00 00")
                        .withParameter(
                                parameterBuilder
                                        .clear()
                                        .withType(ParameterType.INLINE)
                                        .withNumBytes(1)
                                        .withIdentifier("1")
                                        .withEventValue(0)
                                        .withDefaultValue(0)
                                        .build()
                        )
                .withEventType(eventTypeBuilder.clear().withName("Harmony Type").withValue(0).withParameterTable(parameterTables.get(PARAMETER_TABLE_HARMONY)).build())
                .build();

        SysexRoot yamaha = sysexRootBuilder
                .clear()
                .withName("Yamaha")
                .withRootSysex("43")
//                .add(SectionControl)
//                .add(TempoControl)
//                .add(ChordControlType1)
//                .add(ChordControlType2)
                .add(XGParameterChange)
//                .add(ScaleTuning)
//                .add(ScaleTuningPart)
//                .add(ScaleTuningBaseNote)
//                .add(InternalClock)
//                .add(ExternalClock)
//                .add(Style)
//                .add(MIDIMasterTuning)
//                .add(GuideMode)
//                .add(AudioSongControl)
//                .add(Score)
//                .add(Registrations)
//                .add(Harmony)
                .build();
        sysexs.add(yamaha);
    }
}

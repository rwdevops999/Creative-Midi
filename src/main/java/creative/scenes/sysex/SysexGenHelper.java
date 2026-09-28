package creative.scenes.sysex;

import entity.sysex.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SysexGenHelper {
    private static Map<String, Master> masters;

    private static final String PARAMETER_LIST_REAL_REVERB="REAL_REVERB";
    private static final String PARAMETER_LIST_REVERB1="REVERB1";
    private static final String PARAMETER_LIST_REVERB2="REVERB2";
    private static final String PARAMETER_LIST_REVERB3="REVERB3";
    private static final String PARAMETER_LIST_REGISTRATION="REGISTRATION";
    private static final String HARMONY_LIST_REGISTRATION="HARMONY";

    public static void setup() {
        // Parameter Tables
        Map<String, ParametersTable> tables = new HashMap<>();

        ParametersTable.ParametersTableBuilder parametersTableBuilder = new ParametersTable.ParametersTableBuilder();

        tables.put("Table#1", parametersTableBuilder
                .reset()
                .withName("Table1")
                .withDescription("Reverb Time [s]")
                .withSize(70)
                .withValues(
                        "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1.0", "1.1", "1.2",
                        "1.3", "1.4", "1.5", "1.6", "1.7", "1.8", "1.9", "2.0", "2.1", "2.2",
                        "2.3", "2.4", "2.5", "2.6", "2.7", "2.8", "2.9", "3.0", "3.1", "3.2",
                        "3.3", "3.4", "3.5", "3.6", "3.7", "3.8", "3.9", "4.0", "4.1", "4.2",
                        "4.3", "4.4", "4.5", "4.6", "4.7", "4.8", "4.9", "5.0", "5.5", "6.0",
                        "6.5", "7.0", "7.5", "8.0", "8.5", "9.0", "9.5", "10.0", "11.0", "12.0",
                        "13.0", "14.0", "15.0", "16.0", "17.0", "18.0", "19.0", "20.0", "25.0", "30.0"
                )
                .build());

        tables.put("Table#2", parametersTableBuilder
                .reset()
                .withName("Table2")
                .withDescription("Delay  Time (0.1 - 200.0 [ms])")
                .withSize(128)
                .withValues(
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

        tables.put("Table#3", parametersTableBuilder
                .reset()
                .withName("Table3")
                .withDescription("EQ Frequency [Hz]")
                .withSize(61)
                .withValues(
                        "THRU(20)", "22", "25", "28", "32", "36", "40", "45", "50", "56",
                        "63", "70", "80", "90", "100", "110", "125", "140", "160", "180",
                        "200", "225", "250", "280", "315", "355", "400", "450", "500", "560",
                        "630", "700", "800", "900", "1.0k", "1.1k", "1.2k", "1.4k", "1.6k", "1.8k",
                        "2.0k", "2.2k", "2.5k", "2.8k", "3.2k", "3.6k", "4.0k", "4.5k", "5.0k", "5.6K",
                        "6.3k", "7.0k", "8.0k", "9.0k", "10k", "11k", "12k", "14k", "16k", "18k",
                        "THRU(20)"
                )
                .build());

        tables.put("Table#4", parametersTableBuilder
                .reset()
                .withName("Table4")
                .withDescription("Reverb Width; Depth; Height [m]")
                .withSize(105)
                .withValues(
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

        tables.put("Table#99", parametersTableBuilder
                .reset()
                .withName("Table99")
                .withDescription("Harmony Types")
                .withSize(20)
                .withValues(
                    "Duet 1", "Duet 2", "Trio", "Full Chord", "Rock Duet", "Country Duet 1", "Country Duet 2", "Country Trio", "Block", "4-Way Close 1", "4-Way Close 2", "4-Way Close 3", "4-Way Close 4",
                    "4-Way Open 1", "4-Way Open 2", "4-Way Open 3", "1+5", "Octave", "Strum", "Multi Assign"
                )
                .build());

        // Parameters
        Map<String, ParameterList> parameters = new HashMap<>();

        ParameterList.ParameterListBuilder parameterListBuilder = new ParameterList.ParameterListBuilder();
        // REVERB
        parameters.put(PARAMETER_LIST_REAL_REVERB, parameterListBuilder
                .reset()
                .withName(PARAMETER_LIST_REAL_REVERB)
                .withParameters(1, "Reverb Time", "0.3s - 30.0s", 0, 69, tables.get("Table#1"))
                .withParameters(3, "Initial Delay Time", "0.1ms - 200.0ms", 0, 127, tables.get("Table#2"))
                .withParameters(4, "High Damp Frequency", "1.0kHz - 18kHr, Thru", 34, 60, tables.get("Table#3"))
                .withParameters(6, "High Ratio", "0.0 - 1.0", 0, 10)
                .withParameters(13, "EQ Low Frequency", "22Hz - 1.0kHz", 1, 34, tables.get("Table#3"))
                .withParameters(14, "EQ Low Gain", "-12db - 0db - +12db", 52, 76)
                .withParameters(15, "EQ High Frequency", "500Hz - 18kHz", 28, 59, tables.get("Table#3"))
                .withParameters(16, "EQ High Gain", "-12db - 0db - +12db", 52, 76)
                .build());

        parameters.put(PARAMETER_LIST_REVERB1, parameterListBuilder
                .reset()
                .withName(PARAMETER_LIST_REVERB1)
                .withParameters(1, "Reverb Time", "0.3s - 30.0s", 0, 69, tables.get("Table#1)"))
                .withParameters(2, "Diffusion", "0 - 10", 0, 10)
                .withParameters(3, "Initial Delay Time", "0.1ms - 200.0ms", 0, 127, tables.get("Table#2"))
                .withParameters(4, "HPF Cutoff Frequency", "Thru, 22Hz - 8.0kHz", 0, 52, tables.get("Table#3"))
                .withParameters(5, "LPF Cutoff Frequency", "1.0kHz - 18.0kHz, Thru", 34, 60, tables.get("Table#3"))
                .withParameters(10, "Dry/Wet", "D63>W - D=W - D<W63", 1, 127)
                .withParameters(11, "Reverb Delay Time", "0.1ms - 200ms", 0, 127, tables.get("Table#2"))
                .withParameters(12, "Density", "0 - 4", 0, 4)
                .withParameters(13, "ER/Reverb Balance", "E63>R - R=R - E<R63", 1, 127)
                .withParameters(14, "High Damp", "0.1 - 1.0", 1, 10)
                .withParameters(15, "Feedback Level", "-63 - 0 - +63", 1, 127)
                .build());

        parameters.put(PARAMETER_LIST_REVERB2, parameterListBuilder
                .reset()
                .withName(PARAMETER_LIST_REVERB2)
                .withParameters(1, "Reverb Time", "0.3s - 30.0s", 0, 69, tables.get("Table#1)"))
                .withParameters(2, "Diffusion", "0 - 10", 0, 10)
                .withParameters(3, "Initial Delay Time", "0.1ms - 200.0ms", 0, 127, tables.get("Table#2"))
                .withParameters(4, "HPF Cutoff Frequency", "Thru, 22Hz - 8.0kHz", 0, 52, tables.get("Table#3"))
                .withParameters(5, "LPF Cutoff Frequency", "1.0kHz - 18.0kHz, Thru", 34, 60, tables.get("Table#3"))
                .withParameters(14, "High Damp", "0.1 - 1.0", 1, 10)
                .build());

        parameters.put(PARAMETER_LIST_REVERB3, parameterListBuilder
                .reset()
                .withName(PARAMETER_LIST_REVERB3)
                .withParameters(1, "Reverb Time", "0.3s - 30.0s", 0, 69, tables.get("Table#1)"))
                .withParameters(2, "Diffusion", "0 - 10", 0, 10)
                .withParameters(3, "Initial Delay Time", "0.1ms - 200.0ms", 0, 127, tables.get("Table#2"))
                .withParameters(4, "HPF Cutoff Frequency", "Thru, 22Hz - 8.0kHz", 0, 52, tables.get("Table#3"))
                .withParameters(5, "LPF Cutoff Frequency", "1.0kHz - 18.0kHz, Thru", 34, 60, tables.get("Table#3"))
                .withParameters(6, "Width", "0.5m - 30.2m", 0, 104, tables.get("Table#4"))
                .withParameters(7, "Height", "0.5m - 30.2m", 0, 104, tables.get("Table#4"))
                .withParameters(8, "Depth", "0.5m - 30.2m", 0, 104, tables.get("Table#4"))
                .withParameters(9, "Wall Vary", "0 - 30", 0, 30)
                .withParameters(10, "Dry/Wet", "D63>W - D=W - D<W63", 1, 127)
                .withParameters(11, "Reverb Delay Time", "0.1ms - 200ms", 0, 127, tables.get("Table#2"))
                .withParameters(12, "Density", "0 - 4", 0, 4)
                .withParameters(13, "ER/Reverb Balance", "E63>R - R=R - E<R63", 1, 127)
                .withParameters(14, "High Damp", "0.1 - 1.0", 1, 10)
                .withParameters(15, "Feedback Level", "-63 - 0 - +63", 1, 127)
                .build());

        parameters.put(PARAMETER_LIST_REGISTRATION, parameterListBuilder
                .reset()
                .withName(PARAMETER_LIST_REGISTRATION)
                .withParameters(1, "Registration", "0 - 7", 0, 7)
                .build());

        parameters.put(HARMONY_LIST_REGISTRATION, parameterListBuilder
                .reset()
                .withName(HARMONY_LIST_REGISTRATION)
                .withParameters(1, "Harmony", "0 - 19", 0, 19, tables.get("Table#99"))
                .build());

        masters = new HashMap<String, Master>();

        Master.MasterBuilder masterBuilder = new Master.MasterBuilder();
        ParameterChangeTable.ParameterChangeTableBuilder parameterChangeTableBuilder = new ParameterChangeTable.ParameterChangeTableBuilder();
        TypeBlock.TypeBlockBuilder typeBlockBuilder = new TypeBlock.TypeBlockBuilder();

        masters.put("Reverb",
                masterBuilder
                        .reset()
                        .withName("XG (Reverb)")
                        .withBaseSysex("F0 43 10 4C")
                        .withAddress("02 01")
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x0).withNumBytes(2).withParameter("Type").withDescription("MSB/LSB").withDefaultValues(1, 0).build())
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x2).withNumBytes(1).withParameter("Parameter 1").build())
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x3).withNumBytes(1).withParameter("Parameter 2").build())
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x4).withNumBytes(1).withParameter("Parameter 3").build())
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x5).withNumBytes(1).withParameter("Parameter 4").build())
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x6).withNumBytes(1).withParameter("Parameter 5").build())
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x7).withNumBytes(1).withParameter("Parameter 6").build())
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x8).withNumBytes(1).withParameter("Parameter 7").build())
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x9).withNumBytes(1).withParameter("Parameter 8").build())
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x0A).withNumBytes(1).withParameter("Parameter 9").build())
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x0B).withNumBytes(1).withParameter("Parameter 10").build())
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x0C).withNumBytes(1).withParameter("Reverb Return").withDescription("-\u221Edb ... 0dB...+6dB").withDefaultValues(40).build())
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x0D).withNumBytes(1).withParameter("Reverb Pan").withRange(0X1, 0x7F).withDescription("L63...C...R63").withDefaultValues(40).build())
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x10).withNumBytes(1).withParameter("Parameter 11").build())
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x11).withNumBytes(1).withParameter("Parameter 12").build())
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x12).withNumBytes(1).withParameter("Parameter 13").build())
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x13).withNumBytes(1).withParameter("Parameter 14").build())
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x14).withNumBytes(1).withParameter("Parameter 15").build())
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x15).withNumBytes(1).withParameter("Parameter 16").build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("ReadLrgHall").withDescription("").withMsbAndLsb(1,32).withParameters(parameters.get(PARAMETER_LIST_REAL_REVERB)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("ReadMedHall").withDescription("").withMsbAndLsb(1,33).withParameters(parameters.get(PARAMETER_LIST_REAL_REVERB)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("ReadBrtHall").withDescription("").withMsbAndLsb(1,34).withParameters(parameters.get(PARAMETER_LIST_REAL_REVERB)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("BasicHall").withDescription("").withMsbAndLsb(1,21).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("LightHall").withDescription("").withMsbAndLsb(1,22).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("BalladHall").withDescription("").withMsbAndLsb(1,19).withParameters(parameters.get(PARAMETER_LIST_REVERB2)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("PianoHall").withDescription("").withMsbAndLsb(1,20).withParameters(parameters.get(PARAMETER_LIST_REVERB2)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("Hall1").withDescription("").withMsbAndLsb(1,0).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("Hall2").withDescription("").withMsbAndLsb(1,16).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("Hall3").withDescription("").withMsbAndLsb(1,17).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("Hall4").withDescription("").withMsbAndLsb(1,18).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("Hall5").withDescription("").withMsbAndLsb(1,1).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("VocalHall1").withDescription("").withMsbAndLsb(1,27).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("VocalHall2").withDescription("").withMsbAndLsb(1,28).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("RealRoom").withDescription("").withMsbAndLsb(2,32).withParameters(parameters.get(PARAMETER_LIST_REAL_REVERB)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("RealPwrRoom").withDescription("").withMsbAndLsb(2,33).withParameters(parameters.get(PARAMETER_LIST_REAL_REVERB)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("AcousticRoom").withDescription("").withMsbAndLsb(2,20).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("DrumsRoom").withDescription("").withMsbAndLsb(2,21).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("Stage1").withDescription("").withMsbAndLsb(3,16).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("RealLrgPlate").withDescription("").withMsbAndLsb(4,32).withParameters(parameters.get(PARAMETER_LIST_REAL_REVERB)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("RealMedPlate").withDescription("").withMsbAndLsb(4,33).withParameters(parameters.get(PARAMETER_LIST_REAL_REVERB)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("RealRtlPlate").withDescription("").withMsbAndLsb(4,34).withParameters(parameters.get(PARAMETER_LIST_REAL_REVERB)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Reverb").withType("Plate1").withDescription("").withMsbAndLsb(4,16).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("HallM").withMsbAndLsb(1,6).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("HallL").withMsbAndLsb(1,7).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("AtmoHall").withMsbAndLsb(1,23).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("LargeHall").withMsbAndLsb(1,2).withParameters(parameters.get(PARAMETER_LIST_REVERB2)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("MediumHall").withMsbAndLsb(1,3).withParameters(parameters.get(PARAMETER_LIST_REVERB2)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("PercRoom").withMsbAndLsb(2,22).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("Room1").withMsbAndLsb(2,16).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("Room2").withMsbAndLsb(2,17).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("Room3").withMsbAndLsb(2,18).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("Room4").withMsbAndLsb(2,19).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("Room5").withMsbAndLsb(2,0).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("Room6").withMsbAndLsb(2,1).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("Room7").withMsbAndLsb(2,2).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("RoomS").withMsbAndLsb(2,5).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("RoomM").withMsbAndLsb(2,6).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("RoomL").withMsbAndLsb(2,7).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("WarmRoom").withMsbAndLsb(2,3).withParameters(parameters.get(PARAMETER_LIST_REVERB2)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("WhiteRoom").withMsbAndLsb(16,0).withParameters(parameters.get(PARAMETER_LIST_REVERB3)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("WoodyRoom").withMsbAndLsb(2,4).withParameters(parameters.get(PARAMETER_LIST_REVERB2)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("Stage2").withMsbAndLsb(3,17).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("Stage3").withMsbAndLsb(3,0).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("Stage4").withMsbAndLsb(3,1).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("Plate2").withMsbAndLsb(4,17).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("Plate3").withMsbAndLsb(4,0).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("GM Plate").withMsbAndLsb(4,7).withParameters(parameters.get(PARAMETER_LIST_REVERB1)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("RichPlate").withMsbAndLsb(4,1).withParameters(parameters.get(PARAMETER_LIST_REVERB2)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("Tunnel").withMsbAndLsb(17,0).withParameters(parameters.get(PARAMETER_LIST_REVERB3)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("Canyon").withMsbAndLsb(18,0).withParameters(parameters.get(PARAMETER_LIST_REVERB3)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("Legacy").withType("Basement").withMsbAndLsb(19,0).withParameters(parameters.get(PARAMETER_LIST_REVERB3)).build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withCategory("None").withType("NoEffect").withMsbAndLsb(0,0).build())
                        .build());

        masters.put("Registrations",
                masterBuilder
                        .reset()
                        .withName("Registrations")
                        .withBaseSysex("F0 43 73 01 52 25 11")
                        .withAddress("00 02")
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x0).withNumBytes(1).withParameter("Parameter 1").build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withDescription("Registration number").withParameters(parameters.get(PARAMETER_LIST_REGISTRATION)).build())
                        .build());

        masters.put("Harmony",
                masterBuilder
                        .reset()
                        .withName("Harmony")
                        .withBaseSysex("F0 43 10 4C")
                        .withAddress("04 00")
                        .withParameterChangeTableValue(parameterChangeTableBuilder.reset().withId(0x0).withNumBytes(1).withParameter("Parameter 1").build())
                        .withTypeBlockValue(typeBlockBuilder.reset().withDescription("Harmony Type").withParameters(parameters.get(HARMONY_LIST_REGISTRATION)).build())
                        .build());
    }

    public static List<Master> getMasters() {
        return new ArrayList<>(masters.values());
    }
}

package creative.scenes.sysexgen2.definition;

import creative.scenes.sysexgen2.entity.*;
import lombok.Getter;
import util.Vendors;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class SysexGen2Definitions {
    private static final Event.EventBuilder eventBuilder;

    private static final EventType.EventTypeBuilder eventTypeBuilder;
    private static final EventParameter.EventParameterBuilder eventParameterBuilder;

    private static final ParameterTable.ParameterTableBuilder parameterTableBuilder;
    private static final ParametersTableEntry.ParametersTableEntryBuilder parameterTableEntryBuilder;

    private static final DataTable.DataTableBuilder dataTableBuilder;

    @Getter
    private static final Event rootEvent = new Event();

    private static final List<ParameterTable> parameterTables;
    private static final List<DataTable> dataTables;

    static {
        // create builders
        eventBuilder = new Event.EventBuilder();

        eventTypeBuilder = new EventType.EventTypeBuilder();
        eventParameterBuilder = new EventParameter.EventParameterBuilder();

        parameterTableBuilder = new ParameterTable.ParameterTableBuilder();
        parameterTableEntryBuilder = new ParametersTableEntry.ParametersTableEntryBuilder();

        dataTableBuilder = new DataTable.DataTableBuilder();

        parameterTables = new ArrayList<>();
        dataTables = new ArrayList<>();
    }

    private static void createGeneralParameterTables() {
        parameterTables.add(parameterTableBuilder
                .clear()
                .withIdentifier("IntegerInputTable")
                .withName("IntegerInputTable")
                .withDescription("Table to enter integer values")
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(1)
                                .withLabel("Integer")
                                .withDescription("int")
                                .withRange(0, 16384)
                                .withDefaultValue(0)
                                .build())
                .build());
    }

    private static void createCustomParameterTables() {
        // This is for generation ParameterTables from generic ParameterTable
        // changed kan be Label, Description, Default Value
        MutableParameterTable.MutableParameterTableBuilder mutableParameterTableBuilder = new MutableParameterTable.MutableParameterTableBuilder();
        ParameterTable VolumeTable = mutableParameterTableBuilder
                .clear()
                .withIdentifier("VolumeTable")
                .withLabel("Volume")
                .withDefaultValue(64)
                .withRange(0, 16384)
                .fromParameterTable(findParameterTable("IntegerInputTable"))
                .build();
        parameterTables.add(VolumeTable);

        ParameterTable FineTuningTable = mutableParameterTableBuilder
                .clear()
                .withIdentifier("FineTuningTable")
                .withLabel("Tuning")
                .withDefaultValue(64)
                .withRange(0, 16384)
                .fromParameterTable(findParameterTable("IntegerInputTable"))
                .build();
        parameterTables.add(FineTuningTable);

        ParameterTable CoarseTuningTable = mutableParameterTableBuilder
                .clear()
                .withIdentifier("CoarseTuningTable")
                .withLabel("Tuning")
                .withDefaultValue(0)
                .withRange(0, 127)
                .fromParameterTable(findParameterTable("IntegerInputTable"))
                .build();
        parameterTables.add(CoarseTuningTable);

        ParameterTable AudioControlTable = parameterTableBuilder
                .clear()
                .withIdentifier("AudioControlTable")
                .withName("Audio Control")
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(1)
                                .withLabel("Control")
                                .withDescription("0 = Start, 1 = Stop, 2 = Pause")
                                .withDefaultValue(0)
                                .withRange(0, 2)
                                .withDataTable(findDataTable("SwitchControlsDataTable"))
                                .build()
                )
                .build();
        parameterTables.add(AudioControlTable);

        ParameterTable SectionControlTable = parameterTableBuilder
                .clear()
                .withIdentifier("SectionControlTable")
                .withName("Section Control")
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(1)
                                .withLabel("Switch Number")
                                .withDescription("0 - 3, 8 - 11, 16 - 19, 24, 32 - 35")
                                .withDefaultValue(0)
                                .withRange(0, 35)
                                .withDataTable(findDataTable("SwitchNumbersDataTable"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(2)
                                .withLabel("Switch Value")
                                .withDescription("0 = 0ff, 7F = 0n")
                                .withDefaultValue(0x7F)
                                .withRange(0, 0x7F)
                                .withDataTable(findDataTable("OnOffDataTable"))
                                .build()
                )
                .build();
        parameterTables.add(SectionControlTable);

        ParameterTable ScaleTuningPartTable = parameterTableBuilder
                .clear()
                .withIdentifier("ScaleTuningPartTable")
                .withName("Scale Tuning Part")
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(1)
                                .withLabel("Type")
                                .withDescription("0x00 = Main, 0x01 = SUB")
                                .withDefaultValue(0)
                                .withRange(0x0, 0x1)
                                .withDataTable(findDataTable("PartTypeDataTable"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(2)
                                .withLabel("Part")
                                .withDescription("0x4F = LEFT, 0x50 = STYLE, 0x51 = MULTI PAD, 0x52 = RIGHT1, 0x53 = RIGHT2")
                                .withDefaultValue(0)
                                .withRange(0x47, 0x53)
                                .withDataTable(findDataTable("PartValuesDataTable"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(3)
                                .withLabel("Part Value")
                                .withDescription("00 = 0ff, 7F = 0n")
                                .withDefaultValue(0x7F)
                                .withRange(0x00, 0x7F)
                                .withDataTable(findDataTable("OnOffDataTable"))
                                .build()
                )

                .build();
        parameterTables.add(ScaleTuningPartTable);

        ParameterTable RegistrationTable = mutableParameterTableBuilder
                .clear()
                .withIdentifier("RegistrationTable")
                .withLabel("Registration")
                .withDefaultValue(0)
                .withRange(0, 7)
                .fromParameterTable(findParameterTable("IntegerInputTable"))
                .build();
        parameterTables.add(RegistrationTable);

        ParameterTable HarmonyTable = parameterTableBuilder
                .clear()
                .withIdentifier("HarmonyTable")
                .withName("Harmony")
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(1)
                                .withLabel("Type")
                                .withDescription("0 - 19")
                                .withDefaultValue(0)
                                .withRange(0, 19)
                                .withDataTable(findDataTable("HarmonyDataTable"))
                                .build()
                )
                .build();
        parameterTables.add(HarmonyTable);

        ParameterTable EchoTable = parameterTableBuilder
                .clear()
                .withIdentifier("EchoTable")
                .withName("Echo")
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(1)
                                .withLabel("Type")
                                .withDescription("20 - 23")
                                .withDefaultValue(20)
                                .withRange(20, 23)
                                .withDataTable(findDataTable("HarmonyDataTable"))
                                .build()
                )
                .build();
        parameterTables.add(EchoTable);

        ParameterTable TremoloTable = parameterTableBuilder
                .clear()
                .withIdentifier("TremoloTable")
                .withName("Tremolo")
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(1)
                                .withLabel("Type")
                                .withDescription("24 - 27")
                                .withDefaultValue(24)
                                .withRange(24, 27)
                                .withDataTable(findDataTable("HarmonyDataTable"))
                                .build()
                )
                .build();
        parameterTables.add(TremoloTable);

        ParameterTable TrillTable = parameterTableBuilder
                .clear()
                .withIdentifier("TrillTable")
                .withName("Trill")
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(1)
                                .withLabel("Type")
                                .withDescription("28 - 31")
                                .withDefaultValue(28)
                                .withRange(28, 31)
                                .withDataTable(findDataTable("HarmonyDataTable"))
                                .build()
                )
                .build();
        parameterTables.add(TrillTable);

        ParameterTable GuideTable = parameterTableBuilder
                .clear()
                .withIdentifier("GuideTable")
                .withName("Guide")
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(1)
                                .withLabel("Part")
                                .withDescription("Track On-Off")
                                .withDefaultValue(4)
                                .withRange(0, 3)
                                .withDataTable(findDataTable("GuideTrackDataTable"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(2)
                                .withLabel("Mode")
                                .withDescription("0 - 6")
                                .withDefaultValue(7)
                                .withRange(0, 7)
                                .withDataTable(findDataTable("GuideModeDataTable"))
                                .build()
                )
                .build();
        parameterTables.add(GuideTable);

        ParameterTable MIDIMasterTuningTable = mutableParameterTableBuilder
                .clear()
                .withIdentifier("MIDIMasterTuningTable")
                .withLabel("Tuning")
                .withDefaultValue(0)
                .withRange(0, 16383)
                .fromParameterTable(findParameterTable("IntegerInputTable"))
                .build();
        parameterTables.add(MIDIMasterTuningTable);

        String[] labels = {
                "Scale tuning C",
                "Scale tuning C#",
                "Scale tuning D",
                "Scale tuning D#/E\u266D",
                "Scale tuning E",
                "Scale tuning F",
                "Scale tuning G",
                "Scale tuning G#/A\u266D",
                "Scale tuning A",
                "Scale tuning A#/B\u266D",
                "Scale tuning B",
        };

        ParameterTable ScaleTuningTable = parameterTableBuilder
                .clear()
                .withIdentifier("ScaleTuningTable")
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(1)
                                .withLabel("Type")
                                .withDescription("0 - 2")
                                .withDefaultValue(0)
                                .withRange(0, 2)
                                .withDataTable(findDataTable("PartTypeDataTable"))
                                .build()
                )
                .withEntry(  // FIXED VALUE
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(2)
                                .withDefaultValue(40)
                                .withRange(40, 40)
                                .build()
                )
                .withEntries(
                        parameterTableEntryBuilder
                                .clear()
                                .withDefaultValue(0)
                                .withRange(0, 127)
                                .build(), labels, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14
                )
                .build();
        parameterTables.add(ScaleTuningTable);

        ParameterTable ScaleTuningBaseNoteTable = parameterTableBuilder
                .clear()
                .withIdentifier("ScaleTuningBaseNoteTable")
                .withDescription("Scale Tuning Base Notes")
                .withEntry(  // FIXED VALUE
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(1)
                                .withDefaultValue(0)
                                .withRange(0, 11)
                                .withDataTable(findDataTable("BaseNoteDataTable"))
                                .build()
                )
                .withEntry(  // FIXED VALUE
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(2)
                                .withDefaultValue(0)
                                .withRange(0, 127)
                                .withDataTable(findDataTable("BaseNoteModeDataTable"))
                                .build()
                )
                .build();
        parameterTables.add(ScaleTuningBaseNoteTable);

        ParameterTable ScoreTable = parameterTableBuilder
                .clear()
                .withIdentifier("ScoreTable")
                .withDescription("Score Handling")
                .withEntries(
                        parameterTableEntryBuilder
                                .clear()
                                .withDefaultValue(0)
                                .withRange(0, 127)
                                .withDataTable(findDataTable("OnOffDataTable"))
                                .build(),
                        new String[]{
                                "Left Part Indication",
                                "Right Part Indication",
                                "Lyrics Indication",
                                "Chord Indication",
                                "Note Name Indication",
                                "",
                                "",
                                "",
                                "Quantize Triplet",
                                "",
                                "",
                                "Color Note",
                        }, 1, 2, 3, 4, 5, 9, 12
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(6)
                                .withDefaultValue(0)
                                .withRange(0, 2)
                                .withDataTable(findDataTable("SizeDataTable"))
                                .build()
                )
                .withEntries(
                        parameterTableEntryBuilder
                                .clear()
                                .withDefaultValue(0)
                                .withRange(0, 127)
                                .withDataTable(findDataTable("ChannelDataTable"))
                                .build(),
                        new String[]{
                                "Left Channel",
                                "Right Channel",
                        }, 7, 8
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(10)
                                .withDefaultValue(0)
                                .withRange(0, 3)
                                .withDataTable(findDataTable("QuantizeDataTable"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(11)
                                .withDefaultValue(0)
                                .withRange(0, 3)
                                .withDataTable(findDataTable("NoteNameDataTable"))
                                .build()
                )

                .build();
        parameterTables.add(ScoreTable);
    }

    private static ParameterTable findParameterTable(String identifier) {
        ParameterTable result = parameterTables.stream().filter(pt -> pt.getIdentifier().equals(identifier)).findFirst().orElse(null);

        if (result != null) {
            result = new ParameterTable(result);
        }

        return result;
    }

    private static void createDataTables() {
        DataTable SwitchDataTable = dataTableBuilder
                .clear()
                .withTableId("SwitchControlsDataTable")
                .withTableDescription("Audio Switch Controls")
                .withSize(3)
                .withEntries("Start", "Stop", "Pause")
                .build();
        dataTables.add(SwitchDataTable);

        DataTable SwitchNumbersDataTable = dataTableBuilder
                .clear()
                .withTableId("SwitchNumbersDataTable")
                .withTableDescription("Section Switch Number")
                .withSize(36)
                .withEntry(0x00, "INTRO 1")
                .withEntry(0x01, "INTRO 2")
                .withEntry(0x02, "INTRO 3")
                .withEntry(0x03, "INTRO 4")
                .withEntry(0x08, "MAIN A")
                .withEntry(0x09, "MAIN B")
                .withEntry(0x0A, "MAIN C")
                .withEntry(0x0B, "MAIN D")
                .withEntry(0x10, "FILL IN AA")
                .withEntry(0x11, "FILL IN BB")
                .withEntry(0x12, "FILL IN CC")
                .withEntry(0x13, "FILL IN DD")
                .withEntry(0x18, "BREAK FILL")
                .withEntry(0x20, "ENDING 1")
                .withEntry(0x21, "ENDING 2")
                .withEntry(0x22, "ENDING 3")
                .withEntry(0x23, "ENDING 4")
                .build();
        dataTables.add(SwitchNumbersDataTable);

        DataTable OnOffDataTable = dataTableBuilder
                .clear()
                .withTableId("OnOffDataTable")
                .withTableDescription("On Off Values")
                .withSize(128)
                .withEntry(0x00, "OFF")
                .withEntry(0x7F, "ON")
                .build();
        dataTables.add(OnOffDataTable);

        DataTable PartTypeDataTable = dataTableBuilder
                .clear()
                .withTableId("PartTypeDataTable")
                .withTableDescription("Part Type Number")
                .withSize(2)
                .withEntry(0x00, "Main")
                .withEntry(0x01, "Sub")
                .build();
        dataTables.add(PartTypeDataTable);

        DataTable PartValuesDataTable = dataTableBuilder
                .clear()
                .withTableId("PartValuesDataTable")
                .withTableDescription("Part Values")
                .withSize(84)
                .withEntry(0x4F, "Left")
                .withEntry(0x50, "Style")
                .withEntry(0x51, "Multi Pad")
                .withEntry(0x52, "Right1")
                .withEntry(0x53, "Right2")
                .build();
        dataTables.add(PartValuesDataTable);


        DataTable HarmonyDataTable = dataTableBuilder
                .clear()
                .withTableId("HarmonyDataTable")
                .withTableDescription("Harmony Types")
                .withSize(32)
                .withEntries(
                        "Standard Duet 1", "Standard Duet 2", "Standard Trio", "Full Chord",
                        "Rock Duet", "Country Duet 1", "Country Duet 2", "Country Trio", "Block",
                        "4-Way Close 1", "4-Way Close 2", "4-Way Close 3", "4-Way Close 4",
                        "4-Way Open 1", "4-Way Open 2", "4-Way Open 3", "1+5",
                        "Octave", "Strum", "Multi Assign",
                        "Echo 4", "Echo 6", "Echo 8", "Echo 12",
                        "Tremolo 8", "Tremolo 12", "Tremolo 16", "Tremolo 32",
                        "Trill 12", "Trill 16", "Trill 24", "Trill 32"
                )
                .build();
        dataTables.add(HarmonyDataTable);

        DataTable GuideTrackDataTable = dataTableBuilder
                .clear()
                .withTableId("GuideTrackDataTable")
                .withTableDescription("Guide Track")
                .withSize(4)
                .withEntries(
                        "Track1 ON, Track2 ON",
                        "Track1 OFF, Track2 ON",
                        "Track1 ON, Track2 OFF",
                        "Track1 OFF, Track2 OFF")
                .build();
        dataTables.add(GuideTrackDataTable);

        DataTable GuideModeDataTable = dataTableBuilder
                .clear()
                .withTableId("GuideModeDataTable")
                .withTableDescription("Guide Mode")
                .withSize(7)
                .withEntry( 0x00, "Guide Off")
                .withEntry( 0x01, "Follow Lights")
                .withEntry( 0x02, "Any Key")
                .withEntry( 0x03, "Karaoke Key")
                .withEntry( 0x06, "Your Tempo")
                .build();
        dataTables.add(GuideModeDataTable);

        DataTable BaseNoteDataTable = dataTableBuilder
                .clear()
                .withTableId("BaseNoteDataTable")
                .withTableDescription("Base Notes")
                .withSize(12)
                .withEntry( 0x00, "C")
                .withEntry( 0x01, "C#")
                .withEntry( 0x02, "D")
                .withEntry( 0x03, "D#/E\u266D")
                .withEntry( 0x06, "E")
                .withEntry( 0x06, "F")
                .withEntry( 0x06, "F#")
                .withEntry( 0x06, "G")
                .withEntry( 0x06, "G#/A\u266D")
                .withEntry( 0x06, "A")
                .withEntry( 0x06, "A#/B\u266D")
                .withEntry( 0x06, "B")
                .build();
        dataTables.add(BaseNoteDataTable);

        DataTable BaseNoteModeDataTable = dataTableBuilder
                .clear()
                .withTableId("BaseNoteModeDataTable")
                .withTableDescription("Base Note Modes")
                .withSize(128)
                .withEntry( 0x00, "Base Note Only")
                .withEntry( 0x7F, "Base Note and Scale Tune")
                .build();
        dataTables.add(BaseNoteModeDataTable);

        DataTable SizeDataTable = dataTableBuilder
                .clear()
                .withTableId("SizeDataTable")
                .withTableDescription("Size Indication")
                .withSize(3)
                .withEntry(0x00, "Small")
                .withEntry(0x02, "Large")
                .build();
        dataTables.add(SizeDataTable);

        DataTable ChannelDataTable = dataTableBuilder
                .clear()
                .withTableId("ChannelDataTable")
                .withTableDescription("Channels")
                .withSize(128)
                .withEntry(0x00, "Channel 1")
                .withEntry(0x01, "Channel 2")
                .withEntry(0x02, "Channel 3")
                .withEntry(0x03, "Channel 4")
                .withEntry(0x04, "Channel 5")
                .withEntry(0x05, "Channel 6")
                .withEntry(0x06, "Channel 7")
                .withEntry(0x07, "Channel 8")
                .withEntry(0x08, "Channel 9")
                .withEntry(0x09, "Channel 10")
                .withEntry(0x0A, "Channel 11")
                .withEntry(0x0B, "Channel 12")
                .withEntry(0x0C, "Channel 13")
                .withEntry(0x0D, "Channel 14")
                .withEntry(0x0E, "Channel 15")
                .withEntry(0x0F, "Channel 16")
                .withEntry(0x7E, "Off")
                .withEntry(0x7F, "Auto")
                .build();
        dataTables.add(ChannelDataTable);

        DataTable QuantizeDataTable = dataTableBuilder
                .clear()
                .withTableId("QuantizeDataTable")
                .withTableDescription("Channels")
                .withSize(4)
                .withEntry(0x00, "Quarter")
                .withEntry(0x01, "Eighth")
                .withEntry(0x02, "Sixteenth")
                .withEntry(0x03, "Thirty-second")
                .build();
        dataTables.add(QuantizeDataTable);


        DataTable NoteNameDataTable = dataTableBuilder
                .clear()
                .withTableId("NoteNameDataTable")
                .withTableDescription("Note Representation")
                .withSize(3)
                .withEntry(0x00, "A, B, C")
                .withEntry(0x01, "Fixed Do")
                .withEntry(0x02, "Movable Do")
                .build();
        dataTables.add(NoteNameDataTable);
    }

    private static DataTable findDataTable(String identifier) {
        DataTable result = dataTables.stream().filter(dt -> dt.getTableId().equals(identifier)).findFirst().orElse(null);

        if (result != null) {
            result = new DataTable(result);
        }

        return result;
    }


    public static void setup() {
        createDataTables();
        createGeneralParameterTables();
        createCustomParameterTables();

        Event eventVendor = eventBuilder
                .clear()
                .withName("Vendor")
                .build();

        rootEvent.addChild(eventVendor);

        // UniversalRealTime
        Event eventMasterVolume = eventBuilder
                .clear()
                .withName("Master Volume")
                .withSysex("7F 04 01")
                .withDefaultValues(0, 0)
                .withEventType(
                        eventTypeBuilder
                                .clear()
                                .withName("Master Volume EventType")
                                .withEventValues(0, 0)
                                .withParameterTable(findParameterTable("VolumeTable"))
                                .build()
                )
                .withEventParameter(
                        eventParameterBuilder
                                .clear()
                                .withName("Parameter 1")
                                .withParameterId(1)
                                .withNumBytes(2)
                                .build()
                )
                .build();

        Event eventMasterFineTuning = eventBuilder
                .clear()
                .withName("Master Fine Tuning")
                .withSysex("7F 04 03")
                .withDefaultValues(0, 0)
                .withEventType(
                        eventTypeBuilder
                                .clear()
                                .withName("Master Fine Tuning EventType")
                                .withEventValues(0, 0)
                                .withParameterTable(findParameterTable("FineTuningTable"))
                                .build()
                )
                .withEventParameter(
                        eventParameterBuilder
                                .clear()
                                .withName("Parameter 1")
                                .withParameterId(1)
                                .withNumBytes(2)
                                .build()
                )
                .build();

        Event eventMasterCoarseTuning = eventBuilder
                .clear()
                .withName("Master Coarse Tuning")
                .withSysex("7F 04 04 00")
                .withDefaultValues(0, 0)
                .withEventType(
                        eventTypeBuilder
                                .clear()
                                .withName("Master Coarse Tuning EventType")
                                .withEventValues(0, 0)
                                .withParameterTable(findParameterTable("CoarseTuningTable"))
                                .build()
                )
                .withEventParameter(
                        eventParameterBuilder
                                .clear()
                                .withName("Parameter 1")
                                .withParameterId(1)
                                .withNumBytes(1)
                                .build()
                )
                .build();

        Event eventUniversalRealTime = eventBuilder
                .clear()
                .withName("Universal Real Time")
                .withSysex("7F")
                .withChild(eventMasterVolume)
                .withChild(eventMasterFineTuning)
                .withChild(eventMasterCoarseTuning)
                .build();
        rootEvent.addChild(eventUniversalRealTime);

        // UniversalNonRealTime
        Event eventGM1SystemOn = eventBuilder
                .clear()
                .withName("GM1 System On")
                .withSysex("7F 09 01")
                .build();

        Event eventGM2SystemOn = eventBuilder
                .clear()
                .withName("GM2 System On")
                .withSysex("7F 09 03")
                .build();

        Event eventGeneralMIDISystemOff = eventBuilder
                .clear()
                .withName("General MIDI System Off")
                .withSysex("7F 09 02")
                .build();

        Event eventUniversalNonRealTime = eventBuilder
                .clear()
                .withName("Universal Non-Real Time")
                .withSysex("7E")
                .withChild(eventGM1SystemOn)
                .withChild(eventGM2SystemOn)
                .withChild(eventGeneralMIDISystemOff)
                .build();
        rootEvent.addChild(eventUniversalNonRealTime);

        // Vendor

        // - Yamaha
        Event eventInternalClock = eventBuilder
                .clear()
                .withName("Internal Clock")
                .withSysex("73 01 02")
                .build();

        Event eventExternalClock = eventBuilder
                .clear()
                .withName("External Clock")
                .withSysex("73 01 02")
                .build();

        Event eventAudioSongControl = eventBuilder
                .clear()
                .withName("Audio Song Control")
                .withSysex("73 01 50 19 00 00")
                .withDefaultValues(0, 0)
                .withEventType(
                        eventTypeBuilder
                                .clear()
                                .withName("Audio Song Control EventType")
                                .withEventValues(0, 0)
                                .withParameterTable(findParameterTable("AudioControlTable"))
                                .build()
                )
                .withEventParameter(
                        eventParameterBuilder
                                .clear()
                                .withName("Parameter 1")
                                .withParameterId(1)
                                .withNumBytes(1)
                                .build()
                )
                .build();

        Event eventSectionControl =
                eventBuilder
                        .clear()
                        .withName("Section Control")
                        .withSysex("7E 00")
                        .withDefaultValues(0, 0)
                        .withEventType(
                                eventTypeBuilder
                                        .clear()
                                        .withName("Section Control EventType")
                                        .withEventValues(0, 0)
                                        .withParameterTable(findParameterTable("SectionControlTable"))
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("Parameter 1")
                                        .withParameterId(1)
                                        .withNumBytes(1)
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("Parameter 2")
                                        .withParameterId(2)
                                        .withNumBytes(1)
                                        .build()
                        )
                        .build();

        Event eventScaleTuningPart =
                eventBuilder
                        .clear()
                        .withName("Scale Tuning Part")
                        .withSysex("73 01 52 2F 02")
                        .withDefaultValues(0, 0)
                        .withEventType(
                                eventTypeBuilder
                                        .clear()
                                        .withName("Scale Tuning Part EventType")
                                        .withEventValues(0, 0)
                                        .withParameterTable(findParameterTable("ScaleTuningPartTable"))
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("Parameter 1")
                                        .withParameterId(1)
                                        .withNumBytes(1)
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("Parameter 2")
                                        .withParameterId(2)
                                        .withNumBytes(1)
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("Parameter 3")
                                        .withParameterId(3)
                                        .withNumBytes(1)
                                        .build()
                        )
                        .build();

        Event eventRegistration  =
                eventBuilder
                        .clear()
                        .withName("Registration")
                        .withSysex("73 01 52 25 11 00 02 00")
                        .withDefaultValues(0, 0)
                        .withEventType(
                                eventTypeBuilder
                                        .clear()
                                        .withName("Registration EventType")
                                        .withEventValues(0, 0)
                                        .withParameterTable(findParameterTable("RegistrationTable"))
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("Parameter 1")
                                        .withParameterId(1)
                                        .withNumBytes(1)
                                        .build()
                        )
                        .build();

        Event eventHarmony  =
                eventBuilder
                        .clear()
                        .withName("Harmony")
                        .withSysex("73 01 02 00 24")
                        .withDefaultValues(0, 0)
                        .withEventType(
                                eventTypeBuilder
                                        .clear()
                                        .withName("Harmony EventType")
                                        .withEventValues(0, 0)
                                        .withParameterTable(findParameterTable("HarmonyTable"))
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("Parameter 1")
                                        .withParameterId(1)
                                        .withNumBytes(1)
                                        .build()
                        )

                        .build();

        Event eventEcho  =
                eventBuilder
                        .clear()
                        .withName("Echo")
                        .withSysex("73 01 02 00 24")
                        .withDefaultValues(0, 0)
                        .withEventType(
                                eventTypeBuilder
                                        .clear()
                                        .withName("Echo EventType")
                                        .withEventValues(0, 0)
                                        .withParameterTable(findParameterTable("EchoTable"))
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("Parameter 1")
                                        .withParameterId(1)
                                        .withNumBytes(1)
                                        .build()
                        )

                        .build();

        Event eventTremolo  =
                eventBuilder
                        .clear()
                        .withName("Tremolo")
                        .withSysex("73 01 02 00 24")
                        .withDefaultValues(0, 0)
                        .withEventType(
                                eventTypeBuilder
                                        .clear()
                                        .withName("Tremolo EventType")
                                        .withEventValues(0, 0)
                                        .withParameterTable(findParameterTable("TremoloTable"))
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("Parameter 1")
                                        .withParameterId(1)
                                        .withNumBytes(1)
                                        .build()
                        )

                        .build();

        Event eventTrill  =
                eventBuilder
                        .clear()
                        .withName("Trill")
                        .withSysex("73 01 02 00 24")
                        .withDefaultValues(0, 0)
                        .withEventType(
                                eventTypeBuilder
                                        .clear()
                                        .withName("Trill EventType")
                                        .withEventValues(0, 0)
                                        .withParameterTable(findParameterTable("TrillTable"))
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("Parameter 1")
                                        .withParameterId(1)
                                        .withNumBytes(1)
                                        .build()
                        )
                        .build();

        Event eventGuideOn  =
                eventBuilder
                        .clear()
                        .withName("Guide On")
                        .withSysex("73 01 1F 00")
                        .withDefaultValues(0, 0)
                        .withEventType(
                                eventTypeBuilder
                                        .clear()
                                        .withName("Guide EventType")
                                        .withEventValues(0, 0)
                                        .withParameterTable(findParameterTable("GuideTable"))
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("Parameter 1")
                                        .withParameterId(1)
                                        .withNumBytes(1)
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("Parameter 2")
                                        .withParameterId(2)
                                        .withNumBytes(1)
                                        .build()
                        )
                        .build();

        Event eventMIDIMasterTuning  =
                eventBuilder
                        .clear()
                        .withName("MIDI Master Tuning")
                        .withSysex("10 27 30 00 00")
                        .withDefaultValues(0, 0)
                        .withEventType(
                                eventTypeBuilder
                                        .clear()
                                        .withName("MIDI Master Tuning EventType")
                                        .withEventValues(0, 0)
                                        .withParameterTable(findParameterTable("MIDIMasterTuningTable"))
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("Parameter 1")
                                        .withParameterId(1)
                                        .withNumBytes(1, true)
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("Parameter 2")
                                        .withParameterId(2)
                                        .withNumBytes(1,true)
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("Parameter 3")
                                        .withParameterId(3)
                                        .isIgnore(true)
                                        .withNumBytes(1)
                                        .build()
                        )
                        .build();

        Event eventScaleTuning  =
                eventBuilder
                        .clear()
                        .withName("Scale Tuning")
                        .withSysex("73 01 52 2F 02")
                        .withDefaultValues(0, 0)
                        .withEventType(
                                eventTypeBuilder
                                        .clear()
                                        .withName("Scale Tuning EventType")
                                        .withEventValues(0, 0)
                                        .withParameterTable(findParameterTable("ScaleTuningTable"))
                                        .build()
                        )
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 1").withParameterId(1).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 2").withParameterId(2).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 3").withParameterId(3).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 4").withParameterId(4).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 5").withParameterId(5).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 6").withParameterId(6).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 7").withParameterId(7).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 8").withParameterId(8).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 9").withParameterId(9).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 10").withParameterId(10).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 11").withParameterId(11).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 12").withParameterId(12).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 13").withParameterId(13).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 14").withParameterId(14).withNumBytes(1).build())
                        .build();

        Event eventScaleTuningBaseNote  =
                eventBuilder
                        .clear()
                        .withName("Scale Tuning Base Note")
                        .withSysex("73 01 52 2F 02 00 4D")
                        .withDefaultValues(0, 0)
                        .withEventType(
                                eventTypeBuilder
                                        .clear()
                                        .withName("Scale Tuning BaseNote EventType")
                                        .withEventValues(0, 0)
                                        .withParameterTable(findParameterTable("ScaleTuningBaseNoteTable"))
                                        .build()
                        )
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 1").withParameterId(1).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 2").withParameterId(2).withNumBytes(1).build())
                        .build();

        Event eventScore =
                eventBuilder
                    .clear()
                        .withName("Score")
                        .withSysex("73 01 50 12 00") // Here we must add sysex to parameter
                        .withDefaultValues(0, 0)
                        .withEventType(
                                eventTypeBuilder
                                        .clear()
                                        .withName("Score EventType")
                                        .withEventValues(0, 0)
                                        .withParameterTable(findParameterTable("ScoreTable"))
                                        .build()
                        )
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 1").withParameterId(1).withNumBytes(1).withAddress("00").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 2").withParameterId(1).withNumBytes(1).withAddress("01").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 3").withParameterId(1).withNumBytes(1).withAddress("02").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 4").withParameterId(1).withNumBytes(1).withAddress("03").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 5").withParameterId(1).withNumBytes(1).withAddress("04").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 6").withParameterId(1).withNumBytes(1).withAddress("05").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 7").withParameterId(1).withNumBytes(1).withAddress("06").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 8").withParameterId(1).withNumBytes(1).withAddress("07").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 9").withParameterId(1).withNumBytes(1).withAddress("08").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 10").withParameterId(1).withNumBytes(1).withAddress("09").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 11").withParameterId(1).withNumBytes(1).withAddress("0A").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 12").withParameterId(1).withNumBytes(1).withAddress("0B").build())
                    .build();

                Event eventYamaha = eventBuilder
                .clear()
                .withName("Yamaha")
                .withSysex(Vendors.VENDOR_YAMAHA)
                .withChild(eventInternalClock)
                .withChild(eventExternalClock)
                .withChild(eventAudioSongControl)
                .withChild(eventSectionControl)
                .withChild(eventScaleTuningPart)
                .withChild(eventRegistration)
                .withChild(eventHarmony)
                .withChild(eventEcho)
                .withChild(eventTremolo)
                .withChild(eventTrill)
                .withChild(eventGuideOn)
                .withChild(eventMIDIMasterTuning)
                .withChild(eventScaleTuning)
                .withChild(eventScaleTuningBaseNote)
                .withChild(eventScore)
                .build();
        eventVendor.addChild(eventYamaha);  // this adds also into the rootEvent list vendor
    }
}

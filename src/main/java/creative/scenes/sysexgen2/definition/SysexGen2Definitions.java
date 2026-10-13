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
                .withEntry(0X51, "Multi Pad")
                .withEntry(0x52, "Right1")
                .withEntry(0x53, "Right2")
                .build();
        dataTables.add(PartValuesDataTable);
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
                .build();
        eventVendor.addChild(eventYamaha);  // this adds also into the rootEvent list vendor
    }
}

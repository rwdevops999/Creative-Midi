package creative.scenes.sysexgen2.definition;

import creative.scenes.sysexgen2.entity.*;
import lombok.Getter;
import util.Vendors;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class SysexGen2Definitions {
    private static Event.EventBuilder eventBuilder;

    private static EventType.EventTypeBuilder eventTypeBuilder;
    private static EventParameter.EventParameterBuilder eventParameterBuilder;

    private static ParameterTable.ParameterTableBuilder parameterTableBuilder;
    private static ParametersTableEntry.ParametersTableEntryBuilder parameterTableEntryBuilder;

    private static DataTable.DataTableBuilder dataTableBuilder;

    @Getter
    private static final Event rootEvent = new Event();

    private static List<ParameterTable> parameterTables;
    private static List<DataTable> dataTables;

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
                                .withDataValueTable(findDataTable("Switch Controls"))
                                .build()
                )
                .build();
        parameterTables.add(AudioControlTable);
    }

    private static ParameterTable findParameterTable(String identifier) {
        ParameterTable result = parameterTables.stream().filter(pt -> pt.getIdentifier().equals(identifier)).findFirst().orElse(null);

        if (result != null) {
            result = new ParameterTable(result);
        }

        return result;
    }

    private static void createDataValueTables() {
        DataTable SwitchDataTable = dataTableBuilder
                .clear()
                .withTableId("Switch Controls")
                .withTableDescription("Audio Switch Controls")
                .withSize(3)
                .withEntries("Start", "Stop", "Pause")
                .build();
        dataTables.add(SwitchDataTable);
    }

    private static DataTable findDataTable(String identifier) {
        DataTable result = dataTables.stream().filter(dt -> dt.getTableId().equals(identifier)).findFirst().orElse(null);

        if (result != null) {
            result = new DataTable(result);
        }

        return result;
    }


    public static void setup() {
        createDataValueTables();
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

        Event eventYamaha = eventBuilder
                .clear()
                .withName("Yamaha")
                .withSysex(Vendors.VENDOR_YAMAHA)
                .withChild(eventInternalClock)
                .withChild(eventExternalClock)
                .withChild(eventAudioSongControl)
                .build();
        eventVendor.addChild(eventYamaha);  // this adds also into the rootEvent list vendor
    }
}

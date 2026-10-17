package creative.scenes.sysexgen2.definition;

import creative.scenes.sysexgen2.data.ParameterType;
import creative.scenes.sysexgen2.entity.*;
import lombok.Getter;
import util.Vendors;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;

import static util.Constants.BIG_ENDIAN;
import static util.Constants.LITTLE_ENDIAN;

public class SysexGen2Definitions {
    private static final Event.EventBuilder eventBuilder;

    private static final EventType.EventTypeBuilder eventTypeBuilder;
    private static final EventParameter.EventParameterBuilder eventParameterBuilder;

    private static final ParameterTable.ParameterTableBuilder parameterTableBuilder;
    private static final ParametersTableEntry.ParametersTableEntryBuilder parameterTableEntryBuilder;

    private static final DataTable.DataTableBuilder dataTableBuilder;
    private static final DisplayBlock.DisplayBlockBuilder displayBlockBuilder;

    @Getter
    private static final Event rootEvent = new Event("Root");

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

        displayBlockBuilder = new DisplayBlock.DisplayBlockBuilder();

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
                "Scale tuning D#/E♭",
                "Scale tuning E",
                "Scale tuning F",
                "Scale tuning G",
                "Scale tuning G#/A♭",
                "Scale tuning A",
                "Scale tuning A#/B♭",
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

        ParameterTable ChannelsTable = parameterTableBuilder
                .clear()
                .withIdentifier("ChannelsTable")
                .withName("Channels")
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(1)
                                .withLabel("Channel")
                                .withDescription("0x00 - 0x0F")
                                .withDefaultValue(0)
                                .withRange(0x00, 0x0F)
                                .withDataTable(findDataTable("ChannelDataTable"))
                                .build()
                )
                .build();
        parameterTables.add(ChannelsTable);

        // For XG Parameter Change
        ParameterTable RealReverbTable = parameterTableBuilder
                .clear()
                .withIdentifier("RealReverb")
                .withDescription("Block: Reverb")
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(1)
                                .withLabel("Reverb Time")
                                .withDescription("0.3s - 30.0s")
                                .withDefaultValue(0)
                                .withRange(0, 69)
                                .withDataTable(findDataTable("Table#1"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(3)
                                .withLabel("Initial Delay Time")
                                .withDescription("0.1ms - 200.0ms")
                                .withDefaultValue(0)
                                .withRange(0, 127)
                                .withDataTable(findDataTable("Table#2"))
                                .build()
                )

                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(4)
                                .withLabel("High Damp Frequency")
                                .withDescription("1.0kHz - 18kHz, Thru")
                                .withDefaultValue(34)
                                .withRange(34, 60)
                                .withDataTable(findDataTable("Table#3"))
                                .build()
                )

                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(6)
                                .withLabel("High Ratio")
                                .withDescription("0.0 - 1.0")
                                .withDefaultValue(0)
                                .withRange(0, 10)
                                .build()
                )

                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(13)
                                .withLabel("EQ Low Frequency")
                                .withDescription("22Hz - 1.0kHz")
                                .withDefaultValue(1)
                                .withRange(1, 34)
                                .withDataTable(findDataTable("Table#3"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(14)
                                .withLabel("EQ Low Gain")
                                .withDescription("-12db - 0db - +12db")
                                .withDefaultValue(52)
                                .withRange(52, 76)
                                .build()
                )

                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(15)
                                .withLabel("EQ High Frequency")
                                .withDescription("500Hz - 18kHz")
                                .withDefaultValue(28)
                                .withRange(28, 59)
                                .withDataTable(findDataTable("Table#3"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(16)
                                .withLabel("EQ High Gain")
                                .withDescription("-12db - 0db - +12db")
                                .withDefaultValue(52)
                                .withRange(52, 76)
                                .build()
                )

                .build();
        parameterTables.add(RealReverbTable);

        ParameterTable Reverb1Table = parameterTableBuilder
                .clear()
                .withIdentifier("Reverb1")
                .withDescription("Block: Reverb, Chorus, Variation")
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(1)
                                .withLabel("Reverb Time")
                                .withDescription("0.3s - 30.0s")
                                .withDefaultValue(0)
                                .withRange(0, 69)
                                .withDataTable(findDataTable("Table#1"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(2)
                                .withLabel("Diffusion")
                                .withDescription("0 - 10")
                                .withDefaultValue(0)
                                .withRange(0, 10)
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(3)
                                .withLabel("Initial Delay Time")
                                .withDescription("0.1ms - 200.0ms")
                                .withDefaultValue(0)
                                .withRange(0, 127)
                                .withDataTable(findDataTable("Table#2"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(4)
                                .withLabel("HPF Cutoff Frequency")
                                .withDescription("Thru, 22Hz - 8.0kHz")
                                .withDefaultValue(0)
                                .withRange(0, 52)
                                .withDataTable(findDataTable("Table#3"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(5)
                                .withLabel("LPF Cutoff Frequency")
                                .withDescription("1.0kHz - 18.0kHz, Thru")
                                .withDefaultValue(34)
                                .withRange(34, 60)
                                .withDataTable(findDataTable("Table#3"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(10)
                                .withLabel("Dry/Wet")
                                .withDescription("D63>W - D=W - D<W63")
                                .withDefaultValue(1)
                                .withRange(1, 127)
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(11)
                                .withLabel("Reverb Delay Time")
                                .withDescription("0.1ms - 200ms")
                                .withDefaultValue(0)
                                .withRange(0, 127)
                                .withDataTable(findDataTable("Table#2"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(12)
                                .withLabel("Density")
                                .withDescription("0 - 4")
                                .withDefaultValue(0)
                                .withRange(0, 4)
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(13)
                                .withLabel("ER/Reverb Balance")
                                .withDescription("E63>R - R=R - E<R63")
                                .withDefaultValue(1)
                                .withRange(1, 127)
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(14)
                                .withLabel("High Damp")
                                .withDescription("0.1 - 1.0")
                                .withDefaultValue(1)
                                .withRange(1, 10)
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(15)
                                .withLabel("Feedback Level")
                                .withDescription("-63 - 0 - +63")
                                .withDefaultValue(1)
                                .withRange(1, 127)
                                .build()
                )
                .build();
        parameterTables.add(Reverb1Table);

        ParameterTable Reverb2Table = parameterTableBuilder
                .clear()
                .withIdentifier("Reverb2")
                .withDescription("Block: Reverb")
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(1)
                                .withLabel("Reverb Time")
                                .withDescription("0.3s - 30.0s")
                                .withDefaultValue(0)
                                .withRange(0, 69)
                                .withDataTable(findDataTable("Table#1"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(2)
                                .withLabel("Diffusion")
                                .withDescription("0 - 10")
                                .withDefaultValue(0)
                                .withRange(0, 10)
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(3)
                                .withLabel("Initial Delay Time")
                                .withDescription("0.1ms - 200.0ms")
                                .withDefaultValue(0)
                                .withRange(0, 127)
                                .withDataTable(findDataTable("Table#2"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(4)
                                .withLabel("HPF Cutoff Frequency")
                                .withDescription("Thru, 22Hz - 8.0kHz")
                                .withDefaultValue(0)
                                .withRange(0, 52)
                                .withDataTable(findDataTable("Table#3"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(5)
                                .withLabel("LPF Cutoff Frequency")
                                .withDescription("1.0kHz - 18.0kHz, Thru")
                                .withDefaultValue(34)
                                .withRange(34, 60)
                                .withDataTable(findDataTable("Table#3"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(14)
                                .withLabel("High Damp")
                                .withDescription("0.1 - 1.0")
                                .withDefaultValue(0)
                                .withRange(0, 10)
                                .build()
                )
                .build();
        parameterTables.add(Reverb2Table);

        ParameterTable Reverb3Table = parameterTableBuilder
                .clear()
                .withIdentifier("Reverb3")
                .withDescription("Block; Reverb, Variation")

                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(1)
                                .withLabel("Reverb Time")
                                .withDescription("0.3s - 30.0s")
                                .withDefaultValue(0)
                                .withRange(0, 69)
                                .withDataTable(findDataTable("Table#1"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(2)
                                .withLabel("Diffusion")
                                .withDescription("0 - 10")
                                .withDefaultValue(0)
                                .withRange(0, 10)
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(3)
                                .withLabel("Initial Delay Time")
                                .withDescription("0.1ms - 200.0ms")
                                .withDefaultValue(0)
                                .withRange(0, 127)
                                .withDataTable(findDataTable("Table#2"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(4)
                                .withLabel("HPF Cutoff Frequency")
                                .withDescription("Thru, 22Hz - 8.0kHz")
                                .withDefaultValue(0)
                                .withRange(0, 52)
                                .withDataTable(findDataTable("Table#3"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(5)
                                .withLabel("LPF Cutoff Frequency")
                                .withDescription("1.0kHz - 18.0kHz, Thru")
                                .withDefaultValue(34)
                                .withRange(34, 60)
                                .withDataTable(findDataTable("Table#3"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(6)
                                .withLabel("Width")
                                .withDescription("0.5m - 30.2m")
                                .withDefaultValue(0)
                                .withRange(0, 104)
                                .withDataTable(findDataTable("Table#4"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(7)
                                .withLabel("Height")
                                .withDescription("0.5m - 30.2m")
                                .withDefaultValue(0)
                                .withRange(0, 104)
                                .withDataTable(findDataTable("Table#4"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(8)
                                .withLabel("Depth")
                                .withDescription("0.5m - 30.2m")
                                .withDefaultValue(0)
                                .withRange(0, 104)
                                .withDataTable(findDataTable("Table#4"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(9)
                                .withLabel("Wall Vary")
                                .withDescription("0 - 30")
                                .withDefaultValue(0)
                                .withRange(0, 30)
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(10)
                                .withLabel("Dry/Wet")
                                .withDescription("D63>W - D=W - D<W63")
                                .withDefaultValue(1)
                                .withRange(1, 127)
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(11)
                                .withLabel("Reverb Delay Time")
                                .withDescription("0.1ms - 200.0ms")
                                .withDefaultValue(0)
                                .withRange(0, 127)
                                .withDataTable(findDataTable("Table#2"))
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(12)
                                .withLabel("Density")
                                .withDescription("0 - 4")
                                .withDefaultValue(0)
                                .withRange(0, 4)
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(13)
                                .withLabel("ER/Reverb Balance")
                                .withDescription("E63>R - R=R - E<R63")
                                .withDefaultValue(1)
                                .withRange(1, 127)
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(14)
                                .withLabel("High Damp")
                                .withDescription("0.1 - 1.0")
                                .withDefaultValue(1)
                                .withRange(1, 10)
                                .build()
                )
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(15)
                                .withLabel("Feedback Level")
                                .withDescription("-63 - 0 - +63")
                                .withDefaultValue(1)
                                .withRange(1, 127)
                                .build()
                )
                .build();
        parameterTables.add(Reverb3Table);

        ParameterTable ReverbTypesTable = parameterTableBuilder
                .clear()
                .withIdentifier("ReverbTypesTable")
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(1)
                                .withLabel("Reverb type")
                                .withDescription("0 - 4, 8")
                                .withDefaultValue(4)
                                .withRange(0, 8)
                                .withDataTable(findDataTable("ReverbTypeDataTable"))
                                .build()
                )
                .build();
        parameterTables.add(ReverbTypesTable);

        ParameterTable ChorusTypesTable = parameterTableBuilder
                .clear()
                .withIdentifier("ChorusTypesTable")
                .withEntry(
                        parameterTableEntryBuilder
                                .clear()
                                .withParameterId(1)
                                .withLabel("Chorus type")
                                .withDescription("0 - 5")
                                .withDefaultValue(2)
                                .withRange(0, 5)
                                .withDataTable(findDataTable("ChorusTypeDataTable"))
                                .build()
                )
                .build();
        parameterTables.add(ChorusTypesTable);
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
                .withEntry( 0x03, "D#/E♭")
                .withEntry( 0x04, "E")
                .withEntry( 0x05, "F")
                .withEntry( 0x06, "F#")
                .withEntry( 0x07, "G")
                .withEntry( 0x08, "G#/A♭")
                .withEntry( 0x09, "A")
                .withEntry( 0x0A, "A#/B♭")
                .withEntry( 0x0B, "B")
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

        DataTable Table1 = dataTableBuilder
                .clear()
                .withTableId("Table#1")
                .withTableDescription("Reverb Time [s]")
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
                .build();
        dataTables.add(Table1);

        DataTable Table2 = dataTableBuilder
                .clear()
                .withTableId("Table#2")
                .withTableDescription("Reverb Time [s]")
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
                .build();
        dataTables.add(Table2);

        DataTable Table3 = dataTableBuilder
                .clear()
                .withTableId("Table#3")
                .withTableDescription("EQ Frequency [Hz]")
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
                .build();
        dataTables.add(Table3);

        DataTable Table4 = dataTableBuilder
                .clear()
                .withTableId("Table#4")
                .withTableDescription("Reverb Width; Depth; Height [m]")
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
                .build();
        dataTables.add(Table3);

        DataTable ReverbTypeDataTable = dataTableBuilder
                .clear()
                .withTableId("ReverbTypeDataTable")
                .withTableDescription("Reverb Type")
                .withSize(9)
                .withEntry(0, "RoomS")
                .withEntry(1, "RoomM")
                .withEntry(2, "RoomL")
                .withEntry(3, "HallM")
                .withEntry(4, "HallL")
                .withEntry(8, "GM Plate")
                .build();
        dataTables.add(ReverbTypeDataTable);

        DataTable ChorusTypeDataTable = dataTableBuilder
                .clear()
                .withTableId("ChorusTypeDataTable")
                .withTableDescription("Chorus Type")
                .withSize(9)
                .withEntry(0, "GM Chorus1")
                .withEntry(1, "GM Chorus2")
                .withEntry(2, "GM Chorus3")
                .withEntry(3, "GM Chorus4")
                .withEntry(4, "FB Chorus")
                .withEntry(5, "GM Flanger")
                .build();
        dataTables.add(ChorusTypeDataTable);
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
                                .withName("Volume")
                                .withParameterType(ParameterType.PARAMETER)
                                .withParameterId(1)
                                .withNumBytes(2)
                                .build()
                )
                .build();

        Event eventMasterFineTuning = eventBuilder
                .clear()
                .withName("Master Fine-Tuning")
                .withSysex("7F 04 03")
                .withDefaultValues(0, 0)
                .withEventType(
                        eventTypeBuilder
                                .clear()
                                .withName("Master Fine-Tuning EventType")
                                .withEventValues(0, 0)
                                .withParameterTable(findParameterTable("FineTuningTable"))
                                .build()
                )
                .withEventParameter(
                        eventParameterBuilder
                                .clear()
                                .withName("FineTuning")
                                .withParameterType(ParameterType.PARAMETER)
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
                                .withName("CoarseTuning")
                                .withParameterType(ParameterType.PARAMETER)
                                .withParameterId(1)
                                .withNumBytes(1)
                                .build()
                )
                .build();

        Event eventReverbParameter = eventBuilder
                .clear()
                .withName("Reverb Parameter")
                .withSysex("7F 04 05 01 01 01 01 01")
                .withDefaultValues(0, 0)
                .withEventType(
                        eventTypeBuilder
                                .clear()
                                .withName("Reverb EventType")
                                .withEventValues(0, 0)
                                .withParameterTable(findParameterTable("ReverbTypesTable"))
                                .build()
                )
                .withEventParameter(
                        new EventParameter.EventParameterBuilder()
                                .clear()
                                .withName("Reverb Type")
                                .withParameterType(ParameterType.PARAMETER)
                                .withParameterId(1)
                                .withControllerId(0X00)
                                .withNumBytes(1)
                                .withEndian(LITTLE_ENDIAN)
                                // TODO Linked Parameters also need DisplayBlock
                                .withLinkedEventParameter(
                                        new EventParameter.EventParameterBuilder()
                                                .clear()
                                                .withName("Reverb Time")
                                                .withControllerId(0X01)
                                                .withNumBytes(1)
                                                .withEndian(LITTLE_ENDIAN)
                                                .build()
                                )
                                .build()
                )
                .build();

        Event eventChorusParameter = eventBuilder
                .clear()
                .withName("Chorus Parameter")
                .withSysex("7F 04 05 01 01 01 01 02")
                .withDefaultValues(0, 0)
                .withEventType(
                        eventTypeBuilder
                                .clear()
                                .withName("Chorus EventType")
                                .withEventValues(0, 0)
                                .withParameterTable(findParameterTable("ChorusTypesTable"))
                                .build()
                )
                .withEventParameter(
                        new EventParameter.EventParameterBuilder()
                                .clear()
                                .withName("Chorus Type")
                                .withParameterType(ParameterType.PARAMETER)
                                .withParameterId(1)
                                .withControllerId(0x00)
                                .withNumBytes(1)
                                .withEndian(LITTLE_ENDIAN)
                                .withLinkedEventParameter(
                                        new EventParameter.EventParameterBuilder()
                                                .clear()
                                                .withName("Mod Rate")
                                                .withControllerId(0x01)
                                                .withEndian(LITTLE_ENDIAN)
                                                .build()
                                )
                                .withLinkedEventParameter(
                                        new EventParameter.EventParameterBuilder()
                                                .clear()
                                                .withName("Mod Depth")
                                                .withControllerId(0x02)
                                                .withEndian(LITTLE_ENDIAN)
                                                .build()
                                )
                                .withLinkedEventParameter(
                                        new EventParameter.EventParameterBuilder()
                                                .clear()
                                                .withName("Feedback")
                                                .withControllerId(0x03)
                                                .withEndian(LITTLE_ENDIAN)
                                                .build()
                                )
                                .withLinkedEventParameter(
                                        new EventParameter.EventParameterBuilder()
                                                .clear()
                                                .withName("Send To Reverb")
                                                .withControllerId(0x04)
                                                .withEndian(LITTLE_ENDIAN)
                                                .build()
                                )
                                .build()
                )
                .build();

        // TODO Check Other EventParameters (and adapt the tests)
        Event eventChannelPressure = eventBuilder
                .clear()
                .withName("Aftertouch")
                .withSysex("7F 09 01")
                .withDefaultValues(0, 0)
                .withEventType(
                        eventTypeBuilder
                                .clear()
                                .withName("Channel EventType")
                                .withEventValues(0, 0)
                                .withParameterTable(findParameterTable("ChannelsTable"))
                                .build()
                )
                .withEventParameter(
                        eventParameterBuilder
                                .clear()
                                .withName("Channel")
                                .withParameterType(ParameterType.PARAMETER)
                                .withParameterId(1)
                                .withNumBytes(1)
                                .withEndian(LITTLE_ENDIAN)
                                .withUseLowerOnly(true)
                                .build()
                )
                // Here come the controllers
                .withEventParameter(
                        // Pitch Control
                        new EventParameter.EventParameterBuilder()
                                .clear()
                                .withName("Pitch Control")
                                .withParameterType(ParameterType.FREE) // PAY ATTENTION WITh ENDIAN
                                .withDisplayblock(displayBlockBuilder.clear().withLabel("Pitch Control").withDescription("-24..0..24").withRange(0x28, 0x58).withDefaultValue(0x40).build())
                                .withControllerId(0X00)
                                .withNumBytes(1)
                                .withEndian(LITTLE_ENDIAN)
                                .withLinkedEventParameter(
                                        // Filter Cutoff Control
                                        new EventParameter.EventParameterBuilder()
                                                .clear()
                                                .withName("Filter Cutoff Control")
                                                .withControllerId(0X01)
                                                .withEndian(LITTLE_ENDIAN)
                                                .build()
                                )
                                .withLinkedEventParameter(
                                        // Amplitude Control
                                        new EventParameter.EventParameterBuilder()
                                                .clear()
                                                .withName("Amplitude Control")
                                                .withControllerId(0X02)
                                                .withEndian(LITTLE_ENDIAN)
                                                .build()
                                )
                                .withLinkedEventParameter(
                                        // LFO Pitch Depth
                                        new EventParameter.EventParameterBuilder()
                                                .clear()
                                                .withName("LFO Pitch Depth")
                                                .withControllerId(0X03)
                                                .withEndian(LITTLE_ENDIAN)
                                                .build()
                                )
                                .withLinkedEventParameter(
                                        // LFO Filter Depth
                                        new EventParameter.EventParameterBuilder()
                                                .clear()
                                                .withName("LFO Filter Depth")
                                                .withControllerId(0X04)
                                                .withEndian(LITTLE_ENDIAN)
                                                .build()
                                )
                                .withLinkedEventParameter(
                                        // LFO Amplitude Depth
                                        new EventParameter.EventParameterBuilder()
                                                .clear()
                                                .withName("LFO Amplitude Depth")
                                                .withControllerId(0X05)
                                                .withEndian(LITTLE_ENDIAN)
                                                .build()
                                )
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
                .withChild(eventReverbParameter)
                .withChild(eventChorusParameter)
                .withChild(eventChannelPressure)
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

        Event eventScaleOctaveTuning =
                eventBuilder
                        .clear()
                        .withName("Scale/Octave Tuning")
                        .withSysex("7E 08 08")
                        .withEventParameter(eventParameterBuilder.clear().withName("Channel").withParameterType(ParameterType.FREE)
                                .withCallback((channel) -> {
                                    int jj = 0;
                                    int gg = 0;
                                    int mm = 0;

                                    if (channel == 16) {
                                        jj = 0x7F; // Bit 14 and 15 (binair: 00000011)
                                        gg = 0x7F; // Bit From 7 to 13  (binair: 01111111)
                                        mm = 0x7F; // Bit From 0  to 6   (binair: 01111111)
                                    } else {
                                        // Set Specific channel (from 0 to 15)
                                        if (channel >= 14) {
                                            // Channel 14 and 15 are set in the JJ byte
                                            jj = 1 << (channel - 14);
                                        } else if (channel >= 7) {
                                            // Channel 7 till 13 are set in the GG byte
                                            gg = 1 << (channel - 7);
                                        } else {
                                            // Channel 0 till 6 are set in the MM byte
                                            mm = 1 << channel;
                                        }
                                    }

                                    return String.format("%02X %02X %02X", (byte)jj, (byte)gg, (byte)mm);
                                })
                                .withDisplayblock(
                                        displayBlockBuilder
                                                .clear()
                                                .withLabel("Channel")
                                                .withDescription("0-16 (16 = ALL)")
                                                .withRange(0, 16)
                                                .withDefaultValue(0)
                                                .build()
                                )
                                .build())
                        .withEventParameter(eventParameterBuilder.clear().withName("C").withParameterType(ParameterType.FREE).withNumBytes(1).withEndian(LITTLE_ENDIAN)
                                .withDisplayblock(displayBlockBuilder.clear().withLabel("Tune C").withDescription("-64cent..0cent..63cent").withRange(0, 0x7F).withDefaultValues(0x00, 0x40).build()
                                ).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("C#").withParameterType(ParameterType.FREE).withNumBytes(1).withEndian(LITTLE_ENDIAN)
                                .withDisplayblock(displayBlockBuilder.clear().withLabel("Tune C#").withDescription("-64cent..0cent..63cent").withRange(0, 0x7F).withDefaultValues(0x00, 0x40).build()
                                ).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("D").withParameterType(ParameterType.FREE).withNumBytes(1).withEndian(LITTLE_ENDIAN)
                                .withDisplayblock(displayBlockBuilder.clear().withLabel("Tune D").withDescription("-64cent..0cent..63cent").withRange(0, 0x7F).withDefaultValues(0x00, 0x40).build()
                                ).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("D#").withParameterType(ParameterType.FREE).withNumBytes(1).withEndian(LITTLE_ENDIAN)
                                .withDisplayblock(displayBlockBuilder.clear().withLabel("Tune D#/E♭").withDescription("-64cent..0cent..63cent").withRange(0, 0x7F).withDefaultValues(0x00, 0x40).build()
                                ).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("E").withParameterType(ParameterType.FREE).withNumBytes(1).withEndian(LITTLE_ENDIAN)
                                .withDisplayblock(displayBlockBuilder.clear().withLabel("Tune E").withDescription("-64cent..0cent..63cent").withRange(0, 0x7F).withDefaultValues(0x00, 0x40).build()
                                ).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("F").withParameterType(ParameterType.FREE).withNumBytes(1).withEndian(LITTLE_ENDIAN)
                                .withDisplayblock(displayBlockBuilder.clear().withLabel("Tune F").withDescription("-64cent..0cent..63cent").withRange(0, 0x7F).withDefaultValues(0x00, 0x40).build()
                                ).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("F#").withParameterType(ParameterType.FREE).withNumBytes(1).withEndian(LITTLE_ENDIAN)
                                .withDisplayblock(displayBlockBuilder.clear().withLabel("Tune F#").withDescription("-64cent..0cent..63cent").withRange(0, 0x7F).withDefaultValues(0x00, 0x40).build()
                                ).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("G").withParameterType(ParameterType.FREE).withNumBytes(1).withEndian(LITTLE_ENDIAN)
                                .withDisplayblock(displayBlockBuilder.clear().withLabel("Tune G").withDescription("-64cent..0cent..63cent").withRange(0, 0x7F).withDefaultValues(0x00, 0x40).build()
                                ).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("G#").withParameterType(ParameterType.FREE).withNumBytes(1).withEndian(LITTLE_ENDIAN)
                                .withDisplayblock(displayBlockBuilder.clear().withLabel("Tune G#/A♭").withDescription("-64cent..0cent..63cent").withRange(0, 0x7F).withDefaultValues(0x00, 0x40).build()
                                ).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("A").withParameterType(ParameterType.FREE).withNumBytes(1).withEndian(LITTLE_ENDIAN)
                                .withDisplayblock(displayBlockBuilder.clear().withLabel("Tune A").withDescription("-64cent..0cent..63cent").withRange(0, 0x7F).withDefaultValues(0x00, 0x40).build()
                                ).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("A#").withParameterType(ParameterType.FREE).withNumBytes(1).withEndian(LITTLE_ENDIAN)
                                .withDisplayblock(displayBlockBuilder.clear().withLabel("Tune A#/B♭").withDescription("-64cent..0cent..63cent").withRange(0, 0x7F).withDefaultValues(0x00, 0x40).build()
                                ).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("B").withParameterType(ParameterType.FREE).withNumBytes(1).withEndian(LITTLE_ENDIAN)
                                .withDisplayblock(displayBlockBuilder.clear().withLabel("Tune B").withDescription("-64cent..0cent..63cent").withRange(0, 0x7F).withDefaultValues(0x00, 0x40).build()
                                ).build())
                        .build();

        Event eventUniversalNonRealTime = eventBuilder
                .clear()
                .withName("Universal Non-Real Time")
                .withSysex("7E")
                .withChild(eventGM1SystemOn)
                .withChild(eventGM2SystemOn)
                .withChild(eventGeneralMIDISystemOff)
                .withChild(eventScaleOctaveTuning)
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
                                .withName("Control")
                                .withParameterType(ParameterType.PARAMETER)
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
                                        .withName("Section")
                                        .withParameterType(ParameterType.PARAMETER)
                                        .withParameterId(1)
                                        .withNumBytes(1)
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("Switch")
                                        .withParameterType(ParameterType.PARAMETER)
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
                                        .withName("Type")
                                        .withParameterType(ParameterType.PARAMETER)
                                        .withParameterId(1)
                                        .withNumBytes(1)
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("Parameter ID")
                                        .withParameterType(ParameterType.PARAMETER)
                                        .withParameterId(2)
                                        .withNumBytes(1)
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("Switch")
                                        .withParameterType(ParameterType.PARAMETER)
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
                                        .withName("Number")
                                        .withParameterType(ParameterType.PARAMETER)
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
                                        .withName("Type")
                                        .withParameterType(ParameterType.PARAMETER)
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
                                        .withName("Type")
                                        .withParameterType(ParameterType.PARAMETER)
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
                                        .withName("Type")
                                        .withParameterType(ParameterType.PARAMETER)
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
                                        .withName("Type")
                                        .withParameterType(ParameterType.PARAMETER)
                                        .withParameterId(1)
                                        .withNumBytes(1)
                                        .build()
                        )
                        .build();

        Event eventGuide  =
                eventBuilder
                        .clear()
                        .withName("Guide")
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
                                        .withName("Part")
                                        .withParameterType(ParameterType.PARAMETER)
                                        .withParameterId(1)
                                        .withNumBytes(1)
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("Mode")
                                        .withParameterType(ParameterType.PARAMETER)
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
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("MSB")
                                        .withParameterType(ParameterType.FREE)
                                        .withParameterId(1)
                                        .withNumBytes(1, true)
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("LSB")
                                        .withParameterType(ParameterType.FREE)
                                        .withParameterId(2)
                                        .withNumBytes(1,true)
                                        .build()
                        )
                        .withEventParameter(
                                eventParameterBuilder
                                        .clear()
                                        .withName("DONT CARE")
                                        .withParameterType(ParameterType.FREE)
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
                        .withEventParameter(eventParameterBuilder.clear().withName("Type").withParameterType(ParameterType.PARAMETER).withParameterId(1).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Fixed").withParameterType(ParameterType.PARAMETER).withParameterId(2).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("C").withParameterType(ParameterType.PARAMETER).withParameterId(3).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("C#").withParameterType(ParameterType.PARAMETER).withParameterId(4).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("D").withParameterType(ParameterType.PARAMETER).withParameterId(5).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("D#").withParameterType(ParameterType.PARAMETER).withParameterId(6).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("E").withParameterType(ParameterType.PARAMETER).withParameterId(7).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("F").withParameterType(ParameterType.PARAMETER).withParameterId(8).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("F#").withParameterType(ParameterType.PARAMETER).withParameterId(9).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("G").withParameterType(ParameterType.PARAMETER).withParameterId(10).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("G#").withParameterType(ParameterType.PARAMETER).withParameterId(11).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("A").withParameterType(ParameterType.PARAMETER).withParameterId(12).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("A#").withParameterType(ParameterType.PARAMETER).withParameterId(13).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("B").withParameterType(ParameterType.PARAMETER).withParameterId(14).withNumBytes(1).build())
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
                        .withEventParameter(eventParameterBuilder.clear().withName("Base note").withParameterType(ParameterType.PARAMETER).withParameterId(1).withNumBytes(1).build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Mode").withParameterType(ParameterType.PARAMETER).withParameterId(2).withNumBytes(1).build())
                        .build();

        Event eventScore =
                eventBuilder
                    .clear()
                        .withName("Score")
                        .withSysex("73 01 50 12 00") // Here we must add sysex to parameter
                        .withDefaultValues(0, 0)
                        .isNotInline()
                        .withEventType(
                                eventTypeBuilder
                                        .clear()
                                        .withName("Score EventType")
                                        .withEventValues(0, 0)
                                        .withParameterTable(findParameterTable("ScoreTable"))
                                        .build()
                        )
                        .withEventParameter(eventParameterBuilder.clear().withName("Left Part Indication").withParameterType(ParameterType.PARAMETER).withParameterId(1).withNumBytes(1).withAddress("00").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Right Part Indication").withParameterType(ParameterType.PARAMETER).withParameterId(2).withNumBytes(1).withAddress("01").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Lyrics Indication").withParameterType(ParameterType.PARAMETER).withParameterId(3).withNumBytes(1).withAddress("02").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Chord Indication").withParameterType(ParameterType.PARAMETER).withParameterId(4).withNumBytes(1).withAddress("03").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Note Name Indication").withParameterType(ParameterType.PARAMETER).withParameterId(5).withNumBytes(1).withAddress("04").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Size").withParameterType(ParameterType.PARAMETER).withParameterId(6).withNumBytes(1).withAddress("05").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Left Channel").withParameterType(ParameterType.PARAMETER).withParameterId(7).withNumBytes(1).withAddress("06").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Right Channel").withParameterType(ParameterType.PARAMETER).withParameterId(8).withNumBytes(1).withAddress("07").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Quantize Triplet").withParameterType(ParameterType.PARAMETER).withParameterId(9).withNumBytes(1).withAddress("08").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Quantize").withParameterType(ParameterType.PARAMETER).withParameterId(10).withNumBytes(1).withAddress("09").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Note Name").withParameterType(ParameterType.PARAMETER).withParameterId(11).withNumBytes(1).withAddress("0A").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Color Note").withParameterType(ParameterType.PARAMETER).withParameterId(12).withNumBytes(1).withAddress("0B").build())
                    .build();

// Starting wit XG Change Parameter

        Event eventReverb =
                eventBuilder
                        .clear()
                        .withName("Reverb")
                        .withSysex("02 01")
                        .withDefaultValues(1, 0)
                        .isNotInline()

                        // Type Parameter
                        .withEventParameter(eventParameterBuilder.clear().withName("Type").withParameterType(ParameterType.TYPE).withNumBytes(2).withAddress("00").withEndian(LITTLE_ENDIAN).build())

                        // Add Parameters
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 1").withParameterType(ParameterType.PARAMETER).withParameterId(1).withNumBytes(1).withAddress("02").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 2").withParameterType(ParameterType.PARAMETER).withParameterId(2).withNumBytes(1).withAddress("03").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 3").withParameterType(ParameterType.PARAMETER).withParameterId(3).withNumBytes(1).withAddress("04").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 4").withParameterType(ParameterType.PARAMETER).withParameterId(4).withNumBytes(1).withAddress("05").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 5").withParameterType(ParameterType.PARAMETER).withParameterId(5).withNumBytes(1).withAddress("06").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 6").withParameterType(ParameterType.PARAMETER).withParameterId(6).withNumBytes(1).withAddress("07").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 7").withParameterType(ParameterType.PARAMETER).withParameterId(7).withNumBytes(1).withAddress("08").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 8").withParameterType(ParameterType.PARAMETER).withParameterId(8).withNumBytes(1).withAddress("09").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 9").withParameterType(ParameterType.PARAMETER).withParameterId(9).withNumBytes(1).withAddress("0A").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 10").withParameterType(ParameterType.PARAMETER).withParameterId(10).withNumBytes(1).withAddress("0B").build())

                        // For Free Parameters we need additional information (that is normally handled by Parameter Table
                        .withEventParameter(eventParameterBuilder.clear().withName("Reverb Return").withParameterType(ParameterType.FREE).withNumBytes(1).withAddress("0C")
                                .withDisplayblock(
                                        displayBlockBuilder
                                                .clear()
                                                .withLabel("Reverb Return")
                                                .withDescription("-8dB .. OdB .. +6dB")
                                                .withRange(0, 127)
                                                .withDefaultValue(64)
                                                .build()
                                )
                                .build()) // REVERB RETURN
                        .withEventParameter(eventParameterBuilder.clear().withName("Reverb Pan").withParameterType(ParameterType.FREE).withNumBytes(1).withAddress("0D")
                                .withDisplayblock(
                                        displayBlockBuilder
                                                .clear()
                                                .withLabel("Reverb Pan")
                                                .withDescription("L63..C..R63")
                                                .withRange(0, 127)
                                                .withDefaultValue(64)
                                                .build()
                                )
                                .build()) // REVERB PAN

                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 11").withParameterType(ParameterType.PARAMETER).withParameterId(11).withNumBytes(1).withAddress("10").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 12").withParameterType(ParameterType.PARAMETER).withParameterId(12).withNumBytes(1).withAddress("11").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 13").withParameterType(ParameterType.PARAMETER).withParameterId(13).withNumBytes(1).withAddress("12").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 14").withParameterType(ParameterType.PARAMETER).withParameterId(14).withNumBytes(1).withAddress("13").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 15").withParameterType(ParameterType.PARAMETER).withParameterId(15).withNumBytes(1).withAddress("14").build())
                        .withEventParameter(eventParameterBuilder.clear().withName("Parameter 16").withParameterType(ParameterType.PARAMETER).withParameterId(16).withNumBytes(1).withAddress("15").build())

                        // Add Types
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("ReadLrgHall").withEventValues(1,32).withParameterTable(findParameterTable("RealReverb")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("ReadMedHall").withEventValues(1,33).withParameterTable(findParameterTable("RealReverb")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("ReadBrtHall").withEventValues(1,34).withParameterTable(findParameterTable("RealReverb")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("BasicHall").withEventValues(1,21).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("LightHall").withEventValues(1,22).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("BalladHall").withEventValues(1,19).withParameterTable(findParameterTable("Reverb2")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("PianoHall").withEventValues(1,20).withParameterTable(findParameterTable("Reverb2")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("Hall1").withEventValues(1,0).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("Hall2").withEventValues(1,16).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("Hall3").withEventValues(1,17).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("Hall4").withEventValues(1,18).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("Hall5").withEventValues(1,1).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("VocalHall1").withEventValues(1,27).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("VocalHall2").withEventValues(1,28).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("RealRoom").withEventValues(2,32).withParameterTable(findParameterTable("RealReverb")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("RealPwrRoom").withEventValues(2,33).withParameterTable(findParameterTable("RealReverb")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("AcousticRoom").withEventValues(2,20).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("DrumsRoom").withEventValues(2,21).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("Stage1").withEventValues(3,16).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("RealLrgPlate").withEventValues(4,32).withParameterTable(findParameterTable("RealReverb")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("RealMedPlate").withEventValues(4,33).withParameterTable(findParameterTable("RealReverb")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("RealRtlPlate").withEventValues(4,34).withParameterTable(findParameterTable("RealReverb")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Reverb").withName("Plate1").withEventValues(4,16).withParameterTable(findParameterTable("Reverb1")).build())

                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("HallM").withEventValues(1,6).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("HallL").withEventValues(1,7).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("AtmoHall").withEventValues(1,23).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("LargeHall").withEventValues(1,2).withParameterTable(findParameterTable("Reverb2")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("MediumHall").withEventValues(1,3).withParameterTable(findParameterTable("Reverb2")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("PercRoom").withEventValues(2,22).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Room1").withEventValues(2,16).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Room2").withEventValues(2,17).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Room3").withEventValues(2,18).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Room4").withEventValues(2,19).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Room5").withEventValues(2,0).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Room6").withEventValues(2,1).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Room7").withEventValues(2,2).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("RoomS").withEventValues(2,5).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("RoomM").withEventValues(2,6).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("RoomL").withEventValues(2,7).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("WarmRoom").withEventValues(2,3).withParameterTable(findParameterTable("Reverb2")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("WhiteRoom").withEventValues(16,0).withParameterTable(findParameterTable("Reverb3")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("WoodyRoom").withEventValues(2,4).withParameterTable(findParameterTable("Reverb2")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Stage2").withEventValues(3,17).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Stage3").withEventValues(3,0).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Stage4").withEventValues(3,1).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Plate2").withEventValues(4,17).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Plate3").withEventValues(4,0).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("GM Plate").withEventValues(4,7).withParameterTable(findParameterTable("Reverb1")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("RichPlate").withEventValues(4,1).withParameterTable(findParameterTable("Reverb2")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Tunnel").withEventValues(17,0).withParameterTable(findParameterTable("Reverb3")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Canyon").withEventValues(18,0).withParameterTable(findParameterTable("Reverb3")).build())
                        .withEventType(eventTypeBuilder.clear().withCategory("Legacy").withName("Basement").withEventValues(19,0).withParameterTable(findParameterTable("Reverb3")).build())

                        .withEventType(eventTypeBuilder.clear().withCategory("None").withName("No Effect").withEventValues(0,0).build())

                        .build();

        Event eventXFChangeParameter =
                eventBuilder
                        .clear()
                        .withName("XG Change Parameter")
                        .withSysex("10 4C")
                        .withChild(eventReverb)
                        .build();

        Event eventSplitPoint =
                eventBuilder
                        .clear()
                        .withName("Split Point")
                        .withSysex("73 01 51 00 00 00 03 10 00")
                        // Here we don't use a EventType but just free parameters
                        .withEventParameter(eventParameterBuilder.clear().withName("Note Number").withParameterType(ParameterType.FREE).withNumBytes(1)
                                 .withDisplayblock(
                                        displayBlockBuilder
                                                .clear()
                                                .withLabel("Split point")
                                                .withDescription("Note number")
                                                .withRange(0, 127)
                                                .withDefaultValue(0)
                                                .build()
                                )
                                .build()) // SPLIT POINT
                        .build();

        Event eventStyleSelect =
                eventBuilder
                        .clear()
                        .withName("Select Style")
                        .withSysex("73 01 51 05 00 03 04 00 00")
                        // Here we don't use a EventType but just free parameters
                        .withEventParameter(eventParameterBuilder.clear().withName("Style Number").withParameterType(ParameterType.FREE).withEndian(LITTLE_ENDIAN).withNumBytes(2)
                                .withDisplayblock(
                                        displayBlockBuilder
                                                .clear()
                                                .withLabel("Style")
                                                .withDescription("Style number")
                                                .withRange(0, 65535)
                                                .withDefaultValue(0)
                                                .build()
                                )
                                .build()) // SPLIT POINT
                        .build();

        Event eventTempo =
                eventBuilder
                        .clear()
                        .withName("Tempo")
                        .withSysex("7E 01")
                        // Here we are going to define an event callback
                        .withEventParameter(eventParameterBuilder.clear().withName("BPM").withParameterType(ParameterType.FREE)
                                .withCallback((tempo) -> {
                                    int microsecondValue = Math.round(60_000_000F / tempo);

                                    byte t1 = (byte)(microsecondValue & 0x7F);
                                    byte t2 = (byte)((microsecondValue >> 7) & 0x7F);
                                    byte t3 = (byte)((microsecondValue >> 14) & 0x7F);
                                    byte t4 = (byte)((microsecondValue >> 21) & 0x7F);

                                    return String.format("%02X %02X %02X %02X", t4, t3, t2, t1);
                                })
                                .withDisplayblock(
                                        displayBlockBuilder
                                                .clear()
                                                .withLabel("Tempo")
                                                .withDescription("Tempo speed")
                                                .withRange(0, 1020)
                                                .withDefaultValue(100)
                                                .build()
                                )
                                .build()) // SPLIT POINT
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
                .withChild(eventGuide)
                .withChild(eventMIDIMasterTuning)
                .withChild(eventScaleTuning)
                .withChild(eventScaleTuningBaseNote)
                .withChild(eventScore)
                .withChild(eventXFChangeParameter)
                .withChild(eventSplitPoint)
                .withChild(eventStyleSelect)
                .withChild(eventTempo)
                .build();
        eventVendor.addChild(eventYamaha);  // this adds also into the rootEvent list vendor
    }
}

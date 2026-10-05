package creative.scenes.eventlist.parser.processor.data;

import entity.sysex.DataBlock;

import java.util.HashMap;
import java.util.Map;

public class ChorusTypes {
    private static final Map<DataBlock, String> CHORUS_REGISTRY = new HashMap<>();

    static {
        // Reverb
        CHORUS_REGISTRY.put(new DataBlock(1,0), "Hall1");
        CHORUS_REGISTRY.put(new DataBlock(1,16), "Hall2");
        CHORUS_REGISTRY.put(new DataBlock(1,17), "Hall3");
        CHORUS_REGISTRY.put(new DataBlock(1,18), "Hall4");
        CHORUS_REGISTRY.put(new DataBlock(1,1), "Hall5");
        CHORUS_REGISTRY.put(new DataBlock(2,20), "AcousticRoom");
        CHORUS_REGISTRY.put(new DataBlock(2,21), "DrumsRoom");
        CHORUS_REGISTRY.put(new DataBlock(3,16), "Stage1");
        CHORUS_REGISTRY.put(new DataBlock(4,16), "Plate1");

        // Delay
        CHORUS_REGISTRY.put(new DataBlock(21,0), "TempoDelay1");
        CHORUS_REGISTRY.put(new DataBlock(21,16), "TempoDelay2");
        CHORUS_REGISTRY.put(new DataBlock(21,8), "TempoEcho");
        CHORUS_REGISTRY.put(new DataBlock(22,0), "TempoCross1");
        CHORUS_REGISTRY.put(new DataBlock(22,16), "TempoCross2");
        CHORUS_REGISTRY.put(new DataBlock(22,17), "TempoCross3");
        CHORUS_REGISTRY.put(new DataBlock(22,18), "TempoCross4");

        // Modulation
        CHORUS_REGISTRY.put(new DataBlock(66,17), "Chorus1");
        CHORUS_REGISTRY.put(new DataBlock(66,8), "Chorus2");
        CHORUS_REGISTRY.put(new DataBlock(68,16), "Symphonic1");
        CHORUS_REGISTRY.put(new DataBlock(67,8), "Flanger1");
        CHORUS_REGISTRY.put(new DataBlock(107,0), "TempoFlanger");
        CHORUS_REGISTRY.put(new DataBlock(72,0), "Phaser1");
        CHORUS_REGISTRY.put(new DataBlock(108,0), "TempoPhaser1");
        CHORUS_REGISTRY.put(new DataBlock(72,17), "EPPhaser1");
        CHORUS_REGISTRY.put(new DataBlock(99,16), "DualRotBrt");
        CHORUS_REGISTRY.put(new DataBlock(99,17), "DualRotWarm");
        CHORUS_REGISTRY.put(new DataBlock(69,16), "RotarySp1");
        CHORUS_REGISTRY.put(new DataBlock(70,16), "Tremelo1");
        CHORUS_REGISTRY.put(new DataBlock(70,18), "EPTremelo");
        CHORUS_REGISTRY.put(new DataBlock(120,0), "TempoTremelo");
        CHORUS_REGISTRY.put(new DataBlock(71,16), "AutoPan1");
        CHORUS_REGISTRY.put(new DataBlock(121,0), "TempoAtPan1");

        // Legacy
        CHORUS_REGISTRY.put(new DataBlock(1,6), "HallM");
        CHORUS_REGISTRY.put(new DataBlock(1,7), "HallL");
        CHORUS_REGISTRY.put(new DataBlock(1,23), "AtmoHall");
        CHORUS_REGISTRY.put(new DataBlock(2,22), "PercRoom");
        CHORUS_REGISTRY.put(new DataBlock(2,16), "Room1");
        CHORUS_REGISTRY.put(new DataBlock(2,17), "Room2");
        CHORUS_REGISTRY.put(new DataBlock(2,18), "Room3");
        CHORUS_REGISTRY.put(new DataBlock(2,19), "Room4");
        CHORUS_REGISTRY.put(new DataBlock(2,0), "Room5");
        CHORUS_REGISTRY.put(new DataBlock(2,1), "Room6");
        CHORUS_REGISTRY.put(new DataBlock(2,2), "Room7");
        CHORUS_REGISTRY.put(new DataBlock(2,5), "RoomS");
        CHORUS_REGISTRY.put(new DataBlock(2,6), "RoomM");
        CHORUS_REGISTRY.put(new DataBlock(2,7), "RoomL");
        CHORUS_REGISTRY.put(new DataBlock(3,17), "Stage2");
        CHORUS_REGISTRY.put(new DataBlock(3,0), "Stage3");
        CHORUS_REGISTRY.put(new DataBlock(3,1), "Stage4");
        CHORUS_REGISTRY.put(new DataBlock(4,17), "Plate2");
        CHORUS_REGISTRY.put(new DataBlock(4,0), "Plate3");
        CHORUS_REGISTRY.put(new DataBlock(4,7), "GMPlate");
        CHORUS_REGISTRY.put(new DataBlock(20,0), "Karaoke1");
        CHORUS_REGISTRY.put(new DataBlock(20,1), "Karaoke2");
        CHORUS_REGISTRY.put(new DataBlock(20,2), "Karaoke3");
        CHORUS_REGISTRY.put(new DataBlock(9,0), "EarlyRef1");
        CHORUS_REGISTRY.put(new DataBlock(9,1), "EarlyRef2");
        CHORUS_REGISTRY.put(new DataBlock(66,16), "Chorus3");
        CHORUS_REGISTRY.put(new DataBlock(66,1), "Chorus4");
        CHORUS_REGISTRY.put(new DataBlock(65,2), "Chorus5");
        CHORUS_REGISTRY.put(new DataBlock(65,0), "Chorus6");
        CHORUS_REGISTRY.put(new DataBlock(65,1), "Chorus7");
        CHORUS_REGISTRY.put(new DataBlock(65,8), "Chorus8");
        CHORUS_REGISTRY.put(new DataBlock(65,16), "ChorusFast");
        CHORUS_REGISTRY.put(new DataBlock(65,17), "ChorusLite");
        CHORUS_REGISTRY.put(new DataBlock(65,3), "GMChorus1");
        CHORUS_REGISTRY.put(new DataBlock(65,4), "GMChorus2");
        CHORUS_REGISTRY.put(new DataBlock(65,5), "GMChorus3");
        CHORUS_REGISTRY.put(new DataBlock(65,6), "GMChorus4");
        CHORUS_REGISTRY.put(new DataBlock(65,7), "FeedBkChorus");
        CHORUS_REGISTRY.put(new DataBlock(66,0), "Celeste1");
        CHORUS_REGISTRY.put(new DataBlock(66,2), "Celeste2");
        CHORUS_REGISTRY.put(new DataBlock(68,0), "Symphonic2");
        CHORUS_REGISTRY.put(new DataBlock(87,0), "EnsDetune1");
        CHORUS_REGISTRY.put(new DataBlock(87,16), "EnsDetune2");
        CHORUS_REGISTRY.put(new DataBlock(67,16), "Flanger2");
        CHORUS_REGISTRY.put(new DataBlock(67,17), "Flanger3");
        CHORUS_REGISTRY.put(new DataBlock(67,1), "Flanger4");
        CHORUS_REGISTRY.put(new DataBlock(67,0), "Flanger5");
        CHORUS_REGISTRY.put(new DataBlock(67,7), "GMFlanger");
        CHORUS_REGISTRY.put(new DataBlock(72,8), "Phaser2");
        CHORUS_REGISTRY.put(new DataBlock(72,19), "Phaser3");
        CHORUS_REGISTRY.put(new DataBlock(108,16), "TempoPhaser2");
        CHORUS_REGISTRY.put(new DataBlock(72,18), "EPPhaser2");
        CHORUS_REGISTRY.put(new DataBlock(72,16), "EPPhaser3");
        CHORUS_REGISTRY.put(new DataBlock(99,0), "DualRotSp1");
        CHORUS_REGISTRY.put(new DataBlock(99,1), "DualRotSp2");
        CHORUS_REGISTRY.put(new DataBlock(71,17), "RotarySp2");
        CHORUS_REGISTRY.put(new DataBlock(71,18), "RotarySp3");
        CHORUS_REGISTRY.put(new DataBlock(70,17), "RotarySp4");
        CHORUS_REGISTRY.put(new DataBlock(66,18), "RotarySp5");
        CHORUS_REGISTRY.put(new DataBlock(69,0), "RotarySp6");
        CHORUS_REGISTRY.put(new DataBlock(71,22), "RotarySp7");
        CHORUS_REGISTRY.put(new DataBlock(86,0), "2WayRotarySp");
        CHORUS_REGISTRY.put(new DataBlock(71,19), "Tremolo2");
        CHORUS_REGISTRY.put(new DataBlock(70,0), "Tremolo3");
        CHORUS_REGISTRY.put(new DataBlock(71,20), "GtTremelo1");
        CHORUS_REGISTRY.put(new DataBlock(70,19), "GtTremelo2");
        CHORUS_REGISTRY.put(new DataBlock(119,0), "VibeRotor");
        CHORUS_REGISTRY.put(new DataBlock(71,0), "AutoPan2");
        CHORUS_REGISTRY.put(new DataBlock(71,1), "AutoPan3");
        CHORUS_REGISTRY.put(new DataBlock(71,21), "EpAutoPan");
        CHORUS_REGISTRY.put(new DataBlock(121,1), "TempoAtPan2");
        CHORUS_REGISTRY.put(new DataBlock(80,16), "PitchChange1");
        CHORUS_REGISTRY.put(new DataBlock(80,0), "PitchChange2");
        CHORUS_REGISTRY.put(new DataBlock(80,1), "PitchChange3");

        // NoEffect
        CHORUS_REGISTRY.put(new DataBlock(0,0), "No Effect");
    }

    public static String getChorusType(int msb, int lsb) {
        byte bMSB = (byte)(msb & 0xFF);
        byte bLSB = (byte)(lsb & 0xFF);

        DataBlock dataBlock = CHORUS_REGISTRY.keySet().stream().filter(b -> (b.getB1() == bMSB) && (b.getB2() == bLSB)).findFirst().orElse(null);
        if (dataBlock != null) {
            return CHORUS_REGISTRY.get(dataBlock);
        }

        return "unknown chorus type";
    }
}

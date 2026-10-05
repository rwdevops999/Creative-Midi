package creative.scenes.eventlist.parser.processor.data;

import entity.sysex.DataBlock;

import java.util.HashMap;
import java.util.Map;

public class VariationTypes {
    private static final Map<DataBlock, String> VARIATION_REGISTRY = new HashMap<>();

    static {
        // Reverb
        VARIATION_REGISTRY.put(new DataBlock(1,0), "Hall1");
        VARIATION_REGISTRY.put(new DataBlock(1,16), "Hall2");
        VARIATION_REGISTRY.put(new DataBlock(1,17), "Hall3");
        VARIATION_REGISTRY.put(new DataBlock(1,18), "Hall4");
        VARIATION_REGISTRY.put(new DataBlock(1,1), "Hall5");
        VARIATION_REGISTRY.put(new DataBlock(2,20), "AcousticRoom");
        VARIATION_REGISTRY.put(new DataBlock(2,21), "DrumsRoom");
        VARIATION_REGISTRY.put(new DataBlock(3,16), "Stage1");
        VARIATION_REGISTRY.put(new DataBlock(4,16), "Plate1");

        // Delay
        VARIATION_REGISTRY.put(new DataBlock(5,16), "DelayLCR1");
        VARIATION_REGISTRY.put(new DataBlock(5,0), "DelayLCR2");
        VARIATION_REGISTRY.put(new DataBlock(6,0), "DelayLR");
        VARIATION_REGISTRY.put(new DataBlock(7,0), "Echo");
        VARIATION_REGISTRY.put(new DataBlock(8,0), "CrossDelay1");
        VARIATION_REGISTRY.put(new DataBlock(8,16), "CrossDelay2");
        VARIATION_REGISTRY.put(new DataBlock(21,0), "TempoDelay1");
        VARIATION_REGISTRY.put(new DataBlock(21,16), "TempoDelay2");
        VARIATION_REGISTRY.put(new DataBlock(21,8), "TempoEcho");
        VARIATION_REGISTRY.put(new DataBlock(22,0), "TempoCross1");
        VARIATION_REGISTRY.put(new DataBlock(22,16), "TempoCross2");
        VARIATION_REGISTRY.put(new DataBlock(22,17), "TempoCross3");
        VARIATION_REGISTRY.put(new DataBlock(22,18), "TempoCross4");

        // Distortion
        VARIATION_REGISTRY.put(new DataBlock(95,32), "MltDistSolo");
        VARIATION_REGISTRY.put(new DataBlock(95,33), "MltDistBasic");
        VARIATION_REGISTRY.put(new DataBlock(95,34), "MltODChorus");
        VARIATION_REGISTRY.put(new DataBlock(95,35), "MltCrunchWah");
        VARIATION_REGISTRY.put(new DataBlock(95,36), "MltOldDelay");
        VARIATION_REGISTRY.put(new DataBlock(95,37), "MltVintgEcho");
        VARIATION_REGISTRY.put(new DataBlock(96,32), "SmallStDist");
        VARIATION_REGISTRY.put(new DataBlock(96,33), "SmallStOD");
        VARIATION_REGISTRY.put(new DataBlock(96,34), "SmallStVintg");
        VARIATION_REGISTRY.put(new DataBlock(96,35), "SmallStHeavy");
        VARIATION_REGISTRY.put(new DataBlock(97,32), "BCmbClassic");
        VARIATION_REGISTRY.put(new DataBlock(97,33), "BCmbTopBst");
        VARIATION_REGISTRY.put(new DataBlock(97,34), "BCmbCustom");
        VARIATION_REGISTRY.put(new DataBlock(97,35), "BCmbHeavy");
        VARIATION_REGISTRY.put(new DataBlock(98,32), "BLegndBlues");
        VARIATION_REGISTRY.put(new DataBlock(98,33), "BLegndHvy1");
        VARIATION_REGISTRY.put(new DataBlock(98,34), "BLegndHvy2");
        VARIATION_REGISTRY.put(new DataBlock(98,35), "BLegndClean");
        VARIATION_REGISTRY.put(new DataBlock(98,36), "BLegndDtCln");
        VARIATION_REGISTRY.put(new DataBlock(98,18), "VDistCrunch");
        VARIATION_REGISTRY.put(new DataBlock(98,21), "VDistBlues");
        VARIATION_REGISTRY.put(new DataBlock(75,29), "StAmpSolid");
        VARIATION_REGISTRY.put(new DataBlock(75,30), "StAmpCrunch");
        VARIATION_REGISTRY.put(new DataBlock(75,28), "StAmpBlues");
        VARIATION_REGISTRY.put(new DataBlock(98,1), "VDisHd+Dly");

        // EQ & Comp
        VARIATION_REGISTRY.put(new DataBlock(83,16), "CompMed");
        VARIATION_REGISTRY.put(new DataBlock(83,17), "CompHeavy");
        VARIATION_REGISTRY.put(new DataBlock(105,16), "CompMelody");
        VARIATION_REGISTRY.put(new DataBlock(105,17), "CompBass");
        VARIATION_REGISTRY.put(new DataBlock(76,17), "EQTelephone");
        VARIATION_REGISTRY.put(new DataBlock(76,0), "3BandEQ");

        // Modulation
        VARIATION_REGISTRY.put(new DataBlock(66,17), "Chorus1");
        VARIATION_REGISTRY.put(new DataBlock(66,8), "Chorus2");
        VARIATION_REGISTRY.put(new DataBlock(68,16), "Symphonic1");
        VARIATION_REGISTRY.put(new DataBlock(67,8), "Flanger1");
        VARIATION_REGISTRY.put(new DataBlock(104,0), "VFlanger");
        VARIATION_REGISTRY.put(new DataBlock(107,0), "TempoFlanger");
        VARIATION_REGISTRY.put(new DataBlock(72,0), "Phaser1");
        VARIATION_REGISTRY.put(new DataBlock(108,0), "TempoPhaser1");
        VARIATION_REGISTRY.put(new DataBlock(72,17), "EPPhaser1");
        VARIATION_REGISTRY.put(new DataBlock(78,16), "AutoWah1");
        VARIATION_REGISTRY.put(new DataBlock(78,17), "AtWah+Dist1");
        VARIATION_REGISTRY.put(new DataBlock(79,0), "TempoAutoWah");
        VARIATION_REGISTRY.put(new DataBlock(82,0), "TouchWah1");
        VARIATION_REGISTRY.put(new DataBlock(82,16), "TcWah+Dist1");
        VARIATION_REGISTRY.put(new DataBlock(122,0), "PedalWah");
        VARIATION_REGISTRY.put(new DataBlock(122,1), "PWah+Dist");
        VARIATION_REGISTRY.put(new DataBlock(99,16), "DualRotBrt");
        VARIATION_REGISTRY.put(new DataBlock(99,17), "DualRotWarm");
        VARIATION_REGISTRY.put(new DataBlock(69,16), "RotarySP1");
        VARIATION_REGISTRY.put(new DataBlock(70,16), "Tremelo1");
        VARIATION_REGISTRY.put(new DataBlock(70,18), "EPTremelo");
        VARIATION_REGISTRY.put(new DataBlock(120,0), "TempoTremelo");
        VARIATION_REGISTRY.put(new DataBlock(71,16), "AutoPan1");
        VARIATION_REGISTRY.put(new DataBlock(121,0), "TempoAtPan1");

        // Misc
        VARIATION_REGISTRY.put(new DataBlock(94,16), "LoopFX1");
        VARIATION_REGISTRY.put(new DataBlock(94,17), "LoopFX2");
        VARIATION_REGISTRY.put(new DataBlock(94,18), "Lo-FiDrum1");
        VARIATION_REGISTRY.put(new DataBlock(94,19), "Lo-FiDrum2");
        VARIATION_REGISTRY.put(new DataBlock(76,19), "Lo-FiDrum3");
        VARIATION_REGISTRY.put(new DataBlock(76,20), "Lo-FiDrum4");

        // Legacy
        VARIATION_REGISTRY.put(new DataBlock(1,6), "HallM");
        VARIATION_REGISTRY.put(new DataBlock(1,7), "HallL");
        VARIATION_REGISTRY.put(new DataBlock(1,23), "AtmoHall");
        VARIATION_REGISTRY.put(new DataBlock(2,22), "PercRoom");
        VARIATION_REGISTRY.put(new DataBlock(2,16), "Room1");
        VARIATION_REGISTRY.put(new DataBlock(2,17), "Room2");
        VARIATION_REGISTRY.put(new DataBlock(2,18), "Room3");
        VARIATION_REGISTRY.put(new DataBlock(2,19), "Room4");
        VARIATION_REGISTRY.put(new DataBlock(2,0), "Room5");
        VARIATION_REGISTRY.put(new DataBlock(2,1), "Room6");
        VARIATION_REGISTRY.put(new DataBlock(2,2), "Room7");
        VARIATION_REGISTRY.put(new DataBlock(2,5), "RoomS");
        VARIATION_REGISTRY.put(new DataBlock(2,6), "RoomM");
        VARIATION_REGISTRY.put(new DataBlock(2,7), "RoomL");
        VARIATION_REGISTRY.put(new DataBlock(16,0), "WhiteRoom");
        VARIATION_REGISTRY.put(new DataBlock(3,17), "Stage2");
        VARIATION_REGISTRY.put(new DataBlock(3,0), "Stage3");
        VARIATION_REGISTRY.put(new DataBlock(3,1), "Stage4");
        VARIATION_REGISTRY.put(new DataBlock(4,17), "Plate2");
        VARIATION_REGISTRY.put(new DataBlock(4,0), "Plate3");
        VARIATION_REGISTRY.put(new DataBlock(4,7), "GMPLate");
        VARIATION_REGISTRY.put(new DataBlock(17,0), "Tunnel");
        VARIATION_REGISTRY.put(new DataBlock(18,0), "Canyon");
        VARIATION_REGISTRY.put(new DataBlock(19,0), "Basement");
        VARIATION_REGISTRY.put(new DataBlock(20,0), "Karaoke1");
        VARIATION_REGISTRY.put(new DataBlock(20,1), "Karaoke2");
        VARIATION_REGISTRY.put(new DataBlock(20,2), "Karaoke3");
        VARIATION_REGISTRY.put(new DataBlock(9,0), "EarlyRef1");
        VARIATION_REGISTRY.put(new DataBlock(9,1), "EarlyRef2");
        VARIATION_REGISTRY.put(new DataBlock(10,0), "GateReverb1");
        VARIATION_REGISTRY.put(new DataBlock(10,16), "GateReverb2");
        VARIATION_REGISTRY.put(new DataBlock(11,0), "ReverseGate");
        VARIATION_REGISTRY.put(new DataBlock(98,22), "VDistWarm");
        VARIATION_REGISTRY.put(new DataBlock(98,23), "VDistClsHd");
        VARIATION_REGISTRY.put(new DataBlock(98,20), "VDistClsSft");
        VARIATION_REGISTRY.put(new DataBlock(98,24), "VDistMetal");
        VARIATION_REGISTRY.put(new DataBlock(98,19), "VDistEdgy");
        VARIATION_REGISTRY.put(new DataBlock(98,25), "VDistSolid");
        VARIATION_REGISTRY.put(new DataBlock(98,17), "VDistClean1");
        VARIATION_REGISTRY.put(new DataBlock(98,26), "VDistClean2");
        VARIATION_REGISTRY.put(new DataBlock(98,16), "VDistTwin");
        VARIATION_REGISTRY.put(new DataBlock(103,18), "VDistRockbly");
        VARIATION_REGISTRY.put(new DataBlock(98,27), "VDistJzCln");
        VARIATION_REGISTRY.put(new DataBlock(103,19), "VDistFusion");
        VARIATION_REGISTRY.put(new DataBlock(98,0), "VDistHard");
        VARIATION_REGISTRY.put(new DataBlock(98,2), "VDistSoft");
        VARIATION_REGISTRY.put(new DataBlock(75,27), "StAmpClean");
        VARIATION_REGISTRY.put(new DataBlock(75,31), "StAmpHarp");
        VARIATION_REGISTRY.put(new DataBlock(75,16), "DistHard1");
        VARIATION_REGISTRY.put(new DataBlock(75,22), "DistHard2");
        VARIATION_REGISTRY.put(new DataBlock(75,17), "DistSoft1");
        VARIATION_REGISTRY.put(new DataBlock(75,23), "DistSoft2");
        VARIATION_REGISTRY.put(new DataBlock(73,0), "DistHeavy");
        VARIATION_REGISTRY.put(new DataBlock(74,0), "Overdrive");
        VARIATION_REGISTRY.put(new DataBlock(73,8), "StDistortion");
        VARIATION_REGISTRY.put(new DataBlock(74,8), "StOverdrive");
        VARIATION_REGISTRY.put(new DataBlock(75,18), "StDistHard");
        VARIATION_REGISTRY.put(new DataBlock(75,19), "StDistSoft");
        VARIATION_REGISTRY.put(new DataBlock(75,0), "AmpSim1");
        VARIATION_REGISTRY.put(new DataBlock(75,1), "AmpSim2");
        VARIATION_REGISTRY.put(new DataBlock(75,20), "StAmpSim1");
        VARIATION_REGISTRY.put(new DataBlock(75,21), "StAmpSim2");
        VARIATION_REGISTRY.put(new DataBlock(75,8), "StAmpSim3");
        VARIATION_REGISTRY.put(new DataBlock(75,24), "StAmpSim4");
        VARIATION_REGISTRY.put(new DataBlock(75,25), "StAmpSim5");
        VARIATION_REGISTRY.put(new DataBlock(75,26), "StAmpSim6");
        VARIATION_REGISTRY.put(new DataBlock(95,16), "Dist+Delay1");
        VARIATION_REGISTRY.put(new DataBlock(95,0), "Dist+Delay2");
        VARIATION_REGISTRY.put(new DataBlock(95,17), "OD+Delay1");
        VARIATION_REGISTRY.put(new DataBlock(95,1), "OD+Delay2");
        VARIATION_REGISTRY.put(new DataBlock(96,16), "Cmp+Dst+Dly1");
        VARIATION_REGISTRY.put(new DataBlock(96,0), "Cmp+Dst+Dly2");
        VARIATION_REGISTRY.put(new DataBlock(96,17), "Cmp+OD+Dly1");
        VARIATION_REGISTRY.put(new DataBlock(96,1), "Cmp+OD+Dly2");
        VARIATION_REGISTRY.put(new DataBlock(98,3), "VDistS+Dly");
        VARIATION_REGISTRY.put(new DataBlock(103,0), "VDistH+TDly1");
        VARIATION_REGISTRY.put(new DataBlock(103,17), "VDistH+TDly2");
        VARIATION_REGISTRY.put(new DataBlock(103,1), "VDistS+TDly1");
        VARIATION_REGISTRY.put(new DataBlock(103,16), "VDistS+TDly2");
        VARIATION_REGISTRY.put(new DataBlock(100,0), "Dst+TmpDelay");
        VARIATION_REGISTRY.put(new DataBlock(100,1), "OD+TmpDelay");
        VARIATION_REGISTRY.put(new DataBlock(73,16), "Comp+Dist1");
        VARIATION_REGISTRY.put(new DataBlock(73,1), "Comp+Dist2");
        VARIATION_REGISTRY.put(new DataBlock(101,0), "Cmp+Dst+TDly");
        VARIATION_REGISTRY.put(new DataBlock(101,1), "Cmp+OD+TDly1");
        VARIATION_REGISTRY.put(new DataBlock(101,16), "Cmp+OD+TDly2");
        VARIATION_REGISTRY.put(new DataBlock(101,17), "Cmp+OD+TDly3");
        VARIATION_REGISTRY.put(new DataBlock(101,18), "Cmp+OD+TDly4");
        VARIATION_REGISTRY.put(new DataBlock(101,19), "Cmp+OD+TDly5");
        VARIATION_REGISTRY.put(new DataBlock(101,20), "Cmp+OD+TDly6");
        VARIATION_REGISTRY.put(new DataBlock(105,0), "MltBandComp");
        VARIATION_REGISTRY.put(new DataBlock(83,0), "Compressor");
        VARIATION_REGISTRY.put(new DataBlock(84,0), "NoiseGate");
        VARIATION_REGISTRY.put(new DataBlock(76,16), "EQDisco");
        VARIATION_REGISTRY.put(new DataBlock(77,0), "2BandEQ");
        VARIATION_REGISTRY.put(new DataBlock(76,18), "St3BAndEQ");
        VARIATION_REGISTRY.put(new DataBlock(81,16), "HmEnhance1");
        VARIATION_REGISTRY.put(new DataBlock(81,0), "HmEnhance2");
        VARIATION_REGISTRY.put(new DataBlock(115,0), "Isolator");
        VARIATION_REGISTRY.put(new DataBlock(66,16), "Chorus3");
        VARIATION_REGISTRY.put(new DataBlock(66,1), "Chorus4");
        VARIATION_REGISTRY.put(new DataBlock(65,2), "Chorus5");
        VARIATION_REGISTRY.put(new DataBlock(65,0), "Chorus6");
        VARIATION_REGISTRY.put(new DataBlock(65,1), "Chorus7");
        VARIATION_REGISTRY.put(new DataBlock(65,9), "Chorus8");
        VARIATION_REGISTRY.put(new DataBlock(65,16), "ChorusFast");
        VARIATION_REGISTRY.put(new DataBlock(65,17), "ChorusLite");
        VARIATION_REGISTRY.put(new DataBlock(65,3), "GMChorus1");
        VARIATION_REGISTRY.put(new DataBlock(65,4), "GMChorus2");
        VARIATION_REGISTRY.put(new DataBlock(65,5), "GMChorus3");
        VARIATION_REGISTRY.put(new DataBlock(65,6), "GMChorus4");
        VARIATION_REGISTRY.put(new DataBlock(65,7), "FeedBkChorus");
        VARIATION_REGISTRY.put(new DataBlock(66,0), "Celeste1");
        VARIATION_REGISTRY.put(new DataBlock(66,2), "Celeste2");
        VARIATION_REGISTRY.put(new DataBlock(68,0), "Symphonic2");
        VARIATION_REGISTRY.put(new DataBlock(87,0), "EnsDetune1");
        VARIATION_REGISTRY.put(new DataBlock(87,16), "EnsDetune2");
        VARIATION_REGISTRY.put(new DataBlock(65,9), "AmbiChorus");
        VARIATION_REGISTRY.put(new DataBlock(66,9), "AmbiCeleste");
        VARIATION_REGISTRY.put(new DataBlock(68,9), "AmbiSympho");
        VARIATION_REGISTRY.put(new DataBlock(67,16), "Flanger2");
        VARIATION_REGISTRY.put(new DataBlock(67,17), "Flanger2");
        VARIATION_REGISTRY.put(new DataBlock(67,1), "Flanger4");
        VARIATION_REGISTRY.put(new DataBlock(67,0), "Flanger5");
        VARIATION_REGISTRY.put(new DataBlock(67,7), "GMFlanger");
        VARIATION_REGISTRY.put(new DataBlock(110,0), "DynFlanger");
        VARIATION_REGISTRY.put(new DataBlock(67,9), "AmbiFlanger");
        VARIATION_REGISTRY.put(new DataBlock(72,8), "Phaser2");
        VARIATION_REGISTRY.put(new DataBlock(72,19), "Phaser3");
        VARIATION_REGISTRY.put(new DataBlock(108,16), "TempoPhaser2");
        VARIATION_REGISTRY.put(new DataBlock(72,18), "EPPhaser2");
        VARIATION_REGISTRY.put(new DataBlock(72,16), "EPPhaser3");
        VARIATION_REGISTRY.put(new DataBlock(111,0), "DynPhaser");
        VARIATION_REGISTRY.put(new DataBlock(78,0), "AutoWah2");
        VARIATION_REGISTRY.put(new DataBlock(78,1), "AtWah+Dist2");
        VARIATION_REGISTRY.put(new DataBlock(78,21), "AtWah+DistHd");
        VARIATION_REGISTRY.put(new DataBlock(78,23), "AtWah+DistHv");

        VARIATION_REGISTRY.put(new DataBlock(78,25), "AtWah+DistLt");
        VARIATION_REGISTRY.put(new DataBlock(78,18), "AtWah+OD1");
        VARIATION_REGISTRY.put(new DataBlock(78,2), "AtWah+OD2");
        VARIATION_REGISTRY.put(new DataBlock(78,22), "AtWah+ODHd");
        VARIATION_REGISTRY.put(new DataBlock(78,24), "AtWah+ODHv");
        VARIATION_REGISTRY.put(new DataBlock(78,26), "AtWah+ODLt");
        VARIATION_REGISTRY.put(new DataBlock(79,1), "T.AtWah+Dst");
        VARIATION_REGISTRY.put(new DataBlock(79,21), "T.AtWah+DstHd");
        VARIATION_REGISTRY.put(new DataBlock(79,23), "T.AtWah+DstHv");
        VARIATION_REGISTRY.put(new DataBlock(79,25), "T.AtWah+DstLt");

        VARIATION_REGISTRY.put(new DataBlock(79,2), "T.AtWah+OD");
        VARIATION_REGISTRY.put(new DataBlock(79,22), "T.AtWah+ODHd");
        VARIATION_REGISTRY.put(new DataBlock(79,24), "T.AtWah+ODHv");
        VARIATION_REGISTRY.put(new DataBlock(79,26), "T.AtWah+ODLt");
        VARIATION_REGISTRY.put(new DataBlock(82,8), "TouchWah2");
        VARIATION_REGISTRY.put(new DataBlock(82,20), "TouchWah3");
        VARIATION_REGISTRY.put(new DataBlock(82,1), "TcWah+Dist2");
        VARIATION_REGISTRY.put(new DataBlock(82,21), "TcWah+DistHd");
        VARIATION_REGISTRY.put(new DataBlock(82,23), "TcWah+DistHv");
        VARIATION_REGISTRY.put(new DataBlock(82,25), "TcWah+DistLt");

        VARIATION_REGISTRY.put(new DataBlock(82,17), "TcWah+OD1");
        VARIATION_REGISTRY.put(new DataBlock(82,2), "TcWah+OD2");
        VARIATION_REGISTRY.put(new DataBlock(82,22), "TcWah+ODHd");
        VARIATION_REGISTRY.put(new DataBlock(82,24), "TcWah+ODHv");
        VARIATION_REGISTRY.put(new DataBlock(82,26), "TcWah+ODLt");
        VARIATION_REGISTRY.put(new DataBlock(97,16), "Wah+Dst+Dly1");
        VARIATION_REGISTRY.put(new DataBlock(97,0), "Wah+Dst+Dly2");
        VARIATION_REGISTRY.put(new DataBlock(102,0), "Wah+Dst+TDly");
        VARIATION_REGISTRY.put(new DataBlock(97,17), "Wah+OD+Dly1");
        VARIATION_REGISTRY.put(new DataBlock(97,1), "Wah+OD+Dly2");

        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");

        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");

        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");

        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");

        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");
        VARIATION_REGISTRY.put(new DataBlock(,), "");

        VARIATION_REGISTRY.put(new DataBlock(,), "");

        // NoEffect
        VARIATION_REGISTRY.put(new DataBlock(0,0), "No Effect");
        VARIATION_REGISTRY.put(new DataBlock(64,0), "Thru");
    }

    public static String getVariationType(int msb, int lsb) {
        byte bMSB = (byte)(msb & 0xFF);
        byte bLSB = (byte)(lsb & 0xFF);

        DataBlock dataBlock = VARIATION_REGISTRY.keySet().stream().filter(b -> (b.getB1() == bMSB) && (b.getB2() == bLSB)).findFirst().orElse(null);
        if (dataBlock != null) {
            return VARIATION_REGISTRY.get(dataBlock);
        }

        return "unknown variation type";
    }
}

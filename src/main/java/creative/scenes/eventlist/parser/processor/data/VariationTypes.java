package creative.scenes.eventlist.parser.processor.data;

import entity.sysex.DataBlock;

import java.util.HashMap;
import java.util.Map;

public class VariationTypes {
    private static final Map<DataBlock, String> VARIATION_REGISTRY = new HashMap<>();

    static {
        // Reverb
        VARIATION_REGISTRY.put(new DataBlock(,), "Hall1");
        VARIATION_REGISTRY.put(new DataBlock(,), "Hall2");
        VARIATION_REGISTRY.put(new DataBlock(,), "Hall3");
        VARIATION_REGISTRY.put(new DataBlock(,), "Hall4");
        VARIATION_REGISTRY.put(new DataBlock(,), "Hall5");
        VARIATION_REGISTRY.put(new DataBlock(,), "AcousticRoom");
        VARIATION_REGISTRY.put(new DataBlock(,), "DrumsRoom");
        VARIATION_REGISTRY.put(new DataBlock(,), "Stage1");
        VARIATION_REGISTRY.put(new DataBlock(,), "Plate1");

        // Delay
        VARIATION_REGISTRY.put(new DataBlock(,), "DelayLCR1");
        VARIATION_REGISTRY.put(new DataBlock(,), "DelayLCR2");
        VARIATION_REGISTRY.put(new DataBlock(,), "DelayLR");
        VARIATION_REGISTRY.put(new DataBlock(,), "Echo");
        VARIATION_REGISTRY.put(new DataBlock(,), "CrossDelay1");
        VARIATION_REGISTRY.put(new DataBlock(,), "CrossDelay2");
        VARIATION_REGISTRY.put(new DataBlock(,), "TempoDelay1");
        VARIATION_REGISTRY.put(new DataBlock(,), "TempoDelay2");
        VARIATION_REGISTRY.put(new DataBlock(,), "TempoEcho");
        VARIATION_REGISTRY.put(new DataBlock(,), "TempoCross1");
        VARIATION_REGISTRY.put(new DataBlock(,), "TempoCross2");
        VARIATION_REGISTRY.put(new DataBlock(,), "TempoCross3");
        VARIATION_REGISTRY.put(new DataBlock(,), "TempoCross4");

        // Distortion
        VARIATION_REGISTRY.put(new DataBlock(,), "MltDistSolo");
        VARIATION_REGISTRY.put(new DataBlock(,), "MltDistBasic");
        VARIATION_REGISTRY.put(new DataBlock(,), "MltODChorus");
        VARIATION_REGISTRY.put(new DataBlock(,), "MltCrunchWah");
        VARIATION_REGISTRY.put(new DataBlock(,), "MltOldDelay");
        VARIATION_REGISTRY.put(new DataBlock(,), "MltVintgEcho");
        VARIATION_REGISTRY.put(new DataBlock(,), "SmallStDist");
        VARIATION_REGISTRY.put(new DataBlock(,), "SmallStOD");
        VARIATION_REGISTRY.put(new DataBlock(,), "SmallStVintg");
        VARIATION_REGISTRY.put(new DataBlock(,), "SmallStHeavy");
        VARIATION_REGISTRY.put(new DataBlock(,), "BCmbClassic");
        VARIATION_REGISTRY.put(new DataBlock(,), "BCmbTopBst");
        VARIATION_REGISTRY.put(new DataBlock(,), "BCmbCustom");
        VARIATION_REGISTRY.put(new DataBlock(,), "BCmbHeavy");
        VARIATION_REGISTRY.put(new DataBlock(,), "BLegndBlues");
        VARIATION_REGISTRY.put(new DataBlock(,), "BLegndHvy1");
        VARIATION_REGISTRY.put(new DataBlock(,), "BLegndHvy2");
        VARIATION_REGISTRY.put(new DataBlock(,), "BLegndClean");
        VARIATION_REGISTRY.put(new DataBlock(,), "BLegndDtCln");
        VARIATION_REGISTRY.put(new DataBlock(,), "VDistCrunch");
        VARIATION_REGISTRY.put(new DataBlock(,), "VDistBlues");
        VARIATION_REGISTRY.put(new DataBlock(,), "StAmpSolid");
        VARIATION_REGISTRY.put(new DataBlock(,), "StAmpCrunch");
        VARIATION_REGISTRY.put(new DataBlock(,), "StAmpBlues");
        VARIATION_REGISTRY.put(new DataBlock(,), "VDisHd+Dly");

        // EQ & Comp
        VARIATION_REGISTRY.put(new DataBlock(,), "CompMed");
        VARIATION_REGISTRY.put(new DataBlock(,), "CompHeavy");
        VARIATION_REGISTRY.put(new DataBlock(,), "CompMelody");
        VARIATION_REGISTRY.put(new DataBlock(,), "CompBass");
        VARIATION_REGISTRY.put(new DataBlock(,), "EQTelephone");
        VARIATION_REGISTRY.put(new DataBlock(,), "3BandEQ");

        // Modulation
        VARIATION_REGISTRY.put(new DataBlock(,), "Chorus1");
        VARIATION_REGISTRY.put(new DataBlock(,), "Chorus2");
        VARIATION_REGISTRY.put(new DataBlock(,), "Symphonic1");
        VARIATION_REGISTRY.put(new DataBlock(,), "Flanger1");
        VARIATION_REGISTRY.put(new DataBlock(,), "VFlanger");
        VARIATION_REGISTRY.put(new DataBlock(,), "TempoFlanger");
        VARIATION_REGISTRY.put(new DataBlock(,), "Phaser1");
        VARIATION_REGISTRY.put(new DataBlock(,), "TempoPhaser1");
        VARIATION_REGISTRY.put(new DataBlock(,), "EPPhaser1");
        VARIATION_REGISTRY.put(new DataBlock(,), "AutoWah1");
        VARIATION_REGISTRY.put(new DataBlock(,), "AtWah+Dist1");
        VARIATION_REGISTRY.put(new DataBlock(,), "TempoAutoWah");
        VARIATION_REGISTRY.put(new DataBlock(,), "TouchWah1");
        VARIATION_REGISTRY.put(new DataBlock(,), "TcWah+Dist1");
        VARIATION_REGISTRY.put(new DataBlock(,), "PedalWah");
        VARIATION_REGISTRY.put(new DataBlock(,), "PWah+Dist");
        VARIATION_REGISTRY.put(new DataBlock(,), "DualRotBrt");
        VARIATION_REGISTRY.put(new DataBlock(,), "DualRotWarm");
        VARIATION_REGISTRY.put(new DataBlock(,), "RotarySP1");
        VARIATION_REGISTRY.put(new DataBlock(,), "Tremelo1");

        // Misc
        VARIATION_REGISTRY.put(new DataBlock(,), "");

        // Legacy
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

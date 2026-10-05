package creative.scenes.eventlist.parser.processor.data;

import entity.sysex.DataBlock;

import java.util.HashMap;
import java.util.Map;

public class ReverbTypes {
    private static final Map<DataBlock, String> REVERB_REGISTRY = new HashMap<>();

    static {
        // No Effect
        REVERB_REGISTRY.put(new DataBlock(0,0), "No Effect");

        // Reverb
        REVERB_REGISTRY.put(new DataBlock(1,32), "RealLrgHall");
        REVERB_REGISTRY.put(new DataBlock(1,33), "RealMedHall");
        REVERB_REGISTRY.put(new DataBlock(1,34), "RealBrtHall");
        REVERB_REGISTRY.put(new DataBlock(1,21), "BasicHall");
        REVERB_REGISTRY.put(new DataBlock(1,22), "LightHall");
        REVERB_REGISTRY.put(new DataBlock(1,19), "BalladHall");
        REVERB_REGISTRY.put(new DataBlock(1,20), "PianoHall");
        REVERB_REGISTRY.put(new DataBlock(1,0), "Hall1");
        REVERB_REGISTRY.put(new DataBlock(1,16), "Hall2");
        REVERB_REGISTRY.put(new DataBlock(1,17), "Hall3");
        REVERB_REGISTRY.put(new DataBlock(1,18), "Hall4");
        REVERB_REGISTRY.put(new DataBlock(1,1), "Hall5");
        REVERB_REGISTRY.put(new DataBlock(1,27), "VocalHall1");
        REVERB_REGISTRY.put(new DataBlock(1,28), "VocalHall2");
        REVERB_REGISTRY.put(new DataBlock(2,32), "RealRoom");
        REVERB_REGISTRY.put(new DataBlock(2,33), "RealPwrRoom");
        REVERB_REGISTRY.put(new DataBlock(2,20), "AcousticRoom");
        REVERB_REGISTRY.put(new DataBlock(2,21), "DrumsRoom");
        REVERB_REGISTRY.put(new DataBlock(3,16), "Stage1");
        REVERB_REGISTRY.put(new DataBlock(4,32), "RealLrgPlate");
        REVERB_REGISTRY.put(new DataBlock(4,33), "RealMedPlate");
        REVERB_REGISTRY.put(new DataBlock(4,34), "RealRtlPlate");
        REVERB_REGISTRY.put(new DataBlock(4,16), "Plate1");

        // Legacy
        REVERB_REGISTRY.put(new DataBlock(1,6), "HallM");
        REVERB_REGISTRY.put(new DataBlock(1,7), "HallL");
        REVERB_REGISTRY.put(new DataBlock(1,23), "AtmoHall");
        REVERB_REGISTRY.put(new DataBlock(1,2), "LargeHall");
        REVERB_REGISTRY.put(new DataBlock(1,3), "MediumHall");
        REVERB_REGISTRY.put(new DataBlock(2,22), "PercRoom");
        REVERB_REGISTRY.put(new DataBlock(2,16), "Room1");
        REVERB_REGISTRY.put(new DataBlock(2,17), "Room2");
        REVERB_REGISTRY.put(new DataBlock(2,18), "Room3");
        REVERB_REGISTRY.put(new DataBlock(2,19), "Room4");
        REVERB_REGISTRY.put(new DataBlock(2,0), "Room5");
        REVERB_REGISTRY.put(new DataBlock(2,1), "Room6");
        REVERB_REGISTRY.put(new DataBlock(2,2), "Room7");
        REVERB_REGISTRY.put(new DataBlock(2,5), "RoomS");
        REVERB_REGISTRY.put(new DataBlock(2,6), "RoomM");
        REVERB_REGISTRY.put(new DataBlock(2,7), "RoomL");
        REVERB_REGISTRY.put(new DataBlock(2,3), "WarmRoom");
        REVERB_REGISTRY.put(new DataBlock(16,0), "WhiteRoom");
        REVERB_REGISTRY.put(new DataBlock(2,4), "WoodyRoom");
        REVERB_REGISTRY.put(new DataBlock(3,17), "Stage2");
        REVERB_REGISTRY.put(new DataBlock(3,0), "Stage3");
        REVERB_REGISTRY.put(new DataBlock(3,1), "Stage4");
        REVERB_REGISTRY.put(new DataBlock(4,17), "Plate2");
        REVERB_REGISTRY.put(new DataBlock(4,0), "Plate3");
        REVERB_REGISTRY.put(new DataBlock(4,7), "GMPlate");
        REVERB_REGISTRY.put(new DataBlock(4,1), "RichPlate");
        REVERB_REGISTRY.put(new DataBlock(17,0), "Tunnel");
        REVERB_REGISTRY.put(new DataBlock(18,0), "Canyon");
        REVERB_REGISTRY.put(new DataBlock(19,0), "Basement");
    }

    public static String getReverbType(int msb, int lsb) {
        byte bMSB = (byte)(msb & 0xFF);
        byte bLSB = (byte)(lsb & 0xFF);

        DataBlock dataBlock = REVERB_REGISTRY.keySet().stream().filter(b -> (b.getB1() == bMSB) && (b.getB2() == bLSB)).findFirst().orElse(null);
        if (dataBlock != null) {
            return REVERB_REGISTRY.get(dataBlock);
        }

        return "unknown reverb type";
    }
}

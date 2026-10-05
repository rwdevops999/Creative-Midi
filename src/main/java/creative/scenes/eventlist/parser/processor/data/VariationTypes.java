package creative.scenes.eventlist.parser.processor.data;

import entity.sysex.DataBlock;

import java.util.HashMap;
import java.util.Map;

public class VariationTypes {
    private static final Map<DataBlock, String> VARIATION_REGISTRY = new HashMap<>();

    static {
        // NoEffect
        VARIATION_REGISTRY.put(new DataBlock(0,0), "No Effect");
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

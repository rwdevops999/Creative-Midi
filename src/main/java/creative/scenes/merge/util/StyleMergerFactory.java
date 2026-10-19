package creative.scenes.merge.util;

import creative.scenes.voice.provider.CakewalkInsProvider;
import creative.scenes.voice.provider.InstrumentProvider;
import creative.scenes.voice.provider.YamahaMS2SProvider;
import org.apache.commons.io.FilenameUtils;

public class StyleMergerFactory {
    public static StyleMerger getMerger(String vendorId) {
        if (vendorId.equalsIgnoreCase("0X43")) {
            return new YamahaStyleMerger();
        }

        return null;
    }

}

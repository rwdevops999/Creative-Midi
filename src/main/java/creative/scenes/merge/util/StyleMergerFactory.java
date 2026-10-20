package creative.scenes.merge.util;

public class StyleMergerFactory {
    public static StyleMerger getMerger(String vendorId) {
        if (vendorId.equalsIgnoreCase("0X43")) {
            return new YamahaStyleMerger();
        }

        return null;
    }

}

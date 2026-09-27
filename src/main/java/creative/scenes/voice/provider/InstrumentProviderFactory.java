package creative.scenes.voice.provider;

import org.apache.commons.io.FilenameUtils;

public class InstrumentProviderFactory {
    public static InstrumentProvider getProvider(String filename) {
        String extension = FilenameUtils.getExtension(filename);

        if (extension.equalsIgnoreCase("ins")) {
            return new CakewalkInsProvider(filename);
        } else if (extension.equalsIgnoreCase("txt")) {
            return new YamahaMS2SProvider(filename);
        }

        return null;
    }
}

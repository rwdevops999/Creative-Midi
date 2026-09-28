package creative.scenes.voice.provider;

import creative.scenes.voice.util.MS2SParser;
import entity.voice.Group;

import java.io.File;
import java.util.List;

public class YamahaMS2SProvider implements InstrumentProvider {
    private final MS2SParser parser;

    public YamahaMS2SProvider(File insFile) {
        this.parser = new MS2SParser(this, insFile);
        parser.parse(insFile);
    }

    public YamahaMS2SProvider(String filename) {
        this(new File(filename));
    }

    @Override
    public String getSourceType() {
        return "Yamaha";
    }

    @Override
    public List<Group> getGroups() {
        return parser.getRootGroups();
    }
}

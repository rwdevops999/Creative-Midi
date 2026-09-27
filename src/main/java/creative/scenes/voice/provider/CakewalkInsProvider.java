package creative.scenes.voice.provider;

import creative.scenes.voice.util.InsParser;
import entity.voice.Group;

import java.io.File;
import java.util.List;

public class CakewalkInsProvider implements InstrumentProvider {
    private final InsParser parser;

    public CakewalkInsProvider(File insFile) {
        this.parser = new InsParser(this, insFile);
    }

    public CakewalkInsProvider(String filename) {
        this(new File(filename));
    }

    @Override
    public String getSourceType() {
        return ".ins File";
    }

    @Override
    public List<Group> getGroups() {
        // Dit sluit nu naadloos aan op de rootGroups van je nieuwe parser!
        return parser.getRootGroups();
    }
}
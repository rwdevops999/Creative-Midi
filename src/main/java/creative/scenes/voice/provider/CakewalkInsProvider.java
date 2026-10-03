package creative.scenes.voice.provider;

import creative.scenes.voice.util.InsParser;
import creative.scenes.voice.util.MS2SParser;
import entity.voice.Group;
import entity.voice.Patch;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.List;

public class CakewalkInsProvider implements InstrumentProvider {
    private final InsParser parser;
    private final MS2SParser linkedParser;

    public CakewalkInsProvider(File insFile) {
        this.parser = new InsParser(this, insFile);

        String linkedFilename = FilenameUtils.removeExtension(insFile.toString());
        this.linkedParser = new MS2SParser(this, linkedFilename + ".txt");
    }

    public CakewalkInsProvider(String filename) {
        this(new File(filename));
    }

    @Override
    public String getSourceType() {
        return "INS";
    }

    @Override
    public List<Group> getGroups() {
        // Dit sluit nu naadloos aan op de rootGroups van je nieuwe parser!
        return parser.getRootGroups();
    }

    @Override
    public Patch findLinkedPatch(Patch patch) {
        return linkedParser.findPatch(patch);
    }

    public Patch findPatch(int msb, int lsb, int pc) {
        return linkedParser.findPatch(msb, lsb, pc);
    }
}
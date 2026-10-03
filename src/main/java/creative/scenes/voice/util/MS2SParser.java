package creative.scenes.voice.util;

import creative.scenes.voice.provider.InstrumentProvider;
import entity.voice.Group;
import entity.voice.Patch;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MS2SParser {
    private static final Logger logger = LoggerFactory.getLogger(MS2SParser.class);

    @Getter
    private List<Group> rootGroups = new ArrayList<>();

    private InstrumentProvider provider;

    private File file;
    public MS2SParser() {
        super();

        this.provider = null;
        file = null;
    }

    public MS2SParser(InstrumentProvider prov, String filename) {
        this();

        this.provider = prov;
        this.file = new File(filename);
        parse(this.file);
    }

    public MS2SParser(InstrumentProvider prov, File file) {
        this();

        this.provider = prov;
        this.file = file;
        parse(this.file);
    }

    private Group currentGroup = null;
    private void parseGroup (String groupLine) {
        int groupNr = Integer.parseInt(groupLine.substring(2, groupLine.indexOf("]")));
        String groupName = groupLine.substring(groupLine.indexOf("]") + 1);

        if (groupNr == 1) {
            Group group = new Group(groupNr, groupName, null);
            rootGroups.add(group);
            currentGroup = group;
        } else {
            Group parentGroup = currentGroup;
            while (groupNr - 1 != parentGroup.getIndex()) {
                parentGroup = parentGroup.getParent();
            }

            Group group = new Group(groupNr, groupName, parentGroup);
            currentGroup = group;

            parentGroup.getGroups().add(group);
        }
    }

    private void parsePatch (String patchLine) {
        // use currentGroup to store
        try {
            String patchName = patchLine.substring(patchLine.indexOf("]") + 1);

            String data = patchLine.substring(
                    patchLine.indexOf(",") + 1,
                    patchLine.indexOf(']')
            );

            String[] dataArray = data.split(",");
            Patch patch = new Patch(
                    currentGroup,
                    patchName,
                    dataArray[1].trim(),
                    dataArray[2].trim(),
                    dataArray[0].trim(),
                    provider != null ? provider.getSourceType() : "Yamaha");

            // TO GET THE CATEGORIES
            int index = patchName.indexOf(" ");
            if (index != -1) {
                String type = patchName.substring(0, index);
            }

            if (currentGroup != null) {
                currentGroup.getPatches().add(patch);
            }
        } catch (Exception e) {
            logger.error("[CM_MS2S_PARSER] Exception. CAUSE: {}", e.getMessage());
        }
    }

    private void parseVoice(String voice) {
        if (!voice.isEmpty()) {
            if (voice.startsWith("[g")) {
                parseGroup (voice);
            } else if (voice.startsWith("[p")) {
                parsePatch (voice);
            }
        }
    }

    public void parse(File file) {
        rootGroups = new ArrayList<>();

        try (Scanner scanner =  new Scanner(file, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                if ("[end]".equals(line)) {
                    break;
                }
                //process each line in some way
                parseVoice(line);
            }
        } catch (IOException ioe) {
            logger.error("[CM_MS2S_PARSER] Exception. CAUSE: {}", ioe.getMessage());
        }

    }

    private List<Patch> findPatches(List<Group> groups) {
        List<Patch> result = new ArrayList<>();

        for (Group group : groups) {
            result.addAll(group.getPatches());

            if (! group.getGroups().isEmpty()) {
                result.addAll(findPatches(group.getGroups()));
            }
        }

        return result;
    }


    public Patch findPatch(Patch inPatch) {
        List<Patch> patches = findPatches(rootGroups);

//        return patches.stream().filter(p -> (p.getMsb() == inPatch.getMsb() && p.getLsb() == inPatch.getLsb() && p.getPc() == inPatch.getPc())).findFirst().orElse(inPatch);
        return patches.stream().filter(p -> (p.getMsb() == inPatch.getMsb() && p.getLsb() == inPatch.getLsb() && p.getPc() == inPatch.getPc())).findFirst().orElse(null);
    }

    public Patch findPatch(int msb, int lsb, int pc) {
        List<Patch> patches = findPatches(rootGroups);

        return patches.stream().filter(p -> (p.getMsb() == msb && p.getLsb() == lsb && p.getPc() == pc)).findFirst().orElse(null);
    }
}

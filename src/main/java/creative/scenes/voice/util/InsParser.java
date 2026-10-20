package creative.scenes.voice.util;

import entity.voice.Group;
import entity.voice.Patch;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class InsParser {
    private static final Logger logger = LoggerFactory.getLogger(InsParser.class);

    // Final result storage
    @Getter
    private final List<Group> rootGroups = new ArrayList<>();

    private File file;
    public InsParser() {
        super();

        file = null;
    }

    public InsParser(File file) {
        this();

        this.file = file;
        parse(this.file);
    }

    private int extractBankNumber(String key) {
        if (!key.contains("[") || !key.contains("]")) {
            return 0; // Default fallback to Bank 0 if no bracket is specified
        }
        try {
            return Integer.parseInt(key.substring(key.indexOf("[") + 1, key.indexOf("]")));
        } catch (Exception e) {
            return 0;
        }
    }

    private String determineYamahaCategory(int rawPc, int msb, int lsb, String voiceName) {
        // Exception for Drums and SFX based on the list-name of LSB
        if (voiceName.toLowerCase().contains("drum") || lsb == 127 || msb == 127) {
            return "Drums & Percussion";
        }
        if (voiceName.toLowerCase().contains("sfx")) {
            return "SFX";
        }

        if (msb == 8) {
            // JazzArtistGuitar is on LSB 39!
            if (lsb == 39 || lsb == 40) {
                return "Guitar";
            }

            // The mix-bank 1056 (LSB 32) where everything is mixed
            if (lsb == 32) {
                if (rawPc >= 0 && rawPc <= 7) return "Guitar";    // 0 ... 7 are guitars
                if (rawPc >= 48 && rawPc <= 55) return "Strings"; // 48-49 are strings
                if (rawPc >= 56 && rawPc <= 82) return "Brass";   // 56, 64, 82 are brass/sax
                if (rawPc >= 112 && rawPc <= 127) return "Piano"; // 112 is Harpsichord (Piano)
            }
        }

        // 2. Any other Yamaha specific MSB's
        if (msb == 104) {
            return "Guitar";
        }

        // --- FALLBACK ---
        if (rawPc >= 0 && rawPc <= 7)    return "Piano";
        if (rawPc >= 8 && rawPc <= 15)   return "Chromatic Percussion";
        if (rawPc >= 16 && rawPc <= 23)  return "Organ";
        if (rawPc >= 24 && rawPc <= 31)  return "Guitar";
        if (rawPc >= 32 && rawPc <= 39)  return "Bass";
        if (rawPc >= 40 && rawPc <= 47)  return "Strings";
        if (rawPc >= 48 && rawPc <= 55)  return "Ensemble";
        if (rawPc >= 56 && rawPc <= 63)  return "Brass";
        if (rawPc >= 64 && rawPc <= 71)  return "Reed";
        if (rawPc >= 72 && rawPc <= 79)  return "Pipe";
        if (rawPc >= 80 && rawPc <= 87)  return "Synth Lead";
        if (rawPc >= 88 && rawPc <= 95)  return "Synth Pad";
        if (rawPc >= 96 && rawPc <= 103) return "Synth SFX";
        if (rawPc >= 104 && rawPc <= 111) return "Ethnic";
        if (rawPc >= 112 && rawPc <= 119) return "Percussive";
        if (rawPc >= 120 && rawPc <= 127) return "Sound Effects";

        return "Unknown";
    }

    private void parse(File file) {
        logger.debug("Parsing file: {}", file.getAbsolutePath());

        // Raw storage for Pass 1
        Map<String, Map<String, String>> rawSections = new LinkedHashMap<>();

        // --- PASS 1: Read raw sections ---
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String currentSection = "";

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith(";")) continue;

                if (line.startsWith("[") && line.endsWith("]")) {
                    currentSection = line.substring(1, line.length() - 1);
                    rawSections.putIfAbsent(currentSection, new LinkedHashMap<>());
                } else if (line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    if (rawSections.containsKey(currentSection)) {
                        rawSections.get(currentSection).put(parts[0].trim(), parts[1].trim());
                    }
                }
            }
        } catch (IOException ioe) {
            logger.error("[INS_PARSER] Exception. CAUSE {}", ioe.getMessage());
        }

        // --- PASS 2: Parse into structured objects ---

        // --- PASS 2a: Filter the voice lists from the rawSections and convert the PC to Integer ---
        Map<String, Map<Integer, String>> allRawPatchLists = new HashMap<>();

        for (Map.Entry<String, Map<String, String>> sectionEntry : rawSections.entrySet()) {
            String sectionName = sectionEntry.getKey();

            // Skip configuration sections
            if (!sectionName.equalsIgnoreCase("Instruments") &&
                    !sectionName.equalsIgnoreCase("Patch Names") &&
                    !sectionName.equalsIgnoreCase("Bank Select Method")) {

                Map<String, String> rawPairs = sectionEntry.getValue();
                Map<Integer, String> parsedPairs = new HashMap<>();

                // run though the "Text=Text" pairs (e.g. "0=Concert Grand")
                for (Map.Entry<String, String> pair : rawPairs.entrySet()) {
                    try {
                        // Put the PC-string ("0") over to an int (0)
                        int pcNumber = Integer.parseInt(pair.getKey().trim());
                        parsedPairs.put(pcNumber, pair.getValue().trim());
                    } catch (NumberFormatException ignored) {
                        // If the key is not a number (e.g. "Control="), then it is not a patch list
                    }
                }

                // If we've found the valid numbers, we add then to the list
                if (!parsedPairs.isEmpty()) {
                    allRawPatchLists.put(sectionName, parsedPairs);
                }
            }
        }
        // --- PASS 2b: lookup the instrument section (flexible) ---
        Map<String, String> instrumentsSection = null;
        if (rawSections.containsKey("Instruments")) instrumentsSection = rawSections.get("Instruments");
        else if (rawSections.containsKey("instruments")) instrumentsSection = rawSections.get("instruments");
        else {
            for (String sectionName : rawSections.keySet()) {
                if (sectionName.equalsIgnoreCase("RW-SX600") || sectionName.toLowerCase().contains("instrument")) {
                    instrumentsSection = rawSections.get(sectionName);
                    break;
                }
            }
        }

        // If the fallback doesn't find anything, take the last section from the list
        // --- PASS 2c: create the real Entities.Voice.Patch object per category ---
        if (instrumentsSection != null) {
            // Create one master group for the total instrument
            Group instrumentGroup = new Group();
            instrumentGroup.setGroupName("Yamaha PSR-SX600");

            // We use a Map to take care that we re-use categories (like "Piano")
            Map<String, Group> categorieGroepenMap = new HashMap<>();

            // Run through all bank definitions in the file
            for (Map.Entry<String, String> defEntry : instrumentsSection.entrySet()) {
                String key = defEntry.getKey().trim();   // e.g. "Patch"
                String val = defEntry.getValue().trim(); // e.g. "SX600 Bank 104"

                if (key.startsWith("Patch")) {
                    Map<Integer, String> rawPatches = allRawPatchLists.get(val);

                    if (rawPatches != null && !rawPatches.isEmpty()) {
                        int rawBankNum = extractBankNumber(key);

                        // Calculate MSB and LSB (Method 2)
                        int msbInt = (rawBankNum >> 7) & 0x7F;
                        int lsbInt = rawBankNum & 0x7F;

                        // Run though all loose voices in this bank
                        for (Map.Entry<Integer, String> patchEntry : rawPatches.entrySet()) {
                            int rawPc = patchEntry.getKey();
                            String voiceName = patchEntry.getValue();

                            // 1. Calculate the category per patch
                            String categoryName = determineYamahaCategory(rawPc, msbInt, lsbInt, voiceName);

                            // 2. Retrieve the group SPECIFIC for this category
                            Group categoryGroup = categorieGroepenMap.get(categoryName);
                            if (categoryGroup == null) {
                                categoryGroup = new Group();
                                categoryGroup.setGroupName(categoryName);

                                // Change: categorieGroep.setParent(instrumentGroup);
                                // To (no parent, so that the group name stays pure "Organ" or "Piano"):
                                categoryGroup.setParent(null);

                                categorieGroepenMap.put(categoryName, categoryGroup);

                                // Add the category to the master list of the parser
                                rootGroups.add(categoryGroup);
                            }

                            // 3. Adjust the patch and add it to the found/created group
                            Patch uiPatch = new Patch(
                                    categoryGroup,
                                    voiceName,
                                    String.valueOf(msbInt),
                                    String.valueOf(lsbInt),
                                    String.valueOf(rawPc),
                                    "Cakewalk"
                            );
                            categoryGroup.getPatches().add(uiPatch);
                        }
                    }
                }
            }
            // Add the master group to the final list of the parser
            rootGroups.add(instrumentGroup);
        }
    }
}

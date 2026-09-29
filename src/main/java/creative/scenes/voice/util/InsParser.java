package creative.scenes.voice.util;

import creative.scenes.voice.provider.InstrumentProvider;
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

    private InstrumentProvider provider;

    // Final result storage
    @Getter
    private List<Group> rootGroups = new ArrayList<>();

    private File file;
    public InsParser() {
        super();

        this.provider = null;
        file = null;
    }

    public InsParser(InstrumentProvider prov, String filename) {
        this();

        this.provider = prov;
        this.file = new File(filename);
        parse(this.file);
    }

    public InsParser(InstrumentProvider prov, File file) {
        this();

        this.provider = prov;
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

    private String bepaalYamahaCategorie(int rawPc, int msb, int lsb, String lijstNaam, String voiceName) {
        // Uitzondering voor Drums en SFX op basis van de lijstnaam of LSB
        if (voiceName.toLowerCase().contains("drum") || lsb == 127 || msb == 127) {
            return "Drums & Percussion";
        }
        if (voiceName.toLowerCase().contains("sfx")) {
            return "SFX";
        }

        if (msb == 8) {
            // Jouw JazzArtistGuitar zit in LSB 39!
            if (lsb == 39 || lsb == 40) {
                return "Guitar";
            }

            // De mix-bank 1056 (LSB 32) waar alles door elkaar staat
            if (lsb == 32) {
                if (rawPc >= 0 && rawPc <= 7) return "Guitar";    // 0 t/m 7 zijn gitaren
                if (rawPc >= 48 && rawPc <= 55) return "Strings"; // 48-49 zijn strings
                if (rawPc >= 56 && rawPc <= 82) return "Brass";   // 56, 64, 82 zijn brass/sax
                if (rawPc >= 112 && rawPc <= 127) return "Piano"; // 112 is Harpsichord (Piano)
            }
        }

        // 2. Eventueel andere Yamaha specifieke MSB's
        if (msb == 104) {
            return "Guitar";
        }

        // --- FALLBACK ---
        if (rawPc >= 0 && rawPc <= 7)    return "Piano"; // <-- Deze miste!
        if (rawPc >= 8 && rawPc <= 15)   return "Chromatic Percussion";
        if (rawPc >= 16 && rawPc <= 23)  return "Organ";
        if (rawPc >= 24 && rawPc <= 31)  return "Guitar";
        if (rawPc >= 32 && rawPc <= 39)  return "Bass";
        if (rawPc >= 40 && rawPc <= 47)  return "Strings";
        if (rawPc >= 48 && rawPc <= 55)  return "Ensemble"; // <-- Deze miste!
        if (rawPc >= 56 && rawPc <= 63)  return "Brass";    // <-- Deze miste!
        if (rawPc >= 64 && rawPc <= 71)  return "Reed";
        if (rawPc >= 72 && rawPc <= 79)  return "Pipe";
        if (rawPc >= 80 && rawPc <= 87)  return "Synth Lead";
        if (rawPc >= 88 && rawPc <= 95)  return "Synth Pad";
        if (rawPc >= 96 && rawPc <= 103) return "Synth SFX";
        if (rawPc >= 104 && rawPc <= 111) return "Ethnic";
        if (rawPc >= 112 && rawPc <= 119) return "Percussive"; // <-- Deze miste!
        if (rawPc >= 120 && rawPc <= 127) return "Sound Effects";

        return "Unknown";
    }

    private void parse(File file) {
        logger.debug("Parsing file: " + file.getAbsolutePath());

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

        // --- PASS 2a: Filter de klanklijsten uit rawSections en zet de PC om naar Integer ---
        Map<String, Map<Integer, String>> allRawPatchLists = new HashMap<>();

        for (Map.Entry<String, Map<String, String>> sectionEntry : rawSections.entrySet()) {
            String sectionName = sectionEntry.getKey();

            // Sla de configuratie-secties over
            if (!sectionName.equalsIgnoreCase("Instruments") &&
                    !sectionName.equalsIgnoreCase("Patch Names") &&
                    !sectionName.equalsIgnoreCase("Bank Select Method")) {

                Map<String, String> rawPairs = sectionEntry.getValue();
                Map<Integer, String> parsedPairs = new HashMap<>();

                // Loop door de "Tekst=Tekst" pairs (bijv. "0=Concert Grand")
                for (Map.Entry<String, String> pair : rawPairs.entrySet()) {
                    try {
                        // Zet de PC-string ("0") om naar een echte int (0)
                        int pcNumber = Integer.parseInt(pair.getKey().trim());
                        parsedPairs.put(pcNumber, pair.getValue().trim());
                    } catch (NumberFormatException ignored) {
                        // Als de key geen getal is (bijv. "Control="), dan is het geen patchlijst
                    }
                }

                // Als we geldige getallen hebben gevonden, voegen we deze lijst toe
                if (!parsedPairs.isEmpty()) {
                    allRawPatchLists.put(sectionName, parsedPairs);
                }
            }
        }
        // --- PASS 2b: Zoek de instrumenten-sectie (flexibel) ---
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

        // Als de fallback ook niets vond, pakken we als allerlaatste redmiddel de allerlaatste sectie uit de lijst
// --- PASS 2c: Maak de echte Entities.Voice.Patch objecten aan per categorie ---
        if (instrumentsSection != null) {
            // Maak één hoofdgroep aan voor het totale instrument
            Group instrumentGroup = new Group();
            instrumentGroup.setGroupName("Yamaha PSR-SX600");

            // We gebruiken een Map om te zorgen dat we categorieën (zoals "Piano") hergebruiken
            Map<String, Group> categorieGroepenMap = new HashMap<>();

            // Loop door alle bank-definities in het bestand
            for (Map.Entry<String, String> defEntry : instrumentsSection.entrySet()) {
                String key = defEntry.getKey().trim();   // Bijv. "Patch"
                String val = defEntry.getValue().trim(); // Bijv. "RW-SX600 Bank 104"

                if (key.startsWith("Patch")) {
                    Map<Integer, String> rawPatches = allRawPatchLists.get(val);

                    if (rawPatches != null && !rawPatches.isEmpty()) {
                        int rawBankNum = extractBankNumber(key);

                        // Bereken MSB en LSB (Method 2)
                        int msbInt = (rawBankNum >> 7) & 0x7F;
                        int lsbInt = rawBankNum & 0x7F;

                        // Loop door alle losse klanken in deze bank
                        for (Map.Entry<Integer, String> patchEntry : rawPatches.entrySet()) {
                            int rawPc = patchEntry.getKey();
                            String voiceName = patchEntry.getValue();

                            // 1. Bereken de categorie ECHT PER PATCH
                            String categorieNaam = bepaalYamahaCategorie(rawPc, msbInt, lsbInt, val, voiceName);

                            // 2. Haal de groep op SPECIFIEK voor deze categorie
                            Group categorieGroep = categorieGroepenMap.get(categorieNaam);
                            if (categorieGroep == null) {
                                categorieGroep = new Group();
                                categorieGroep.setGroupName(categorieNaam);

                                // VERANDER DIT VAN: categorieGroep.setParent(instrumentGroup);
                                // NAAR DIT (Geen parent, zodat de groepsnaam puur "Organ" of "Piano" blijft):
                                categorieGroep.setParent(null);

                                categorieGroepenMap.put(categorieNaam, categorieGroep);

                                // Voeg de categorie direct toe aan de hoofdlijst van de parser
                                rootGroups.add(categorieGroep);
                            }

                            // 3. Maak de patch aan en voeg hem toe aan de zojuist gevonden/gemaakte groep
                            Patch uiPatch = new Patch(
                                    categorieGroep,
                                    voiceName,
                                    String.valueOf(msbInt),
                                    String.valueOf(lsbInt),
                                    String.valueOf(rawPc),
                                    "Cakewalk"
                            );
                            categorieGroep.getPatches().add(uiPatch);
                        }
                    }
                }
            }
            // Voeg de hoofdgroep toe aan de eindlijst van de parser
            rootGroups.add(instrumentGroup);
        }
    }
}

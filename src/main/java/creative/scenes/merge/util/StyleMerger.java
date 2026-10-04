package creative.scenes.merge.util;

import creative.scenes.eventlist.EventsDisplayPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class StyleMerger {
    private static final Logger logger = LoggerFactory.getLogger(StyleMerger.class);

    private static final Map<String, String> MARKER_MAP = new HashMap<>();

    static {
        // Format: ("Name_in_Cakewalk", "Correct_Yamaha_Name")
        MARKER_MAP.put("Main_A", "Main A");
        MARKER_MAP.put("Main B", "Main B");
        MARKER_MAP.put("Intro_A", "Intro A");
        MARKER_MAP.put("Fill_In_A", "Fill In AA"); // Voorbeeld van hernoeming naar double letter
        MARKER_MAP.put("Ending_A", "Ending A");
    }

    public void merge(String originalStylePath, String newMidiPath, String newStylePath) {
        try {
            mergeStyleWithMidi(originalStylePath, newMidiPath, newStylePath);
            System.out.println("Stijl succesvol gegenereerd met gecontroleerde markers!");
        } catch (Exception e) {
            logger.error("[CM_STYLE_MERGER] Exception. CAUSE {}", e.getMessage());
        }
    }

    public static void mergeStyleWithMidi(String origStylePath, String newMidiPath, String outStylePath) throws Exception {
        // 1. Lees het originele stijlbestand in als bytes
        byte[] origStyleBytes = Files.readAllBytes(Paths.get(origStylePath));

        // Vraag de lengte van het MIDI-gedeelte van de OUDE stijl op om een veilige startindex te bepalen
        // Hierdoor weten we exact waar de muzieknoten ophouden en de Yamaha-chunks beginnen
        InputStream is = new ByteArrayInputStream(origStyleBytes);
        Sequence origSequence = MidiSystem.getSequence(is);

        // We schrijven de oude sequence tijdelijk naar een stream om de pure MIDI-byte-lengte te meten
        ByteArrayOutputStream tempBaos = new ByteArrayOutputStream();
        MidiSystem.write(origSequence, 0, tempBaos);
        int origMidiLength = tempBaos.size();

        // Zoek de CASM-offset met de veilige startindex
        int casmOffset = findCasmOffset(origStyleBytes, origMidiLength);

        if (casmOffset == -1) {
            throw new IllegalArgumentException("Geen CASM-chunk gevonden in het originele stijlbestand.");
        }

        // Extraheer de Yamaha-extensies
        int extensionLength = origStyleBytes.length - casmOffset;
        byte[] yamahaExtensions = new byte[extensionLength];
        System.arraycopy(origStyleBytes, casmOffset, yamahaExtensions, 0, extensionLength);

        // 2. Laad de nieuwe Type 1 MIDI uit Cakewalk en converteer naar Type 0 + bewerk markers
        File midiFile = new File(newMidiPath);
        Sequence type1Sequence = MidiSystem.getSequence(midiFile);
        Sequence type0Sequence = convertType1ToType0WithMarkerCheck(type1Sequence);

        // 3. Schrijf de nieuwe Type 0 MIDI naar een byte-array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MidiSystem.write(type0Sequence, 0, baos);
        byte[] newMidiType0Bytes = baos.toByteArray();

        // 4. Samenvoegen en wegschrijven
        try (FileOutputStream fos = new FileOutputStream(outStylePath)) {
            fos.write(newMidiType0Bytes);
            fos.write(yamahaExtensions);
        }
    }

    /**
     * Zoekt naar de CASM-chunk, maar start met zoeken vanaf een veilige offset
     * (bijvoorbeeld de lengte van de nieuwe MIDI) of zoekt achterwaarts om
     * valse 'CASM' bytes in de muzieknoten te voorkomen.
     */
    private static int findCasmOffset(byte[] bytes, int estimatedMidiLength) {
        // We starten het zoeken pas na de geschatte MIDI-lengte om valse noten-matches te omzeilen
        int startScan = Math.min(estimatedMidiLength, bytes.length - 4);

        for (int i = startScan; i < bytes.length - 4; i++) {
            if (bytes[i] == 'C' && bytes[i+1] == 'A' && bytes[i+2] == 'S' && bytes[i+3] == 'M') {
                return i;
            }
        }

        // Mocht dat mislukken (bijv. als de nieuwe midi langer is dan de oude),
        // probeer dan als fallback een volledige scan
        for (int i = 0; i < bytes.length - 4; i++) {
            if (bytes[i] == 'C' && bytes[i+1] == 'A' && bytes[i+2] == 'S' && bytes[i+3] == 'M') {
                return i;
            }
        }
        return -1;
    }

    /**
     * Convert type 1 midi to Type 0 and check/adjust midi markers
     */
    private static Sequence convertType1ToType0WithMarkerCheck(Sequence source) throws Exception {
        Sequence target = new Sequence(source.getDivisionType(), source.getResolution());
        Track track0 = target.createTrack();

        for (Track track : source.getTracks()) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();

                if (isEndOfTrack(message)) {
                    continue;
                }

                // Controleer of het event een Marker (0x06) of Tekst (0x01) meta-event is
                if (message instanceof MetaMessage mm) {
                    int type = mm.getType();

                    if (type == 0x01 || type == 0x06) {
                        String currentMarkerName = new String(mm.getData(), StandardCharsets.ISO_8859_1).trim();

                        // Systeemlogboek om te zien welke markers er langskomen
                        System.out.println("Gevonden marker: [" + currentMarkerName + "] op tick " + event.getTick());

                        // Controleer of deze marker aangepast moet worden
                        if (MARKER_MAP.containsKey(currentMarkerName)) {
                            String newMarkerName = MARKER_MAP.get(currentMarkerName);
                            System.out.println(" -> Wijzigt marker naar: [" + newMarkerName + "]");

                            // Maak een nieuw MetaMessage aan met de gecorrigeerde tekst
                            byte[] nameBytes = newMarkerName.getBytes(StandardCharsets.ISO_8859_1);
                            MetaMessage correctedHint = new MetaMessage();
                            correctedHint.setMessage(type, nameBytes, nameBytes.length);

                            event = new MidiEvent(correctedHint, event.getTick());
                        }
                    }
                }

                track0.add(event);
            }
        }
        return target;
    }

    private static boolean isEndOfTrack(MidiMessage message) {
        if (message instanceof MetaMessage mm) {
            return mm.getType() == 0x2F;
        }
        return false;
    }
}

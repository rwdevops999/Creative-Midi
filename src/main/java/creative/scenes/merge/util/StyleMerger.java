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

    private static void mergeStyleWithMidi(String origStylePath, String newMidiPath, String outStylePath) throws Exception {
        // 1. Lees de binaire bytes van de originele stijl
        byte[] origStyleBytes = Files.readAllBytes(Paths.get(origStylePath));

        // Vind de CASM-offset in de oude stijl (veilige scan vanaf het einde)
        int safeStartIdx = Math.max(0, origStyleBytes.length - 10000);
        int casmOffset = -1;
        for (int i = safeStartIdx; i < origStyleBytes.length - 4; i++) {
            if (origStyleBytes[i] == 'C' && origStyleBytes[i+1] == 'A' && origStyleBytes[i+2] == 'S' && origStyleBytes[i+3] == 'M') {
                casmOffset = i;
                break;
            }
        }

        if (casmOffset == -1) {
            throw new IllegalArgumentException("Geen CASM-chunk gevonden in het originele stijlbestand.");
        }

        // Extraheer de CASM + OTS extensies
        int extensionLength = origStyleBytes.length - casmOffset;
        byte[] yamahaExtensions = new byte[extensionLength];
        System.arraycopy(origStyleBytes, casmOffset, yamahaExtensions, 0, extensionLength);

        // 2. Laad de gewijzigde Cakewalk Type 1 MIDI
        File midiFile = new File(newMidiPath);
        Sequence cakewalkSeq = MidiSystem.getSequence(midiFile);

        // Converteer Cakewalk Type 1 naar een strikte Yamaha Type 0 structuur
        Sequence yamahaType0Seq = convertCakewalkToYamahaType0(cakewalkSeq);

        // 3. Schrijf de herstelde MIDI naar bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MidiSystem.write(yamahaType0Seq, 0, baos);
        byte[] cleanMidiBytes = baos.toByteArray();

        // 4. Voeg de schone MIDI samen met de oude CASM-tabel
        try (FileOutputStream fos = new FileOutputStream(outStylePath)) {
            fos.write(cleanMidiBytes);
            fos.write(yamahaExtensions);
        }

        System.out.println("Stijl succesvol hersteld en samengevoegd!");
    }

    private static Sequence convertCakewalkToYamahaType0(Sequence source) throws Exception {
        Sequence target = new Sequence(source.getDivisionType(), source.getResolution());
        Track track0 = target.createTrack();

        // Voeg de verplichte Yamaha Style Headers handmatig toe op tick 0 (Maat 1)
        // Dit zorgt ervoor dat het keyboard het bestand herkent als een legitieme stijl
        addYamahaHeaderMarker(track0, "SFF1", 0);
        addYamahaHeaderMarker(track0, "SInt", 0);

        // Loop door alle Cakewalk tracks en voeg ze samen in Track 0
        for (Track track : source.getTracks()) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();

                // Sla de standaard 'End of Track' markers over om corruptie te voorkomen
                if (message instanceof MetaMessage && ((MetaMessage) message).getType() == 0x2F) {
                    continue;
                }

                // Voeg het event toe aan de gezamenlijke track
                track0.add(event);
            }
        }
        return target;
    }

    private static void addYamahaHeaderMarker(Track track, String text, long tick) throws Exception {
        byte[] textBytes = text.getBytes(StandardCharsets.ISO_8859_1);
        MetaMessage message = new MetaMessage();
        // 0x01 is het binaire MIDI-type voor een Text Event dat Yamaha vereist voor SFF1/SInt
        message.setMessage(0x01, textBytes, textBytes.length);
        track.add(new MidiEvent(message, tick));
    }
}
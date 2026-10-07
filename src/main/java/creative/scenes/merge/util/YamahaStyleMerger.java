package creative.scenes.merge.util;

import javax.sound.midi.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class YamahaStyleMerger {
    public void mergeStyleAndMidi(File styleFile, File midiFile, File outputFile) {
        try {
            // 1. Lees beide bestanden in als MidiSequence
            Sequence styleSeq = MidiSystem.getSequence(styleFile);
            Sequence midiInSeq = MidiSystem.getSequence(midiFile); ;

            MidiFileFormat format = MidiSystem.getMidiFileFormat(midiFile);
            boolean isType1 = format.getType() == 1;

            Sequence midiSeq;
            if (isType1) {
                midiSeq = convertType1ToType0(midiInSeq);
            } else {
                midiSeq = midiInSeq;
            }

            // 2. Maak een nieuwe Sequence aan (we gebruiken de timing/PPQ van het Style-bestand)
            Sequence mergedSeq = new Sequence(styleSeq.getDivisionType(), styleSeq.getResolution());

            // WAAROM STAP 3
            // 3. Kopieer alle tracks uit het Style-bestand naar de nieuwe sequence
            for (Track track : styleSeq.getTracks()) {
                Track newTrack = mergedSeq.createTrack();
                for (int i = 0; i < track.size(); i++) {
                    newTrack.add(track.get(i));
                }
            }

            // 4. Kopieer alle tracks uit het nieuwe MIDI-bestand naar de nieuwe sequence
            // Let op: Yamaha instrument-begeleiding gebruikt vaak MIDI-kanalen 9 t/m 16.
            // Zorg dat de kanalen in je nieuwe MIDI niet conflicteren, of pas ze hier eventueel aan.
            for (Track track : midiSeq.getTracks()) {
                Track newTrack = mergedSeq.createTrack();
                for (int i = 0; i < track.size(); i++) {
                    newTrack.add(track.get(i));
                }
            }

            // 5. Schrijf de samengevoegde MIDI-data tijdelijk weg naar een byte array
            File tempMidi = File.createTempFile("temp_midi", ".mid");
            MidiSystem.write(mergedSeq, 1, tempMidi);

            // 6. Yamaha Styles bevatten cruciale CASM-data buiten de standaard MIDI-structuur (aan het einde).
            // We moeten deze non-MIDI data handmatig uit het originele .sty bestand vissen en achter het nieuwe bestand plakken.
            appendCasmData(styleFile, tempMidi, outputFile);

            // Ruim het tijdelijke bestand op
            tempMidi.delete();
        } catch (InvalidMidiDataException imde) {
            System.out.println("EXCEPTION 1");
        } catch (IOException ioe) {
            System.out.println("EXCEPTION 2");
        }
    }

    private static void appendCasmData(File originalStyle, File mergedMidi, File outputFile) throws IOException {
        byte[] styleBytes = java.nio.file.Files.readAllBytes(originalStyle.toPath());

        // Zoek naar de "CASM" marker in het originele Style-bestand
        int casmIndex = -1;
        for (int i = 0; i < styleBytes.length - 4; i++) {
            if (styleBytes[i] == 'C' && styleBytes[i+1] == 'A' && styleBytes[i+2] == 'S' && styleBytes[i+3] == 'M') {
                casmIndex = i;
                break;
            }
        }

        // Kopieer de samengevoegde MIDI-basis
        byte[] midiBytes = java.nio.file.Files.readAllBytes(mergedMidi.toPath());

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(midiBytes);

            // Als er een CASM-sectie is gevonden, plakken we deze erachteraan
            if (casmIndex != -1) {
                int casmLength = styleBytes.length - casmIndex;
                fos.write(styleBytes, casmIndex, casmLength);
                System.out.println("CASM-sectie gevonden en succesvol toegevoegd (" + casmLength + " bytes).");
            } else {
                System.out.println("Waarschuwing: Geen CASM-sectie gevonden in het bronbestand. Het resultaat is een standaard MIDI-bestand.");
            }
        }
    }

    private Sequence convertType1ToType0(Sequence sourceSequence) {
        try {
            // 1. Maak een nieuwe Sequence aan met dezelfde timing-resolutie (PPQ of SMPTE)
            Sequence targetSequence = new Sequence(sourceSequence.getDivisionType(), sourceSequence.getResolution());

            // 2. Maak één enkel spoor aan voor de Type 0 Sequence
            Track mainTrack = targetSequence.createTrack();

            // 3. Loop door alle sporen van het originele bestand
            for (Track track : sourceSequence.getTracks()) {
                for (int i = 0; i < track.size(); i++) {
                    MidiEvent event = track.get(i);

                    // Voeg elk event toe aan het nieuwe hoofdspoor.
                    // Java's Track klasse sorteert de events automatisch op basis van hun tick-waarde.
                    mainTrack.add(event);
                }
            }

            return targetSequence;
        } catch (Exception e) {
            System.err.println("Fout tijdens de conversie: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}

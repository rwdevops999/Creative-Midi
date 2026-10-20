package creative.scenes.merge.util;

import creative.scenes.convertor.MidiConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Put this later in a factory when using 0x43 (Yamaha ID)
 */
public class YamahaStyleMerger implements StyleMerger {
    private static final Logger logger = LoggerFactory.getLogger(YamahaStyleMerger.class);

    public YamahaStyleMerger() {
    }

    @Override
    public void mergeStyleAndMidi(File styleFile, File midiFile, File outputFile) {
        try {
            // 1. Read both files as a MidiSequence
            Sequence styleSeq = MidiSystem.getSequence(styleFile);
            Sequence midiInSeq = MidiSystem.getSequence(midiFile);

            MidiFileFormat format = MidiSystem.getMidiFileFormat(midiFile);
            boolean isType1 = format.getType() == 1;

            Sequence midiSeq;
            if (isType1) {
                MidiConvertor midiConvertor = new MidiConvertor();
//                midiSeq = convertType1ToType0(midiInSeq);
                midiSeq = midiConvertor.convertType1ToType0(midiInSeq);
            } else {
                midiSeq = midiInSeq;
            }

            // 2. Create a new Sequence (use the timing/PPQ of the style file)
            Sequence mergedSeq = new Sequence(styleSeq.getDivisionType(), styleSeq.getResolution());

            // 3. copy all tracks from the style file to the new sequence
            for (Track track : styleSeq.getTracks()) {
                Track newTrack = mergedSeq.createTrack();
                for (int i = 0; i < track.size(); i++) {
                    newTrack.add(track.get(i));
                }
            }

            // 4. copy all tracks from the MIDI file to the new sequence
            // Attention: Yamaha instruments use mostly MIDI channels 9 to 16.
            // Make sure that the channels in the new MIDI don't conflict, or adjust them here.
            for (Track track : midiSeq.getTracks()) {
                Track newTrack = mergedSeq.createTrack();
                for (int i = 0; i < track.size(); i++) {
                    newTrack.add(track.get(i));
                }
            }

            // 5. Write the merged MIDI temporarly to a byte array
            File tempMidi = File.createTempFile("temp_midi", ".mid");
            MidiSystem.write(mergedSeq, 1, tempMidi);

            // 6. Yamaha Styles contain crucial CASM data outside the standard MIDI structure (at the end).
            // We have to retrieve this non-MIDI data manually from the original .sty file and glue it at the end of the new file.
            appendCASMData(styleFile, tempMidi, outputFile);

            // Clean up the temporarily file
            tempMidi.delete();
        } catch (InvalidMidiDataException e) {
            logger.error("Exception(1): CAUSE {}", e.getMessage());
        } catch (IOException ioe) {
            logger.error("Exception(2): CAUSE {}", ioe.getMessage());
        }
    }

    private static void appendCASMData(File originalStyle, File mergedMidi, File outputFile) throws IOException {
        byte[] styleBytes = java.nio.file.Files.readAllBytes(originalStyle.toPath());

        // Look for the "CASM" marker in the original style file
        int CASMIndex = -1;
        for (int i = 0; i < styleBytes.length - 4; i++) {
            if (styleBytes[i] == 'C' && styleBytes[i+1] == 'A' && styleBytes[i+2] == 'S' && styleBytes[i+3] == 'M') {
                CASMIndex = i;
                break;
            }
        }

        // Copy the merged MIDI base
        byte[] midiBytes = java.nio.file.Files.readAllBytes(mergedMidi.toPath());

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(midiBytes);

            // If there is a CASM chunk found, glue it at the end
            if (CASMIndex != -1) {
                int CASMLength = styleBytes.length - CASMIndex;
                fos.write(styleBytes, CASMIndex, CASMLength);
                logger.debug("CASM chunk found and added ({}  bytes).", CASMLength);
            } else {
                logger.warn("No CASM chunk found in the source file. The result is a standard MIDI file.");
            }
        }
    }
}

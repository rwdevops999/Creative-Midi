package creative.scenes.extract.util;

import creative.scenes.convertor.MidiConvertor;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

public class StyleMidiExtractor {

    public StyleMidiExtractor() {
    }

    /**
     * Extracts the MIDI sequence from a Style file and saves it as Type 0 or Type 1.
     *
     * @param styleFile  The input Yamaha Style file (.sty, .prs, etc.)
     * @param outputFile The destination .mid file
     * @param midiType   The desired MIDI architecture format (0 or 1)
     * @throws InvalidMidiDataException If the file contains corrupt or unreadable MIDI data
     * @throws IOException              If file reading or writing fails
     */
    public void extractAndSaveMidi(File styleFile, File outputFile, int midiType)
            throws InvalidMidiDataException, IOException {

        if (midiType != 0 && midiType != 1) {
            throw new IllegalArgumentException("MIDI type must be either 0 or 1.");
        }

        // 1. Extract the raw Sequence directly from the Style file
        Sequence originalSequence = MidiSystem.getSequence(styleFile);
        Sequence outputSequence;

        MidiConvertor midiConvertor = new MidiConvertor();

        // 2. Format the Sequence structure according to the requested MIDI Type
        if (midiType == 0) {
            outputSequence = midiConvertor.convertToType0(originalSequence);
        } else {
            outputSequence = midiConvertor.convertToType1(originalSequence);
        }

        // 3. Write the reconstructed sequence out to disk
        // MidiSystem.write uses the file type integer corresponding to Type 0 or Type 1
        int bytesWritten = MidiSystem.write(outputSequence, midiType, outputFile);
        if (bytesWritten == -1) {
            throw new IOException("The system architecture failed to write the requested MIDI format.");
        }
    }
}
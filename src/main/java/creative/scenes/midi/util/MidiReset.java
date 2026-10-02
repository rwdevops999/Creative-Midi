package creative.scenes.midi.util;

import creative.scenes.midi.data.ByteType;
import creative.scenes.midi.data.MessageType;
import entity.midi.Midi;
import util.ApplicationInfo;

import java.util.ArrayList;
import java.util.List;

public class MidiReset {
    private List<String> midiMessages = new ArrayList<>();
    private MidiWriter midiWriter = new MidiWriter();

    public MidiReset() {
    }

    public void reset() {
        Midi midi = new Midi();
        midi.setMessageType(MessageType.channel.name());
        midi.setByte1Type(ByteType.FreeValue);
        midi.setByte2Type(ByteType.FreeValue);
        midi.setByte1(0);

        // Pitch Wheel
        midi.setStatus("E0");
        midi.setByte1(0x0);
        midi.setByte2(0x40);
        collectMessages(midi);

        // Modulation Wheel
        midi.setStatus("B0");
        midi.setByte1(0x01);
        midi.setByte2(0x00);
        collectMessages(midi);

        // Volume
        midi.setStatus("B0");
        midi.setByte1(0x07);
        midi.setByte2(0x7F);
        collectMessages(midi);

        // Panning
        midi.setStatus("B0");
        midi.setByte1(0x0A);
        midi.setByte2(0x40);
        collectMessages(midi);

        // Sustain Pedal
        midi.setStatus("B0");
        midi.setByte1(0x40);
        midi.setByte2(0x00);
        collectMessages(midi);

        // Sustenuto
        midi.setStatus("B0");
        midi.setByte1(0x42);
        midi.setByte2(0x00);
        collectMessages(midi);

        // Soft Pedal
        midi.setStatus("B0");
        midi.setByte1(0x43);
        midi.setByte2(0x00);
        collectMessages(midi);

        // All Controllers Off
        midi.setStatus("B0");
        midi.setByte1(0x79);
        midi.setByte2(0x00);
        collectMessages(midi);

        // All Notes Off
        midi.setStatus("B0");
        midi.setByte1(0x7B);
        midi.setByte2(0x00);
        collectMessages(midi);

        executeBulk(midiWriter, midiMessages);
    }

    private void collectMessages(Midi midi) {
        for (int i = 0; i < 16; i++) {
            midi.setChannel(i);
            midiMessages.add(midi.toString());
        }
    }

    private void executeBulk(MidiWriter midiWriter, List<String> midiMessages) {
        for (String mid : midiMessages) {
            if (! midiWriter.sendMidiAsString(mid)) {
                break;
            }
        }
    }
}

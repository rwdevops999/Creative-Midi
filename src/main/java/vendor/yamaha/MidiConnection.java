package vendor.yamaha;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;

public class MidiConnection {

    public static MidiDevice openDigitalKeyboard1() throws Exception {

        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {

            if (info.getName().equals("Digital-Keyboard-1")) {

                MidiDevice device =
                        MidiSystem.getMidiDevice(info);

                device.open();

                System.out.println(
                        "Opened MIDI device: " + info.getName()
                );

                return device;
            }
        }

        throw new Exception(
                "Digital-Keyboard-1 was not found."
        );
    }
}
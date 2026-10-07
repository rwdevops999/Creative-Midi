package vendor.yamaha;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public class TrackedReceiver implements Receiver {
    private final Receiver targetReceiver;
    private final MidiDevice originDevice;
    private final String deviceName;

    public TrackedReceiver(MidiDevice device) throws Exception {
        this.originDevice = device;
        this.targetReceiver = device.getReceiver();
        this.deviceName = device.getDeviceInfo().getName();
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        // Stuurt het bericht blind door naar de echte MIDI-poort
        targetReceiver.send(message, timeStamp);
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public MidiDevice getOriginDevice() {
        return this.originDevice;
    }

    @Override
    public void close() {
        targetReceiver.close();
    }
}
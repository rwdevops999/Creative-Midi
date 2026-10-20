package creative.scenes.voice.components;

import custom.components.voice.ChannelIndicator;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import java.util.Map;

public class MultiChannelMidiReceiver implements Receiver {
    private Map<Integer, ChannelIndicator> indicators;

    public MultiChannelMidiReceiver() {
        super();
    }

    public MultiChannelMidiReceiver(Map<Integer, ChannelIndicator> channelIndicators) {
        this();

        indicators = channelIndicators;
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        if (message instanceof ShortMessage sm) {
            int channel = sm.getChannel(); // 0 = Ch 1, 1 = Ch 2, 2 = Ch 3
            int command = sm.getCommand();

            if (channel >= 0 && channel <= 2) {
                boolean turnOn;

                if (command == ShortMessage.NOTE_ON) {
                    turnOn = (sm.getData2() > 0);
                } else if (command == ShortMessage.NOTE_OFF) {
                    turnOn = false;
                } else {
                    return;
                }

                indicators.get(0).setActive(turnOn);
            }
        }
    }

    @Override
    public void close() {}
}
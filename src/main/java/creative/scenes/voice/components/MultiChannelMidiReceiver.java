package creative.scenes.voice.components;

import custom.components.voice.ChannelIndicator;
import javafx.application.Platform;
import lombok.Getter;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import java.util.List;
import java.util.Map;

public class MultiChannelMidiReceiver implements Receiver {
    private Map<Integer, ChannelIndicator> indicators;
    @Getter
    private Map<Integer, Boolean> listen;

    public MultiChannelMidiReceiver() {
        super();
    }

    public MultiChannelMidiReceiver(Map<Integer, ChannelIndicator> channelIndicators, Map<Integer, Boolean> channelListen) {
        this();

        indicators = channelIndicators;
        listen = channelListen;
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        if (message instanceof ShortMessage sm) {
            int channel = sm.getChannel();

            // 1. Thread-safe null/bounds check to avoid NullPointerException
            if (listen == null || !listen.getOrDefault(channel, false)) {
                return;
            }

            int command = sm.getCommand();

            // 2. Bound checking matching your indicator layout
            if (indicators != null && indicators.containsKey(channel)) {
                boolean turnOn;

                if (command == ShortMessage.NOTE_ON) {
                    turnOn = (sm.getData2() > 0);
                } else if (command == ShortMessage.NOTE_OFF) {
                    turnOn = false;
                } else {
                    return;
                }

                // 3. FIXED: Changed 'indicators.get(0)' to 'indicators.get(channel)'
                // 4. CRITICAL: JavaFX UI updates MUST execute on the Application Thread
                Platform.runLater(() -> {
                    ChannelIndicator indicator = indicators.get(channel);
                    if (indicator != null) {
                        indicator.setActive(turnOn);
                    }
                });
            }
        }
    }

    @Override
    public void close() {}
}
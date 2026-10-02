package creative.scenes.midi.util;

import creative.scenes.voice.components.MultiChannelMidiReceiver;
import custom.components.voice.ChannelIndicator;
import custom.dialog.DialogFactory;
import eventhandlers.IThreadEventHandler;
import util.ApplicationInfo;
import util.Worker;

import java.util.List;
import java.util.Map;

public class MidiDemo {
    private static Worker worker;
    private static boolean isPlaying = false;
    public static void playDemo(IThreadEventHandler successHandler, IThreadEventHandler interruptedHandler, MultiChannelMidiReceiver multiChannelMidiReceiver) {
        worker = new Worker(ApplicationInfo.getInstance().getMidiOutputDevice(), multiChannelMidiReceiver);
        worker.setOnSucceededHandler(successHandler);
        worker.setOnInterruptedHandler(interruptedHandler);
        if (worker.readyToRun()) {
            worker.run();
            isPlaying = true;
        } else {
            DialogFactory.renderErrorDialog("Device not ready !!!!");
            if (interruptedHandler != null) {
                interruptedHandler.handle();
            }
        }
    }

    public static boolean isPlaying() {
        return isPlaying;
    }

    public static void stopDemo(Map<Integer, ChannelIndicator> channelIndicators) {
        if (worker != null && worker.isAlive()) {
            worker.interrupt();
            isPlaying = false;
            for (ChannelIndicator channelIndicator : channelIndicators.values()) {
                if (channelIndicator != null) {
                    channelIndicator.setActive(false);
                }
            }
/*            if (channelIndicator != null) {
                channelIndicator.updateLight(false);
            } */
        }
    }
}

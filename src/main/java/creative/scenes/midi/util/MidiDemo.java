package creative.scenes.midi.util;

import custom.components.voice.ChannelIndicator;
import custom.dialog.DialogFactory;
import eventhandlers.IThreadEventHandler;
import util.ApplicationInfo;
import util.Worker;

public class MidiDemo {
    private static Worker worker;
    private static boolean isPlaying = false;
    public static void playDemo(IThreadEventHandler successHandler, IThreadEventHandler interruptedHandler, ChannelIndicator midiChannelIndicator, int midiChannel) {
        worker = new Worker(ApplicationInfo.getInstance().getMidiOutputDevice(), midiChannelIndicator, midiChannel);
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

    public static void stopDemo(ChannelIndicator channelIndicator) {
        if (worker != null && worker.isAlive()) {
            worker.interrupt();
            isPlaying = false;
            if (channelIndicator != null) {
                channelIndicator.updateLight(false);
            }
        }
    }
}

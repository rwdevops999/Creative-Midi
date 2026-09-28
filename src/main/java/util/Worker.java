package util;

import custom.components.voice.ChannelIndicator;
import eventhandlers.IThreadEventHandler;
import javafx.concurrent.Task;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

@Setter
public class Worker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Worker.class);
    public IThreadEventHandler onSucceededHandler;
//    public IThreadEventHandler onFailedHandler;
      public IThreadEventHandler onInterruptedHandler;

    private static Sequencer sequencer = null;
    private static Receiver receiver = null;
    private static File midiFile = null;

    public Worker() {
    }

    public Worker (MidiDevice device, ChannelIndicator midiChannelIndicator, int midiChannel) {
        try {
            if (!device.isOpen()) {
                device.open();
            }

            sequencer = MidiSystem.getSequencer(false);
            sequencer.open();

            Transmitter transmitter = sequencer.getTransmitter();
            Receiver receiver = device.getReceiver();
            if (midiChannelIndicator != null) {
                midiChannelIndicator.setRealReceiver(receiver);
                receiver = midiChannelIndicator.getMidiReceiver(midiChannel);
            }

            transmitter.setReceiver(receiver);

            midiFile = ApplicationInfo.getInstance().getMidiToTry();
            if (midiFile != null) {
                Sequence sequence = MidiSystem.getSequence(midiFile);
                sequencer.setSequence(sequence);
            }
        } catch (MidiUnavailableException mue) {
            logger.error("[CM_DEVICE_SEQUENCER] EXCEPTION: Midi is not available. CAUSE: {}", mue.getMessage());
        } catch (InvalidMidiDataException imde) {
            logger.error("[CM_DEVICE_SEQUENCER] EXCEPTION: Midi file in bad format. CAUSE: {}", imde.getMessage());
        }  catch (IOException ioe) {
            logger.error("[CM_DEVICE_SEQUENCER] EXCEPTION: Error setting midi file. CAUSE: {}", ioe.getMessage());
        }
    }

    public boolean readyToRun() {
        return (sequencer != null);
    }

    private static final Object lock = new Object();

    private final Task<String> workerTask = new Task<>() {
        @Override
        protected String call() throws Exception {
                sequencer.addMetaEventListener(meta -> {
                    if (meta.getType() == 47) {
                        sequencer.stop();
                        sequencer.close();
                        if (onSucceededHandler != null) {
                            onSucceededHandler.handle();
                        }
                    }
                });

            try {
                sequencer.start();
                sequencer.addMetaEventListener(new MetaEventListener() {
                    @Override
                    public void meta(MetaMessage meta) {
                        // Type 47 staat voor het einde van de MIDI-track
                        if (meta.getType() == 47) {
                            System.out.println("De MIDI is afgelopen!");
                            synchronized (lock) {
                                lock.notify(); // Maak de hoofdthread wakker
                            }
                        }
                    }
                });

                synchronized (lock) {
                    while (sequencer.isRunning()) {
                        lock.wait();
                    }
                }
            } catch (InterruptedException ie) {
                logger.debug("[CM_WORKER] EXCEPTION: Play thread interrupted. CAUSE: {}", ie.getMessage());
            } finally {
                sequencer.close();
            }

            return null;
        }
    };

    private Thread workerThread = null;

    @Override
    public void run() {
        workerThread = new Thread(workerTask);
        workerThread.setDaemon(true);
        workerThread.start();
    }

    public void interrupt() {
        if (sequencer != null && sequencer.isRunning()) {
            sequencer.stop();
            try {
                sequencer.setSequence((Sequence) null);
            } catch (Exception e) {
            }
        }
        workerThread.interrupt();
    }

    public boolean isAlive() {
        if (workerThread != null) {
            return workerThread.isAlive();
        }

        return false;
    }
}

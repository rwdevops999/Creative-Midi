package creative.scenes.eventlist.parser;

import creative.scenes.eventlist.parser.data.EventType;
import creative.scenes.eventlist.parser.entity.MidiEventInfo;
import creative.scenes.eventlist.parser.entity.TimeSignature;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.*;
import java.time.Duration;
import java.util.LinkedList;
import java.util.Queue;

import static creative.scenes.eventlist.parser.data.MidiConstants.TEMPO_EVENT;
import static creative.scenes.eventlist.parser.data.MidiConstants.TIME_SIGNATURE_EVENT;

public class MidifileHelper {
    private static final Logger logger = LoggerFactory.getLogger(MidifileHelper.class);

    private static int ppq = 480;
    private static int bpm = 120;
    private static TimeSignature timeSignature = new TimeSignature(4,4);
    private static String duration;

    public static void setPPQ(int value) {
        MidifileHelper.ppq = value;
    }

    public static int getPPQ() {
        return MidifileHelper.ppq;
    }

    public static int getBPM() {
        return MidifileHelper.bpm;
    }

    public static TimeSignature getTimeSignature() {
        return MidifileHelper.timeSignature;
    }

    public static String getDuration() {
        return MidifileHelper.duration;
    }

    public static void retrieveBpmAndTimeSignature(Sequence sequence) {
        int foundValue = 0;
        // calculate default duration
        double seconds = (double) (sequence.getTickLength() * 60) / (bpm * MBTCalculator.getTargetPPQ());
        Duration durationFromSecs = Duration.ofMillis((long) (seconds * 1000));

        MidifileHelper.duration = formatDuration(durationFromSecs);

        for (Track track : sequence.getTracks()) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);

                MidiMessage message = event.getMessage();

                if (message instanceof MetaMessage metaMessage) {
                    if (metaMessage.getType() == TEMPO_EVENT) {
                        MidifileHelper.bpm = processTempo(metaMessage);

                        seconds = (double) (sequence.getTickLength() * 60) / (bpm * MidifileHelper.getPPQ());
                        durationFromSecs = Duration.ofMillis((long) (seconds * 1000));

                        MidifileHelper.duration = formatDuration(durationFromSecs);
                        foundValue++;
                        if (foundValue == 2) {
                            break;
                        }

                    } else if (metaMessage.getType() == TIME_SIGNATURE_EVENT) {
                        byte[] data = metaMessage.getData();

                        if (data.length >= 2) {
                            // 1. The numerator is straightforward
                            int numerator = data[0] & 0xFF;

                            // 2. The denominator is stored as a power of 2 (2^n)
                            int denominatorPower = data[1] & 0xFF;
                            int denominator = (int) Math.pow(2, denominatorPower);

                            MidifileHelper.timeSignature = new TimeSignature(numerator, denominator);

                            foundValue++;
                            if (foundValue == 2) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private static int processTempo(MetaMessage metaMessage) {
        double bpm = 0.0f;
        byte[] data = metaMessage.getData();

        if (data.length == 3) {
            // Reconstruct the microseconds per quarter note from the 3 bytes
            int mpqn = ((data[0] & 0xFF) << 16)
                    | ((data[1] & 0xFF) << 8)
                    | (data[2] & 0xFF);

            // Calculate BPM
            bpm = Math.floor(60_000_000f / mpqn);
        }

        return (int)bpm;
    }

    private static String formatDuration(Duration duration) {
        return String.format("%02d:%02d:%02d",
                duration.toMinutesPart(),
                duration.toSecondsPart(),
                duration.toMillisPart()
        );
    }

    @Getter
    private static Queue<MidiEventInfo> events = new LinkedList<>();

    public static void processMidi(Sequence sequence) {
        events = new LinkedList<>();

        int midiEvents = 0;
        int metaEvents = 0;
        int sysexEvents = 0;

        int trackNumber = 0;

        try {
            for (Track track : sequence.getTracks()) {
                trackNumber++;

                for (int i = 0; i < track.size(); i++) {
                    boolean handled = false;
                    MidiEvent event = track.get(i);

                    long tick = MBTCalculator.convertTick(event.getTick());
                    MidiMessage message;
                    MidiEventInfo midiEventInfo = new MidiEventInfo(tick, MBTCalculator.calculateMBT(tick), trackNumber, event);
                    message = event.getMessage();
                    if (message instanceof ShortMessage midiMessage) {
                        midiEvents++;
                        if (midiEvents == 6) {
                            System.out.println("TEST");
                        }
                        midiEventInfo.setEventType(EventType.MIDI);
/* TODO HandleMessage
                        if (MidiProcessor.handleMessage(midiMessage, midiEventInfo)) {
                            handled = true;
                        } */
                    } else if (message instanceof MetaMessage metaMessage) {
                        metaEvents++;
                        midiEventInfo.setEventType(EventType.META);
/* TODO HandleMessage
                        if (MetaProcessor.handleMessage(metaMessage, midiEventInfo)) {
                            handled = true;
                        } */
                    } else if (message instanceof SysexMessage sysexMessage) {
                        sysexEvents++;
                        midiEventInfo.setEventType(EventType.SYSEX);
/* TODO HandleMessage
                        if (SysExProcessor.handleMessage(sysexMessage, midiEventInfo)) {
                            handled = true;
                        } */
                    } else {
                        logger.error("[CM_MIDI_FILE_PROCESSOR] Unknown message type: " + message.getClass().getName());
                    }

                    if (handled) {
                        events.add(midiEventInfo);
                    }
                }
            }

            logger.debug("MIDI EVENTS  = {}", midiEvents);
            logger.debug("META EVENTS  = {}", metaEvents);
            logger.debug("SYSEX EVENTS = {}", sysexEvents);
        } catch (Exception e) {
            logger.error("[CM_MIDIFILE_HELPER] Exception. CAUSE {}", e.getMessage());
        }
    }
}

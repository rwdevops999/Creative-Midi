package creative.scenes.eventlist.parser;

import creative.scenes.eventlist.parser.entity.MbtPosition;
import creative.scenes.eventlist.parser.entity.TimeSignature;
import creative.scenes.eventlist.parser.processor.MidiFileProcessor;
import lombok.Getter;
import lombok.Setter;

/**
 * The MBT calculator is used to calculate MBT from ticks
 */
public class MBTCalculator {
    private final static int BEAT_UNIT = 4;
    @Setter
    private static int ppq = 480;
    private static int beatsPerMeasure = 4;     // Upper position of Time Signature
    private static int beatValue = 4;           // Lower position of Time Signature

    @Getter
    private static int targetPPQ = 480;

    public static void setTargetPPQ(int targetPPQ) {
        MBTCalculator.targetPPQ = targetPPQ;
        ppqScalingFactor = (double)targetPPQ / ppq;
    }

    private static double ppqScalingFactor = 1.0;

    /**
     * Set PPQ and calculate scaling factor or targetPPQ / ppq
     *
     * @param ppq
     */
    public static void setPPQ(int ppq) {
        MBTCalculator.ppq = ppq;
        ppqScalingFactor = (double)targetPPQ / ppq;
    }

    public static void setTimeSignature(TimeSignature timeSignature) {
        MBTCalculator.beatsPerMeasure = timeSignature.getBeatsPerMeasure();
        MBTCalculator.beatValue = timeSignature.getBeatValue();
    }

    public static MbtPosition calculateMBT(long absoluteTick) {
        // 1. Scale the absolute tick dynamically based on PPQ-ratio
        long correctedTick = (long) (absoluteTick * ppqScalingFactor);

        // 2. Calculate how many ticks there go in one (beat)
        // By /4 measure = targetPPQ. By /8 measure = targetPPQ / 2
        long ticksPerBeat = (targetPPQ * 4) / beatValue;

        // 3. How many ticks go in one complete measure?
        long ticksPerMeasure = ticksPerBeat * beatsPerMeasure;

        // 4. Calulate the measure (based on calculation done in 1)
        long measure = (correctedTick / ticksPerMeasure) + 1;
        long remainder = correctedTick % ticksPerMeasure;

        // 5. Calculate the beat inside this measure (based on calculation done in 1)
        long beat = (remainder / ticksPerBeat) + 1;

        // 6. Calculate the remaining ticks inside this beat
        long tick = remainder % ticksPerBeat;

        // 7. And this is the MBT position
        return new MbtPosition(measure, beat, tick);
    }

    public static double convertToSeconds(long ticks) {
//        double secondsPerTick = 60.0 / (MidiFileProcessor.getBPM() * ppq);
        double secondsPerTick1 = 60.0 / (MidifileHelper.getBPM() * targetPPQ);

        return ticks * secondsPerTick1;
    }
/*
//    public static long convertTick(long tick) {
//        return (Math.round(tick * ppqScalingFactor));
//    } */
    public static long convertTick(long tick) {
        return tick;
    }
}

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
    @Getter
    private static int targetPPQ = 480;
    private static int ppq = 480;
    private static int beat = 4;
    private static int beatsPerMeasure = 4;

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
        MBTCalculator.beat = timeSignature.getBeatsPerMeasure();
        MBTCalculator.beatsPerMeasure = timeSignature.getBeatValue();
    }

    public static MbtPosition calculateMBT(long absoluteTick) {
        long correctedTick = absoluteTick * 4;

        // How many ticks are there in 1 complete measure
        long ticksPerMeasure = targetPPQ * beatsPerMeasure;

        // calculate the measure
        long measure = (correctedTick / ticksPerMeasure) + 1;
        long remainder = correctedTick % ticksPerMeasure;

        // calculate the beat inside the measure
        long beat = (remainder / targetPPQ) + 1;

        // calculate the remaining ticks
        long tick = remainder % targetPPQ;

        // the gives the correct MBT position
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

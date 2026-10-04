package creative.scenes.eventlist.data;

import entity.sysex.Sysex;

import java.util.*;

public class EventKey {
    private static List<String> keys = new ArrayList<>();

    private List<String> values = new ArrayList<>();

    // General keys
    public static String MIDI                  ="MIDI";
    public static String META                  ="META";
    public static String SYSEX                 ="SYSEX";

    // midi keys
    public static String MIDI_ONE              ="MIDI_ONE";
    public static String MIDI_TWO              ="MIDI_TWO";

    // meta keys
    public static String META_TEXT             ="META_TEXT";
    public static String META_COPYRIGHT        ="META_COPYRIGHT";
    public static String META_TRACK_NAME       ="META_TRACK_NAME";
    public static String META_INSTRUMENT_NAME  ="META_INSTRUMENT_NAME";
    public static String META_LYRIC            ="META_LYRIC";
    public static String META_MARKER           ="META_MARKER";
    public static String META_CUE_POINT        ="META_CUE_POINT";
    public static String META_MIDI_PORT        ="META_MIDI_PORT";
    public static String META_TEMPO            ="META_TEMPO";
    public static String META_SMPTE_OFFET      ="META_SMPTE_OFFET";
    public static String META_TIME_SIGNATURE   ="META_TIME_SIGNATURE";
    public static String META_KEY_SIGNATURE    ="META_KEY_SIGNATURE";
    public static String META_SEQUENCER        ="META_SEQUENCER";

    // sysex keys
    public static String SYSEX_SE_EEN           ="SYSEX_SE_EEN";
    public static String SYSEX_SE_TWEE          ="SYSEX_SE_TWEE";

    static {
        keys.add(MIDI);
        keys.add(META);
        keys.add(SYSEX);

        keys.add(MIDI_ONE);
        keys.add(MIDI_TWO);

        keys.add(META_TEXT);
        keys.add(META_COPYRIGHT);
        keys.add(META_TRACK_NAME);
        keys.add(META_INSTRUMENT_NAME);
        keys.add(META_LYRIC);
        keys.add(META_MARKER);
        keys.add(META_CUE_POINT);
        keys.add(META_MIDI_PORT);
        keys.add(META_TEMPO);
        keys.add(META_SMPTE_OFFET);
        keys.add(META_TIME_SIGNATURE);
        keys.add(META_KEY_SIGNATURE);
        keys.add(META_SEQUENCER);

        keys.add(SYSEX_SE_EEN);
        keys.add(SYSEX_SE_TWEE);
    }

    public EventKey(String ...values) {
        Collections.addAll(this.values, values);
    }

    public boolean hasValue(String target) {
        return this.values.contains(target);
    }

    public void addValue(String value) {
        if (! hasValue(value)) {
            this.values.add(value);
        }
    }

    public static List<String> getEvents(String type) {
        return keys.stream().filter(k -> (k.startsWith(type) && !k.equals(type))).toList();
    }
}

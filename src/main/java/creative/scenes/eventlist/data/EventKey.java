package creative.scenes.eventlist.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventKey {
    private List<Integer> values = new ArrayList<>();

    // General keys
    public static int MIDI                  =-1;
    public static int META                  =-2;
    public static int SYSEX                 =-3;

    // midi keys

    // meta keys
    public static int META_TEXT             =1;
    public static int META_COPYRIGHT        =2;
    public static int META_TRACKNAME        =3;
    public static int META_INSTRUMENTNAME   =4;
    public static int META_LYRIC            =5;
    public static int META_MARKER           =6;
    public static int META_CUEPOINT         =7;
    public static int META_MIDIPORT         =8;
    public static int META_TEMPO            =9;
    public static int META_SMPTE_OFFET      =10;
    public static int META_TIME_SIGNATURE   =11;
    public static int META_KEY_SIGNATURE    =12;
    public static int META_SEQUENCER        =13;

    // sysex keys

    public EventKey(Integer ...values) {
        Collections.addAll(this.values, values);
    }

    public boolean hasValue(Integer target) {
        return this.values.contains(target);
    }

    public void addValue(int value) {
        if (! hasValue(value)) {
            this.values.add(value);
        }
    }
}

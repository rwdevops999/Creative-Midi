package creative.scenes.eventlist.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventKey {
    private List<Integer> values = new ArrayList<>();

    // General keys
    public static int KEY_MIDI=0;
    public static int KEY_META=1;
    public static int KEY_SYSEX=2;

    // midi keys

    // meta keys

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

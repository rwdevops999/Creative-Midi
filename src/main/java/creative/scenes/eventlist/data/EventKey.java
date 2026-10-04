package creative.scenes.eventlist.data;

public record EventKey(Integer value1, Integer value2, Integer value3) {
    // General keys
    public static int KEY_MIDI=0;
    public static int KEY_META=1;
    public static int KEY_SYSEX=2;

    // midi keys

    // meta keys

    // sysex keys

    public EventKey(Integer value1) {
        this(value1, null, null);
    }

    public EventKey(Integer value1, Integer value2) {
        this(value1, value2, null);
    }

    public boolean hasValue(Integer target) {
        // use Objects.equals to prevent NullPointerExceptions if the values are null
        return java.util.Objects.equals(value1, target) ||
                java.util.Objects.equals(value2, target) ||
                java.util.Objects.equals(value3, target);
    }
}

package creative.scenes.sysexgen2.entity;

public interface IEvent {
    public final String SYSEX_START="F0";
    public final String SYSEX_END="F7";

    // returns and space in front of given string
    public default String extend(String str) {
        return " " + str;
    }
}

package creative.scenes.sysexgen.entity;

public interface IEvent {
    String SYSEX_START="F0";
    String SYSEX_END="F7";

    // returns and space in front of given string
    default String extend(String str) {
        return " " + str;
    }
}

package util;

public class Constants {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static final String HELP = "-h";
    public static final String LOGGING = "-l";
    public static final String TEST = "-t";
    public static final String DEBUG = "-d";

    public static final int APP_WIDTH = 800;
    public static final int APP_HEIGHT = 600;
    public final static int MONITOR_HEIGHT=200;

    public final static String SPA="SPA";

    public final static String[] BASE_NOTES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    public final static int OS_MAC=1;
    public final static int OS_WINDOWS=2;

    public static String MONITOR_ACTION_CLEAR = "Clear";
    public static String MONITOR_ACTION_EXPORT = "Export";

    public static String SYSEX_START_BYTE="F0";
    public static String SYSEX_STOP_BYTE="F7";
}

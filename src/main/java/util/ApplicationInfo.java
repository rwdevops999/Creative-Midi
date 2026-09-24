package util;

public class ApplicationInfo {
    private static final ApplicationInfo INSTANCE = new ApplicationInfo();

    private ApplicationInfo() {}

    public static ApplicationInfo getInstance() {
        return INSTANCE;
    }

    // Debugging
    private boolean debugging = false;

    public boolean isDebugging() {
        return debugging;
    }

    public void setDebugging(boolean debugging) {
        this.debugging = debugging;
    }
}

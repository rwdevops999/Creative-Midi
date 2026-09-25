package util;

import javafx.scene.Scene;
import lombok.Getter;
import lombok.Setter;

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

    // Root Scene
    @Getter
    @Setter
    private Scene rootScene;
}

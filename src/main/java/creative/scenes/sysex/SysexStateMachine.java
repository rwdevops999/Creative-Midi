package creative.scenes.sysex;

import creative.scenes.SceneActionsPane;

public class SysexStateMachine {
    public static final int STATE_IDLE=0;
    public static final int STATE_NEW=1;

    private static int runningState;

    public static void setState(int state) {
        runningState = state;
        refresh();
    }

    public static void refresh() {
        handleState();
    }

    private static SceneActionsPane sceneActionsPane;
    public static void setActionsPane (SceneActionsPane actionsPane) {
        sceneActionsPane = actionsPane;
    }

    private static void handleState() {
        switch (runningState) {
            case STATE_IDLE:
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_NEW, true);
                break;
        }
    }
}

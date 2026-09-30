package creative.scenes.sysex;

import communication.CommunicationModel;
import creative.scenes.SceneActionsPane;
import creative.scenes.sysex.data.SysexState;
import entity.sysex.Sysex;

import java.util.Stack;

public class SysexStateMachine {
    private SysexState currentState;
    private final Stack<SysexState> stateHistory = new Stack<>();

    private static SceneActionsPane sceneActionsPane;

    public SysexStateMachine(SysexState initialState) {
        transitionTo(initialState, true);
    }

    public void transitionTo(SysexState newState, boolean clearHistory) {
        if (clearHistory) {
            stateHistory.clear();
        }

        if (currentState != null) {
            stateHistory.push(currentState);
        }
        this.currentState = newState;
        updateUI();
    }

    // Herstel de vorige state
    public void undoState() {
        if (!stateHistory.isEmpty()) {
            this.currentState = stateHistory.pop();
            updateUI();
        }
    }

    // De centrale plek waar buttons worden en-/disabled op basis van de state
    private void updateUI() {
        switch (currentState) {
            case EMPTY -> {
                CommunicationModel.setStatus("SYSEX STATE = EMPTY");
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_NEW, true);
            }
            case NEW -> {
                CommunicationModel.setStatus("SYSEX STATE = NEW");
            }
            case DIRTY -> {
                CommunicationModel.setStatus("SYSEX STATE = DIRTY");
            }
        }
    }

    public SysexState getCurrentState() {
        return currentState;
    }

    public static void setActionsPane(SceneActionsPane sceneActionsPane) {
        SysexStateMachine.sceneActionsPane = sceneActionsPane;
    }
}

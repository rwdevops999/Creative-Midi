package creative.scenes.sysex;

import communication.CommunicationModel;
import creative.scenes.SceneActionsPane;
import creative.scenes.sysex.data.SysexState;

import java.util.Stack;
import java.util.function.BooleanSupplier;

public class SysexStateMachine {
    private SysexState currentState;
    private final Stack<SysexState> stateHistory = new Stack<>();

    private static SceneActionsPane sceneActionsPane;

    public SysexStateMachine(SysexState initialState) {
        transitionTo(initialState, true, null);
    }

    public SysexState getLastState() {
        if (! stateHistory.isEmpty()) {
            return stateHistory.peek();
        }

        return SysexState.EMPTY;
    }

    public void transitionTo(SysexState newState, boolean clearHistory, BooleanSupplier supplier) {
        if (clearHistory) {
            stateHistory.clear();
        }

        if (currentState != null) {
            stateHistory.push(currentState);
        }
        this.currentState = newState;
        updateUI(supplier);
    }

    public void undoStateWithSkips(SysexState skipState, BooleanSupplier supplier) {
        if (!stateHistory.isEmpty()) {
            if (skipState.equals(SysexState.ADDABLE) || skipState.equals(SysexState.UPDATABLE)) {
                while (!stateHistory.isEmpty() && stateHistory.peek().equals(skipState)) {
                    stateHistory.pop();
                }
            }

            this.currentState = getLastState();
            updateUI(supplier);
        }
    }

    // The central place where buttons will be enabled or disabled based on the state
    private void updateUI(BooleanSupplier supplier) {

        sceneActionsPane.disableAll();
        sceneActionsPane.setEnable(SceneActionsPane.BUTTON_EXPORT, true);
        switch (currentState) {
            case EMPTY -> {
                CommunicationModel.setStatus("SYSEX STATE = EMPTY");
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_NEW, true);
            }
            case NEW -> {
                CommunicationModel.setStatus("SYSEX STATE = NEW");
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_NEW, true);
            }
            case ADDABLE -> {
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_NEW, true);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_ADD, true);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_UPDATE, false);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_ACTION, supplier != null && supplier.getAsBoolean());
                CommunicationModel.setStatus("SYSEX STATE = ADDABLE");
            }
            case UPDATABLE -> {
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_NEW, true);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_ADD, false);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_UPDATE, true);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_ACTION, supplier != null && supplier.getAsBoolean());
                CommunicationModel.setStatus("SYSEX STATE = UPDATABLE");
            }
            case ADDED -> {
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_NEW, true);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_ADD, true);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_DELETE, true);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_ACTION, supplier != null && supplier.getAsBoolean());
                CommunicationModel.setStatus("SYSEX STATE = ADDED");
            }
            case LOADED -> {
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_NEW, true);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_ADD, true);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_DELETE, true);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_ACTION, true);
                CommunicationModel.setStatus("SYSEX STATE = LOADED");
            }
            case UPDATED -> {
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_NEW, true);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_ADD, true);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_DELETE, true);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_ACTION, supplier != null && supplier.getAsBoolean());
                CommunicationModel.setStatus("SYSEX STATE = UPDATED");
            }
        }
    }

    public static void setActionsPane(SceneActionsPane sceneActionsPane) {
        SysexStateMachine.sceneActionsPane = sceneActionsPane;
    }
}

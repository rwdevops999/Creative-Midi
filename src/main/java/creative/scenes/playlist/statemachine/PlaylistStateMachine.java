package creative.scenes.playlist.statemachine;

import communication.CommunicationModel;
import creative.scenes.SceneActionsPane;
import lombok.Getter;

import java.util.Stack;
import java.util.function.BooleanSupplier;

public class PlaylistStateMachine {
    @Getter
    private PlaylistState currentState;

    private final Stack<PlaylistState> stateHistory = new Stack<>();

    private static SceneActionsPane sceneActionsPane;

    public PlaylistStateMachine(PlaylistState initialState) {
        transitionTo(initialState, true, null);
    }

    public PlaylistState getLastState() {
        if (! stateHistory.isEmpty()) {
            return stateHistory.peek();
        }

        return PlaylistState.EMPTY;
    }

    public void transitionTo(PlaylistState newState, boolean clearHistory, BooleanSupplier supplier) {
        if (clearHistory) {
            stateHistory.clear();
        }

        if (currentState != null) {
            stateHistory.push(currentState);
        }

        this.currentState = newState;
        updateUI(supplier);
    }

    // Herstel de vorige state
    public void undoState(BooleanSupplier supplier) {
        if (!stateHistory.isEmpty()) {
            this.currentState = stateHistory.pop();
            updateUI(supplier);
        }
    }

    public void undoStateWithSkips(PlaylistState skipState, BooleanSupplier supplier) {
        if (!stateHistory.isEmpty()) {
            if (skipState.equals(PlaylistState.ADDABLE) || skipState.equals(PlaylistState.UPDATABLE)) {
                while (!stateHistory.isEmpty() && stateHistory.peek().equals(skipState)) {
                    stateHistory.pop();
                }
            }

            this.currentState = getLastState();
            updateUI(supplier);
        }
    }

    // De centrale plek waar buttons worden en-/disabled op basis van de state
    private void updateUI(BooleanSupplier supplier) {

        sceneActionsPane.disableAll();
        switch (currentState) {
            case EMPTY -> {
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_NEW, true);
                CommunicationModel.setStatus("PLAYLIST STATE = EMPTY");
            }
            case NEW -> {
                CommunicationModel.setStatus("PLAYLIST STATE = NEW");
            }
            case ADDABLE -> {
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_NEW, true);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_ADD, true);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_UPDATE, false);
                CommunicationModel.setStatus("PLAYLIST STATE = ADDABLE");
            }
            case UPDATABLE -> {
                CommunicationModel.setStatus("PLAYLIST STATE = UPDATABLE");
            }
            case ADDED -> {
                CommunicationModel.setStatus("PLAYLIST STATE = ADDED");
            }
            case LOADED -> {
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_NEW, true);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_DELETE, true);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_ACTION, true);
                CommunicationModel.setStatus("PLAYLIST STATE = LOADED");
            }
            case UPDATED -> {
                CommunicationModel.setStatus("PLAYLIST STATE = UPDATED");
            }
        }
    }

    public static void setActionsPane(SceneActionsPane sceneActionsPane) {
        PlaylistStateMachine.sceneActionsPane = sceneActionsPane;
    }
}

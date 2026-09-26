package creative.scenes;

import javafx.scene.layout.Pane;

public interface IScene {
    default public Pane render(IScene callingScene) {
        return null;
    }

    default public Pane renderScene(IScene callingScene, Pane pane) {
        return null;
    }
}

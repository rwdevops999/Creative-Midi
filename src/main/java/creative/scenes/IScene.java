package creative.scenes;

import javafx.scene.layout.Pane;

public interface IScene {
    public Pane render(IScene callingScene);
}

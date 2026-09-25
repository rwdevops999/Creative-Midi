package router;

import creative.scenes.IScene;

public interface IRouter {
    void routeTo (IScene callingScene, IScene calledScene, boolean cleanup);
}

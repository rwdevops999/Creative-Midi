package creative.scenes.base;

import creative.scenes.IScene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

import static util.constants.SPA;

public class Base extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(Base.class);

    public Base(Pane caller) {
        super();
    }

    public Base(String id, Pane content, IScene callingScene, boolean addClosePane) {
        super();

        setId(SPA);
        ApplicationInfo.getInstance().setSpa(this);

        logger.debug("[CM_BASE] Building {}", getId());

        if (content != null) {
            setCenter(content);
        }

        if (addClosePane) {
            setBottom(new BaseClosePane(callingScene, null));
        }

        logger.debug("[CM_BASE] Built {}", getId());
    }

    // ACCESSOR
    public BaseClosePane getClosePane() {
        return (BaseClosePane) getBottom();
    }
}


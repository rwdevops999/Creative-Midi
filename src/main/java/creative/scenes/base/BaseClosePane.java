package creative.scenes.base;

import communication.CommunicationModel;
import creative.scenes.IScene;
import router.Router;
import util.ColorScheme;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.ColorScheme.getColor;

public class BaseClosePane extends HBox {
    private static final Logger logger = LoggerFactory.getLogger(BaseClosePane.class);

    private static final int PANE_HEIGHT = 30;

    private EventHandler<ActionEvent> closeHandler = null;

    public BaseClosePane() {
        super();
    }

    public BaseClosePane(IScene callingScene, EventHandler<ActionEvent> handler) {
        this();

        if (handler != null) {
            closeHandler = handler;
        }

        setId("ClosePane");

        logger.debug("[CM_CLOSE_PANE] Building {}", getId());

        Background background = new Background(new BackgroundFill(getColor(), null, null));
        setBackground(background);
        setPrefHeight(PANE_HEIGHT);
        setAlignment(Pos.CENTER);

        Button closeButton = new Button("Close");
        closeButton.setId("CloseButton");

        closeButton.setOnAction(e -> {
            if (closeHandler != null) {
                closeHandler.handle(null);
            }

            CommunicationModel.clearStatus();

            Router router = new Router();
            router.routeTo(null, callingScene, true);
        });

        getChildren().addAll(closeButton);

        logger.debug("[CM_CLOSE_PANE] Built {}", getId());
    }

    public void setActionHandler(EventHandler<ActionEvent> handler) {
        closeHandler = handler;
    }
}

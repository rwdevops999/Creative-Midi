package custom.components;

import creative.scenes.IScene;
import creative.scenes.test.TestScene;
import creative.scenes.test.test1.Test1Pane;
import creative.scenes.test.test2.Test2Pane;
import creative.scenes.test.test3.Test3Pane;
import creative.scenes.test.test4.Test4Pane;
import javafx.scene.layout.Pane;
import router.Router;
import util.Util;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

public class ActionButton extends Button {
    private static final Logger logger = LoggerFactory.getLogger(ActionButton.class);

    public ActionButton() {
        super();
    }

    public ActionButton(IScene ownerScene, String name, String tooltip, String iconname, IScene forScene) {
        this();

        logger.debug("[CM_ACTION_BUTTON] Create action button {}", name);

        EventHandler<ActionEvent> handler = e -> {
            Router router = new Router();
            router.routeTo(ownerScene, forScene, true);
        };

        Tooltip tooltipField = new Tooltip( tooltip);

        String nameOfIcon = "/icons/" + iconname + ".png";
        Image icon = new Image(Util.class.getResourceAsStream(nameOfIcon));
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(96);
        imageView.setFitHeight(96);

        setPrefSize(96,96);
        setPadding(new Insets(1));
        setId(name);
        setTooltip(tooltipField);
        setGraphic(imageView);
        setOnAction(handler);
        setStyle("-fx-background-color: yellow; -fx-padding: 1;");

        logger.debug("[CM_UTIL] Created icon button {}", name);
    }

    public ActionButton(IScene ownerScene, String name, String tooltip, String iconname, String paneName) {
        this();

        logger.debug("[CM_ACTION_BUTTON] Create action button {}", name);

        EventHandler<ActionEvent> handler = e -> {
            Pane resultingPane = null;

            Router router = new Router();

            switch (paneName) {
                case "Test1":
                    resultingPane = new Test1Pane();
                    break;
                case "Test2":
                    resultingPane = new Test2Pane();
                    break;
                case "Test3":
                    resultingPane = new Test3Pane();
                    break;
                case "Test4":
                    resultingPane = new Test4Pane();
                    break;
            }

            router.routeTo(ownerScene, new TestScene(), resultingPane, true);
        };

        Tooltip tooltipField = new Tooltip( tooltip);

        String nameOfIcon = "/icons/" + iconname + ".png";
        Image icon = new Image(Util.class.getResourceAsStream(nameOfIcon));
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(96);
        imageView.setFitHeight(96);

        setPrefSize(96,96);
        setPadding(new Insets(1));
        setId(name);
        setTooltip(tooltipField);
        setGraphic(imageView);
        setOnAction(handler);
        setStyle("-fx-background-color: yellow; -fx-padding: 1;");

        logger.debug("[CM_UTIL] Created icon button {}", name);
    }
}

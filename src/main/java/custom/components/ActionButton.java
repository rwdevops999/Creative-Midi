package custom.components;

import creative.scenes.IScene;
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
}

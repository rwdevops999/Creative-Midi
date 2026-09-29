package creative.scenes.sysex.component;

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

public class SysexActionButton extends Button {
    private static final Logger logger = LoggerFactory.getLogger(SysexActionButton.class);

    private static final int BUTTON_SIZE=36;

    public SysexActionButton() {
        super();
    }

    public SysexActionButton(String name, String tooltip, String iconname, EventHandler<ActionEvent> handler) {
        this();

        logger.debug("[CM_ACTION_BUTTON] Create action button {}", name);

        Tooltip tooltipField = new Tooltip( tooltip);

        String nameOfIcon = "/icons/" + iconname + ".png";
        Image icon = new Image(Util.class.getResourceAsStream(nameOfIcon));
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(BUTTON_SIZE);
        imageView.setFitHeight(BUTTON_SIZE);

        setPrefSize(BUTTON_SIZE,BUTTON_SIZE);
        setPadding(new Insets(1));
        setId(name);
        setTooltip(tooltipField);
        setGraphic(imageView);
        setOnAction(handler);
        setStyle("-fx-background-color: yellow; -fx-padding: 1;");

        logger.debug("[CM_UTIL] Created icon button {}", name);
    }
}

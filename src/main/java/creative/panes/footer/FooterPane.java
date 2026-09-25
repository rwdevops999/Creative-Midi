package creative.panes.footer;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.Util.setPaneBackground;

public class FooterPane extends HBox {
    private static final Logger logger = LoggerFactory.getLogger(FooterPane.class);

    public static final int PANE_WIDTH = 800;
    public static final int PANE_HEIGHT = 40;

    public FooterPane() {
        setId("FooterPane");

        setPrefSize(PANE_WIDTH, PANE_HEIGHT);

        setAlignment(Pos.CENTER_LEFT);

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_FOOTER_PANE] Building {}", getId());

        setPaneBackground(this);

        getChildren().add(new CopyrightPane());
        getChildren().add(new StatusPane());
        getChildren().add(new ClockPane());

        logger.debug("[CM_FOOTER_PANE] Built {}", getId());
    }
}

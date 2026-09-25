package creative.panes.header;

import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.Util.setPaneBackground;
import static util.constants.APP_WIDTH;

public class HeaderPane extends GridPane {
    private static final Logger logger = LoggerFactory.getLogger(HeaderPane.class);

    public static final int PANE_HEIGHT = 35;

    public HeaderPane() {
        super();

        setId("HeaderPane");
        setVgap(10);
        setPrefSize(APP_WIDTH, PANE_HEIGHT);

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_HEADER_PANE] Building {}", getId());

        setPaneBackground(this);

        add(new TitlePane(), 0, 0, 10, 1);
        add(new SelectorsPane(), 10, 0, 10, 1);
        add(new ControlsPane(), 20, 0, 2, 1);

        logger.debug("[CM_HEADER_PANE] Built {}", getId());
    }
}

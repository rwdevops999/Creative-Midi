package creative.panes.header;

import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ColorScheme;
import util.DummyUtil;

import java.awt.Color;

import static util.ColorScheme.getColor;
import static util.constants.APP_WIDTH;

public class HeaderPane extends GridPane {
    private static final Logger logger = LoggerFactory.getLogger(HeaderPane.class);

    public static final int PANE_HEIGHT = 60;

    public HeaderPane() {
        super();

        setId("headerPane");
        setHgap(10);
        setVgap(10);
        setPrefSize(APP_WIDTH, PANE_HEIGHT);

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_HEADER_PANE] Building {}", getId());

        DummyUtil.showPaneBorder(this, getColor("test", "red"));

        add(new TitlePane(), 0, 0, 10, 1);

        logger.debug("[CM_HEADER_PANE] Built {}", getId());
    }
}

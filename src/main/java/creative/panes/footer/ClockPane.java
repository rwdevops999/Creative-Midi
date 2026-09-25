package creative.panes.footer;

import eu.hansolo.medusa.Clock;
import eu.hansolo.medusa.ClockBuilder;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ColorScheme;

public class ClockPane extends HBox {
    private static final Logger logger = LoggerFactory.getLogger(ClockPane.class);

    private static final int PANE_WIDTH = 100;

    public ClockPane() {
        super();

        setId("ClockPane");

        setPrefSize(PANE_WIDTH, FooterPane.PANE_HEIGHT);
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(0, 0, 3, 0));

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_CLOCK_PANE] Building {}", getId());

        Clock clock = ClockBuilder.create()
                .skinType(Clock.ClockSkinType.INDUSTRIAL)
                .dateVisible(false)
                .maxHeight(FooterPane.PANE_HEIGHT)
                .prefHeight(FooterPane.PANE_HEIGHT)
                .prefWidth(PANE_WIDTH)
                .running(true)
                .build();
        getChildren().add(clock);

        logger.debug("[CM_CLOCK_PANE] Built {}", getId());
    }
}

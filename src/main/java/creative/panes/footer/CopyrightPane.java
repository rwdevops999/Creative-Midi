package creative.panes.footer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CopyrightPane extends HBox {
    private static final Logger logger = LoggerFactory.getLogger(CopyrightPane.class);

    private static final int PANE_WIDTH = 240;

    public CopyrightPane() {
        super();

        setId("copyrightPane");

        setPrefSize(PANE_WIDTH, FooterPane.PANE_HEIGHT);
        setAlignment(Pos.CENTER_LEFT);

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_COPYRIGHT_PANE] Building {}", getId());

        Font font = Font.font("Arial", FontWeight.NORMAL, 12);
        Text copyright = new Text("Copyright © 2026 Rudi Welter");
        copyright.setFont(font);
        HBox.setMargin(copyright, new Insets(0, 0, 0, 5));
        getChildren().add(copyright);

        logger.debug("[CM_COPYRIGHT_PANE] Built {}", getId());
    }
}

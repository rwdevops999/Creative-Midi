package creative.panes.footer;

import communication.CommunicationModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.ColorScheme.getColor;

public class StatusPane extends HBox {
    private static final Logger logger = LoggerFactory.getLogger(StatusPane.class);

    private static final int PANE_WIDTH = 440;

    public StatusPane() {
        super();

        setId("StatusPane");

        setPrefSize(PANE_WIDTH, FooterPane.PANE_HEIGHT);
        setAlignment(Pos.CENTER_LEFT);

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_STATUS_PANE] Building {}", getId());

        Font font = Font.font("Arial", FontWeight.BOLD, 12);

        Text statusText = new Text();
        statusText.setFill(getColor("status"));
        statusText.setWrappingWidth(0);
        statusText.setFont(font);
        HBox.setMargin(statusText, new Insets(0, 0, 0, 5));
        getChildren().add(statusText);
        statusText.textProperty().bindBidirectional(CommunicationModel.getStatusProperty());

        logger.debug("[CM_STATUS_PANE] Built {}", getId());
    }
}

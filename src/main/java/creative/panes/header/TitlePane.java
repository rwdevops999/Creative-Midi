package creative.panes.header;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DummyUtil;

import java.io.InputStream;
import java.util.Objects;

import static util.ColorScheme.getColor;

public class TitlePane extends HBox {
    private static final Logger logger = LoggerFactory.getLogger(TitlePane.class);

    private static final int PANE_WIDTH = 400;

    private static final int ICON_SIZE = HeaderPane.PANE_HEIGHT + 4;

    public TitlePane() {
        super();

        setId("TitlePane");

        setPrefSize(PANE_WIDTH, HeaderPane.PANE_HEIGHT);
        setSpacing(25);

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_TITLE_PANE] Building {}", getId());

        DummyUtil.showPaneBorder(this, getColor("test", "green"));

        Image icon = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icons/appicon.png")));
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(ICON_SIZE);
        imageView.setFitHeight(ICON_SIZE);
        imageView.setPreserveRatio(true);
        getChildren().add(imageView);
        HBox.setMargin(imageView, new Insets(10));

        InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/GreatVibes-Regular.ttf");
        Font font = Font.loadFont(fontStream, 30);

        Text title = new Text("Creative Midi");
        title.setFont(font);
        setAlignment(Pos.CENTER_LEFT);
        getChildren().add(title);

        logger.debug("[CM_TITLE_PANE] Built {}", getId());
    }
}

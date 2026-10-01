package creative.scenes.playlist;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Util;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneHeightAsPercentage;

public class SongTitlePane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(PlaylistDetailsPane.class);

    public SongTitlePane() {
        super();

        setId("SongTitlePane");
    }

    private PlaylistDetailsPane parent;

    public SongTitlePane(PlaylistDetailsPane owner) {
        this();

//        showPaneBorder(this, getColor("border", "red", null));

        setPaneHeightAsPercentage(this, owner, 5);

        parent = owner;

        buildPane();
    }

    private final StringProperty songTitleProperty = new SimpleStringProperty("");

    private void buildPane() {
        logger.debug("[CM_SONG_TITLE_PANE] Building {}", getId());

        Label songTitle = new Label();
        songTitle.textProperty().bind(songTitleProperty);
        songTitle.setStyle(
                "-fx-text-fill: black; " +
                        "-fx-font-weight: bold;"
        );
        songTitle.setId("DetailsTitleLabel");
        getChildren().add(songTitle);

        Separator separator = new Separator(Orientation.HORIZONTAL);
        getChildren().add(separator);

        logger.debug("[CM_SONG_TITLE_PANE] Built {}", getId());
    }

    public void updateSongTitle (String title) {
        songTitleProperty.set(title);
    }

    // ACCESSORS
    public PlaylistDetailsPane getPlaylistDetailsPane() {
        return parent;
    }
}

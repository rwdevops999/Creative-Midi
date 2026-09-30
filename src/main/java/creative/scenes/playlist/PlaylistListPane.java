package creative.scenes.playlist;

import entity.playlist.Song;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;
import static util.Util.setPaneWidthAsPercentage;

public class PlaylistListPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(PlaylistListPane.class);

    public PlaylistListPane() {
        super();

        setId("PlaylistListPane");
    }

    private PlaylistPane parent;

    public PlaylistListPane(PlaylistPane owner) {
        this();

        parent = owner;

        showPaneBorder(this, getColor("border", "red", null));

        setPaneWidthAsPercentage(this, owner, 30);
        setPaneBackground(this);

        buildPane();
    }

    private ListView<String> playlistListView;
    private void buildPane() {
        logger.debug("[CM_PLAYLIST_LIST_PANE] Building {}", getId());

        playlistListView = new ListView<String>();
        playlistListView.setId("PlaylistListView");
//        playlistListView.setItems(FXCollections.observableArrayList(PlaylistContainer.getSongnamesFromPlaylist()));
        playlistListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        });
        VBox.setVgrow(playlistListView, Priority.ALWAYS);
        getChildren().add(playlistListView);

        logger.debug("[CM_PLAYLIST_LIST_PANE] Built {}", getId());
    }

    public Song getSelectedSong() {
        return null;
    }

    // ACCESSORS
    public PlaylistPane getPlaylistPane() {
        return parent;
    }
}

package creative.scenes.playlist;

import custom.dialog.MappingDialog;
import entity.playlist.Mapping;
import entity.playlist.Song;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;
import util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneHeightAsPercentage;

public class SongMappingsPane extends StackPane {
    private static final Logger logger = LoggerFactory.getLogger(SongMappingsPane.class);

    public SongMappingsPane() {
        super();

        setId("SongMappingsPane");
    }

    private PlaylistDetailsPane parent;
    public SongMappingsPane(PlaylistDetailsPane owner) {
        this();

        parent = owner;

        showPaneBorder(this, getColor("border", "red", null));
        setPaneHeightAsPercentage(this, owner, 50);

        buildPane(new ArrayList<>());
    }

    private final ObjectProperty<ObservableList<Mapping>> mappingProperty = new SimpleObjectProperty<>(FXCollections.observableArrayList(new ArrayList<>()));

    private TableView<Mapping> table  = new TableView<>();
    private boolean isPaneBuilt = false;
    public void buildPane(List<Mapping> mappings) {
        if (getPlaylistDetailsPane().getSongDetailsPane().getSong() != null && ! isPaneBuilt) {
            table = new TableView<>();
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            table.itemsProperty().bindBidirectional(mappingProperty);

            buildColumns(table);
            setMappingData(mappings);
            buildContextMenu(table);

            getChildren().add(table);

            isPaneBuilt = true;
        }

        logger.debug("[CM_SONG_MAPPING_PANE] Built {}", getId());
    }

    private void buildContextMenu(TableView<Mapping> table) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem addMenuItem = new MenuItem("Add mapping");
        addMenuItem.setOnAction(e -> {
            new MappingDialog(this, table, false, null);
            getPlaylistDetailsPane().getSongDetailsPane().updateSongMapping(new ArrayList<Mapping>(table.getItems()));
        });

        MenuItem deleteMenuItem = new MenuItem("Delete mapping");
        deleteMenuItem.setOnAction(e -> {
            Mapping mapping = table.getSelectionModel().getSelectedItem();
            table.getItems().remove(mapping);
            getPlaylistDetailsPane().getSongDetailsPane().updateSongMapping(new ArrayList<Mapping>(table.getItems()));
        });
        deleteMenuItem.disableProperty().bind(
                table.getSelectionModel().selectedItemProperty().isNull()
        );

        MenuItem editMenuItem = new MenuItem("Edit mapping");
        editMenuItem.setOnAction(e -> {
            Mapping mapping = table.getSelectionModel().getSelectedItem();
            showAddMappingDialog(table, true, mapping);
            getPlaylistDetailsPane().getSongDetailsPane().updateSongMapping(new ArrayList<Mapping>(table.getItems()));
        });
        editMenuItem.disableProperty().bind(
                table.getSelectionModel().selectedItemProperty().isNull()
        );

        contextMenu.getItems().addAll(addMenuItem, deleteMenuItem, editMenuItem);
        table.setContextMenu(contextMenu);
    }

    private void setMappingData(List<Mapping> mappings) {
        ObservableList<Mapping> tableData = FXCollections.observableArrayList(mappings);
        mappingProperty.set(tableData);
    }

    private void buildColumns(TableView<Mapping> table) {
        TableColumn<Mapping, String> receiveColumn = new TableColumn<>("Received");
        receiveColumn.setCellValueFactory(new PropertyValueFactory<>("receive"));

        TableColumn<Mapping, String> replyColumn = new TableColumn<>("Reply");
        replyColumn.setCellValueFactory(new PropertyValueFactory<>("reply"));

        table.getColumns().addAll(receiveColumn, replyColumn);
    }

    private void showAddMappingDialog(TableView<Mapping> table, boolean isUpdate, @Nullable Mapping currentMapping) {
        new MappingDialog(this, table, isUpdate, currentMapping);
    }

    public void setSong(Song song) {
        if (song != null) {
            if (table != null) {
                ObservableList<Mapping> tableData = FXCollections.observableArrayList(new ArrayList<>(song.getMappings()));
                mappingProperty.set(tableData);
            }
        }
    }

    // ACCESSORS
    public PlaylistDetailsPane getPlaylistDetailsPane() {
        return parent;
    }
}

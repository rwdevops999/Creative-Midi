package creative.scenes.playlist;

import entity.playlist.Mapping;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

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

//        showPaneBorder(this, getColor("border", "red", null));
        setPaneHeightAsPercentage(this, owner, 50);

        buildPane();
    }

    private TableView<Mapping> table;

    public void buildPane() {
        logger.debug("[CM_SONG_MAPPINGS_PANE] Building {}", getId());

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        defineTableColumns(table);
        setTableData(table);
        createContextMenu(table);
        getChildren().add(table);

        logger.debug("[CM_SONG_MAPPINGS_PANE] Built {}", getId());
    }

    private void defineTableColumns(TableView<Mapping> table) {
        TableColumn<Mapping, String> receiveColumn = new TableColumn<>("Received");
        receiveColumn.setCellValueFactory(new PropertyValueFactory<>("receive"));
        table.getColumns().add(receiveColumn);

        TableColumn<Mapping, String> replyColumn = new TableColumn<>("Reply");
        replyColumn.setCellValueFactory(new PropertyValueFactory<>("reply"));
        table.getColumns().add(replyColumn);
    }

    private final ObjectProperty<ObservableList<Mapping>> mappingProperty = new SimpleObjectProperty<>(FXCollections.observableArrayList(new ArrayList<>()));
    private void setTableData(TableView<Mapping> table) {
        ObservableList<Mapping> tableData = FXCollections.observableArrayList(new ArrayList<>());
        table.setItems(tableData);
        table.itemsProperty().bindBidirectional(mappingProperty);
    }

    private void createContextMenu(TableView<Mapping> table) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem addMenuItem = new MenuItem("Add mapping");
        addMenuItem.setOnAction(e -> {
            // TODO add context menu action
        });
        contextMenu.getItems().add(addMenuItem);

        MenuItem deleteMenuItem = new MenuItem("Delete mapping");
        deleteMenuItem.setOnAction(e -> {
            // TODO add context menu action
        });
        deleteMenuItem.disableProperty().bind(
            table.getSelectionModel().selectedItemProperty().isNull()
        );
        contextMenu.getItems().add(deleteMenuItem);

        MenuItem editMenuItem = new MenuItem("Edit mapping");
        editMenuItem.setOnAction(e -> {
            // TODO add context menu action
        });
        editMenuItem.disableProperty().bind(
            table.getSelectionModel().selectedItemProperty().isNull()
        );
        contextMenu.getItems().add(editMenuItem);

        table.setContextMenu(contextMenu);
    }

    // ACCESSORS
    public PlaylistDetailsPane getPlaylistDetailsPane() {
        return parent;
    }
}

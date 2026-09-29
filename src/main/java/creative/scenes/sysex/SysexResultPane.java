package creative.scenes.sysex;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;
import static util.Util.setPaneHeightAsPercentage;

public class SysexResultPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(SysexResultPane.class);

    public SysexResultPane() {
        super();

        setId("SysexResultPane");
    }

    private SysexPane parent;
    public SysexResultPane(SysexPane owner) {
        this();

        this.parent = owner;

//        showPaneBorder(this, getColor("border", "red", null));
        setPaneHeightAsPercentage(this, owner, 21);
        setPaneBackground(this);

        buildPane();
    }

    ListView<String> listView = null;

    @Getter     // ACCESSOR
    ActionsPane actionsPane = null;
    private void buildPane() {
        logger.debug("[CM_SYSEX_RESULT_PANE] Building {}", getId());

        ContextMenu contextMenu = new ContextMenu();
        MenuItem copy = new MenuItem("Copy To Clipboard");
        copy.setDisable(true);
        copy.setOnAction(event -> {
            StringSelection stringSelection = new StringSelection(listView.getSelectionModel().getSelectedItem());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });
        MenuItem clear = new MenuItem("Clear All");
        clear.setOnAction(event -> listView.getItems().clear());
        contextMenu.getItems().addAll(copy, clear);

        listView = new ListView<>();
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                copy.setDisable(false);
            }
        });
        listView.setContextMenu(contextMenu);
        getChildren().add(listView);

        actionsPane = new ActionsPane(sysexSupplier);
        getChildren().add(actionsPane);

        logger.debug("[CM_SYSEX_RESULT_PANE] Built {}", getId());
    }

    private List<String> currentSysexList = new ArrayList<>();

    public void renderSysex(List<String> sysexList) {
        currentSysexList = sysexList;

        ObservableList<String> items = FXCollections.observableArrayList(sysexList);

        listView.getItems().clear();
        listView.getItems().addAll(items);

        if (actionsPane != null) {
            actionsPane.setEnables(sysexSupplier);
        }
    }

    public Supplier<List<String>> sysexSupplier = () -> currentSysexList;

    // ACCESSORS
    public SysexPane getSysexPane() {
        return parent;
    }
}

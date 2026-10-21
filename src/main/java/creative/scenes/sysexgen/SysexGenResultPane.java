package creative.scenes.sysexgen;

import creative.scenes.sysexgen.components.SysexActionsPane;
import creative.scenes.sysexgen.entity.Event;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.*;

public class SysexGenResultPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(SysexGenResultPane.class);

    public SysexGenResultPane() {
        super();

        setId("SysexGenResultPane");
    }

    private ListView<String> listView = null;

    public SysexGenResultPane(SysexGenPane owner) {
        this();

        setPaneMinHeightAsPercentage(this, owner, 21);
        setPaneBackground(this);

//        showPaneBorder(this, getColor("border", "red"));

        buildPane();
    }

    private final BooleanProperty disabledContextMenu = new SimpleBooleanProperty(true);
    private SysexActionsPane actionsPane;

    private void buildPane() {
        logger.debug("[CM_SYSEX_GEN_RESULT_PANE] building {}", getId());

        ContextMenu contextMenu = new ContextMenu();
        MenuItem copy = new MenuItem("Copy To Clipboard");
        copy.disableProperty().bind(disabledContextMenu);

        copy.setOnAction(event -> {
            StringSelection stringSelection = new StringSelection(listView.getSelectionModel().getSelectedItem());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });

        MenuItem clear = new MenuItem("Clear All");
        clear.disableProperty().bind(disabledContextMenu);
        clear.setOnAction(event -> {
            listView.getItems().clear();
            disabledContextMenu.set(true);
        });
        contextMenu.getItems().addAll(copy, clear);

        listView = new ListView<>();
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                disabledContextMenu.set(false);
            }
        });
        listView.setContextMenu(contextMenu);

        getChildren().add(listView);

      actionsPane = new SysexActionsPane(sysexSupplier);
        getChildren().add(actionsPane);

        logger.debug("[CM_SYSEX_GEN_RESULT_PANE] built {}", getId());
    }

    private List<String> currentSysexList = new ArrayList<>();
    public void showSysex(Event event) {
        showSysex(event.complete(new ArrayList<>(event.createSysexStrings())));
    }

    public void showSysex(List<String> sysexList) {
        currentSysexList = sysexList;

        listView.getItems().clear();
        for (String sysexString : sysexList) {
            listView.getItems().add(sysexString);
        }

        handleDisabling(sysexList);
    }

    private void handleDisabling(List<String> sysexList) {
        disabledContextMenu.set(sysexList.isEmpty());
        getSysexActionsPane().setButtonsDisabled(sysexList.isEmpty());

    }
    public Supplier<List<String>> sysexSupplier = () -> currentSysexList;

    // ACCESSORS
    public SysexActionsPane getSysexActionsPane() {
        return actionsPane;
    }
}

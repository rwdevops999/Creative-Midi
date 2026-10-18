package creative.scenes.sysexgen2;

import creative.scenes.sysexgen2.entity.Event;
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

    public SysexGenResultPane(SysexGen2Pane owner) {
        this();

        setPaneMinHeightAsPercentage(this, owner, 21);
        setPaneBackground(this);

        showPaneBorder(this, getColor("border", "red"));

        buildPane();
    }

    private final BooleanProperty disabled = new SimpleBooleanProperty(true);

    private void buildPane() {
        logger.debug("[CM_SYSEX_GEN_RESULT_PANE] building {}", getId());

        ContextMenu contextMenu = new ContextMenu();
        MenuItem copy = new MenuItem("Copy To Clipboard");
        copy.disableProperty().bind(disabled);

        copy.setOnAction(event -> {
            StringSelection stringSelection = new StringSelection(listView.getSelectionModel().getSelectedItem());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });

        MenuItem clear = new MenuItem("Clear All");
        clear.disableProperty().bind(disabled);
        clear.setOnAction(event -> {
            listView.getItems().clear();
            disabled.set(true);
        });
        contextMenu.getItems().addAll(copy, clear);

        listView = new ListView<>();
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                disabled.set(false);
            }
        });
        listView.setContextMenu(contextMenu);

        getChildren().add(listView);

  /*      ActionsPane actionsPane = new ActionsPane(sysexSupplier);
        getChildren().add(actionsPane);
  */
        logger.debug("[CM_SYSEX_GEN_RESULT_PANE] built {}", getId());
    }

    public void showSysex(Event event) {
        List<String> sysexList = event.complete(new ArrayList<>(event.createSysexStrings()));

        for (String sysexString : sysexList) {
            listView.getItems().add(sysexString);
        }

        disabled.set(! sysexList.isEmpty());

        System.out.println("RENDER THIS");
    }
}

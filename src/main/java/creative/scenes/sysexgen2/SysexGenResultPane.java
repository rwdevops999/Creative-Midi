package creative.scenes.sysexgen2;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ColorScheme;
import util.Util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;
import static util.Util.setPaneMinWidthAsPercentage;

public class SysexGenResultPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(SysexGenResultPane.class);

    public SysexGenResultPane() {
        super();

        setId("SysexGenResultPane");
    }

    private ListView<String> listView = null;

    public SysexGenResultPane(SysexGen2Pane owner) {
        this();

        setPaneMinWidthAsPercentage(this, owner, 21);
        setPaneBackground(this);

        showPaneBorder(this, getColor("border", "red"));

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_SYSEX_GEN_RESULT_PANE] building {}", getId());

        ContextMenu contextMenu = new ContextMenu();
        MenuItem copy = new MenuItem("Copy To Clipboard");
        copy.setDisable(true);
/*        copy.setOnAction(event -> {
            StringSelection stringSelection = new StringSelection(listView.getSelectionModel().getSelectedItem());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }); */
        MenuItem clear = new MenuItem("Clear All");
//        clear.setOnAction(event -> listView.getItems().clear());
        contextMenu.getItems().addAll(copy, clear);

        listView = new ListView<>();
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                copy.setDisable(false);
            }
        });
        listView.setContextMenu(contextMenu);

        getChildren().add(listView);

  /*      ActionsPane actionsPane = new ActionsPane(sysexSupplier);
        getChildren().add(actionsPane);
  */
        logger.debug("[CM_SYSEX_GEN_RESULT_PANE] built {}", getId());
    }
}

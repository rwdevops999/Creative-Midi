package creative.scenes.sysex;

import entity.midi.Midi;
import entity.sysex.Sysex;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Util;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneWidthAsPercentage;

public class SysexListPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(SysexListPane.class);

    public SysexListPane() {
        super();

        setId("SysexListPane");
    }

    private SysexPane parent;
    public SysexListPane(SysexPane owner) {
        this();

//        showPaneBorder(this, getColor("border", "red", null));
        setPaneWidthAsPercentage(this, owner, 30);

        parent = owner;

        buildPane();
    }

    private ListView<Sysex> sysexListView;
    private void buildPane() {
        logger.debug("[CM_SYSEX_LIST_PANE] Building {}", getId());

        sysexListView = new ListView<>();
        setListviewItems();

        sysexListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            SysexPane sysexPane = getSysexPane();
            SysexDetailsPane sysExDetailsPane = sysexPane.getSysexDetailsPane();
//            if (newValue != null) {
                sysExDetailsPane.setSysex(newValue);
//            }
        });
        sysexListView.setCellFactory(param -> new ListCell<Sysex>() {
            @Override
            protected void updateItem(Sysex sysex, boolean empty) {
                super.updateItem(sysex, empty);

                if (empty || sysex == null) {
                    setText(null);
                } else {
                    setText(sysex.getName());
                }
            }
        });

        VBox.setVgrow(sysexListView, Priority.ALWAYS);
        getChildren().add(sysexListView);

        logger.debug("[CM_SYSEX_LIST_PANE] Built {}", getId());
    }

    private void setListviewItems() {
        sysexListView.getItems().clear();
        sysexListView.setItems(FXCollections.observableArrayList(SysexContainer.getSysexList()));
    }

    public void triggerReload(Sysex selectable) {
        setListviewItems();

        if (selectable != null) {
            sysexListView.getSelectionModel().select(selectable);
        }
    }

    // ACCESSORS
    public SysexPane getSysexPane() {
        return parent;
    }
}

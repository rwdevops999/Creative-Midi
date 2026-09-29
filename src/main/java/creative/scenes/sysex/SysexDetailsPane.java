package creative.scenes.sysex;

import creative.scenes.sysex.component.AddableSysexPane;
import creative.scenes.sysex.component.SysexNamePane;
import creative.scenes.voice.components.AddablePane;
import entity.sysex.Sysex;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;
import static util.Util.setPaneWidthAsPercentage;

public class SysexDetailsPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(SysexDetailsPane.class);

    public SysexDetailsPane() {
        super();

        setId("SysexDetailsPane");

        setPadding(new Insets(0, 0, 0, 0));
        setSpacing(2);
    }

    private List<AddableSysexPane> addPanes = new ArrayList<>();

    private SysexPane parent;
    private final StringProperty dsSysexName = new SimpleStringProperty();
    private final StringProperty[] dsSysexValue = new StringProperty[SYSEX_VALUES];

    public SysexDetailsPane(SysexPane owner) {
        this();

        dsSysexValue[0] = new SimpleStringProperty();
        addPanes.add(new AddableSysexPane(this, 0, dsSysexValue[0]));
        for (int i = 1; i < SYSEX_VALUES; i++) {
            dsSysexValue[i] = new SimpleStringProperty();
            addPanes.add(null);
        }

        parent = owner;

        showPaneBorder(this, getColor("border", "red", null));
        setPaneWidthAsPercentage(this, owner, 60);
        setPaneBackground(this);

        for (int i =0; i < SYSEX_VALUES; i++) {
            dsSysexValue[i] = new SimpleStringProperty();
        }

        buildPane();
    }

    private static final int SYSEX_VALUES = 12;

    private void buildPane() {
        logger.debug("[CM_SYSEX_DETAILS_PANE] Building {}", getId());

        getChildren().add(new SysexNamePane(this, dsSysexName));

        getChildren().add(addPanes.get(0));

        buildDynamicPane();

        logger.debug("[CM_SYSEX_DETAILS_PANE] Built {}", getId());
    }

    private void buildDynamicPane() {
        int index = 0;
        for (AddableSysexPane addPane : addPanes) {
            if (addPane != null) {
                if (index > 0) {
                    addPane.setId("Deletable");
                    getChildren().add(addPane);
                }
                index++;
            }
        }
    }

    private void removeDeletables() {
        getChildren().removeIf(node -> "Deletable".equals(node.getId()));
    }

    public void addPane(int id) {
        for (int i = addPanes.size() - 2; i > id; i-- ) {
            AddableSysexPane addPane = addPanes.get(i);
            if (addPane != null) {
                addPane.setButtonId(i+1);
            }
            addPanes.set(i+1, addPane);
        }

        if (id+1 < addPanes.size()) {
            addPanes.set(id+1, new AddableSysexPane(this, id+1, dsSysexValue[id+1]));
        }

        removeDeletables();
        buildDynamicPane();
    }

    public void removePane(int id) {
        if (id > 0) {
            for (int i = id; i < addPanes.size() - 1; i++) {
                AddableSysexPane addPane = addPanes.get(i+1);
                if (addPane != null) {
                    addPane.setButtonId(i);
                }
                addPanes.set(i, addPane);
            }

            addPanes.set(addPanes.size()-1, null);

            removeDeletables();
            buildDynamicPane();
        }
    }

    public void setSysex (Sysex sysex) {
        System.out.println("ADDING SYSEX DETAILS");
    }

    // ACCESSORS
    public SysexPane getSysexPane() {
        return parent;
    }
}

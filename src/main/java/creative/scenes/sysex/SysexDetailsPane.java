package creative.scenes.sysex;

import creative.scenes.sysex.component.AddableSysexPane;
import creative.scenes.sysex.component.SysexNamePane;
import creative.scenes.sysex.data.SysexChangeHandler;
import creative.scenes.sysex.data.SysexState;
import entity.sysex.Sysex;
import entity.sysex.SysexContent;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;
import static util.Util.setPaneWidthAsPercentage;

public class SysexDetailsPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(SysexDetailsPane.class);

    @Getter
    private final SysexStateMachine stateMachine = new SysexStateMachine(SysexState.EMPTY);

    public SysexDetailsPane() {
        super();

    //    stateMachine = new SysexStateMachine(SysexState.EMPTY);

        setId("SysexDetailsPane");

        setPadding(new Insets(0, 0, 0, 0));
        setSpacing(2);
    }

    private final List<AddableSysexPane> addPanes = new ArrayList<>();

    private SysexPane parent;
    private final StringProperty dsSysexName = new SimpleStringProperty("Unknown");
    private final StringProperty[] dsSysexValue = new StringProperty[SYSEX_VALUES];

    private Sysex workingSysex = new Sysex();

    @Getter
    private BooleanSupplier supplier = new BooleanSupplier() {
        @Override
        public boolean getAsBoolean() {
            boolean result = false;

            for (StringProperty stringProperty : dsSysexValue) {
                result = result || (stringProperty != null && !stringProperty.get().isEmpty());
            }

            return result;
        }
    };

    private final SysexChangeHandler changeHandler = () -> {
        Sysex originalSysex = ApplicationInfo.getInstance().getCurrentSysex();
        if (! workingSysex.equals(originalSysex)) {
            ApplicationInfo.getInstance().setDirtySysex(workingSysex);

            if (originalSysex.getName().equals(dsSysexName.get())) {
                stateMachine.transitionTo(SysexState.UPDATABLE, false, supplier);
            } else {
                stateMachine.transitionTo(SysexState.ADDABLE, false, supplier);
            }
        } else {
            ApplicationInfo.getInstance().setDirtySysex(null);
            stateMachine.undoStateWithSkips(stateMachine.getLastState(), supplier);
        }
    };

    public SysexDetailsPane(SysexPane owner, Sysex sysex) {
        this();

        if (sysex != null) {
            this.workingSysex = sysex;
            ApplicationInfo.getInstance().setCurrentSysex(new Sysex(sysex));
        }

        dsSysexValue[0] = new SimpleStringProperty();
        addPanes.add(new AddableSysexPane(this, 0));
        for (int i = 1; i < SYSEX_VALUES; i++) {
            dsSysexValue[i] = new SimpleStringProperty();
            addPanes.add(null);
        }

        setupDatasource();

        parent = owner;

        showPaneBorder(this, getColor("border", "red", null));
        setPaneWidthAsPercentage(this, owner, 60);
        setPaneBackground(this);

        for (int i =0; i < SYSEX_VALUES; i++) {
            dsSysexValue[i] = new SimpleStringProperty();
        }

        buildPane();
    }

    private void setupDatasource() {
        dsSysexName.addListener((observable, oldValue, newValue) -> workingSysex.setName(newValue));
    }

    private static final int SYSEX_VALUES = 12;

    private void buildPane() {
        logger.debug("[CM_SYSEX_DETAILS_PANE] Building {}", getId());
        if (ApplicationInfo.getInstance().getCurrentSysex() == null) {
            return;
        }

        getChildren().add(new SysexNamePane(this, dsSysexName, changeHandler));

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

                final int fixedIntIndex = index;

                addPane.getInputField().textProperty().bindBidirectional(dsSysexValue[fixedIntIndex]);
                dsSysexValue[index].addListener((observable, oldValue, newValue) -> {
                        if (newValue != null && !newValue.isEmpty()) {
                            if (workingSysex.getList().size() > fixedIntIndex) {
                                workingSysex.getList().set(fixedIntIndex, new SysexContent(dsSysexValue[fixedIntIndex].get()));
                            } else {
                                workingSysex.getList().add(fixedIntIndex, new SysexContent(dsSysexValue[fixedIntIndex].get()));
                            }
                        } else {
                            workingSysex.getList().remove(fixedIntIndex);
                        }
                        changeHandler.handle();
                });

                index++;
            }
        }
    }

    private void removeDeletable() {
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
            addPanes.set(id+1, new AddableSysexPane(this, id+1));
        }

        removeDeletable();
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

            removeDeletable();
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

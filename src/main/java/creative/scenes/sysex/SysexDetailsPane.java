package creative.scenes.sysex;

import creative.scenes.sysex.component.AddableSysexPane;
import creative.scenes.sysex.component.SysexNamePane;
import creative.scenes.sysex.data.SysexState;
import entity.sysex.Sysex;
import entity.sysex.SysexContent;
import eventhandlers.ChangeHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

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
                result = result || (stringProperty != null && stringProperty.get() != null && !stringProperty.get().isEmpty());
            }

            return result;
        }
    };

    private final ChangeHandler changeHandler = () -> {
        Sysex originalSysex = ApplicationInfo.getInstance().getCurrentSysex();
        if (workingSysex != null && ! workingSysex.equals(originalSysex)) {
//            ApplicationInfo.getInstance().setDirtySysex(workingSysex);

            if (originalSysex.getName().equals(dsSysexName.get()) && (SysexContainer.containsSysex(dsSysexName.get()))) {
                stateMachine.transitionTo(SysexState.UPDATABLE, false, supplier);
            } else {
                stateMachine.transitionTo(SysexState.ADDABLE, false, supplier);
            }
        } else {
//            ApplicationInfo.getInstance().setDirtySysex(null);
            stateMachine.undoStateWithSkips(stateMachine.getLastState(), supplier);
        }
    };

    public SysexDetailsPane(SysexPane owner, Sysex sysex) {
        this();

        if (sysex != null) {
            this.workingSysex = sysex;
            ApplicationInfo.getInstance().setCurrentSysex(new Sysex(sysex));
        }

        for (int i = 0; i < SYSEX_VALUES; i++) {
            dsSysexValue[i] = new SimpleStringProperty("");
        }

        parent = owner;

        showPaneBorder(this, getColor("border", "red", null));
        setPaneWidthAsPercentage(this, owner, 60);
        setPaneBackground(this);

        buildPane();
    }

    private void setupNameDatasource(String name) {
        dsSysexName.set(name);
        dsSysexName.addListener((observable, oldValue, newValue) -> {
            if (workingSysex != null) {
                workingSysex.setName(newValue);
            }
        });
    }

    private void clearDataSource() {
        if (workingSysex != null) {
            dsSysexName.set(workingSysex.getName());

            for (int i = 0; i < dsSysexValue.length; i++) {
                dsSysexValue[i] = new SimpleStringProperty(workingSysex.getList().isEmpty() ? "" : i < workingSysex.getList().size() ? workingSysex.getList().get(i).getContent() : "");
            }
        } else {
            dsSysexName.set("");

            for (int i = 0; i < dsSysexValue.length; i++) {
                dsSysexValue[i] = new SimpleStringProperty("");
            }
        }
    }

    private static final int SYSEX_VALUES = 12;

    private void buildPane() {
        logger.debug("[CM_SYSEX_DETAILS_PANE] Building {}", getId());
        if (ApplicationInfo.getInstance().getCurrentSysex() == null) {
            return;
        }

        setupNameDatasource(workingSysex.getName());
        getChildren().add(new SysexNamePane(this, dsSysexName, changeHandler));

        buildDynamicPane();

        logger.debug("[CM_SYSEX_DETAILS_PANE] Built {}", getId());
    }

    private void buildDynamicPane() {
        int index = 0;

        for (SysexContent sysexContent : workingSysex.getList()) {
            dsSysexValue[index] = new SimpleStringProperty("");
            if (sysexContent != null) {
                dsSysexValue[index].set(sysexContent.getContent());
            }

            AddableSysexPane pane = new AddableSysexPane(this, index);
            pane.setButtonId(index);

            pane.setId("Deletable");

            addPanes.add(pane);
            getChildren().add(pane);

            bindProperty(index, pane);

            index++;
        }

        Label addLabel = new Label();
        String message = "Still " + (SYSEX_VALUES - workingSysex.getList().size()) + " entries allowed";
        addLabel.setId("Deletable");
        if (SYSEX_VALUES - workingSysex.getList().size() == 0) {
            message = "No more entries allowed";
            addLabel.setTextFill(getColor("sysex", "entries", "full"));
        }

        addLabel.setText(message);
        getChildren().add(addLabel);
    }

    private void bindProperty(int index, AddableSysexPane pane) {
        final int fixedIntIndex = index;

        pane.getInputField().textProperty().bindBidirectional(dsSysexValue[fixedIntIndex]);
        dsSysexValue[index].addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                if (workingSysex.getList().size() > fixedIntIndex) {
                    workingSysex.getList().set(fixedIntIndex, new SysexContent(dsSysexValue[fixedIntIndex].get()));
                } else {
                    workingSysex.getList().add(fixedIntIndex, new SysexContent(dsSysexValue[fixedIntIndex].get()));
                }
            } else {
                if (! workingSysex.getList().isEmpty()) {
                    workingSysex.getList().remove(fixedIntIndex);
                }
            }
            changeHandler.handle();
        });
    }

    private void removeDeletable() {
        getChildren().removeIf(p -> "Deletable".equals(p.getId()));
    }

    public void addPane(int id) {
        if (workingSysex != null) {
            if (workingSysex.getList().size() < SYSEX_VALUES) {
                workingSysex.getList().add(id+1, new SysexContent(""));
            }
        }

        removeDeletable();
        buildDynamicPane();
    }

    public void removePane(int id) {
        if (id > 0) {
            if (workingSysex != null && workingSysex.getList().size() > 0) {
                workingSysex.getList().remove(id);
            }

            removeDeletable();
            buildDynamicPane();
        }
    }

    public void setSysex (Sysex sysex) {
        workingSysex = sysex;

        if (sysex != null) {
            ApplicationInfo.getInstance().setCurrentSysex(new Sysex(sysex));
        } else {
            ApplicationInfo.getInstance().setCurrentSysex(null);
        }

        clearDataSource();
        getChildren().clear();
        buildPane();

        stateMachine.transitionTo(SysexState.LOADED, true, supplier);
    }

    public Sysex getSysex () {
        return workingSysex;
    }

    // ACCESSORS
    public SysexPane getSysexPane() {
        return parent;
    }
}

package creative.scenes;

import creative.scenes.midi.MidiPane;
import creative.scenes.sysex.SysexPane;
import entity.AEntity;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static util.Util.*;

public class SceneActionsPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(SceneActionsPane.class);

    public static final int[] BUTTON = new int[] { 0x01, 0x02, 0x04, 0x08, 0x10, 0x20 };
    public static final int BUTTON_NEW=0;
    public static final int BUTTON_ADD=1;
    public static final int BUTTON_DELETE=2;
    public static final int BUTTON_UPDATE=3;
    public static final int BUTTON_ACTION=4;
    public static final int BUTTON_EXPORT=5;

    private final BooleanProperty[] disabledProperties = new SimpleBooleanProperty[6];

    public SceneActionsPane() {
        super();

        setId("SceneActionsPane");

        for (int i = 0; i < 6; i++) {
            disabledProperties[i] = new SimpleBooleanProperty(false);
        }
    }

    private Pane parent;

    public SceneActionsPane(Pane owner, String actionName, BiConsumer<AEntity, Pane>[] consumers, Supplier<AEntity> supplier) {
        this();

        this.parent = owner;

        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(5, 0, 0, 0));
        setSpacing(5);

        setPaneBackground(this);

        buildPane(owner, consumers, supplier, actionName);
    }

    public void setEnables(int enable) {
        for (int i = 0; i < 6; i++) {
            disabledProperties[i].set((enable & BUTTON[i]) == 0);
        }
    }

    private void buildPane(Pane owner, BiConsumer<AEntity, Pane>[] consumers, Supplier<AEntity> supplier, String actionName) {
        logger.debug("[CM_SCENE_ACTIONS_PANE] Building {}", getId());

        setPaneWidthAsPercentage(this, owner, 10);

        getChildren().add(createButton(this, "newButton", "New", 80, consumers[BUTTON_NEW], disabledProperties[BUTTON_NEW], supplier));
        getChildren().add(createButton(this, "addButton", "Add", 80, consumers[BUTTON_ADD], disabledProperties[BUTTON_ADD], supplier));
        getChildren().add(createButton(this, "deleteButton", "Delete", 80, consumers[BUTTON_DELETE], disabledProperties[BUTTON_DELETE], supplier));
        getChildren().add(createButton(this, "updateButton", "Update", 80, consumers[BUTTON_UPDATE], disabledProperties[BUTTON_UPDATE], supplier));
        Button actionButton = createButton(this, "actionButton", actionName, 80, consumers[BUTTON_ACTION], disabledProperties[BUTTON_ACTION], supplier);
        getChildren().add(actionButton);
        getChildren().add(createButton(this, "exportButton", "Export", 80, consumers[BUTTON_EXPORT], disabledProperties[BUTTON_EXPORT], supplier));

        logger.debug("[CM_SCENE_ACTIONS_PANE] Built {}", getId());
    }

    public void setEnable(int id, boolean enable) {
        disabledProperties[id].set(!enable);
    };

    // ACCESSOR
    public MidiPane getMidiPane() {
        return (MidiPane) parent;
    }

    public SysexPane getSysexPane() {
        return (SysexPane) parent;
    }
}

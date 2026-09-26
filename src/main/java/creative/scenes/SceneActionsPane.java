package creative.scenes;

import creative.scenes.midi.MidiPane;
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
    private final BooleanProperty[] disabledProperties = new SimpleBooleanProperty[6];

    public SceneActionsPane() {
        super();

        setId("SceneActionsPane");

        for (int i = 0; i < 6; i++) {
            disabledProperties[i] = new SimpleBooleanProperty(false);
        }
    }

    private MidiPane parent;

    public SceneActionsPane(MidiPane owner, String actionName, BiConsumer<AEntity, Pane>[] consumers, Supplier<AEntity> supplier) {
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

        getChildren().add(createButton(this, "newButton", "New", 80, consumers[0], disabledProperties[0], supplier));
        getChildren().add(createButton(this, "addButton", "Add", 80, consumers[1], disabledProperties[1], supplier));
        getChildren().add(createButton(this, "deleteButton", "Delete", 80, consumers[2], disabledProperties[2], supplier));
        getChildren().add(createButton(this, "updateButton", "Update", 80, consumers[3], disabledProperties[3], supplier));
        Button actionButton = createButton(this, "actionButton", actionName, 80, consumers[4], disabledProperties[4], supplier);
        getChildren().add(actionButton);
        getChildren().add(createButton(this, "exportButton", "Export", 80, consumers[5], disabledProperties[5], supplier));

        logger.debug("[CM_SCENE_ACTIONS_PANE] Built {}", getId());
    }

    // ACCESSOR
    public MidiPane getMidiPane() {
        return parent;
    }
}

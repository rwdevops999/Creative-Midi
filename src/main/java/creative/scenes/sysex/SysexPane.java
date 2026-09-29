package creative.scenes.sysex;

import communication.CommunicationModel;
import creative.scenes.SceneActionsPane;
import creative.scenes.sysex.consumer.*;
import entity.AEntity;
import entity.sysex.Sysex;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

public class SysexPane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(SysexPane.class);

    public SysexPane() {
        super();

        setId("SysexPane");

        buildPane();
    }

    public void buildPane() {
        logger.debug("[CM_SYSEX_PANE] Building {}", getId());

        CommunicationModel.setStatus("Handling SYSEX");

        setLeft(new SysexListPane(this));
        setCenter(new SysexDetailsPane(this));

        BiConsumer<AEntity, Pane>[] consumers = new BiConsumer[]{
                new NewConsumer<Sysex, Pane>(),
                new AddConsumer<Sysex, Pane>(),
                new DeleteConsumer<Sysex, Pane>(),
                new UpdateConsumer<Sysex, Pane>(),
                new SendConsumer<Sysex, Pane>(),
                new ExportConsumer<Sysex, Pane>()
        };

        SceneActionsPane sceneActionsPane = new SceneActionsPane(this, "Send", consumers, this::getEntity);
        sceneActionsPane.setEnables(SceneActionsPane.BUTTON[0]);
        setRight(sceneActionsPane);

        logger.debug("[CM_SYSEX_PANE] Built {}", getId());
    }

    public AEntity getEntity() {
        SysexListPane listPane = (SysexListPane)getLeft();
//        return listPane.getSelectedItem();
        return null;
    };

    // ACCESSORS
    public SysexListPane getSysexListPane() {
        return (SysexListPane) getLeft();
    }

    public SysexDetailsPane getSysexDetailsPane() {
        return (SysexDetailsPane) getCenter();
    }

    public SceneActionsPane getSceneActionsPane() {
        return (SceneActionsPane) getRight();
    }
}

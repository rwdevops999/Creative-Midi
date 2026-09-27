package creative.scenes.midi.consumer;

import creative.scenes.SceneActionsPane;
import creative.scenes.midi.MidiDetailPane;
import creative.scenes.midi.MidiPane;
import creative.scenes.midi.provider.MidiProvider;
import entity.AEntity;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

import java.util.function.BiConsumer;

public class SaveConsumer<T extends AEntity, P extends Pane> implements BiConsumer<T, P> {
    private static final Logger logger = LoggerFactory.getLogger(SaveConsumer.class);

    @Override
    public void accept(T entity, P pane) {
        logger.debug("[CM_SAVE_CONSUMER] handling Save Midi");

        MidiProvider midiProvider = ApplicationInfo.getInstance().getMidiProvider();
        midiProvider.exportMidis();

        SceneActionsPane sceneActionsPane = (SceneActionsPane)pane;
        ApplicationInfo.getInstance().setGlobalDirty(false);
        sceneActionsPane.setEnable(SceneActionsPane.BUTTON_EXPORT, false);
    }
}

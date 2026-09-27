package creative.scenes.midi.consumer;

import creative.scenes.SceneActionsPane;
import creative.scenes.midi.MidiDetailPane;
import creative.scenes.midi.MidiPane;
import creative.scenes.midi.provider.MidiProvider;
import entity.AEntity;
import entity.midi.Midi;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

import java.util.function.BiConsumer;

public class DeleteConsumer<T extends AEntity, P extends Pane> implements BiConsumer<T, P> {
    private static final Logger logger = LoggerFactory.getLogger(DeleteConsumer.class);

    @Override
    public void accept(T entity, P pane) {
        logger.debug("[CM_DELETE_CONSUMER] handling Delete Midi");

        Midi midi = (Midi)entity;
        SceneActionsPane sceneActionsPane = (SceneActionsPane)pane;

        MidiProvider midiProvider = ApplicationInfo.getInstance().getMidiProvider();

        if (midiProvider != null) {
            midiProvider.deleteMidi(midi);
            sceneActionsPane.getMidiPane().getMidiListPane().updateMidiList();

            ApplicationInfo.getInstance().setCurrentMidi(null);
            sceneActionsPane.getMidiPane().setCenter(new MidiDetailPane(sceneActionsPane.getMidiPane()));

            int enables =
                    SceneActionsPane.BUTTON[SceneActionsPane.BUTTON_NEW] |
                    SceneActionsPane.BUTTON[SceneActionsPane.BUTTON_EXPORT];
            sceneActionsPane.setEnables(enables);
        }

        System.out.println("END");
    }
}

package creative.scenes.midi.consumer;

import creative.scenes.SceneActionsPane;
import creative.scenes.midi.MidiPane;
import creative.scenes.midi.provider.MidiProvider;
import entity.AEntity;
import entity.midi.Midi;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

import java.util.function.BiConsumer;

public class AddConsumer<T extends AEntity, P extends Pane> implements BiConsumer<T, P> {
    private static final Logger logger = LoggerFactory.getLogger(AddConsumer.class);

    @Override
    public void accept(T entity, P pane) {
        logger.debug("[CM_ADD_CONSUMER] handling Add Midi");

        SceneActionsPane sceneActionsPane = (SceneActionsPane) pane;
        MidiPane midiPane = sceneActionsPane.getMidiPane();

        Midi midi = (Midi)entity;

        MidiProvider midiProvider = ApplicationInfo.getInstance().getMidiProvider();
        midiProvider.addMidi(midi);

        ApplicationInfo.getInstance().setGlobalDirty(true);

        midiPane.getMidiListPane().updateMidiList(midi);
    }
}

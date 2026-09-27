package creative.scenes.midi.consumer;

import creative.scenes.SceneActionsPane;
import creative.scenes.midi.MidiDetailPane;
import creative.scenes.midi.MidiListPane;
import creative.scenes.midi.MidiPane;
import creative.scenes.midi.data.ByteType;
import creative.scenes.midi.provider.MidiProvider;
import entity.AEntity;
import entity.midi.Midi;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

import java.io.ByteArrayOutputStream;
import java.util.function.BiConsumer;

public class NewConsumer<T extends AEntity, P extends Pane> implements BiConsumer<T, P> {
    private static final Logger logger = LoggerFactory.getLogger(NewConsumer.class);

    @Override
    public void accept(T entity, P pane) {
        logger.debug("[CM_NEW_CONSUMER] handling New Midi");
        SceneActionsPane sceneActionsPane = (SceneActionsPane) pane;
        MidiPane midiPane = sceneActionsPane.getMidiPane();

        Midi midi = new Midi();

        ApplicationInfo.getInstance().setCurrentMidi(midi);

        MidiDetailPane midiDetailPane = new MidiDetailPane(midiPane);
        midiPane.setCenter(midiDetailPane);

        MidiProvider midiProvider = ApplicationInfo.getInstance().getMidiProvider();

        sceneActionsPane.setEnable(SceneActionsPane.BUTTON_ADD, true);
        sceneActionsPane.setEnable(SceneActionsPane.BUTTON_ACTION, true);
        sceneActionsPane.setEnable(SceneActionsPane.BUTTON_UPDATE, midiProvider.constainsMidi(midi.getName()));
    }
}

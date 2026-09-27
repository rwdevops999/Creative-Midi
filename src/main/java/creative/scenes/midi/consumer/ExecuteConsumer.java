package creative.scenes.midi.consumer;

import creative.scenes.midi.util.MidiWriter;
import entity.AEntity;
import entity.midi.Midi;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.function.BiConsumer;

public class ExecuteConsumer<T extends AEntity, P extends Pane> implements BiConsumer<T, P> {
    private static final Logger logger = LoggerFactory.getLogger(ExecuteConsumer.class);

    @Override
    public void accept(T entity, P pane) {
        logger.debug("[CM_EXCECUTE_CONSUMER] handling Execute Midi");
        Midi midiToSend = (Midi)entity;

        MidiWriter midiWriter = new MidiWriter();
        midiWriter.sendMidi(midiToSend);
    }
}

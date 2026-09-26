package creative.scenes.midi;

import communication.CommunicationModel;
import creative.scenes.SceneActionsPane;
import creative.scenes.midi.consumer.*;
import creative.scenes.midi.provider.MidiProvider;
import entity.AEntity;
import entity.midi.Midi;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

import java.util.function.BiConsumer;

public class MidiPane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(MidiPane.class);

    public MidiPane() {
        super();

        setId("MidiMainPane");

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_MIDI_PANE] Building {}", getId());

        MidiProvider midiProvider = new MidiProvider("midievents.json");
        ApplicationInfo.getInstance().setMidiProvider(midiProvider);

        CommunicationModel.setStatus("Setup MIDI");

        setLeft(new MidiListPane(this));
        setCenter(new MidiDetailPane(this));

        BiConsumer<AEntity, Pane>[] consumers = new BiConsumer[]{
                new NewConsumer<Midi, Pane>(),
                new AddConsumer<Midi, Pane>(),
                new DeleteConsumer<Midi, Pane>(),
                new UpdateConsumer<Midi, Pane>(),
                new ExecuteConsumer<Midi, Pane>(),
                new SaveConsumer<Midi, Pane>()
        };

        CommunicationModel.setStatus("Handling MIDI");

        SceneActionsPane actionsPane = new SceneActionsPane(this, "Send", consumers, this::getEntity);
        setRight(actionsPane);

        actionsPane.setEnables(SceneActionsPane.BUTTON[0]);
        logger.debug("[CM_MIDI_PANE] Built {}", getId());
    }

    public AEntity getEntity() {
        return null;
//        return DirectSingleton.getInstance().getSelectedMidi();
    }

    // ACCESSORS
    public MidiListPane getMidiListPane() {
        return (MidiListPane)getLeft();
    }

    public MidiDetailPane getMidiDetailPane() {
        return (MidiDetailPane) getCenter();
    }

    public SceneActionsPane getActionsPane() {
        return (SceneActionsPane) getRight();
    }
}

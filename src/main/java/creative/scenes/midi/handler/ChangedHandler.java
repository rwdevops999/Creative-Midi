package creative.scenes.midi.handler;

import communication.CommunicationModel;
import creative.scenes.SceneActionsPane;
import creative.scenes.midi.MidiDetailPane;
import entity.AEntity;
import entity.midi.Midi;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import util.ApplicationInfo;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ChangedHandler<T extends AEntity, U extends Pane> implements BiConsumer<T,U> {
    @Override
    public void accept(T t, U u) {
        Midi midi = (Midi)t;
        MidiDetailPane midiDetailPane = (MidiDetailPane)u;
        SceneActionsPane sceneActionsPane = midiDetailPane.getMidiPane().getActionsPane();

        CommunicationModel.setStatus(midi.toString());

        Midi originalMidi = ApplicationInfo.getInstance().getCurrentMidi();
        boolean isDirty = ! originalMidi.equals(midi);

        if (isDirty) {
            sceneActionsPane.setEnable(SceneActionsPane.BUTTON_ACTION, true);
            ApplicationInfo.getInstance().setDirtyMidi(midi);
            if (ApplicationInfo.getInstance().getMidiProvider().constainsMidi(midi.getName())) {
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_ADD, false);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_UPDATE, true);
            } else {
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_ADD, true);
                sceneActionsPane.setEnable(SceneActionsPane.BUTTON_UPDATE, false);
            }
        } else  {
            ApplicationInfo.getInstance().setDirtyMidi(null);
        }
    }
}

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

        CommunicationModel.setStatus(midi.toString());

        Midi originalMidi = ApplicationInfo.getInstance().getCurrentMidi();
        boolean isDirty = ! originalMidi.equals(midi);

        int enables =
                SceneActionsPane.BUTTON[SceneActionsPane.BUTTON_NEW] |
                SceneActionsPane.BUTTON[SceneActionsPane.BUTTON_ACTION] |
                SceneActionsPane.BUTTON[SceneActionsPane.BUTTON_EXPORT];
        if (isDirty) {
            System.out.println("DIRTY");
            ApplicationInfo.getInstance().setDirtyMidi(midi);
            if (midi.getName().equals(originalMidi.getName())) {
                enables |=
                        SceneActionsPane.BUTTON[SceneActionsPane.BUTTON_UPDATE];
            } else {
                enables |=
                        SceneActionsPane.BUTTON[SceneActionsPane.BUTTON_ADD];
            }
        } else  {
            System.out.println("NOT DIRTY");
            ApplicationInfo.getInstance().setDirtyMidi(null);
        }

        SceneActionsPane sceneActionsPane = midiDetailPane.getMidiPane().getActionsPane();
        sceneActionsPane.setEnables(enables);
    }
}

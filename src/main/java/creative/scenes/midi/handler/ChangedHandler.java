package creative.scenes.midi.handler;

import communication.CommunicationModel;
import entity.AEntity;
import entity.midi.Midi;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import util.ApplicationInfo;

import java.util.function.Consumer;

public class ChangedHandler<T extends AEntity> implements Consumer<T> {
    @Override
    public void accept(T t) {
        Midi midi = (Midi)t;

        CommunicationModel.setStatus(midi.toString());

        Midi originalMidi = ApplicationInfo.getInstance().getCurrentMidi();
        boolean isDirty = ! originalMidi.equals(midi);

        if (isDirty) {
            System.out.println("CHANGIE");
        }
    }
}

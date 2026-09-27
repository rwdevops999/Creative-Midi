package creative.scenes.midi.handler;

import communication.CommunicationModel;
import entity.midi.Midi;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import util.ApplicationInfo;

public class ChangedHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent actionEvent) {
        Midi currentMidi = ApplicationInfo.getInstance().getCurrentMidi();

        CommunicationModel.setStatus(currentMidi.toString());

        System.out.println("CHANGIE");
    }
}

package creative.scenes.midi.handler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ChangedHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent actionEvent) {
        System.out.println("CHANGIE");
    }
}

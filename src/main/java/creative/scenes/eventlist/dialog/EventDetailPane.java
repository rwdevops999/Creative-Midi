package creative.scenes.eventlist.dialog;

import creative.scenes.eventlist.parser.entity.MidiEventInfo;
import entity.voice.Patch;
import javafx.scene.layout.GridPane;

public class EventDetailPane  extends GridPane {
    public EventDetailPane() {
        super();
    }

    public EventDetailPane(MidiEventInfo eventInfo) {
        this();

        buildPane(eventInfo);
    }

    private void buildPane(MidiEventInfo eventInfo) {
        // TODO
        System.out.println("Building the pane");
    }
}

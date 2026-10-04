package creative.scenes.eventlist.dialog;

import creative.scenes.eventlist.parser.entity.MidiEventInfo;
import creative.scenes.voice.dialog.VoiceDetailPane;
import entity.voice.Patch;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventDetailDialog extends Dialog<Void> {
    private static final Logger logger = LoggerFactory.getLogger(EventDetailDialog.class);

    public EventDetailDialog() {
        super();

        initStyle(StageStyle.UNDECORATED);
    }

    public EventDetailDialog(MidiEventInfo eventInfo) {
        this();

        getDialogPane().setMinWidth(400);
        getDialogPane().setPrefWidth(400);
        getDialogPane().setMaxWidth(400);

        getDialogPane().setPadding(new Insets(5));

        buildDialog(eventInfo);
    }

    public void buildDialog(MidiEventInfo eventInfo) {
        logger.debug("[CM_DIALOG_EVENT_DETAIL] Building event detail dialog");

        Label headerLabel = new Label(eventInfo.getDescription());
        headerLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        getDialogPane().setHeader(headerLabel);

        EventDetailPane eventDetailPane = new EventDetailPane(eventInfo);

        getDialogPane().setContent(eventDetailPane);
        getDialogPane().getButtonTypes().add(ButtonType.OK);

        logger.debug("[CM_DIALOG_VOICE_DETAIL] Built voice detail dialog");
    }
}

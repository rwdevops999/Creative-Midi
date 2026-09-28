package creative.scenes.voice.dialog;

import entity.voice.Patch;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoiceDetailDialog extends Dialog<Void> {
    private static final Logger logger = LoggerFactory.getLogger(VoiceDetailDialog.class);

    public VoiceDetailDialog() {
        super();

        initStyle(StageStyle.UNDECORATED);
    }

    public VoiceDetailDialog(Patch patch) {
        this();

        getDialogPane().setMinWidth(400);
        getDialogPane().setPrefWidth(400);
        getDialogPane().setMaxWidth(400);

        getDialogPane().setPadding(new Insets(5));

        buildDialog(patch);
    }

    public void buildDialog(Patch patch) {
        logger.debug("[CM_DIALOG_VOICE_DETAIL] Building voice detail dialog");

        Label headerLabel = new Label(patch.getPatch());
        headerLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        getDialogPane().setHeader(headerLabel);

        VoiceDetailPane voiceDetailPane = new VoiceDetailPane(patch);

        getDialogPane().setContent(voiceDetailPane);
        getDialogPane().getButtonTypes().add(ButtonType.OK);

        logger.debug("[CM_DIALOG_VOICE_DETAIL] Built voice detail dialog");
    }
}

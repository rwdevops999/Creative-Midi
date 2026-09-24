package creative;


import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreativeApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(CreativeApp.class);

    public static void startup(String[] args) {
        logger.debug("[CM_CREATIVE_APP] ENTRY: Launch JavaFX Application");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.debug("[CM_CREATIVE_APP] Starting CREATIVE MIDI");

        primaryStage.setTitle("Creative Midi");
        primaryStage.initStyle(StageStyle.UNDECORATED);
    }
}

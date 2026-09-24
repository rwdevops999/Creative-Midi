package creative;


import creative.panes.RootPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Initializer;

import java.util.Objects;

public class CreativeApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(CreativeApp.class);

    private double xOffset = 0;
    private double yOffset = 0;

    public static void startup(String[] args) {
        logger.debug("[CM_CREATIVE_APP] ENTRY: Launching Creative Midi Application");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.debug("[CM_CREATIVE_APP] Starting CREATIVE MIDI");

        primaryStage.setTitle("Creative Midi");
        primaryStage.initStyle(StageStyle.UNDECORATED);

        Initializer init = new Initializer();
        init.initApp();

        RootPane rootPane = new RootPane();

        Scene rootScene = new Scene(rootPane);
        String css = Objects.requireNonNull(CreativeApp.class.getResource("/styles/style.css")).toExternalForm();
        rootScene.getStylesheets().add(css);

        primaryStage.setScene(rootScene);
        primaryStage.show();

        handleMouseEvents(primaryStage, rootPane);
    }

    private void handleMouseEvents(Stage primaryStage, RootPane rootPane) {
        rootPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        rootPane.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
    }
}

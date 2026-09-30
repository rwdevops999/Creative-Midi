package util;

import entity.AEntity;
import entity.midi.NoteEntity;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static util.Constants.*;

public class Util {
    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    public static void setButtonIcon (ButtonBase button, String iconname) {
        Image icon = new Image(Objects.requireNonNull(Util.class.getClassLoader().getResourceAsStream(iconname)));
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);

        button.setGraphic(imageView);
    }

    public static void setPaneBackground(Pane pane) {
        Background background = new Background(new BackgroundFill(ColorScheme.getColor(), null, null));
        pane.setBackground(background);
    }

    public static void setControlBackground(Control control) {
        Background background = new Background(new BackgroundFill(ColorScheme.getColor(), null, null));
        control.setBackground(background);
    }

    public static void setPaneWidthAsPercentage(Pane originator, Pane parent, int size) {
        originator.prefWidthProperty().bind(parent.widthProperty().multiply(size / 100.0));
    }

    public static void resizePaneWidth(Pane originator, Pane parent, int size) {
        originator.prefWidthProperty().bind(parent.widthProperty().multiply(size / 100.0));
        originator.minWidthProperty().bind(parent.widthProperty().multiply(size / 100.0));
        originator.maxWidthProperty().bind(parent.widthProperty().multiply(size / 100.0));
    }

    public static void setPaneHeightAsPercentage(Pane originator, Pane parent, int size) {
        originator.prefHeightProperty().bind(parent.heightProperty().multiply(size / 100.0));
    }

    public static List<NoteEntity> calcNotes (int baseOctave) {
        List<NoteEntity> result = new ArrayList<>();
        for (int i = 0; i < 128; i++) {
            int noteIndex = i % 12;
            int octave = -2 + baseOctave + (i / 12);
            result.add(new NoteEntity(i, String.format("%s%d", BASE_NOTES[noteIndex], octave)));
        }

        return result;
    }

    public static NoteEntity calcNote (int notevalue, int baseOctave) {
        int noteIndex = notevalue % 12;
        int octave = -2 + baseOctave + (notevalue / 12);
        return new NoteEntity(notevalue, String.format("%s%d", BASE_NOTES[noteIndex], octave));
    }

    public static Button createButton (Pane owner, String id, String caption, int percentage, BiConsumer<AEntity, Pane> consumer, BooleanProperty disableProperty, Supplier<AEntity> supplier) {
        float perc = percentage / 100.0f;
        Button button = new Button(caption);
        button.prefWidthProperty().bind(owner.widthProperty().multiply(perc));
        button.setId(id);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                consumer.accept(supplier.get(), owner);
            }
        });
        button.disableProperty().bind(disableProperty);

        return button;
    }

    public static List<String> loadVoiceFilenames(String directoryName) {
        logger.debug("[CM_UTIL] Loading voice files from {}", directoryName);
        Path dirPath = Paths.get(directoryName); // Use your actual path

        try {
            List<String> fileNames = Files.list(dirPath)
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(naam -> {
                        String extension = FilenameUtils.getExtension(naam);
                        String provider = "";

                        if (extension.toLowerCase().equals("ins")) {
                            provider = "Cakewalk";
                        } else {
                            provider = "Yamaha";
                        }

                        return FilenameUtils.removeExtension(naam) + " (" + provider + ")";
                    })
                    .toList();

            return fileNames;
        } catch (IOException ioe) {
            logger.error("[CM_INIT] EXCEPTION: Getting voice files CAUSE: {}", ioe.getMessage());
        }

        return new ArrayList<>();
    }

}

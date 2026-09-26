package util;

import entity.midi.NoteEntity;
import javafx.scene.control.ButtonBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static util.constants.*;

public class Util {
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

    public static void setPaneHeightAsPercentage(Pane originator, Pane parent, int size) {
        originator.prefHeightProperty().bind(parent.heightProperty().multiply(size / 100.0));
    }

    public static List<NoteEntity> calcNotes (int baseOctave) {
        List<NoteEntity> result = new ArrayList<>();
        for (int i = 0; i < 128; i++) {
            int noteIndex = i % 12;
            int octave = -2 + baseOctave + (i / 12);
            NoteEntity entity = new NoteEntity(i, String.format("%s%d", BASE_NOTES[noteIndex], octave));
            result.add(entity);
        }

        return result;
    }
}

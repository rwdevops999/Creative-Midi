package util;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;

public class Util {
    public static void setPaneBackground(Pane pane) {
        Background background = new Background(new BackgroundFill(ColorScheme.getColor(), null, null));
        pane.setBackground(background);
    }
}

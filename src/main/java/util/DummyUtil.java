package util;

import javafx.scene.control.Control;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

// TODO Remove me later
public class DummyUtil {
    private static final int BORDER_SIZE=3;
    public static void showPaneBorder (Pane pane, Color color) {
        pane.setBorder(new Border(new BorderStroke(
                color,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(BORDER_SIZE)
        )));
    }

    public static void showPaneBorder (Control control, Color color) {
        control.setBorder(new Border(new BorderStroke(
                color,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(BORDER_SIZE)
        )));
    }
}

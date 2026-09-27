package custom.components;

import javafx.scene.control.ListCell;
import javafx.scene.shape.Rectangle;

public class ColoredListCell extends ListCell<ColoredItem> {
    @Override
    protected void updateItem(ColoredItem item, boolean empty) {
        Rectangle rect = new Rectangle(20, 20);

        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            rect.setFill(item.getColor().getColor());
            setText(item.getText());
            setGraphic(rect);
        }
    }
}

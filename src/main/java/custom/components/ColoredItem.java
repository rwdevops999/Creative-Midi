package custom.components;

import custom.data.AppColor;
import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public class ColoredItem {
    private String text;
    private AppColor color;

    public ColoredItem() {
    }

    public ColoredItem(String text, Color color) {
        this();

        this.text = text;
        this.color = new AppColor(color);
    }

    public String getColoredText() {
        String result = "[";

        if (color.equals(Color.RED)) {
            result += "ERROR";
        }

        if (color.equals(Color.GREEN)) {
            result += "INCOMING";
        }

        if (color.equals(Color.BLUE)) {
            result += "OUTGOING";
        }

        if (color.equals(Color.ORANGE)) {
            result += "WARNING";
        }

        return result + "] " + text;
    }
}
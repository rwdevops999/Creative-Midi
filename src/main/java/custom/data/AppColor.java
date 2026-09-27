package custom.data;

import javafx.beans.NamedArg;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

@Getter
@Setter
public class AppColor {
    private Color color;
    private Function<String, String> renderer = null;

    private final static Function<String, String> errorRenderer = new Function<String, String>() {
        @Override
        public String apply(String s) {
            return "[X] " + s;
        }
    };

    private final static Function<String, String> warningRenderer = new Function<String, String>() {
        @Override
        public String apply(String s) {
            return "[!] " + s;
        }
    };

    private final static Function<String, String> incomingRenderer = new Function<String, String>() {
        @Override
        public String apply(String s) {
            return "[<] " + s;
        }
    };

    private final static Function<String, String> outgoingRenderer = new Function<String, String>() {
        @Override
        public String apply(String s) {
            return "[>] " + s;
        }
    };

    public AppColor(@NamedArg("color") Color color) {
        this.color = color;
        if (this.color == Color.RED) {
            this.renderer = errorRenderer;
        } else if (this.color == Color.GREEN) {
            this.renderer = incomingRenderer;
        } else if (this.color == Color.BLUE) {
            this.renderer = outgoingRenderer;
        } else if (this.color == Color.ORANGE) {
            this.renderer = warningRenderer;
        }
    }
}

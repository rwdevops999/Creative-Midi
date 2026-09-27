package custom.components;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class SimpleHexTextField extends TextField {
    public SimpleHexTextField() {
        super();
        this.setTextFormatter(createHexFormatter());
    }

    private TextFormatter<String> createHexFormatter() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) {
                return change;
            }

            String regex = "^[0-9a-fA-F]{1,2}(?: [0-9a-fA-F]{0,2})* ?$";

            if (newText.matches(regex)) {
                change.setText(change.getText().toUpperCase());
                return change;
            }

            return null;
        };

        return new TextFormatter<>(filter);
    }

    public byte[] getByteValues() {
        String text = getText().trim();
        if (text.isEmpty()) {
            return new byte[0];
        }

        String[] hexPairs = text.split(" ");
        byte[] bytes = new byte[hexPairs.length];

        for (int i = 0; i < hexPairs.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hexPairs[i], 16);
        }

        return bytes;
    }
}
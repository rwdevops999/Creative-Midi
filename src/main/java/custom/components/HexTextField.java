package custom.components;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class HexTextField extends TextField {

    public HexTextField() {
        super();
        setupHexFilter();
    }

    private void setupHexFilter() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();

            // Regex: Allow 0 to 2 hexadecimal characters
            if (newText.matches("^[0-9a-fA-F]{0,2}$")) {
                // Force input to uppercase for standard formatting
                change.setText(change.getText().toUpperCase());
                return change;
            }

            return null; // Reject invalid characters or length > 2
        };

        this.setTextFormatter(new TextFormatter<>(filter));
    }

    /**
     * Helper method to quickly get the integer value of the hex input.
     * @return the integer value, or -1 if the field is empty.
     */
    public int getNumericValue() {
        String text = this.getText();
        if (text == null || text.isEmpty()) {
            return -1;
        }
        return Integer.parseInt(text, 16);
    }
}

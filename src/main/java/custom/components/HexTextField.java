package custom.components;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.util.function.UnaryOperator;

public class HexTextField extends TextField {

    private final IntegerProperty hexValue = new SimpleIntegerProperty(this, "hexValue", 0);
    private boolean isUpdating = false; // Prevents infinite loops during synchronization

    public HexTextField() {
        setupHexSync();
    }

    private void setupHexSync() {
        // 1. When the IntegerProperty changes (e.g., set to 123), update the text to HEX ("7B")
        hexValue.addListener((obs, oldVal, newVal) -> {
            if (isUpdating) return;
            isUpdating = true;
            try {
                if (newVal == null) {
                    setText("00");
                } else {
                    setText(String.format("%02X", newVal.intValue() & 0xFF));
                }
            } finally {
                isUpdating = false;
            }
        });

        // 2. Control what the user can type (Only 0-9, A-F, max 2 chars)
        this.textProperty().addListener((obs, oldValue, newValue) -> {
            if (isUpdating) return;

            // If the user cleared the field, treat it as 0
            if (newValue.isEmpty()) {
                isUpdating = true;
                hexValue.set(0);
                isUpdating = false;
                return;
            }

            // Force validation: If it's not a valid 1-2 char Hex string, reject the keystroke
            if (!newValue.matches("^[0-9a-fA-F]{1,2}$")) {
                setText(oldValue);
                return;
            }

            // If it is valid hex, parse it back to the IntegerProperty
            try {
                isUpdating = true;
                hexValue.set(Integer.parseInt(newValue.trim(), 16));
            } catch (NumberFormatException e) {
                setText(oldValue);
            } finally {
                isUpdating = false;
            }
        });

        // 3. Trigger initial formatting for the startup value (e.g., 123 -> "7B")
        setText(String.format("%02X", hexValue.get() & 0xFF));
    }

    public int getHexValue() { return hexValue.get(); }
    public void setHexValue(int value) { this.hexValue.set(value); }
    public IntegerProperty hexValueProperty() { return hexValue; }
}

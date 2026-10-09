package custom.components;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class LedToggleButton extends ToggleButton {

    // Het led-lampje (een cirkel met een straal van 6 pixels)
    private final Circle ledNode;

    // Kleuren voor de aan- en uit-status
    private static final Color LED_ON = Color.YELLOW;
    private static final Color LED_OFF = Color.YELLOW.darker().darker();

    public LedToggleButton(String text) {
        super(text);

        // Initialiseer de LED in de 'uit' stand
        ledNode = new Circle(4, LED_OFF);

        // Voeg een subtiel randje toe voor een realistischer LED-effect
        ledNode.setStroke(Color.GRAY);
        ledNode.setStrokeWidth(1);

        // Koppel de LED aan de knop als grafisch element
        this.setGraphic(ledNode);

        // Voeg een listener toe die reageert op het selecteren/deselecteren
        this.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    // Knop is geselecteerd: LED licht geel op
                    ledNode.setFill(LED_ON);
                } else {
                    // Knop is niet geselecteerd: LED gaat uit (donkergrijs)
                    ledNode.setFill(LED_OFF);
                }
            }
        });
    }
}

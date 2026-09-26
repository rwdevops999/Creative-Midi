package custom.components;

import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import util.ColorScheme;

public class OnOffSwitch extends Pane {
    private final BooleanProperty switchedOn = new SimpleBooleanProperty(false);
    private final TranslateTransition translateAnimation = new TranslateTransition(Duration.millis(150));
    private final FillTransition fillAnimation = new FillTransition(Duration.millis(150));

    public OnOffSwitch() {
        Rectangle background = new Rectangle(50, 25);
        background.setArcWidth(25);
        background.setArcHeight(25);
        background.setFill(ColorScheme.getColor("component", "OnOffSwitch", "fill"));
        background.setStroke(ColorScheme.getColor("component", "OnOffSwitch", "stroke"));

        Circle trigger = new Circle(10);
        trigger.setFill(ColorScheme.getColor("component", "OnOffSwitch", "trigger"));
        trigger.setCenterX(13);
        trigger.setCenterY(12.5);

        getChildren().addAll(background, trigger);

        // set animations
        translateAnimation.setNode(trigger);
        fillAnimation.setShape(background);

        // add mouse click listener
        setOnMouseClicked(event -> switchedOn.set(!switchedOn.get()));

        // add logic
        switchedOn.addListener((obs, oldValue, newValue) -> {
            translateAnimation.stop();
            fillAnimation.stop();

            if (newValue) { // AAN status
                translateAnimation.setToX(24);
                fillAnimation.setToValue(ColorScheme.getColor("component", "OnOffSwitch", "on"));
            } else { // UIT status
                translateAnimation.setToX(0);
                fillAnimation.setToValue(ColorScheme.getColor("component", "OnOffSwitch", "off"));
            }

            translateAnimation.play();
            fillAnimation.play();
        });
    }

    public BooleanProperty switchedOnProperty() {
        return switchedOn;
    }

    public boolean isSwitchedOn() {
        return switchedOn.get();
    }
}
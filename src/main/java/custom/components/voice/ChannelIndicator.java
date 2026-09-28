package custom.components.voice;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import java.util.concurrent.atomic.AtomicInteger;

import static util.ColorScheme.getColor;

public class ChannelIndicator extends VBox {
//    private final Color ledOn = Color.RED;
//    private final Color ledOff = Color.web("#4A0000"); // Donkerrood (uitstand)
    private final Color ledOn = getColor("voice", "led", "on");
    private final Color ledOff = getColor("voice", "led", "off");

    private final Circle redLight;

    private Receiver keyboardReceiver;

    public void setRealReceiver(Receiver receiver) {
        this.keyboardReceiver = receiver;
    }

    public ChannelIndicator() {
        this.redLight = new Circle(5);
        this.redLight.setFill(ledOff);
        this.redLight.setStroke(Color.BLACK);
        this.redLight.setStrokeWidth(1.0);

        setAlignment(Pos.CENTER);

        buildPane();
    }

    private void buildPane() {
        getChildren().add(redLight);
    }

    public void updateLight(boolean isPlaying) {
        Platform.runLater(() -> {
            redLight.setFill(isPlaying ? ledOn : ledOff);
        });
    }

    private final AtomicInteger activeNotesCount = new AtomicInteger(0);

    public Receiver getMidiReceiver(int midiChannel) {
        return new Receiver() {
            @Override
            public void send(MidiMessage message, long timeStamp) {
                if (keyboardReceiver != null) {
                    keyboardReceiver.send(message, timeStamp);
                }

                try {
                    if (message instanceof ShortMessage) {
                        ShortMessage sm = (ShortMessage) message;

                        // Controleer of het kanaal van het bericht matcht met midiChannel (bijv. 0)
                        if (sm.getChannel() == midiChannel) {

                            // Note On (noot ingedrukt) -> Lampje AAN
                            if (sm.getCommand() == ShortMessage.NOTE_ON && sm.getData2() > 0) {
                                activeNotesCount.incrementAndGet();
                                updateLight(true); // Gaat aan bij de eerste noot
                            }
                            else if (sm.getCommand() == ShortMessage.NOTE_OFF ||
                                    (sm.getCommand() == ShortMessage.NOTE_ON && sm.getData2() == 0)) {

                                if (activeNotesCount.get() > 0) {
                                    activeNotesCount.decrementAndGet();
                                }

                                // Pas als er écht geen enkele noot meer klinkt, gaat het lampje uit
                                if (activeNotesCount.get() == 0) {
                                    updateLight(false);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void close() {
                if (keyboardReceiver != null) {
                    keyboardReceiver.close();
                }
            }
        };
    }
}

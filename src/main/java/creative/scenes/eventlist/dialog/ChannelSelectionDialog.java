package creative.scenes.eventlist.dialog;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.function.BiConsumer;

public class ChannelSelectionDialog extends Dialog<Character> {

    // Gebruik Character (wrapper) in plaats van de primitive char
    private Character result = 0;

    private final BiConsumer<Character, Boolean> consumer = (b, set) -> {
        if (set) {
            result = (char) (result | b);   // Bitwise OR om de bit te zetten
        } else {
            result = (char) (result & ~b);  // Bitwise AND met NOT om de bit te wissen
        }
    };

    public ChannelSelectionDialog(Character initialConfig) {
        setTitle("Channel Selection");
        setHeaderText("Select the Channels to be displayed (1-16). Without channel are the system events");

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Layout voor de 16 checkboxes (4x4 grid)
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);

        Character start = 0x1;

        for (int i = 1; i <= 16; i++) {
            CheckBox checkBox = new CheckBox("Channel " + i);
            checkBox.setId("Channel" + i);

            // Leg de huidige bitmasker vast voor deze specifieke checkbox
            final Character currentBitMask = start;

            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                consumer.accept(currentBitMask, newValue);
            });

            // Zet de initiële vinkjes op basis van de input
            checkBox.setSelected((initialConfig & start) != 0);

            // Voeg toe aan grid (kolom, rij) -> creëert een nette 4x4 matrix
            int col = (i - 1) % 4;
            int row = (i - 1) / 4;
            grid.add(checkBox, col, row);

            // Shift naar de volgende bit
            start = (char) (start << 1);

            Button allButton = new Button("All ");
            allButton.setOnAction(event -> {
                for (int i1 = 1; i1 <= 16; i1++) {
                    CheckBox cb = (CheckBox)grid.lookup("#Channel"+i1);
                    cb.setSelected(true);
                }
            });
            grid.add(allButton, 4, 0);

            Button noneButton = new Button("None");
            noneButton.setOnAction(event -> {
                for (int i1 = 1; i1 <= 16; i1++) {
                    CheckBox cb = (CheckBox)grid.lookup("#Channel"+i1);
                    cb.setSelected(false);
                }
            });
            grid.add(noneButton, 4, 3);
        }



        getDialogPane().setContent(grid);

        // Bepaal wat de dialoog teruggeeft als er op OK wordt geklikt
        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return result;
            }
            return null; // Geeft null terug bij Annuleren of sluiten
        });
    }
}
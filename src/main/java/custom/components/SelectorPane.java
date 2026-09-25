package custom.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SelectorPane extends HBox {
    private static final Logger logger = LoggerFactory.getLogger(SelectorPane.class);

    public SelectorPane() {
        super();

        setSpacing(5);
        setAlignment(Pos.CENTER_LEFT);
    }

    public SelectorPane(String id) {
        this();

        setId(id);
    }

    public SelectorPane(String name, String tooltip, List<String> selections, EventHandler<ActionEvent> handler, String selected) {
        this(name);

        buildPane(name, tooltip);
    }

    private void buildPane(String name, String tooltip) {
        logger.debug("[CM_SELECTOR_PANE] Building {}", getId());

        Label selectorLabel = new Label(name);
        selectorLabel.setPrefWidth(70);
        getChildren().add(selectorLabel);

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPrefWidth(150);
        comboBox.setPromptText(tooltip);
        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(comboBox.getPromptText());
                } else {
                    setText(item);
                }
            }
        });
        getChildren().add(comboBox);

        logger.debug("[CM_SELECTOR_PANE] Built {}", getId());
    }
}

package creative.panes.header;

import custom.components.SelectorPane;
import eventhandlers.KeyboardHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DummyUtil;

import java.util.ArrayList;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;

public class SelectorsPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(SelectorsPane.class);

    private static final int PANE_WIDTH = 250;

    public SelectorsPane() {
        super();

        setId("SelectorsPane");
        setPrefSize(PANE_WIDTH, HeaderPane.PANE_HEIGHT);

        setPadding(new Insets(10));
        setSpacing(10);

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_SELECTORS_PANE] Building {}", getId());

        showPaneBorder(this, getColor("test", "blue"));

        SelectorPane keyboardSelectorPane = new SelectorPane("Keyboard", "select a keyboard", new ArrayList<>(), new KeyboardHandler<>(), "");
        SelectorPane deviceSelectorPane = new SelectorPane("Device", "Select a device", new ArrayList<>(), null, "");
//        SelectorPane keyboardSelectorPane = new SelectorPane("Keyboard", "select a keyboard", VoiceContainer.getVoiceFilenames(), new KeyboardHandler<>(), voices);
//        SelectorPane deviceSelectorPane = new SelectorPane("Device", "Select a device", new ArrayList<>(), null, Globals.getSelectedDevice());
        getChildren().addAll(keyboardSelectorPane, deviceSelectorPane);

        logger.debug("[CM_SELECTORS_PANE] Built {}", getId());
    }
}

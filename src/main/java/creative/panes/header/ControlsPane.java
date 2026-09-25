package creative.panes.header;

import custom.components.IconButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;
import util.Theme;
import util.Util;

import static util.Theme.switchTheme;
import static util.Util.setButtonIcon;

public class ControlsPane extends HBox {
    private static final Logger logger = LoggerFactory.getLogger(ControlsPane.class);

    private static final int PANE_WIDTH = 150;

    public ControlsPane() {
        super();

        setId("ControlsPane");

        setPrefSize(PANE_WIDTH, HeaderPane.PANE_HEIGHT);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(10));
        setSpacing(10);

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_CONTROLS_PANE] Building {}", getId());

        // Midi Off Button
        getChildren().add(new IconButton("midioff", "midi off", "icons/midi.png", e -> {
        }));

        // Light/dark Button
        ToggleButton toggleButton = new ToggleButton();
        toggleButton.setSelected(!Theme.isModeLight());
        setButtonIcon(toggleButton, Theme.getThemeIcon());
        toggleButton.setOnAction(e -> {
            Theme.switchMode(!toggleButton.isSelected());
            Util.setButtonIcon(toggleButton, Theme.getThemeIcon());
            switchTheme(ApplicationInfo.getInstance().getRootScene());

        });
        getChildren().add(toggleButton);

        // Exit Button
        getChildren().add(new IconButton("exit", "exit", "icons/exit.png", e -> {
            AnsiConsole.systemUninstall();
            System.exit(0);
        }));

        logger.debug("[CM_CONTROLS_PANE] Built {}", getId());
    }
}

package custom.components;

import communication.CommunicationModel;
import entity.device.DeviceInfo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

import java.util.List;
import java.util.Objects;

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

        buildPane(name, tooltip, selections, selected, handler);
    }

    private ComboBox<String> comboBox;

    private void buildPane(String name, String tooltip, List<String> selections, String selected, EventHandler<ActionEvent> handler) {
        logger.debug("[CM_SELECTOR_PANE] Building {}", getId());

        Label selectorLabel = new Label(name);
        selectorLabel.setPrefWidth(70);
        getChildren().add(selectorLabel);

        comboBox = new ComboBox<>();
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

        comboBox.setOnAction(e -> {
            if (!mute) {
                selectedDevice = comboBox.getValue();
            }
        });

        comboBox.setOnAction(Objects.requireNonNullElseGet(handler, () -> event -> {
            if (!mute) {
                selectedDevice = comboBox.getValue();
            }
        }));

        if (selections != null && !selections.isEmpty()) {
            comboBox.getItems().addAll(selections);
        }

        if (selected != null && !selected.isEmpty()) {
            comboBox.getSelectionModel().select(selected);
            comboBox.fireEvent(new ActionEvent(comboBox, comboBox));
        }

        getChildren().add(comboBox);

        logger.debug("[CM_SELECTOR_PANE] Built {}", getId());
    }

    private boolean mute = false;
    private String selectedDevice = "";

    public void updateComboData(DeviceInfo deviceInfo) {
        List<String> candidates = deviceInfo.getCandidates();

        mute = true;

        // first set the new list in the combo
        if (candidates != null && ! candidates.isEmpty()) {
            comboBox.getItems().clear();
            comboBox.getItems().addAll(candidates);

            // Continue here
            if (candidates.contains(selectedDevice)) {
                comboBox.getSelectionModel().select(selectedDevice);
                deviceInfo.selectDevice(selectedDevice);
                ApplicationInfo.getInstance().setSelectedDevice(selectedDevice);
            } else {
                if (! selectedDevice.isEmpty()) {
                    ApplicationInfo.getInstance().setMidiOutputDevice(null);
                    if (! deviceInfo.deselectDevice(selectedDevice)) {
                        ApplicationInfo.getInstance().setMidiInputDevice(null);
                        ApplicationInfo.getInstance().setMidiOutputDevice(null);
                    }

                    selectedDevice = "";
                    ApplicationInfo.getInstance().setSelectedDevice(selectedDevice);

                    comboBox.getSelectionModel().clearSelection();
                }

                // Current Selected is not more in the new list
                String defaultDeviceName = deviceInfo.getDefaultDeviceName();

                if (candidates.contains(defaultDeviceName)) {
                    comboBox.getSelectionModel().select(defaultDeviceName);

                    CommunicationModel.monitorInfo("Connected to device " + defaultDeviceName);
                    // NOTIFICATION (Connection established)
                    Notifications.create()
                            .title("Connection established")
                            .text("Connected to device '" +  defaultDeviceName + "'")
                            .hideAfter(Duration.seconds(3)) // Automatically hides after 5 seconds
                            .position(Pos.TOP_LEFT)     // Set corner position on screen
                            .showInformation();
                    selectedDevice = defaultDeviceName;
                    ApplicationInfo.getInstance().setSelectedDevice(selectedDevice);
                    deviceInfo.selectDevice(selectedDevice);
                }
            }
        } else {
            if (! selectedDevice.isEmpty()) {
                comboBox.getItems().clear();
                comboBox.getSelectionModel().clearSelection();

                // NOTIFICATION (Connection Lost)
                CommunicationModel.monitorWarning("Connection to device " + selectedDevice + " lost");
                Notifications.create()
                        .title("Connection lost")
                        .text("The connection with Device '" +  selectedDevice + "'is lost")
                        .hideAfter(Duration.seconds(3)) // Automatically hides after 5 seconds
                        .position(Pos.TOP_LEFT)     // Set corner position on screen
                        .showInformation();

                if (! deviceInfo.deselectDevice(selectedDevice)) {
                    ApplicationInfo.getInstance().setMidiInputDevice(null);
                    ApplicationInfo.getInstance().setMidiOutputDevice(null);
                }
                
                selectedDevice = "";
                ApplicationInfo.getInstance().setSelectedDevice(selectedDevice);
            }
        }

        mute = false;
    }
}

package creative.scenes.eventlist;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;

public class FilterPane extends HBox {
    public static int MIDI_EVENT = 0;
    public static int META_EVENT = 1;
    public static int SYSEX_EVENT = 2;
    public static int CHANNEL = 3;

    private ObjectProperty<Integer> selectedChannel = new SimpleObjectProperty<>(0);

    public FilterPane() {
        super();

        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(0, 0, 0, 5));
    }

    private boolean[] selectedEvents;
    private EventsDisplayPane parent;
    public FilterPane(EventsDisplayPane owner, boolean[] selected) {
        this();

        parent = owner;

        selectedEvents = selected;

        showPaneBorder(this, getColor("border", "red"));
        buildPane();

        updateFiltering();
    }

    public void buildPane() {
        // Global
        Label filterLabel = new Label("Filter");
        filterLabel.setStyle("-fx-font-weight: bold");
        getChildren().add(filterLabel);

        // MIDI
        Label midiLabel = new Label("Midi");
        getChildren().add(midiLabel);

        CheckBox midiCheckBox = new CheckBox();
        midiCheckBox.setSelected(selectedEvents[MIDI_EVENT]);
        midiCheckBox.setOnAction(event -> {
            selectedEvents[MIDI_EVENT] = ! selectedEvents[MIDI_EVENT];
            updateFiltering();
        });
        getChildren().add(midiCheckBox);

        Button midiButton = new Button("Details");
        midiButton.setOnAction(event -> {
            // TODO Was never implemented
        });
        getChildren().add(midiButton);

        // META
        Label metaLabel = new Label("Meta");
        getChildren().add(metaLabel);

        CheckBox metaCheckBox = new CheckBox();
        metaCheckBox.setSelected(selectedEvents[META_EVENT]);
        metaCheckBox.setOnAction(event -> {
            selectedEvents[META_EVENT] = ! selectedEvents[META_EVENT];
            updateFiltering();
        });
        getChildren().add(metaCheckBox);

        Button metaButton = new Button("Details");
        metaButton.setOnAction(event -> {
            // TODO Was never implemented
        });
        getChildren().add(metaButton);

        // SYSEX
        Label sysexLabel = new Label("SysEx");
        getChildren().add(sysexLabel);

        CheckBox sysexCheckBox = new CheckBox();
        sysexCheckBox.setSelected(selectedEvents[SYSEX_EVENT]);
        sysexCheckBox.setOnAction(event -> {
            selectedEvents[SYSEX_EVENT] = ! selectedEvents[SYSEX_EVENT];
            updateFiltering();
        });
        getChildren().add(sysexCheckBox);

        Button sysexButton = new Button("Details");
        sysexButton.setOnAction(event -> {
            // TODO Was never implemented
        });
        getChildren().add(sysexButton);

        // CHANNEL
        Label channelLabel = new Label("Channel");
        getChildren().add(channelLabel);

        CheckBox channelCheckBox = new CheckBox();
        channelCheckBox.setSelected(selectedEvents[CHANNEL]);
        channelCheckBox.setOnAction(event -> {
            selectedEvents[CHANNEL] = ! selectedEvents[CHANNEL];
            updateFiltering();
        });

        Spinner<Integer> channelSpinner = new Spinner<>(0, 16, 0);
        channelSpinner.getValueFactory().valueProperty().bindBidirectional(selectedChannel);
        channelSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(this::updateFiltering);
        });
        getChildren().addAll(channelCheckBox, channelSpinner);

        Button channelButton = new Button("Details");
        channelButton.setOnAction(event -> {
            // TODO Was never implemented
        });
        getChildren().add(channelButton);

    }

    private void updateFiltering() {
        getEventDisplayPane().handleFiltering(selectedEvents, selectedChannel.get());
    }

    // ACCESSORS
    public EventsDisplayPane getEventDisplayPane() {
        return parent;
    }
}

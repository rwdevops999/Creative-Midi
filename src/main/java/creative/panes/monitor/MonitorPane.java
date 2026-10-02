package creative.panes.monitor;

import communication.CommunicationModel;
import creative.panes.monitor.data.CommunicationType;
import creative.scenes.midi.data.MessageType;
import creative.scenes.midi.util.MidiReset;
import creative.scenes.midi.util.MidiWriter;
import creative.scenes.playlist.PlaylistContainer;
import creative.scenes.playlist.PlaylistListPane;
import creative.scenes.playlist.PlaylistPane;
import custom.components.ColoredItem;
import custom.components.ColoredListCell;
import entity.midi.Midi;
import entity.playlist.Song;
import eventhandlers.MonitorExportHandler;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Synchronized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;
import util.ColorScheme;
import util.Constants;
import util.Registry;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static util.Constants.MONITOR_ACTION_CLEAR;
import static util.Constants.MONITOR_ACTION_EXPORT;
import static util.Util.setPaneHeightAsPercentage;

public class MonitorPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(MonitorPane.class);

    private MidiWriter midiWriter = new MidiWriter();
    private Midi clockMidi = new Midi();

    public MonitorPane() {
        super();

        setId("MonitorPane");
    }

    public MonitorPane(Pane owner) {
        this();

        clockMidi.setMessageType(MessageType.system.name());
        clockMidi.setStatus("F8");

        buildPane(owner);
    }

    private ListView<ColoredItem> messagesView;
    private final ObservableList<ColoredItem> messages = FXCollections.observableArrayList();

    private void buildPane(Pane owner) {
        logger.debug("[CM_MONITOR_PANE] Building {}", getId());

        setPaneHeightAsPercentage(this, owner, 20);

        messagesView = new ListView<>();
        messagesView.setCellFactory(lv -> new ColoredListCell());
        messagesView.setItems(messages);
        getChildren().add(messagesView);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem clearMonitor = new MenuItem("Clear");
        clearMonitor.setOnAction(e -> messagesView.getItems().clear());
        MenuItem exportMonitor = new MenuItem("Export");
        exportMonitor.setOnAction(e -> {
            MonitorExportHandler handler = new MonitorExportHandler();
            handler.handle(List.copyOf(messages));
        });
        contextMenu.getItems().addAll(clearMonitor, exportMonitor);
        messagesView.setContextMenu(contextMenu);

        ObjectProperty<CommunicationType> type = new SimpleObjectProperty<>();
        type.bindBidirectional(CommunicationModel.getTypeProperty());

        Label message = new Label();
        message.textProperty().bindBidirectional(CommunicationModel.getMessageProperty());
        message.setVisible(false);
        message.textProperty().addListener((observable, oldValue, newValue) -> handleMonitoringMessage(type.get(), newValue));
        getChildren().add(message);

        VBox.setVgrow(messagesView, Priority.ALWAYS);

        logger.debug("[CM_MONITOR_PANE] Built {}", getId());
    }

    private void handleMonitoringMessage(CommunicationType cType, String text) {

        logger.debug("[CM_MONITOR_PANE] handleMonitoringMessage {}, {}", cType.name(), text);
        switch(cType) {
            case OUTGOING:
                messages.add(new ColoredItem(text, ColorScheme.getColor("monitor", "message", "outbound")));
                break;
            case INCOMING:
                messages.add(new ColoredItem(text, ColorScheme.getColor("monitor", "message", "inbound")));
                AtomicReference<String> arText = new AtomicReference<>(text);
                Platform.runLater(() -> {
                    if ("START".equals(arText.get())) {
                        executeAction(Constants.MONITOR_ACTION_CLEAR);
                        messages.add(new ColoredItem("Song: ... STARTED", ColorScheme.getColor("Monitor", "Message", "Incoming")));
                    } else if ("STOP".equals(arText.get())) {
                        messages.add(new ColoredItem("Song: ... STOPPED", ColorScheme.getColor("Monitor", "Message", "Incoming")));
                    }
                });
                break;
            case INFO:
                messages.add(new ColoredItem(text, ColorScheme.getColor("monitor", "message", "info")));
                break;
            case ERROR:
                messages.add(new ColoredItem(text, ColorScheme.getColor("monitor", "message", "error")));
                break;
            case WARNING:
                messages.add(new ColoredItem(text, ColorScheme.getColor("monitor", "message", "warning")));
                break;
            case ACTION:
                executeAction(text);
                break;
            case SONG_SELECT:
//                new MidiReset().reset();
                stopClockStream();

                Registry.publish("SelectSong", text);
                executeAction(Constants.MONITOR_ACTION_CLEAR);

                Song song = PlaylistContainer.getSong(text);
                if (song != null) {
                    sendMidiStopByte();

                    startClockStream(song.getSongInfo().getTempo());
                    messages.add(new ColoredItem("Song set at BPM: " + song.getSongInfo().getTempo(), ColorScheme.getColor("Monitor", "Message", "Song")));
                }

                messagesView.scrollTo(messages.size() - 1);
                break;
        }

        Platform.runLater(() -> messagesView.scrollTo(messages.size()));
    }

    private void executeAction(String action) {
        if (action.equals(MONITOR_ACTION_CLEAR)) {
            messages.clear();
        } else if (action.equals(MONITOR_ACTION_EXPORT)) {
            MonitorExportHandler handler = new MonitorExportHandler();
            handler.handle(List.copyOf(messages));
        }
    }

    private ScheduledFuture<?> clockTaskHandle;
    private ScheduledExecutorService clockExecutor;

    @Synchronized
    private void startClockStream(int bpm) {
        logger.error("[CM_MONITOR_PANE] START CLOCK: {}", bpm);
        // Safety check: Explicitly clear any existing task handle before setting a new one
        if (clockTaskHandle != null) {
            clockTaskHandle.cancel(true);
        }

        if (clockExecutor == null || clockExecutor.isShutdown()) {
            clockExecutor = Executors.newSingleThreadScheduledExecutor();
        }

        double doubleInterval = 60000000.0 / (bpm * 24.0);
        long intervalMicroseconds = Math.round(doubleInterval);

        // 2. Capture the assignment directly to your handle variable
        clockTaskHandle = clockExecutor.scheduleAtFixedRate(this::sendMidiClock, 0, intervalMicroseconds, TimeUnit.MICROSECONDS);
    }

    private synchronized void stopClockStream() {
        logger.error("[CM_MONITOR_PANE] STOP CLOCK");
        if (clockExecutor != null) {
            // 1. Tell the executor to stop accepting new tasks
            clockExecutor.shutdown();

            // 2. Force-kill any tasks currently executing right now
            clockExecutor.shutdownNow();

            try {
                // 3. CRITICAL: Block execution until the old thread is 100% dead.
                // This prevents the "double clock" overlap issue.
                if (!clockExecutor.awaitTermination(200, TimeUnit.MILLISECONDS)) {
                    logger.debug("[CM_MONITOR_PANE] EXCEPTION. Old clock thread was stubborn, forcing cleanup.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // 4. Clear the reference so Java Garbage Collection deletes it from memory
            clockExecutor = null;
        }
    }

    private void sendMidiStopByte() {
        Midi midi = new Midi();
        midi.setMessageType(MessageType.system.name());
        midi.setStatus("FC");
        midiWriter.sendMidiAsString(midi.toString());
    }

    private void sendMidiClock() {
        midiWriter.sendMidiAsString(clockMidi.toString());
    }
}

package creative.panes.monitor;

import communication.CommunicationModel;
import creative.panes.monitor.data.CommunicationType;
import custom.components.ColoredItem;
import custom.components.ColoredListCell;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ColorScheme;
import util.Constants;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static util.Constants.MONITOR_ACTION_CLEAR;
import static util.Constants.MONITOR_ACTION_EXPORT;
import static util.Util.setPaneHeightAsPercentage;

public class MonitorPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(MonitorPane.class);

    public MonitorPane() {
        super();

        setId("MonitorPane");
    }

    public MonitorPane(Pane owner) {
        this();

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
/*                AtomicReference<String> arText = new AtomicReference<>(text);
                Platform.runLater(() -> {
                    if ("START".equals(arText.get()) && DirectSingleton.getInstance().getSelectedSong() != null) {
                        executeAction(Constants.MONITOR_ACTION_CLEAR);
                        messages.add(new ColoredItem("Selected song: " + DirectSingleton.getInstance().getSelectedSong() + " ... STARTED", ColorScheme.getColor("Monitor", "Message", "Incoming")));
                    } else if ("STOP".equals(arText.get()) && DirectSingleton.getInstance().getSelectedSong() != null) {
                        messages.add(new ColoredItem("Selected song: " + DirectSingleton.getInstance().getSelectedSong() + " ... FINISHED", ColorScheme.getColor("Monitor", "Message", "Incoming")));
                    }
                }); */
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
                System.out.println("MONITOR ACTION");
                break;
            case SONG_SELECT:
                System.out.println("SONG SELECT");
                break;
/*            case ACTION:
                executeAction(text);
                break;
            case SONG_SELECT:
                sendMidiByte(MIDI_STOP);
                stopClockStream();
                DirectSingleton.getInstance().setSelectedSong(text);

                BorderPane rootPane = (BorderPane)Globals.getRootScene().getRoot();

                PlaylistPane playlistPane = (PlaylistPane)rootPane.lookup("#PlaylistFunctionalPane");
                if (playlistPane != null) {
                    PlaylistListPane playlistListPane = playlistPane.getPlaylistListPane();
                    if (playlistListPane != null) {
                        playlistListPane.selectSong(text);
                    }
                }

                Platform.runLater(() -> {
                    executeAction(Constants.MONITOR_ACTION_CLEAR);
                    messages.add(new ColoredItem("Selected song: " + DirectSingleton.getInstance().getSelectedSong(), ColorScheme.getColor("Monitor", "Message", "Song")));
                    //                  messagesView.scrollTo(messages.size() - 1);
                    Song song = PlaylistContainer.getSongByName(text);
                    if (song != null) {
                        sendMidiByte(MIDI_STOP);
                        stopClockStream();

                        initMidi();

                        int bpm = song.getSongInfo().getTempo();

                        startClockStream(bpm);

                        Platform.runLater(() -> {
                            messages.add(new ColoredItem("Selected song: " + DirectSingleton.getInstance().getSelectedSong() + " at BPM: " + bpm, ColorScheme.getColor("Monitor", "Message", "Song")));
                            //                                messagesView.scrollTo(messages.size() - 1);
                        });
                    } else {
                        logger.error("[CM_MONITOR_PANE] SONG NOT FOUND: {}", text);
                    }
                });

                break; */
        }

        Platform.runLater(() -> messagesView.scrollTo(messages.size()));
    }
/*
    private void executeAction(String action) {
        if (action.equals(MONITOR_ACTION_CLEAR)) {
            messages.clear();
        } else if (action.equals(MONITOR_ACTION_EXPORT)) {
            MonitorExportHandler handler = new MonitorExportHandler();
            handler.handle(List.copyOf(messages));
        }
    } */
}

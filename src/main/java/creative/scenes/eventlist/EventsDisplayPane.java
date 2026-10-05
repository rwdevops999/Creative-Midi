package creative.scenes.eventlist;

import creative.scenes.base.Base;
import creative.scenes.base.BaseClosePane;
import creative.scenes.eventlist.data.EventKey;
import creative.scenes.eventlist.data.EventKeyValue;
import creative.scenes.eventlist.dialog.EventDetailDialog;
import creative.scenes.eventlist.parser.entity.MidiEventInfo;
import creative.scenes.voice.dialog.VoiceDetailDialog;
import custom.dialog.DialogFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;

import static creative.scenes.eventlist.data.EventKeyValue.*;


public class EventsDisplayPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(EventsDisplayPane.class);

    public EventsDisplayPane() {
        super();

        setId("EventsDisplayPane");
    }

    private EventsPane parent;
    public EventsDisplayPane(EventsPane owner) {
        this();

        parent = owner;

//        showPaneBorder(this, getColor("border", "red"));
        buildPane();
    }

    private final int[] columnSizes = {10,4,24,13,7,10,32};

    private TableView<MidiEventInfo> table;
    private boolean[] selectedEvents = {true, true, true, false};
    private int selectedChannel = 0;

    private void buildPane() {
        logger.debug("[CM_EVENTS_DISPLAY_PANE] Building {}", getId());

        setStyle("-fx-font-size: 10px;");

        FilterPane filterPane = new FilterPane(this, selectedEvents);
        getChildren().add(filterPane);

        table = new TableView<>();
        table.setFixedCellSize(20.0);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        createEventColumns(table);
        createContextMenu(table);

        getChildren().add(table);

        logger.debug("[CM_EVENTS_DISPLAY_PANE] Built {}", getId());
    }

    private void createEventColumns(TableView<MidiEventInfo> table) {
        TableColumn<MidiEventInfo, String> mbtColumn = new TableColumn<>("MBT");
        setColumnSize(mbtColumn, columnSizes[0]);
        mbtColumn.setCellValueFactory(new PropertyValueFactory<>("mbtPosition"));
        mbtColumn.setCellFactory(column -> createStringTableCell());
        table.getColumns().add(mbtColumn);

        TableColumn<MidiEventInfo, Integer> channelColumn = new TableColumn<>("Ch");
        setColumnSize(channelColumn, columnSizes[1]);
        channelColumn.setCellValueFactory(new PropertyValueFactory<>("channel"));
        channelColumn.setCellFactory(column -> createIntegerTableCell());
        table.getColumns().add(channelColumn);

        TableColumn<MidiEventInfo, String> kindColumn = new TableColumn<>("Description");
        setColumnSize(kindColumn, columnSizes[2]);
        kindColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        kindColumn.setCellFactory(column -> createStringTableCell());
        table.getColumns().add(kindColumn);

        TableColumn<MidiEventInfo, String> data2Column = new TableColumn<>("Comment");
        setColumnSize(data2Column, columnSizes[3]);
        data2Column.setCellValueFactory(new PropertyValueFactory<>("comment"));
        data2Column.setCellFactory(column -> createStringTableCell());
        table.getColumns().add(data2Column);

        TableColumn<MidiEventInfo, Integer> dataColumn = new TableColumn<>("Value");
        setColumnSize(dataColumn, columnSizes[4]);
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("data2"));
        dataColumn.setCellFactory(column -> createIntegerTableCell());
        table.getColumns().add(dataColumn);

        TableColumn<MidiEventInfo, String> durationColumn = new TableColumn<>("Duration");
        setColumnSize(durationColumn, columnSizes[5]);
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationColumn.setCellFactory(column -> createStringTableCell());
        table.getColumns().add(durationColumn);

        TableColumn<MidiEventInfo, String> messageColumn = new TableColumn<>("Message");
        setColumnSize(messageColumn, columnSizes[6]);
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        messageColumn.setCellFactory(column -> createStringTableCell());
        table.getColumns().add(messageColumn);
    }

    private final BooleanProperty contextMenuItemDisable = new SimpleBooleanProperty(true);
    private void createContextMenu(TableView<MidiEventInfo> table) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteEvent = new MenuItem("Delete");
        deleteEvent.disableProperty().bindBidirectional(contextMenuItemDisable);
        deleteEvent.setOnAction(e -> {
            List<MidiEventInfo> selectedEvents = table.getSelectionModel().getSelectedItems();

            for (MidiEventInfo selectedEvent : selectedEvents) {
                deleteEvent(selectedEvent, currentSequence);
            }

            getEventsPane().getEventlistPane().getFileSelectionPane().updateSequence(currentSequence);
            executeFiltering();

            ApplicationInfo.getInstance().setMidiChanged(true);

            setFileHasChangedCallback();
        });
        contextMenu.getItems().add(deleteEvent);

        SeparatorMenuItem sep = new SeparatorMenuItem();
        contextMenu.getItems().add(sep);

        MenuItem infoEvent = new MenuItem("Info");
        infoEvent.disableProperty().bindBidirectional(contextMenuItemDisable);
        infoEvent.setOnAction(e -> {
            List<MidiEventInfo> selectedEvents = table.getSelectionModel().getSelectedItems();

            EventDetailDialog dialog = new EventDetailDialog(selectedEvents.get(0));
            dialog.showAndWait();
        });
        contextMenu.getItems().add(infoEvent);

        table.setContextMenu(contextMenu);
    }

    private final EventHandler handler = event -> {
        if (ApplicationInfo.getInstance().isMidiChanged()) {
            if (DialogFactory.renderConfirmationDialog("Midi Changed", "Midi has changed. Save?")) {
                getEventsPane().getEventlistPane().getFileSelectionPane().handleSaveFile();

                ApplicationInfo.getInstance().setMidiChanged(false);
            }
        }
    };

    private void setFileHasChangedCallback() {
        Base base = (Base)ApplicationInfo.getInstance().getSpa();
        BaseClosePane closePane = base.getClosePane();

        closePane.setActionHandler(handler);

        // Also activate the save button above
        getEventsPane().getEventlistPane().getFileSelectionPane().fileHasChanged();
    }

    private void setColumnSize(TableColumn column, int size) {
        double dsize = size / 100.0;
        column.prefWidthProperty().bind(table.widthProperty().multiply(dsize));
        column.minWidthProperty().bind(table.widthProperty().multiply(dsize));
    }

    private TableCell<MidiEventInfo, String> createStringTableCell() {
        return new TableCell<>() {
            // Create a single label reuse instance per cell container
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    // Clear both text and graphic to prevent color bleed artifacts
                    setStyle("-fx-text-fill: black;");
                    setText(null);
                } else {
                    MidiEventInfo eventInfo = getTableRow() != null ? getTableRow().getItem() : null;

                    if (eventInfo != null) {
                        String hexColor = String.format("#%08X", eventInfo.getColorAsLong());
                        setText(item);
                        setStyle("-fx-text-fill: " + hexColor + ";");
                    } else {
                        setText(null);
                        setText(null);
                        setStyle("-fx-text-fill: black;");
                    }
                }
            }
        };
    }

    private TableCell<MidiEventInfo, Integer> createIntegerTableCell() {
        return new TableCell<MidiEventInfo, Integer>() {
            // Create a single label reuse instance per cell container
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    // Clear both text and graphic to prevent color bleed artifacts
                    setStyle("-fx-text-fill: black;");
                    setText(null);
                } else {
                    MidiEventInfo eventInfo = getTableRow() != null ? getTableRow().getItem() : null;

                    if (eventInfo != null) {
                        String hexColor = String.format("#%08X", eventInfo.getColorAsLong());
                        setText(item.toString());
                        setStyle("-fx-text-fill: " + hexColor + ";");
                    } else {
                        setText(null);
                        setStyle("-fx-text-fill: black;");
                    }
                }
            }
        };
    }

    private List<MidiEventInfo> currentEvents = new ArrayList<>();
    private Sequence currentSequence = null;

    public void setEventInfo(Sequence sequence, Queue<MidiEventInfo> events) {
        currentEvents = new ArrayList<>(events);
        currentSequence = sequence;

        getEventsPane().getEventlistPane().getFileSelectionPane().setEventInfo(sequence, events);

        if (table != null) {
            executeFiltering();
        }
    }

    private void executeFiltering() {
        List<MidiEventInfo> filteredEvents = new ArrayList<>(currentEvents);

        if (! filteredEvents.isEmpty()) {
            if (! selectedEvents[FilterPane.MIDI_EVENT]) {
                filteredEvents = filteredEvents.stream().filter(e -> ! e.getEventKey().hasValue(MIDI)).toList();
            }

            if (! selectedEvents[FilterPane.SYSEX_EVENT]) {
                filteredEvents = filteredEvents.stream().filter(e -> ! e.getEventKey().hasValue(SYSEX)).toList();
            }

            if (! selectedEvents[FilterPane.META_EVENT]) {
                filteredEvents = filteredEvents.stream().filter(e -> ! e.getEventKey().hasValue(META)).toList();
            }

            if (selectedEvents[FilterPane.CHANNEL]) {
                if  (selectedChannel > 0) {
                    filteredEvents = filteredEvents.stream().filter(e -> ((e.getChannel() != null) && e.getChannel() == selectedChannel)).toList();
                } else {
                    filteredEvents = filteredEvents.stream().filter(e -> e.getChannel() == null).toList();
                }
            }

            if (! hiddenEvents.isEmpty()) {
                for (EventKeyValue hiddenEvent : hiddenEvents) {
//                    filteredEvents = filteredEvents.stream().filter(e -> ! e.getEventKey().hasValue(hiddenEvent)).toList();
                    filteredEvents = filteredEvents.stream().filter(e -> ! e.getEventKey().is(hiddenEvent)).toList();
                }
            }

            contextMenuItemDisable.set(filteredEvents.isEmpty());

            List<MidiEventInfo> sortedQueue = filteredEvents.stream()
                    .sorted(Comparator.comparing(MidiEventInfo::getMbtPosition)).toList();

            ObservableList<MidiEventInfo> data = FXCollections.observableArrayList(sortedQueue);
            table.getItems().clear();
            table.setItems(data);
            table.scrollTo(0);
        } else {
            if (table != null) {
                table.getItems().clear();
            }
        }
    }

    private static List<EventKeyValue> hiddenEvents = new ArrayList<>();
    public void handleFiltering(boolean[] selected, int channel, List<EventKeyValue> hiddenEvents) {
        selectedEvents = selected;
        selectedChannel = channel;
        EventsDisplayPane.hiddenEvents = hiddenEvents;

        executeFiltering();
    }

    public void deleteEvent(MidiEventInfo eventInfoToDelete, Sequence activeSequence) {
        currentEvents.remove(eventInfoToDelete);

        MidiEvent targetEvent = eventInfoToDelete.getOriginalEvent();
        if (targetEvent == null || targetEvent.getMessage() == null) return;

        // 1. Calculate the exact start-tick (bijv. 10 * 4 = 40)
        long startTick = eventInfoToDelete.getTick() * 4;
        long endTickWindow = startTick + 5; // window of 5 ticks for LSB and PC

        MidiMessage targetMsg = targetEvent.getMessage();
        int targetChannel = -1;

        // 2. Find out the read MIDI-channel of the selected event
        if (targetMsg instanceof ShortMessage) {
            targetChannel = ((ShortMessage) targetMsg).getChannel();
        }

        // If it is not ShortMessage (so we don't have a channel), stop
        if (targetChannel == -1) return;

        // 3. Loop through all tracks of the sequence
        for (Track track : activeSequence.getTracks()) {
            java.util.List<MidiEvent> eventsToRemove = new java.util.ArrayList<>();

            for (int i = 0; i < track.size(); i++) {
                MidiEvent currentEvent = track.get(i);
                long currentTick = currentEvent.getTick();

                // Check A: Does the event fall inside the window
                if (currentTick >= startTick && currentTick <= endTickWindow) {
                    MidiMessage currentMsg = currentEvent.getMessage();

                    if (currentMsg instanceof ShortMessage currentSm) {

                        // CRUCIAL CHECK: The event must exactly match with the selected MIDI-channel!
                        if (currentSm.getChannel() == targetChannel) {
                            int cmd = currentSm.getCommand();

                            boolean isMSB = (cmd == ShortMessage.CONTROL_CHANGE && currentSm.getData1() == 0);
                            boolean isLSB = (cmd == ShortMessage.CONTROL_CHANGE && currentSm.getData1() == 32);
                            boolean isPC = (cmd == ShortMessage.PROGRAM_CHANGE);

                            if (isMSB || isLSB || isPC) {
                                eventsToRemove.add(currentEvent);
                            }
                        }
                    }
                }
            }

            // STAP B: Erase now ONLY the selected channel-events
            for (MidiEvent eventToDelete : eventsToRemove) {
                track.remove(eventToDelete);
            }
        }
    }

    // ACCESSORS
    public EventsPane getEventsPane() {
        return parent;
    }
}


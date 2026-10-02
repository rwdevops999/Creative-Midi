package creative.scenes.eventlist;

import creative.scenes.eventlist.parser.data.EventType;
import creative.scenes.eventlist.parser.entity.MidiEventInfo;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.Sequence;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;

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

    private int[] columnSizes = {10,4,15,4,15,8,53};

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
        mbtColumn.setCellFactory(column -> {
            return createStringTableCell();
        });
        table.getColumns().add(mbtColumn);
/*
        TableColumn<MidiEventInfo, Integer> channelColumn = new TableColumn<>("Ch");
        setColumnSize(channelColumn, columnSizes[1]);
        channelColumn.setCellValueFactory(new PropertyValueFactory<>("channel"));
        channelColumn.setCellFactory(column -> {
            return createIntegerTableCell();
        });
        table.getColumns().add(channelColumn);

        TableColumn<MidiEventInfo, String> kindColumn = new TableColumn<>("Kind");
        setColumnSize(kindColumn, columnSizes[2]);
        kindColumn.setCellValueFactory(new PropertyValueFactory<>("kind"));
        kindColumn.setCellFactory(column -> {
            return createStringTableCell();
        });
        table.getColumns().add(kindColumn);

        TableColumn<MidiEventInfo, Integer> data2Column = new TableColumn<>("Value");
        setColumnSize(data2Column, columnSizes[3]);
        data2Column.setCellValueFactory(new PropertyValueFactory<>("data2"));
        data2Column.setCellFactory(column -> {
            return createIntegerTableCell();
        });
        table.getColumns().add(data2Column);

        TableColumn<MidiEventInfo, String> dataColumn = new TableColumn<>("Data");
        setColumnSize(dataColumn, columnSizes[4]);
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
        dataColumn.setCellFactory(column -> {
            return createStringTableCell();
        });
        table.getColumns().add(dataColumn);

        TableColumn<MidiEventInfo, String> durationColumn = new TableColumn<>("Duration");
        setColumnSize(durationColumn, columnSizes[5]);
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationColumn.setCellFactory(column -> {
            return createStringTableCell();
        });
        table.getColumns().add(durationColumn);

        TableColumn<MidiEventInfo, String> messageColumn = new TableColumn<>("Message");
        setColumnSize(messageColumn, columnSizes[6]);
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
        messageColumn.setCellFactory(column -> {
            return createStringTableCell();
        });
        table.getColumns().add(messageColumn); */
    }

    private final BooleanProperty deleteMenuDisable = new SimpleBooleanProperty(true);
    private void createContextMenu(TableView<MidiEventInfo> table) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteEvent = new MenuItem("Delete");
        deleteEvent.disableProperty().bindBidirectional(deleteMenuDisable);
        deleteEvent.setOnAction(e -> {
            // TODO
        });
        contextMenu.getItems().add(deleteEvent);
        table.setContextMenu(contextMenu);
    }

    private void setColumnSize(TableColumn column, int size) {
        double dsize = size / 100.0;
        column.prefWidthProperty().bind(table.widthProperty().multiply(dsize));
        column.minWidthProperty().bind(table.widthProperty().multiply(dsize));
    }

    private TableCell<MidiEventInfo, String> createStringTableCell() {
        return new TableCell<MidiEventInfo, String>() {
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
//TODO                        String hexColor = String.format("#%08X", eventInfo.getItemColor() & 0xFFFFFFFFL);
//                        setText(item);
//                        setStyle("-fx-text-fill: " + hexColor + ";");
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
//TODO                        String hexColor = String.format("#%08X", eventInfo.getItemColor() & 0xFFFFFFFFL);
//                        setText(item.toString());
//                        setStyle("-fx-text-fill: " + hexColor + ";");
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

//        parent.getEventListPane().getFileSelectionPane().setEventInfo(sequence, events);

        if (table != null) {
            executeFiltering();
        }
    }

    private void executeFiltering() {
        List<MidiEventInfo> filteredEvents = new ArrayList<>(currentEvents);

        if (! filteredEvents.isEmpty()) {
            if (! selectedEvents[FilterPane.MIDI_EVENT]) {
                filteredEvents = filteredEvents.stream().filter(e -> ! e.getEventType().equals(EventType.MIDI)).toList();
            }

            if (! selectedEvents[FilterPane.SYSEX_EVENT]) {
                filteredEvents = filteredEvents.stream().filter(e -> ! e.getEventType().equals(EventType.SYSEX)).toList();
            }

            if (! selectedEvents[FilterPane.META_EVENT]) {
                filteredEvents = filteredEvents.stream().filter(e -> ! e.getEventType().equals(EventType.META)).toList();
            }

            if (selectedEvents[FilterPane.CHANNEL] && selectedChannel > 0) {
                filteredEvents = filteredEvents.stream().filter(e -> ((e.getChannel() != null) && e.getChannel() == selectedChannel)).toList();
            }

            deleteMenuDisable.set(filteredEvents.isEmpty());

            List<MidiEventInfo> sortedQueue = filteredEvents.stream()
                    .sorted(Comparator.comparing(MidiEventInfo::getMbtPosition)).toList();

            ObservableList<MidiEventInfo> data = FXCollections.observableList(sortedQueue);
            table.setItems(data);
            table.scrollTo(0);
        }
    }

    public void handleFiltering(boolean[] selected, int channel) {
        selectedEvents = selected;
        selectedChannel = channel;
        executeFiltering();
    }

    // ACCESSORS
    public EventsPane getEventsPane() {
        return parent;
    }
}


package creative.scenes.eventlist.dialog;

import creative.scenes.eventlist.data.EventKeyValue;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static util.Util.setPaneBackground;
import static util.Util.toPascalCase;

public class FilterDialog extends Dialog<List<EventKeyValue>> {
    public FilterDialog() {
        super();

        initStyle(StageStyle.UNDECORATED);
    }

    private String selectedGroup;
    public FilterDialog(String group, List<EventKeyValue> availableEvents, List<EventKeyValue> hiddenEvents) {
        this();

        selectedGroup = toPascalCase(group);

        setPaneBackground(getDialogPane());

        setTitle("Filtering");
        setHeaderText("Determine the events to be shown or hidden");

        List<EventKeyValue> allEvents = new ArrayList<>(availableEvents);
        allEvents.addAll(hiddenEvents);

        setupDialog(availableEvents, hiddenEvents, allEvents);
        buildDialogContent();
    }

    private List<EventKeyValue> visibleEventKeys = new ArrayList<>();
    private List<EventKeyValue> hiddenEventKeys = new ArrayList<>();

    private List<String> groups;

    private final ListProperty<EventKeyValue> visibleEventsProperty = new SimpleListProperty<>(
            FXCollections.observableArrayList(visibleEventKeys)
    );
    private final ListProperty<EventKeyValue> hiddenEventsProperty = new SimpleListProperty<>(
            FXCollections.observableArrayList(hiddenEventKeys)
    );

    private final BooleanProperty addDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty removeDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty dsParentFiltering = new SimpleBooleanProperty(false);

    private void setupDialog(List<EventKeyValue> availableEvents, List<EventKeyValue> hiddenEvents, List<EventKeyValue> allEvents) {
        visibleEventKeys = availableEvents.stream().sorted(Comparator.comparing(EventKeyValue::name)).toList();
        hiddenEventKeys = hiddenEvents.stream().sorted(Comparator.comparing(EventKeyValue::name)).toList();

        groups = allEvents.stream().map(e -> toPascalCase(e.name().split("_")[0])).distinct().toList();

        handleEvents();

        updateDisables();
    }

    private void handleEvents() {
        visibleEventsProperty.set(FXCollections.observableArrayList(removeParentMessages(visibleEventKeys)));
        hiddenEventsProperty.set(FXCollections.observableArrayList(hiddenEventKeys));
    }

    private void updateEvents() {
        List<EventKeyValue> events = new ArrayList<>(visibleEventKeys);
        List<EventKeyValue> currentHidden = new ArrayList<>(hiddenEventsProperty.get());
        events.removeAll(currentHidden);

        visibleEventsProperty.set(FXCollections.observableArrayList(removeParentMessages(events)));
    }

    private List<EventKeyValue> removeParentMessages(List<EventKeyValue> events) {
        if (dsParentFiltering.get()) {
            return events;
        }

        return events.stream().filter(ekv -> !ekv.name().contains("_MESSAGE")).toList();
    }

    private void buildDialogContent() {
        ButtonType useButtonType = new ButtonType("Filter", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(useButtonType, ButtonType.CANCEL);

        TabPane content = buildContent();
        if (selectedGroup != null) {
            selectDefaultTab(content, selectedGroup);
        }
        getDialogPane().setContent(content);

        setResultConverter(dialogButton -> {
            if (dialogButton == useButtonType) {
                List<EventKeyValue> hidden = hiddenEventsProperty.get();
                return hidden;
            }

            return null; // return null when we cancel
        });
    }

    private TabPane buildContent() {
        return buildPane(groups);
    }

    private TabPane buildPane(List<String> groups) {
        TabPane tabPane = new TabPane();

        for (String group : groups) {
            Tab tab = new Tab(group);
            tab.setDisable(!group.equals(selectedGroup));
            tab.setContent(buildContentPane());
            tab.setClosable(false);
            tabPane.getTabs().add(tab);
        }

        return tabPane;
    }

    private ListView<EventKeyValue> availableEventsListView;
    private ListView<EventKeyValue> hiddenEventsListView;

    private HBox buildContentPane() {
        HBox hBox = new HBox();

        // Setup HBOX
        hBox.setSpacing(5);

        // build the visible list
        Pane pane = buildListPane("visible", visibleEventsProperty, true);
        hBox.getChildren().add(pane);
        availableEventsListView = lookup(pane);
        hBox.getChildren().add(buildButtonsPane());
        pane = buildListPane("hidden", hiddenEventsProperty, false);
        hBox.getChildren().add(pane);
        hiddenEventsListView = lookup(pane);

        return hBox;
    }

    private VBox buildListPane(String label, ListProperty<EventKeyValue> listProperty, boolean parentFiltering) {
        VBox vBox = new VBox();

        // setup VBOX
        vBox.setPadding(new Insets(10));

        Label lbl = new Label(label);
        vBox.getChildren().add(lbl);

        ListView<EventKeyValue> listView = new ListView<>();
        listView.setId("listview");
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.itemsProperty().bind(listProperty);
        listView.setCellFactory(lv -> new ListCell<EventKeyValue>() {
            @Override
            protected void updateItem(EventKeyValue item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    String value = item.name().replaceFirst("_", "@#").split("@#")[1];
                    setText(toPascalCase(value));
                }
            }
        });
        vBox.getChildren().add(listView);

        if (parentFiltering) {
            CheckBox checkbox = new CheckBox("Filter on parents");
            checkbox.selectedProperty().bindBidirectional(dsParentFiltering);
            vBox.getChildren().add(checkbox);

            dsParentFiltering.addListener((observable, oldValue, newValue) -> {
                updateEvents();
            });
        }

        return vBox;
    }

    private VBox buildButtonsPane() {
        VBox vBox = new VBox();

        // setup buttons pane
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);

        String flatButtonStyle =
                "-fx-background-color: #a9a9a9; " +     // Gray background (or 'transparent')
                        "-fx-background-radius: 0; " +  // Square hooks
                        "-fx-text-fill: #333333; " +    // Text color
                        "-fx-font-size: 14px; " +       // Size of characters
                        "-fx-font-weight: bold; " +     // Bold text
                        "-fx-cursor: hand;";

        Button addSelected = new Button(">");
        addSelected.disableProperty().bind(addDisabled);
        addSelected.setStyle(flatButtonStyle);

        addSelected.setOnAction(e -> {
            if (! availableEventsListView.getSelectionModel().getSelectedIndices().isEmpty()) {
                moveSelected (visibleEventsProperty, hiddenEventsProperty, new ArrayList<>(availableEventsListView.getSelectionModel().getSelectedItems()));
            }
        });

        vBox.getChildren().add(addSelected);

        Button addAll = new Button("»"); // >>
        addAll.disableProperty().bind(addDisabled);
        addAll.setStyle(flatButtonStyle);
        addAll.setOnAction(e -> moveSelected (visibleEventsProperty, hiddenEventsProperty, visibleEventsProperty.get()));
        vBox.getChildren().add(addAll);

        Button removeAll = new Button("«"); // <<
        removeAll.disableProperty().bind(removeDisabled);
        removeAll.setStyle(flatButtonStyle);
        removeAll.setOnAction(e -> moveSelected (hiddenEventsProperty, visibleEventsProperty, hiddenEventsProperty.get()));
        vBox.getChildren().add(removeAll);

        Button removeSelected = new Button("<"); // <
        removeSelected.disableProperty().bind(removeDisabled);
        removeSelected.setStyle(flatButtonStyle);
        removeSelected.setOnAction(e -> {
            if (! hiddenEventsListView.getSelectionModel().getSelectedIndices().isEmpty()) {
                moveSelected (hiddenEventsProperty, visibleEventsProperty, new ArrayList<>(hiddenEventsListView.getSelectionModel().getSelectedItems()));
            }
        });
        vBox.getChildren().add(removeSelected);

        return vBox;
    }

    private void moveSelected (ListProperty<EventKeyValue> from, ListProperty<EventKeyValue> to, List<EventKeyValue> selected) {
        to.get().addAll(selected);
        from.get().removeAll(selected);

        sortList(to);

        updateDisables();
    }

    private void moveSingle (EventKeyValue which, ListProperty<EventKeyValue> from, ListProperty<EventKeyValue> to) {
        to.get().add(which);
        from.get().remove(which);

        sortList(to);

        updateDisables();
    }

    private void sortList(ListProperty<EventKeyValue> list) {
        SortedList<EventKeyValue> sortedOnName = new SortedList<>(list.get(), Comparator.comparing(EventKeyValue::name));
        list.set(FXCollections.observableArrayList(sortedOnName));
    }

    private void updateDisables() {
        addDisabled.set(visibleEventsProperty.get().isEmpty());
        removeDisabled.set(hiddenEventsProperty.get().isEmpty());
    }

    private ListView<EventKeyValue> lookup(Pane pane) {
        return pane.getChildren().stream()
                .filter(node -> "listview".equals(node.getId()))
                .map(node -> (ListView<EventKeyValue>) node)
                .findFirst()
                .orElse(null);
    }

    private void selectDefaultTab(TabPane tabPane, String name) {
        // search all tabs
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText() != null && tab.getText().equals(name)) {
                // Select the tab when the name fits
                tabPane.getSelectionModel().select(tab);
                break; // Stop the loop as soon the tab is found
            }
        }
    }
}

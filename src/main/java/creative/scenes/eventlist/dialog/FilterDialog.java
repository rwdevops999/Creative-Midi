package creative.scenes.eventlist.dialog;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

import static util.Util.setPaneBackground;

public class FilterDialog extends Dialog<List<String>> {
    public FilterDialog() {
        super();

        initStyle(StageStyle.UNDECORATED);
    }

    private String selectedGroup;
    public FilterDialog(String group, List<String> availableEvents, List<String> hiddenEvents) {
        this();

        selectedGroup = toPascalCase(group);

        setPaneBackground(getDialogPane());

        setTitle("Filtering");
        setHeaderText("Determine the events to be shown or hidden");

        List<String> allEvents = new ArrayList<>(availableEvents);
        allEvents.addAll(hiddenEvents);

        setupDialog(availableEvents, hiddenEvents, allEvents);
        buildDialogContent();
    }

    private List<String> visibleEventKeys = new ArrayList<>();
    private List<String> hiddenEventKeys = new ArrayList<>();

    private List<String> groups;

    private final ListProperty<String> visibleEventsProperty = new SimpleListProperty<>(
            FXCollections.observableArrayList(visibleEventKeys)
    );
    private final ListProperty<String> hiddenEventsProperty = new SimpleListProperty<>(
            FXCollections.observableArrayList(hiddenEventKeys)
    );

    private final BooleanProperty addDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty removeDisabled = new SimpleBooleanProperty(true);

    private void setupDialog(List<String> availableEvents, List<String> hiddenEvents, List<String> allEvents) {
        visibleEventKeys = availableEvents;
        hiddenEventKeys = hiddenEvents;

        groups = allEvents.stream().map(e -> toPascalCase(e.split("_")[0])).distinct().toList();

        visibleEventKeys = visibleEventKeys.stream().map(e -> {
            String value = e.replaceFirst("_", "@#").split("@#")[1];
            return toPascalCase(value);
        }).toList();
        visibleEventsProperty.set(FXCollections.observableArrayList(visibleEventKeys));

        hiddenEventKeys = hiddenEventKeys.stream().map(e -> {
            String value = e.replaceFirst("_", "@#").split("@#")[1];
            return toPascalCase(value);
        }).toList();
        hiddenEventsProperty.set(FXCollections.observableArrayList(hiddenEventKeys));

        updateDisables();
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
                List<String> hidden = hiddenEventsProperty.get();
                return hidden.stream().map(s -> selectedGroup.toUpperCase() + "_" + s.toUpperCase().replace(" ", "_")).toList();
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

    private ListView<String> availableEventsListView;
    private ListView<String> hiddenEventsListView;

    private HBox buildContentPane() {
        HBox hBox = new HBox();

        // Setup HBOX
        hBox.setSpacing(5);

        // build the visible list
        Pane pane = buildListPane("visible", visibleEventsProperty   );
        hBox.getChildren().add(pane);
        availableEventsListView = lookup(pane);
        hBox.getChildren().add(buildButtonsPane());
        pane = buildListPane("hidden", hiddenEventsProperty   );
        hBox.getChildren().add(pane);
        hiddenEventsListView = lookup(pane);

        return hBox;
    }

    private VBox buildListPane(String label, ListProperty<String> listProperty) {
        VBox vBox = new VBox();

        // setup VBOX
        vBox.setPadding(new Insets(10));

        Label lbl = new Label(label);
        vBox.getChildren().add(lbl);

        ListView<String> listView = new ListView<>();
        listView.setId("listview");
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.itemsProperty().bind(listProperty);
        vBox.getChildren().add(listView);

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

    private String toPascalCase(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        // Split by underscores, spaces, or hyphens
        String[] words = input.split("[_\\s-]+");
        StringBuilder pascalCaseString = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                // Capitalize first letter, lowercase the rest
                pascalCaseString.append(" ").append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase());
            }
        }

        return pascalCaseString.toString().trim();
    }

    private void moveSelected (ListProperty<String> from, ListProperty<String> to, List<String> selected) {
        to.get().addAll(selected);
        from.get().removeAll(selected);

        updateDisables();
    }

    private void updateDisables() {
        addDisabled.set(visibleEventsProperty.get().isEmpty());
        removeDisabled.set(hiddenEventsProperty.get().isEmpty());
    }

    private ListView<String> lookup(Pane pane) {
        return pane.getChildren().stream()
                .filter(node -> "listview".equals(node.getId()))
                .map(node -> (ListView<String>) node)
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

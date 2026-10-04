package creative.scenes.test.test3;

import communication.CommunicationModel;
import creative.scenes.eventlist.data.EventKey;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static util.Util.setPaneBackground;

public class Test3Pane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(Test3Pane.class);
    public Test3Pane() {
        super();

        setId("Test3");
        CommunicationModel.setStatus("Running Test3");

        setupTest();
        runTest();
    }

    private List<String> visibleEventKeys = new ArrayList<>();
    private List<String> hiddenEventKeys = new ArrayList<>();

    private final ListProperty<String> visibleEventsProperty = new SimpleListProperty<>(
            FXCollections.observableArrayList(visibleEventKeys)
    );

    private final ListProperty<String> hiddenEventsProperty = new SimpleListProperty<>(
            FXCollections.observableArrayList(hiddenEventKeys)
    );

    private List<String> groups;

    private void setupTest() {
        logger.debug("[CM_TEST1_PANE] Setting up {}", getId());

        setPaneBackground(this);

        visibleEventKeys.clear();
        visibleEventKeys.addAll(EventKey.getEvents(EventKey.META));

        groups = visibleEventKeys.stream().map(e -> toPascalCase(e.split("_")[0])).distinct().toList();

        visibleEventKeys = visibleEventKeys.stream().map(e -> {
            String value = e.replaceFirst("_", "@#").split("@#")[1];
            return toPascalCase(value);
        }).toList();
        visibleEventsProperty.set(FXCollections.observableArrayList(visibleEventKeys));

        updateDisables();

        logger.debug("[CM_TEST1_PANE] Set up {}", getId());
    }

    private void runTest() {
        logger.debug("[CM_TEST1_PANE] Executing {}", getId());

        TabPane tabPane = buildPane(groups);
        getChildren().add(tabPane);

        logger.debug("[CM_TEST1_PANE] Executed {}", getId());
    }

    private TabPane buildPane(List<String> groups) {
        TabPane tabPane = new TabPane();

        for (String group : groups) {
            Tab tab = new Tab(group);
            tab.setContent(buildContentPane(group));
            tab.setClosable(false);
            tabPane.getTabs().add(tab);
        }

        return tabPane;
    }

    private ListView<String> availableEventsListView;
    private ListView<String> hiddenEventsListView;

    private HBox buildContentPane(String group) {
        HBox hBox = new HBox();

        // Setup HBOX
        hBox.setSpacing(5);

        // build the visible list
        Pane pane = buildListPane("visible", visibleEventsProperty   );
        hBox.getChildren().add(pane);
        availableEventsListView = lookup(pane, "listview");
        hBox.getChildren().add(buildButtonsPane());
        pane = buildListPane("hidden", hiddenEventsProperty   );
        hBox.getChildren().add(pane);
        hiddenEventsListView = lookup(pane, "listview");

        Button getResult = new Button("Get Result");
        hBox.getChildren().add(getResult);

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

    private BooleanProperty addDisabled = new SimpleBooleanProperty(true);
    private BooleanProperty removeDisabled = new SimpleBooleanProperty(true);

    private VBox buildButtonsPane() {
        VBox vBox = new VBox();

        // setup buttons pane
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);

        String flatButtonStyle =
                "-fx-background-color: #a9a9a9; " + // Grijze achtergrond (of 'transparent')
                        "-fx-background-radius: 0; " +       // Rechte hoeken (gebruik bijv. 4px voor licht afgerond)
                        "-fx-text-fill: #333333; " +         // Tekstkleur
                        "-fx-font-size: 14px; " +            // Lettergrootte
                        "-fx-font-weight: bold; " +          // Dikgedrukte tekst
                        "-fx-cursor: hand;";

        Button addSelected = new Button("\u003E");
        addSelected.disableProperty().bind(addDisabled);
        addSelected.setStyle(flatButtonStyle);

        addSelected.setOnAction(e -> {
            if (! availableEventsListView.getSelectionModel().getSelectedIndices().isEmpty()) {
                hiddenEventKeys = moveSelected (visibleEventsProperty, hiddenEventsProperty, new ArrayList<>(availableEventsListView.getSelectionModel().getSelectedItems()));
            }
        });

        vBox.getChildren().add(addSelected);

        Button addAll = new Button("\u00BB"); // >>
        addAll.disableProperty().bind(addDisabled);
        addAll.setStyle(flatButtonStyle);
        addAll.setOnAction(e -> {
            hiddenEventKeys = moveSelected (visibleEventsProperty, hiddenEventsProperty, visibleEventKeys);
        });
        vBox.getChildren().add(addAll);

        Button removeAll = new Button("\u00AB"); // <<
        removeAll.disableProperty().bind(removeDisabled);
        removeAll.setStyle(flatButtonStyle);
        removeAll.setOnAction(e -> {
            visibleEventKeys = moveSelected (hiddenEventsProperty, visibleEventsProperty, visibleEventKeys);
        });
        vBox.getChildren().add(removeAll);

        Button removeSelected = new Button("\u003C"); // <
        removeSelected.disableProperty().bind(removeDisabled);
        removeSelected.setStyle(flatButtonStyle);
        removeSelected.setOnAction(e -> {
            if (! hiddenEventsListView.getSelectionModel().getSelectedIndices().isEmpty()) {
                visibleEventKeys = moveSelected (hiddenEventsProperty, visibleEventsProperty, new ArrayList<>(hiddenEventsListView.getSelectionModel().getSelectedItems()));
            }
        });
        vBox.getChildren().add(removeSelected);

        return vBox;
    }
    // this method must come inside the Dialog
    public String toPascalCase(String input) {
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

    private ListView<String> lookup(Pane pane, String id) {
        return pane.getChildren().stream()
                .filter(node -> "listview".equals(node.getId()))
                .map(node -> (ListView<String>) node)
                .findFirst()
                .orElse(null);
    }

    private List<String> moveSelected (ListProperty<String> from, ListProperty<String> to, List<String> selected) {
        to.get().addAll(selected);
        from.get().removeAll(selected);

        updateDisables();

        return to.get();
    }

    private void updateDisables() {
        addDisabled.set(visibleEventsProperty.get().isEmpty());
        removeDisabled.set(hiddenEventsProperty.get().isEmpty());

    }
}

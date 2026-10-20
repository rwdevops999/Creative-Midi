package creative.scenes.voice.components;

import creative.scenes.voice.ExcludesPane;
import creative.scenes.voice.util.VoiceFinder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.*;

import static util.Util.setPaneWidthAsPercentage;

public class ExcludePane extends GridPane {
    public ExcludePane() {
        super();

        setId("ExcludePane");

        int[] columnSizes = {42,16,42};
        for (int size: columnSizes) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(size);
            col.setHalignment(HPos.LEFT);
            getColumnConstraints().add(col);
        }

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.CENTER);
        getRowConstraints().add(rowConstraints);
    }

//    private String selectedGroup = null;


    private ListView<String> availableTypesListView;
    private ListView<String> excludedTypesListView;

    private ExcludesPane parent;
    private Set<String> availableVoiceTypes;
    private Set<String> excludedVoiceTypes = new HashSet<>();

    public ExcludePane(ExcludesPane owner) {
        this();

        parent = owner;

        availableVoiceTypes = new HashSet<>(VoiceFinder.getVoiceTypes());

        setPaneWidthAsPercentage(this, owner, 50);

        buildPanel();
    }

    private void buildPanel() {
        String flatButtonStyle =
                "-fx-background-color: #a9a9a9; " +          // Gray background (or 'transparent')
                        "-fx-background-radius: 0; " +       // Square corners
                        "-fx-text-fill: #333333; " +         // Text color
                        "-fx-font-size: 14px; " +            // Text size
                        "-fx-font-weight: bold; " +          // Bold text
                        "-fx-cursor: hand;";

        int row = 0;
        // ROW (available, excluded)
        Label available = new Label("Available");
        add(available, 0, row);

        Label excluded = new Label("Excluded");
        add(excluded, 2, row);

        row++;
        ObservableList<String> availableItems = FXCollections.observableArrayList(availableVoiceTypes);
        availableTypesListView = new ListView<>();
        availableTypesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        availableTypesListView.getStyleClass().add("no-horizontal-scroll");
        availableTypesListView.setItems(availableItems);
        add(availableTypesListView, 0, row, 1, 4);

        ObservableList<String> excludedItems = FXCollections.observableArrayList(excludedVoiceTypes);
        excludedTypesListView = new ListView<>();
        excludedTypesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        excludedTypesListView.getStyleClass().add("no-horizontal-scroll");
        excludedTypesListView.setItems(excludedItems);
        add(excludedTypesListView, 2, row, 1, 4);

        // Buttons
        Button addAll = new Button("»"); // >>
        addAll.setDisable(availableVoiceTypes.isEmpty());
        addAll.setAlignment(Pos.CENTER);
        addAll.setStyle(flatButtonStyle);
        addAll.setOnAction(e -> {
            if (! availableVoiceTypes.isEmpty()) {
                move(availableVoiceTypes, excludedVoiceTypes);
                updateListViews();
                updateResults();
            }
        });
        add(addAll, 1, row, 3, 1);

        row++;

        Button addSel = new Button(">"); // >
        addSel.setDisable(availableVoiceTypes.isEmpty());
        addSel.setStyle(flatButtonStyle);
        addSel.setAlignment(Pos.CENTER);
        addSel.setOnAction(e -> {
            if (! availableTypesListView.getSelectionModel().getSelectedIndices().isEmpty()) {
                excludedVoiceTypes = moveSelected (availableVoiceTypes, excludedVoiceTypes, new HashSet<>(availableTypesListView.getSelectionModel().getSelectedItems()));
                updateListViews();
                updateResults();
            }
        });
        add(addSel, 1, row, 3, 1);

        row++;
        Button delSel = new Button("<"); // <
        delSel.setDisable(excludedVoiceTypes.isEmpty());
        delSel.setStyle(flatButtonStyle);
        delSel.setAlignment(Pos.CENTER);
        delSel.setOnAction(e -> {
            if (! excludedTypesListView.getSelectionModel().getSelectedIndices().isEmpty()) {
                availableVoiceTypes = moveSelected (excludedVoiceTypes, availableVoiceTypes, new HashSet<>(excludedTypesListView.getSelectionModel().getSelectedItems()));
                updateListViews();
                updateResults();
            }
        });
        add(delSel, 1, row, 3, 1);

        row++;

        Button delAll = new Button("«"); // <<
        delAll.setDisable(excludedVoiceTypes.isEmpty());
        delAll.setAlignment(Pos.CENTER);
        delAll.setStyle(flatButtonStyle);
        delAll.setOnAction(e -> {
            if (! excludedVoiceTypes.isEmpty()) {
                move (excludedVoiceTypes, availableVoiceTypes);
                updateListViews();
                updateResults();
            }
        });
        add(delAll, 1, row, 3, 1);
    }

    private void move (Set<String> src, Set<String> dst) {
        dst.addAll(src);
        src.clear();
    }

    private Set<String> moveSelected (Set<String> src, Set<String> dst, Set<String> selected) {
        dst.addAll(selected);
        src.removeAll(selected);

        return dst;
    }

    private void updateListViews () {
        getChildren().clear();
        buildPanel();
    }

    private void updateResults() {
        getExcludesPane().getVoiceSearchPane().getVoicePane().getVoiceSearchResultsPane().setExcludes(new ArrayList<>(excludedVoiceTypes));
    }

    // ACCESSOR
    public ExcludesPane getExcludesPane() {
        return parent;
    }
}

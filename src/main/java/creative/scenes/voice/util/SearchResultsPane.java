package creative.scenes.voice.util;

import creative.scenes.voice.VoiceSearchResultsPane;
import creative.scenes.voice.dialog.VoiceDetailDialog;
import creative.scenes.voice.provider.InstrumentProvider;
import entity.voice.Patch;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import system.SysData;
import util.ApplicationInfo;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsPane extends AnchorPane {
    private static final Logger logger = LoggerFactory.getLogger(SearchResultsPane.class);

    public SearchResultsPane() {
        super();

        setId("SearchResultsPane");
    }

    private List<Patch> tableResults = new ArrayList<>();

    private VoiceSearchResultsPane parent;
    public SearchResultsPane(VoiceSearchResultsPane owner) {
        this();

        parent = owner;

        buildPane();

        setTopAnchor(table, 0.0);
        setBottomAnchor(table, 0.0);
        setLeftAnchor(table, 0.0);
        setRightAnchor(table, 0.0);

    }

    private final TableView<Patch> table = new TableView<>();
    private TableColumn<Patch, String> patchColumn = null;

    private void buildPane() {
        logger.debug("[CM_SEARCH_RESULTS_PANE] Building {}", getId());

        patchColumn = new TableColumn<>("Patch");
        patchColumn.setCellValueFactory(new PropertyValueFactory<>("patch"));
        patchColumn.setCellFactory(column -> {
            return new TableCell<Patch, String>() {
                // We make once the icon for a cel for beter performances
                private final Label starIcon = new Label("★");
                {
                    starIcon.setTextFill(Color.GOLD); // Of Color.YELLOW, maar GOLD oogt vaak mooier
                    starIcon.setStyle("-fx-font-size: 12px; -fx-text-fill: #FFD700; -fx-font-weight: bold;");
                }
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item);

                        // Retrieve the Patch object from the current row
                        Patch currentPatch = getTableView().getItems().get(getIndex());

                        // is it a favortie?
                        if (SysData.isFavorite(currentPatch)) { // Pas dit aan naar jouw eigen 'isFavorite' methode of check
                            // Place the icon right of the tekst
                            setContentDisplay(ContentDisplay.RIGHT);
                            setGraphic(starIcon);
                        } else {
                            setGraphic(null); // Remove the star if it is no favorite
                        }
                    }
                }
            };
        });

        TableColumn<Patch, Integer> bankColumn  = new TableColumn<>("Bank");
        bankColumn.setCellValueFactory(new PropertyValueFactory<>("bank"));

        TableColumn<Patch, Integer> msbColumn  = new TableColumn<>("MSB");
        msbColumn.setCellValueFactory(new PropertyValueFactory<>("msb"));

        TableColumn<Patch, Integer> lsbColumn  = new TableColumn<>("LSB");
        lsbColumn.setCellValueFactory(new PropertyValueFactory<>("lsb"));

        TableColumn<Patch, Integer> pcColumn  = new TableColumn<>("PC");
        pcColumn.setCellValueFactory(new PropertyValueFactory<>("pc"));

        table.setId("VoicesTable");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.getColumns().addAll(patchColumn, bankColumn, msbColumn, lsbColumn, pcColumn);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.widthProperty().addListener((observable, oldValue, newValue) -> {
            double tableWidth = newValue.doubleValue();

            // Correct if necessary with 2 pixels for the outer edge of the table
            double availableWidth = tableWidth - 2.0;

            // Bereken de exacte doelbreedtes
            double w60 = availableWidth * 0.60;
            double w10 = availableWidth * 0.10;

            // Force JavaFX to set MIN, MAX en PREF on exactly th same values
            setAbsoluteColumnWidth(patchColumn, w60);
            setAbsoluteColumnWidth(bankColumn,  w10);
            setAbsoluteColumnWidth(msbColumn,   w10);
            setAbsoluteColumnWidth(lsbColumn,   w10);
            setAbsoluteColumnWidth(pcColumn,    w10);
        });
        createContextMenu(table);

        getChildren().add(table);

        logger.debug("[CM_SEARCH_RESULTS_PANE] Built {}", getId());
    }

    private void setAbsoluteColumnWidth(TableColumn<?, ?> column, double width) {
        column.setMinWidth(width);
        column.setMaxWidth(width);
        column.setPrefWidth(width);
    }

    private void createContextMenu(TableView<Patch> table) {
        // add Context Menu here
        ContextMenu contextMenu = new ContextMenu();

        // MenuItem (Set as Right1)
        MenuItem right1 = new MenuItem("Use as right1");
        right1.setOnAction(event -> {
            Patch patch = table.getSelectionModel().getSelectedItem();
//            sendAsMidi(0, patch);
        });

        // MenuItem (Set as Right2)
        MenuItem right2 = new MenuItem("Use as right2");
        right2.setOnAction(event -> {
            Patch patch = table.getSelectionModel().getSelectedItem();
//            sendAsMidi(1, patch);
        });

        // MenuItem (Set as Left)
        MenuItem left = new MenuItem("Use as left");
        left.setOnAction(event -> {
            Patch patch = table.getSelectionModel().getSelectedItem();
//            sendAsMidi(2, patch);
        });

        // MenuItem (Details)
        MenuItem details = new MenuItem("Show details");
        details.setOnAction(event -> {
            Patch patch = table.getSelectionModel().getSelectedItem();

            InstrumentProvider instrumentProvider = ApplicationInfo.getInstance().getInstrumentProvider();
            if ("INS".equals(instrumentProvider.getSourceType())) {
                patch = instrumentProvider.findLinkedPatch(patch);
            }

            VoiceDetailDialog dialog = new VoiceDetailDialog(patch);
            dialog.showAndWait();
        });

        // MenuItem (Favorites)
        MenuItem favorites = new MenuItem("Add as favorite");

        contextMenu.getItems().addAll(right1, left, new SeparatorMenuItem(), details, new SeparatorMenuItem(), favorites);

        contextMenu.setOnShowing(event -> {
            Patch selectedPatch = table.getSelectionModel().getSelectedItem();

            if (selectedPatch != null) {
                // Check if the patch is in the ObservableSet
                if (SysData.isFavorite(selectedPatch)) {
                    favorites.setText("Remove from Favorites");
                    favorites.setOnAction(e -> {
                        SysData.removeFromFavorites(selectedPatch);
                        table.refresh(); // Update the star immediatly
                    });
                } else {
                    favorites.setText("Add to Favorites");
                    favorites.setOnAction(e -> {
                        SysData.getFavorites().add(selectedPatch);
                        table.refresh(); // Update the star immediatly
                    });
                }
            }
        });

        table.setContextMenu(contextMenu);
    }

    public void showResults (List<Patch> data) {
        ObservableList<Patch> tableData = FXCollections.observableArrayList(data);
        table.setItems(tableData);

        autoFitColumn(table, patchColumn);
    }

    public static <S, T> void autoFitColumn(TableView<S> tableView, TableColumn<S, T> column) {
        Platform.runLater(() -> {
            // Start with the width of the header text (plus some padding)
            Text headerText = new Text(column.getText());
            double maxWidth = headerText.getLayoutBounds().getWidth() + 20.0;

            // Iterate through rows to find the longest cell text
            for (S item : tableView.getItems()) {
                if (column.getCellValueFactory() != null) {
                    var observableValue = column.getCellValueFactory().call(new TableColumn.CellDataFeatures<>(tableView, column, item));
                    if (observableValue != null && observableValue.getValue() != null) {
                        String cellTextString = observableValue.getValue().toString();
                        Text cellTextNode = new Text(cellTextString);
                        double cellWidth = cellTextNode.getLayoutBounds().getWidth() + 20.0; // 20px padding for cell borders

                        if (cellWidth > maxWidth) {
                            maxWidth = cellWidth;
                        }
                    }
                }
            }
            // Explicitly set the column width
            column.setPrefWidth(maxWidth);
        });
    }

    public void clear() {
        table.getItems().clear();
    }

    public Patch getSelectedPatch() {
        return table.getSelectionModel().getSelectedItem();
    }

    // ACCESSORS
    public VoiceSearchResultsPane getVoiceSearchResultsPane() {
        return parent;
    }
}

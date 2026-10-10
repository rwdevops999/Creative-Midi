package creative.scenes.voice.components;

import creative.scenes.voice.GroupSearchPane;
import creative.scenes.voice.PatchSearchPane;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;

import static util.Util.setPaneWidthAsPercentage;

public class TitleSearchPane extends GridPane {
    private static final Logger logger = LoggerFactory.getLogger(TitleSearchPane.class);

    public TitleSearchPane() {
        super();

        setId("TitleSearchPane");
    }

    public TitleSearchPane(Pane owner, String title) {
        super();

        setPaneWidthAsPercentage(this, owner, 50);

        int[] columnSizes = {12,3,50,3,12,20};
        for (int size: columnSizes) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(size);
            col.setHalignment(HPos.LEFT);
            getColumnConstraints().add(col);
        }

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.CENTER);
        getRowConstraints().add(rowConstraints);

        buildPane(owner, title);
    }

    private Button searchButton;

    private void buildPane(Pane owner, String title) {
        logger.debug("[CM_TITLE_SEARCH_PANE] Building {}", getId());

        int row = 0;
        // ROW (label and search button)
        Label titleLabel = new Label(title);
        add(titleLabel, 0, row, 3, 1);

        searchButton = new Button("Search");
        searchButton.setOnAction(event -> {
            if (owner instanceof PatchSearchPane patchSearchPane) {
                patchSearchPane.handleSearch();
            } else if (owner instanceof GroupSearchPane groupSearchPane) {
                groupSearchPane.handleSearch();
            }
        });

        add(searchButton, 4, row, 2, 1);

        logger.debug("[CM_TITLE_SEARCH_PANE] Built {}", getId());
    }

    public Button getSearchButton() {
        return searchButton;
    }
}

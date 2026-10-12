package creative.scenes.extract;

import creative.scenes.merge.util.YamahaStyleMerger;
import custom.components.ActionButton;
import custom.dialog.DialogFactory;
import custom.dialog.NameDialog;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;
import util.Registry;
import util.properties.PropertyContainer;
import util.properties.PropertyType;
import vendor.yamaha.YamahaStyleUploader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;

public class ExtractPane extends GridPane {
    private static final Logger logger = LoggerFactory.getLogger(ExtractPane.class);

    public ExtractPane() {
        super();

        setId("MergePane");
        setPadding(new Insets(5));
        setVgap(10);

        int totalColumns = 10;
        double percentagePerColumn = 100.0 / totalColumns; // Dit is ~8.3333%

        for (int i = 0; i < totalColumns; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(percentagePerColumn);
            col.setHalignment(HPos.LEFT);
            getColumnConstraints().add(col);
        }

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.CENTER);
        getRowConstraints().add(rowConstraints);

//        showPaneBorder(this, getColor("border", "red"));
        setPaneBackground(this);

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_EXTRACT_PANE] Building {}", getId());

        logger.debug("[CM_EXTRACT_PANE] Built {}", getId());
    }

    // ACCESSORS
}

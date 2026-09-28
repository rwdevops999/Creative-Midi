package creative.scenes.voice.components;

import creative.scenes.voice.GroupSearchPane;
import creative.scenes.voice.util.VoiceFinder;
import javafx.scene.control.Button;
import util.ApplicationInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.List;

import static util.Util.setPaneWidthAsPercentage;

public class GroupPane extends GridPane {
    private String selectedGroup = null;

    public GroupPane() {
        super();

        setId("GroupPane");

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
    }

    private GroupSearchPane parent;

    public GroupPane(GroupSearchPane owner) {
        super();

        parent = owner;

        setPaneWidthAsPercentage(this, owner, 50);

        buildPane();
    }

    private void buildPane() {
        int row = 0;

        // ROW (+, input, -)
        List<String> groupNames = VoiceFinder.getGroupsNames(ApplicationInfo.getInstance().getInstrumentProvider().getGroups(), "");
        ObservableList<String> groups = FXCollections.observableArrayList(groupNames);
        ComboBox<String> groupsComboBox = new ComboBox<>(groups);
        groupsComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedGroup = newValue;
            linkedSearchButton.fire();
        });
//        groupsComboBox.setOnAction(e -> linkedSearchButton.fire());

        groupsComboBox.setMaxWidth(Double.MAX_VALUE);

        add(groupsComboBox, 2, row, 3, 1);

    }

    public String getSelectedGroup() {
        if (selectedGroup != null) {
            return selectedGroup.toLowerCase();
        }

        return null;
    }

    private Button linkedSearchButton;
    public void addLinkedSearchButton(Button button) {
        linkedSearchButton = button;
    }

    // ACCESSOR
    public GroupSearchPane getGroupSearchPane() {
        return parent;
    }
}

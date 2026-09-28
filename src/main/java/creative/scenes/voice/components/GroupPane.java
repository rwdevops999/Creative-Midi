package creative.scenes.voice.components;

import creative.scenes.voice.GroupSearchPane;
import creative.scenes.voice.util.VoiceFinder;
import custom.components.SelectorPane;
import entity.device.DeviceInfo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import util.Registry;

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
        this();

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
        groupsComboBox.setMaxWidth(Double.MAX_VALUE);

        Registry.register("VoiceGroupSelector", groupsComboBox, (node, data) -> {
            List<String> newGroupNames = VoiceFinder.getGroupsNames(ApplicationInfo.getInstance().getInstrumentProvider().getGroups(), "");
            groupsComboBox.getItems().clear();
            ObservableList<String> newGroups = FXCollections.observableArrayList(newGroupNames);
            groupsComboBox.setItems(newGroups);

            // update excludes pane
            getGroupSearchPane().getVoiceSearchPane().getExcludesPane().updateExcludes();

            // clean results pane
            getGroupSearchPane().getVoiceSearchPane().getVoicePane().getVoiceSearchResultsPane().clear();
        });

        add(groupsComboBox, 2, row, 3, 1);
    }

    public String getSelectedGroup() {
        if (selectedGroup != null) {
            return selectedGroup;
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

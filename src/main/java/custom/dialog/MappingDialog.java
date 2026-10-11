package custom.dialog;

import creative.scenes.eventlist.data.EventKeyValue;
import creative.scenes.playlist.SongMappingsPane;
import creative.scenes.playlist.statemachine.PlaylistState;
import creative.scenes.sysex.SysexContainer;
import custom.Selector;
import entity.playlist.Mapping;
import entity.sysex.Sysex;
import eventhandlers.ChangeHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MappingDialog extends Dialog<Mapping> {
    public MappingDialog() {
        super();
    }

    private String action;

    private Pane parent;

    public MappingDialog(Pane owner, TableView<Mapping> table, boolean isUpdate, @Nullable Mapping currentMapping, List<Mapping> existingsMappings) {
        this();

        parent = owner;

        action = isUpdate ? "Update" : "Add";

        setTitle(action + " Mapping");
        setHeaderText("Select the incoming and outgoing mappings");

        buildDialogContent(isUpdate, currentMapping, existingsMappings);

        Optional<Mapping> result = showAndWait();

        result.ifPresent(rvalue -> {
            if (isUpdate) {
                int index = table.getItems().indexOf(currentMapping);
                if (index != -1) {
                    table.getItems().set(index, currentMapping);
                }
            } else if (table.getItems() != null) {
                Mapping mapping = new Mapping(result.get().getReceive(), result.get().getReply());
                table.getItems().add(mapping);
            }
        });
    }

    private void buildDialogContent(boolean isUpdate, Mapping currentMapping, List<Mapping> existingMappings) {
        ButtonType changeButtonType = new ButtonType(action, ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(changeButtonType, ButtonType.CANCEL);

        GridPane content = new GridPane();
        content.setHgap(10);
        content.setVgap(10);

        Label receiveLabel = new Label("Receive");
        content.add(receiveLabel, 0, 1);

        List<String> mappings = new ArrayList<>(SysexContainer.getSysexNames());
        List<String> removableSysexs = new ArrayList<>();

        for (String mapping : mappings) {
            Sysex sysex = SysexContainer.getSysex(mapping);
            if (sysex.getList().size() > 1) {
                removableSysexs.add(mapping);
            } else {
                if (! isUpdate) {
                    if (existingMappings.stream().anyMatch(m -> m.getReceive().equals(mapping))) {
                        removableSysexs.add(mapping);
                    }
                }
            }
        }

        mappings.removeAll(removableSysexs);
        Selector<String> receiveSelect = new Selector<>(null, mappings.stream().sorted().toList(), currentMapping != null ? currentMapping.getReceive() : null);
        content.add(receiveSelect, 1, 1);

        Label replyLabel = new Label("Reply");
        content.add(replyLabel, 0, 2);
        Selector<String> replySelect = new Selector<>(null, SysexContainer.getSysexNames(), currentMapping != null ? currentMapping.getReply() : null);
        content.add(replySelect, 1, 2);

        getDialogPane().setContent(content);

        setResultConverter(dialogButton -> {
            if (dialogButton == changeButtonType) {
                if (isUpdate) {
                    currentMapping.setReceive(receiveSelect.getSelectedItem());
                    currentMapping.setReply(replySelect.getSelectedItem());

                    return currentMapping;
                }

                return new Mapping(receiveSelect.getSelectedItem(), replySelect.getSelectedItem());
            }
            return null;
        });
    }

    // ACCESSSORS
    private SongMappingsPane getSongMappingsPane() {
        if (parent instanceof SongMappingsPane) {
            return (SongMappingsPane) parent;
        }

        return null;
    }
}

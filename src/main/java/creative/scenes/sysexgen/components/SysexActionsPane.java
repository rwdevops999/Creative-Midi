package creative.scenes.sysexgen.components;

import creative.scenes.sysex.SysexContainer;
import creative.scenes.sysex.util.SysexWriter;
import custom.dialog.DialogFactory;
import custom.dialog.NameDialog;
import entity.sysex.Sysex;
import entity.sysex.SysexContent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class SysexActionsPane extends HBox {
    public SysexActionsPane() {
        super();

        setPadding(new Insets(2));
        setSpacing(10);

        setAlignment(Pos.CENTER);
    }

    public SysexActionsPane(Supplier<List<String>> supplier) {
        this();

        buildPane(supplier);
    }

    private BooleanProperty dsDisabled = new SimpleBooleanProperty(true);
    private BooleanProperty dsDisabledExport = new SimpleBooleanProperty(true);

    private void buildPane(Supplier<List<String>> supplier) {

        SysexActionButton sysExSend = new SysexActionButton("sendButton", "send sysex", "send", e-> {
            List<String> sysexList = supplier.get();

            List<SysexContent> contents = sysexList.stream().map(s -> {
                SysexContent content = new SysexContent();
                content.setContent(s);
                return content;
            }).toList();

            Sysex sysExToSend = new Sysex("Send", contents);

            SysexWriter sysExWriter = new SysexWriter();
            sysExWriter.sendSysex(sysExToSend);
        });
        sysExSend.disableProperty().bind(dsDisabled);
        getChildren().add(sysExSend);

        SysexActionButton sysExAddToList = new SysexActionButton("addButton", "add sysex to list", "add-to-list", e-> {
            List<String> sysexList = supplier.get();

            List<SysexContent> contents = sysexList.stream().map(s -> {
                SysexContent content = new SysexContent();
                content.setContent(s);
                return content;
            }).toList();

            NameDialog nameDialog = new NameDialog("sysex", null);
            Optional<String> sysexName = nameDialog.showAndWait();

            sysexName.ifPresent(name -> {
                Sysex sysEx = new Sysex(name, contents);
                if (SysexContainer.containsSysex(name)) {
                    if (DialogFactory.renderConfirmationDialog("Sysex exists already", "Replace ?")) {
                        SysexContainer.replaceSysex(sysEx);
                        dsDisabledExport.set(false);
                    }
                } else {
                    SysexContainer.addSysex(sysEx);
                }
            });
        });
        sysExAddToList.disableProperty().bind(dsDisabled);
        getChildren().add(sysExAddToList);

        SysexActionButton sysExExportToFile = new SysexActionButton("exportButton", "export sysex to file", "export-to-file", e-> {
            SysexContainer.exportSysEx();
            dsDisabledExport.set(true);
        });
        sysExExportToFile.disableProperty().bind(dsDisabledExport);
        getChildren().add(sysExExportToFile);
    }

    public void setButtonsDisabled(boolean disabled) {
        dsDisabled.set(disabled);
    }
}

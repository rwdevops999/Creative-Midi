package creative.scenes.sysexgen;

import creative.scenes.sysex.SysexContainer;
import creative.scenes.sysex.util.SysexWriter;
import creative.scenes.sysexgen.component.SysexActionButton;
import custom.dialog.NameDialog;
import entity.sysex.Sysex;
import entity.sysex.SysexContent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class SysexGenActionsPane extends HBox {
    public SysexGenActionsPane() {
        super();

        setId("SysexGenActionsPane");

        setPadding(new Insets(2));
        setSpacing(10);
        setAlignment(Pos.CENTER);
    }

    public SysexGenActionsPane(Supplier<List<String>> supplier) {
        this();

        buildPane(supplier);
    }

    private void buildPane(Supplier<List<String>> supplier) {
        List<String> sysexList = supplier.get();

        SysexActionButton sysexSend = new SysexActionButton("sendButton", "send sysex", "send", e-> {
            List<SysexContent> contents = sysexList.stream().map(s -> {
                SysexContent content = new SysexContent();
                content.setContent(s);
                return content;
            }).toList();

            Sysex sysexToSend = new Sysex("Send", contents);

            SysexWriter sysExWriter = new SysexWriter();
            sysExWriter.sendSysex(sysexToSend);
        });
        getChildren().add(sysexSend);

        SysexActionButton sysexAddToList = new SysexActionButton("addButton", "add sysex to list", "add-to-list", e-> {
            List<SysexContent> contents = sysexList.stream().map(s -> {
                SysexContent content = new SysexContent();
                content.setContent(s);
                return content;
            }).toList();

            NameDialog nameDialog = new NameDialog("sysex", null);
            Optional<String> sysexName = nameDialog.showAndWait();


            sysexName.ifPresent(name -> {
                Sysex sysEx = new Sysex(name, contents);
                SysexContainer.addSysex(sysEx);
            });
        });
        getChildren().add(sysexAddToList);

        SysexActionButton sysexExportToFile = new SysexActionButton("exportButton", "export sysex to file", "export-to-file", e-> {
            SysexContainer.exportSysEx();
        });
        getChildren().add(sysexExportToFile);

        sysexSend.setDisable(sysexList.isEmpty());
        sysexAddToList.setDisable(sysexList.isEmpty());
        sysexExportToFile.setDisable(sysexList.isEmpty());
    }

    public void setEnables(Supplier<List<String>> supplier) {
        getChildren().clear();
        buildPane(supplier);
    }
}

package creative.scenes.sysex;

import creative.scenes.sysex.component.SysexActionButton;
import custom.dialog.NameDialog;
import entity.sysex.Sysex;
import entity.sysex.SysexContent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ActionsPane extends HBox {
    public ActionsPane() {
        super();

        setPadding(new Insets(2));
        setSpacing(10);
        setAlignment(Pos.CENTER);
    }

    public ActionsPane(Supplier<List<String>> supplier) {
        this();

        buildPane(supplier);
    }

    private void buildPane(Supplier<List<String>> supplier) {
        SysexActionButton sysExSend = new SysexActionButton("sendButton", "send sysex", "send", e-> {
            List<String> sysexList = supplier.get();

            List<SysexContent> contents = sysexList.stream().map(s -> {
                SysexContent content = new SysexContent();
                content.setContent(s);
                return content;
            }).toList();

            Sysex sysExToSend = new Sysex("Send", contents);

// TODO            SysExWriter sysExWriter = new SysExWriter();
//            sysExWriter.sendSysex(sysExToSend);
        });
        getChildren().add(sysExSend);

        SysexActionButton sysExAddToList = new SysexActionButton("addButton", "add sysex to list", "add-to-list", e-> {
            List<String> sysexList = supplier.get();

            List<SysexContent> contents = sysexList.stream().map(s -> {
                SysexContent content = new SysexContent();
                content.setContent(s);
                return content;
            }).toList();

            NameDialog nameDialog = new NameDialog();
            Optional<String> sysexName = nameDialog.showAndWait();

            sysexName.ifPresent(name -> {
                Sysex sysEx = new Sysex(name, contents);
// TODO                SysExContainer.addSysex(sysEx);
            });
        });
        getChildren().add(sysExAddToList);

        SysexActionButton sysExExportToFile = new SysexActionButton("exportButton", "export sysex to file", "export-to-file", e-> {
// TODO            SysExContainer.exportSysEx();
        });
        getChildren().add(sysExExportToFile);
    }
}

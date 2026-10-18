package creative.scenes.sysexgen2;

import creative.scenes.sysexgen2.entity.Event;
import javafx.geometry.Insets;
import javafx.scene.control.TabPane;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SysexGenParametersPane extends TabPane {
    public SysexGenParametersPane() {
        super();
    }

    public SysexGen2Pane parent;
//    private List<InputBlock> parameterBlockList = new ArrayList<>();

    private static final int TAB_SIZE = 10;

    public SysexGenParametersPane(SysexGen2Pane owner) {
        this();

        parent = owner;

        setPadding(new Insets(5));

        this.setPrefWidth(100);
        this.setPrefHeight(100);

        buildTabs();
    }

    private void buildTabs() {
        getTabs().clear();

/*        int numtabs = list.size() / TAB_SIZE;
        if (! list.isEmpty()) {
            numtabs++;
        } */
    }

    private void handleEvent(Event event) {
        if (event != null) {
            List<String> sysexs = event.complete(new ArrayList<>(event.createSysexStrings()));


            System.out.println("SYSEXS contains the list of results");
//            showSysexResult
        }
    }

    public void setEvent (Event eventToProcess) {
        System.out.println("PROCESSING: " + eventToProcess.getName());

        // Parameters must be created here

        handleEvent(eventToProcess);
    }

    // ACCESSORS
    public SysexGen2Pane getSysexGen2Pane() {
        return parent;
    }
}

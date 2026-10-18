package creative.scenes.sysexgen2;

import creative.scenes.sysexgen2.entity.Event;
import creative.scenes.sysexgen2.entity.EventParameter;
import creative.scenes.sysexgen2.entity.InputBlock;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static util.Util.setPaneHeightAsPercentage;

public class SysexGenParametersPane extends TabPane {
    public SysexGenParametersPane() {
        super();
    }

    public SysexGen2Pane parent;
    private List<InputBlock> parameterBlocks = new ArrayList<>();

    private static final int TAB_SIZE = 10;

    public SysexGenParametersPane(SysexGen2Pane owner) {
        this();

        parent = owner;

        setPadding(new Insets(5));

        buildTabs(parameterBlocks);
    }

    private void buildTabs(List<InputBlock> list) {
        getTabs().clear();

        int numtabs = list.size() / TAB_SIZE;
        if (! list.isEmpty()) {
            numtabs++;
        }

        for (int i = 0; i < numtabs; i++) {
            List<InputBlock> partiallyList = list.stream().filter(ib -> ib.getParameter() != null).skip((long) i * TAB_SIZE).limit(TAB_SIZE).toList();

            Tab tab = new Tab("Parameters " + (i + 1));
            tab.setContent(createContent(partiallyList));
            getTabs().add(tab);
        }
    }

    private Consumer<InputBlock> consumer = new  Consumer<InputBlock>() {
        @Override
        public void accept(InputBlock inputBlock) {
/*            if (inputBlock.getInputValues() != null) {
                List<String> sysexList = master.generateSysEx(parameterBlockList);
                parent.getSysexResultPane().renderSysex(sysexList);
            } */
        }
    };

    private VBox createContent(List<InputBlock> list) {
        VBox pane = new VBox();

/*        if (! list.isEmpty()) {
            for (InputBlock inputBlock : list) {
                if (inputBlock.getParameter() != null && ! inputBlock.getParameter().equals("Type")) {
                    pane.getChildren().add(new ParameterPane(inputBlock, consumer));
                }
            }
*/

        return pane;
    }

    public void setEvent(Event event) {
        if (event != null) {
            List<String> sysexs = event.complete(new ArrayList<>(event.createSysexStrings()));

            List<InputBlock> inputBlocks = new ArrayList<>();

            // What ?

            // - Parameters (do not include type parameters)
            if (event.hasParameters()) {
                // TODO Pass here the parameter
                InputBlock inputBlock = createInputBlockFrom(null);
                inputBlocks.add(inputBlock);
            }

            this.parameterBlocks = inputBlocks;

            if (!this.parameterBlocks.isEmpty()) {

            } else {
                showSysexResult(event);
            }
        }
    }

    private InputBlock createInputBlockFrom(EventParameter parameter) {
        // TODO create this function
        return null;
    }

    public void showSysexResult(Event event) {
        getSysexGen2Pane().getSysexGenResultPane().showSysex(event);
    }

    // ACCESSORS
    public SysexGen2Pane getSysexGen2Pane() {
        return parent;
    }
}

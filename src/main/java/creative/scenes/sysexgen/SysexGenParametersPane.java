package creative.scenes.sysexgen;

import creative.scenes.sysexgen.component.ParameterPane;
import entity.sysex.InputBlock;
import entity.sysex.Master;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setControlBackground;

public class SysexGenParametersPane extends TabPane {
    private static final Logger logger = LoggerFactory.getLogger(SysexGenParametersPane.class);

    private static final int TAB_SIZE = 10;

    public SysexGenParametersPane() {
        super();

        setId("SysExGenParametersPane");

        setPadding(new Insets(5));
    }

    private SysexGenPane parent;

    public SysexGenParametersPane(SysexGenPane owner) {
        parent = owner;

//        showPaneBorder(this, getColor("border", "blue"));

        this.setPrefWidth(100);
        this.setPrefHeight(100);

        setControlBackground(this);

        buildPane();
    }

    private List<InputBlock> parameterBlockList = new ArrayList<>();

    private void buildPane() {
        logger.debug("[CM_SYSEX_GEN_PARAMETERS_PANE] Building {}", getId());

        buildTabs(parameterBlockList);

        logger.debug("[CM_SYSEX_GEN_PARAMETERS_PANE] Built {}", getId());
    }

    private void buildTabs (List<InputBlock> list) {
        list = list.stream().filter(ib -> ib.getParameter() != null && ! ib.getParameter().equals("Type")).toList();

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

    private Master master;

    private Consumer<InputBlock> consumer = new  Consumer<InputBlock>() {
        @Override
        public void accept(InputBlock inputBlock) {
            if (inputBlock.getInputValues() != null) {
                List<String> sysexList = master.generateSysEx(parameterBlockList);
                parent.getSysexGenResultPane().renderSysex(sysexList);
            }
        }
    };

    private VBox createContent(List<InputBlock> list) {
        VBox pane = new VBox();

        if (! list.isEmpty()) {
            for (InputBlock inputBlock : list) {
                if (inputBlock.getParameter() != null && ! inputBlock.getParameter().equals("Type")) {
                    pane.getChildren().add(new ParameterPane(inputBlock, consumer));
                }
            }
        }

        return pane;
    }

    public void setParameters (Master master, List<InputBlock> list) {
        this.master = master;
        this.parameterBlockList = list;

        buildTabs(parameterBlockList);
    }

    public void clearParameters () {
        this.master = master;
        this.parameterBlockList = new ArrayList<>();

        buildTabs(parameterBlockList);
    }

    // ACCESSORS
    public SysexGenPane getSysexGenPane() {
        return parent;
    }
}


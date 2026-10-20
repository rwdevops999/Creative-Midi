package creative.scenes.sysexgen;

import creative.scenes.sysexgen.components.ParameterPane;
import creative.scenes.sysexgen.data.ParameterType;
import creative.scenes.sysexgen.entity.*;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SysexGenParametersPane extends TabPane {
    public SysexGenParametersPane() {
        super();
    }

    public SysexGenPane parent;
    private List<InputBlock> parameterBlocks = new ArrayList<>();

    private static final int TAB_SIZE = 8;

    public SysexGenParametersPane(SysexGenPane owner) {
        this();

        parent = owner;

        setPadding(new Insets(10));

        buildTabs(parameterBlocks);
    }

    private void buildTabs(List<InputBlock> list) {
        getTabs().clear();

        int numTabs = list.size() / TAB_SIZE;
        if (! list.isEmpty()) {
            numTabs++;
        }

        for (int i = 0; i < numTabs; i++) {
            List<InputBlock> partiallyList = list.stream().filter(ib -> ib.getParameter() != null).skip((long) i * TAB_SIZE).limit(TAB_SIZE).toList();

            Tab tab = new Tab("Parameters " + (i + 1));
            tab.setContent(createContent(partiallyList));
            getTabs().add(tab);
        }
    }

    private final Consumer<InputBlock> consumer = inputBlock -> {
        inputBlock.getParameter().setValue(inputBlock.getValue());
        showSysexResult(inputBlock.getEvent());
    };

    private VBox createContent(List<InputBlock> list) {
        VBox pane = new VBox();
        pane.setPadding(new Insets(10));

        if (! list.isEmpty()) {
            for (InputBlock inputBlock : list) {
                if (inputBlock.getParameter() != null && !inputBlock.getParameter().getParameterType().equals(ParameterType.TYPE)) {
                    pane.getChildren().add(new ParameterPane(inputBlock, consumer));
                }
            }
        }

        return pane;
    }

    public void setEvent(Event event) {
        if (event != null) {
            List<InputBlock> inputBlocks = new ArrayList<>();

            // - Parameters (do not include type parameters)
            if (event.hasParameters()) {
                for (EventParameter parameter : event.getParameters()) {
                    if (!ParameterType.TYPE.equals(parameter.getParameterType())) {
                        inputBlocks.addAll(handleParameter(parameter, event));
                    }
                }
            }

            this.parameterBlocks = inputBlocks;

            if (!this.parameterBlocks.isEmpty()) {
                buildTabs(this.parameterBlocks);
            }

            showSysexResult(event);
        } else {
            getTabs().clear();
            getSysexGenPane().getSysexGenResultPane().showSysex(new ArrayList<>());
        }
    }

    public void showSysexResult(Event event) {
        getSysexGenPane().getSysexGenResultPane().showSysex(event);
    }

    private List<InputBlock> handleParameter(EventParameter parameter, Event event) {
        List<InputBlock> result = new ArrayList<>();

        if (ParameterType.PARAMETER.equals(parameter.getParameterType())) {
            int parameterID = parameter.getParameterId();
            EventType eventType = event.findEventTypeFromDefaultValue();
            if (eventType != null) {
                ParameterTable pTable = eventType.getParameterTable();
                if (pTable != null) {
                    ParametersTableEntry entry = pTable.getEntryOfParameterId(parameterID);
                    if (entry != null) {
                        InputBlock ib = new InputBlock();

                        ib.setEvent(event);
                        ib.setParameter(parameter);
                        ib.setDataTable(entry.getDataTable());
                        ib.setValue(new DataBlock(entry.getDefaultValue()));
                        ib.setDescription(entry.getDescription());
                        ib.setLabel(entry.getLabel());
                        ib.setRange(entry.getRange());
                        result.add(ib);
                    }
                }
            }
        } else if (ParameterType.FREE.equals(parameter.getParameterType())) {
            if (! parameter.isIgnore()) {
                if (parameter.getDisplayBlock() != null) {
                    InputBlock ib = new InputBlock();

                    ib.setEvent(event);

                    ib.setParameter(parameter);
                    ib.setDataTable(null);
                    ib.setValue(parameter.getDisplayBlock().getDefaultValue());
                    ib.setDescription(parameter.getDisplayBlock().getDescription());
                    ib.setLabel(parameter.getDisplayBlock().getLabel());
                    ib.setRange(parameter.getDisplayBlock().getRange());
                    result.add(ib);
                }
            }
        } else if (ParameterType.LINKED.equals(parameter.getParameterType())) {
            if (parameter.getDisplayBlock() != null) {
                InputBlock ib = new InputBlock();

                ib.setEvent(event);

                ib.setParameter(parameter);
                ib.setDataTable(null);
                ib.setValue(parameter.getDisplayBlock().getDefaultValue());
                ib.setDescription(parameter.getDisplayBlock().getDescription());
                ib.setLabel(parameter.getDisplayBlock().getLabel());
                ib.setRange(parameter.getDisplayBlock().getRange());
                result.add(ib);
            }
        }

        return result;
    }

    // ACCESSORS
    public SysexGenPane getSysexGenPane() {
        return parent;
    }
}

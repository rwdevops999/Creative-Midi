package creative.scenes.sysexgen2;

import creative.scenes.sysexgen2.components.ParameterPane;
import creative.scenes.sysexgen2.data.ParameterType;
import creative.scenes.sysexgen2.definition.SysexGen2Definitions;
import creative.scenes.sysexgen2.entity.*;
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

    private static final int TAB_SIZE = 8;

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
            inputBlock.getParameter().setValue(inputBlock.getValue());
            showSysexResult(inputBlock.getEvent());
        }
    };

    private VBox createContent(List<InputBlock> list) {
        VBox pane = new VBox();

        if (! list.isEmpty()) {
            for (InputBlock inputBlock : list) {
                if (inputBlock.getParameter() != null && !inputBlock.getParameter().equals("Type")) {
                    pane.getChildren().add(new ParameterPane(inputBlock, consumer));
                }
            }
        }

        return pane;
    }

    private int checkedParameters = 0;
    private int errorParameters = 0;
    private int unhandledParameters = 0;

    public void setEvent(Event event) {
/*
        checkedParameters = 0;
        errorParameters = 0;
        unhandledParameters = 0;

        System.out.println("CHECKING INPUT");
        checkAllParameters(SysexGen2Definitions.getRootEvent());

        System.out.println("CHECKED [" + checkedParameters + "] Parameters => [" + errorParameters + "] errors, [" + unhandledParameters + "] unhandled");
*/

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

            System.out.println("CHECK");

            this.parameterBlocks = inputBlocks;

            if (!this.parameterBlocks.isEmpty()) {
                buildTabs(this.parameterBlocks);
            }

            showSysexResult(event);
        }
    }

    private InputBlock createInputBlockFrom(EventParameter parameter) {
        // TODO create this function
        return null;
    }

    public void showSysexResult(Event event) {
        getSysexGen2Pane().getSysexGenResultPane().showSysex(event);
    }

    private List<InputBlock> checkAllParameters(Event event) {
        System.out.println("CHECKING: " + event.getName());

        List<InputBlock> result = new ArrayList<>();

        if (event.hasChildren()) {
            for (Event child :  event.getChildren()) {
                result.addAll(checkAllParameters(child));
            }
        }

        if (event.hasParameters()) {
            for (EventParameter parameter : event.getParameters()) {
                result.addAll(handleParameter(parameter, event));
            }
        }

        return result;
    }

    private List<InputBlock> handleParameter(EventParameter parameter, Event event) {
        List<InputBlock> result = new ArrayList<>();

        checkedParameters++;
        System.out.println("CHECK PARAMETER [" + parameter.getParameterType().name() + "]: " + parameter.getName());
        if (ParameterType.PARAMETER.equals(parameter.getParameterType())) {
            System.out.println("PARAMETER is a NORMAL PARAMETER");

            int parameterID = parameter.getParameterId();
            if (parameterID == 2) {
                System.out.println("Check");
            }
            EventType eventType = event.findEventTypeFromDefaultValue();
            if (eventType == null) {
                errorParameters++;
                System.out.println("ERROR Event of parameter has no Event Type");
            } else {
                ParameterTable pTable = eventType.getParameterTable();
                if (pTable == null) {
                    errorParameters++;
                    System.out.println("ERROR EventType has no parameter Table");
                } else {
                    ParametersTableEntry entry = pTable.getEntryOfParameterId(parameterID);
                    if (entry == null) {
                        System.out.println("(Is Possible) There is no entry in ParameterTable " + pTable.getIdentifier()  + " for parameter with ID = " + parameterID);
                    } else {
                        InputBlock ib = new InputBlock();

                        ib.setEvent(event);
                        ib.setParameter(parameter);
                        ib.setDataTable(entry.getDataTable());
                        ib.setValue(new DataBlock(entry.getDefaultValue()));
                        ib.setDescription(entry.getDescription());
                        ib.setLabel(entry.getLabel());
                        ib.setRange(entry.getRange());
                        result.add(ib);
                        System.out.println("VALID: Parameter is OK");
                    }
                }
            }
        } else if (ParameterType.FREE.equals(parameter.getParameterType())) {
            System.out.println("PARAMETER is a FREE PARAMETER");

            if (! parameter.isIgnore()) {
                if (parameter.getDisplayBlock() == null) {
                    errorParameters++;
                    System.out.println("ERROR: Parameter has no display block");
                } else {
                    InputBlock ib = new InputBlock();

                    ib.setEvent(event);

                    ib.setParameter(parameter);
                    ib.setDataTable(null);
                    ib.setValue(parameter.getDisplayBlock().getDefaultValue());
                    ib.setDescription(parameter.getDisplayBlock().getDescription());
                    ib.setLabel(parameter.getDisplayBlock().getLabel());
                    ib.setRange(parameter.getDisplayBlock().getRange());
                    result.add(ib);
                    System.out.println("VALID: Parameter is OK");
                }
            } else {
                unhandledParameters++;
            }
        } else if (ParameterType.LINKED.equals(parameter.getParameterType())) {
            System.out.println("PARAMETER is a LINKED PARAMETER");

            if (parameter.getDisplayBlock() == null) {
                errorParameters++;
                System.out.println("ERROR: Parameter has no display block");
            } else {
                InputBlock ib = new InputBlock();

                ib.setEvent(event);

                ib.setParameter(parameter);
                ib.setDataTable(null);
                ib.setValue(parameter.getDisplayBlock().getDefaultValue());
                ib.setDescription(parameter.getDisplayBlock().getDescription());
                ib.setLabel(parameter.getDisplayBlock().getLabel());
                ib.setRange(parameter.getDisplayBlock().getRange());
                result.add(ib);
                System.out.println("VALID: Parameter is OK");
            }
        } else {
            unhandledParameters++;
            System.out.println("PARAMETER is a TYPE PARAMETER => not handled");
        }

        return result;
    }

    // ACCESSORS
    public SysexGen2Pane getSysexGen2Pane() {
        return parent;
    }
}

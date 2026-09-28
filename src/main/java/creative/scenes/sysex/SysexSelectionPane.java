package creative.scenes.sysex;

import entity.sysex.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;

public class SysexSelectionPane extends GridPane {
    private static final Logger logger = LoggerFactory.getLogger(SysexSelectionPane.class);

    public SysexSelectionPane() {
        super();

        setId("SysexSelectionPane");

        setVgap(5);
        setPadding(new Insets(5));
    }

    private SysexPane parent;

    public SysexSelectionPane(SysexPane owner) {
        this();

        parent = owner;

//        showPaneBorder(this, getColor("border", "green"));

        setPaneBackground(this);

        int totalColumns = 10;
        double percentagePerColumn = 100.0 / totalColumns; // Dit is ~8.3333%

        for (int i = 0; i < totalColumns; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(percentagePerColumn);
            col.setHalignment(HPos.LEFT);
            getColumnConstraints().add(col);
        }

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.CENTER);
        getRowConstraints().add(rowConstraints);

        buildPane();
    }

    private Master selectedMaster;
    private List<String> availableCategories = new ArrayList<>();

    private void buildPane() {
        logger.debug("[CM_SYSEXSELECTION_PANE] Building {}", getId());

        getChildren().clear();

        int row = 0;
        // ROW0 (master selection pane)
        Label sysexLabel = new Label("SysEx");
        add(sysexLabel, 0, row);

        ComboBox<Master> masterComboBox = new ComboBox<>();
        masterComboBox.getItems().addAll(SysexGenHelper.getMasters());
        masterComboBox.setConverter(new StringConverter<Master>() {
            @Override
            public String toString(Master master) {
                // Return the field to display
                return (master != null) ? master.getName() : "";
            }

            @Override
            public Master fromString(String string) {
                // Not needed unless the ComboBox is editable
                return null;
            }
        });

        if (selectedMaster != null) {
            masterComboBox.setValue(selectedMaster);
        }

        masterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (!newValue.equals(selectedMaster)) {
                    selectedMaster = newValue;
                    availableCategories = selectedMaster.getCategories();

                    processMaster();

                    buildPane();
                }
            }
        });
        add(masterComboBox, 1, row, 2, 1);

        if (!availableCategories.isEmpty()) {
            row++;

            Label categoryLabel = new Label("Category");
            add(categoryLabel, 0, row);

            ComboBox<String> categoriesComboBox = new ComboBox<>();
            categoriesComboBox.getItems().addAll(availableCategories);
            add(categoriesComboBox, 1, row, 2, 1);
            categoriesComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    if (!newValue.equals(selectedCategory)) {
                        selectedCategory = newValue;
                        selectedTypeBlock = null;

                        availableTypes = selectedMaster.getTypesOfCategory(selectedCategory);

                        buildPane();
                    }
                }
            });

            if (selectedCategory != null) {
                categoriesComboBox.setValue(selectedCategory);
            }

            if (!availableTypes.isEmpty()) {
                Label typeLabel = new Label("Type");
                add(typeLabel, 3, row);

                ComboBox<TypeBlock> typesComboBox = new ComboBox<>();
                typesComboBox.getItems().addAll(availableTypes);
                typesComboBox.setConverter(new StringConverter<TypeBlock>() {
                    @Override
                    public String toString(TypeBlock type) {
                        // Return the field to display
                        return (type != null) ? type.getType() : "";
                    }

                    @Override
                    public TypeBlock fromString(String string) {
                        // Not needed unless the ComboBox is editable
                        return null;
                    }
                });

                typesComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        if (! newValue.equals(selectedTypeBlock)) {
                            selectedTypeBlock = newValue;
                            processType();
                        }
                    }
                });
                add(typesComboBox, 4, row, 2, 1);

                if (selectedTypeBlock != null) {
                    typesComboBox.setValue(selectedTypeBlock);
                }

            }
        }

        logger.debug("[CM_SYSEXSELECTION_PANE] Built {}", getId());
    }

    private List<InputBlock> inputBlockList = new ArrayList<>();
    private TypeBlock selectedTypeBlock;
    private String selectedCategory;
    private List<TypeBlock> availableTypes = new ArrayList<>();

    private void processMaster() {
        inputBlockList = new ArrayList<>();

        selectedTypeBlock = selectedMaster.getDefaultTypeBlock();

        InputBlock.InputBlockBuilder inputBlockBuilder = new InputBlock.InputBlockBuilder();

        for (ParameterChangeTable pct : selectedMaster.getParameterChangeTable()) {
            String parameter = null;
            String display = null;
            boolean useMsbLsb = false;
            Integer parameterId = null;

            if (pct.getParameter().equals("Type")) {
                parameter = "Type";
                useMsbLsb = true;
                if (pct.hasDefaultValues()) {
                    DataBlock defaultValues = pct.getDefaultValues();

                    // default values determine selectedCategory and selectedType;
                    selectedTypeBlock = selectedMaster.findTypeBlockByDefaultValues(defaultValues.getB1(), defaultValues.getB2());
                    selectedCategory = selectedTypeBlock.getCategory();
                    availableTypes = selectedMaster.getTypesOfCategory(selectedCategory);
                }
            } else if (pct.getParameter().startsWith("Parameter")) {
                String[] split = pct.getParameter().split(" ");
                parameterId = Integer.parseInt(split[1]);
            } else {
                parameter =pct.getParameter();
                display = pct.getDescription();
            }

            int min = 0x0;
            int max = 0x7F;

            if (pct.hasRange()) {
                min = pct.getRangeValues().getB1();
                max = pct.getRangeValues().getB2();
            }

            inputBlockList.add(inputBlockBuilder.reset()
                    .withPosition(pct.getId())
                    .withNumBytes(pct.getNumBytes())
                    .withParameterId(parameterId)
                    .withParameter(parameter)
                    .withDisplay(display)
                    .withLsbMsb(useMsbLsb)
                    .withMinMax(min, max)
                    .build());
        }

        processType();
    }

    private void processType() {
        TypeBlock block = selectedTypeBlock;
        if (block != null) {
            ParameterBlock[] parameters = selectedTypeBlock.getParameters().getParameters();

            for (InputBlock inputBlock : inputBlockList) {
                if (inputBlock.getUseMsbLsb()) {
                    inputBlock.setInputValues(new DataBlock(block.getMsb(), block.getLsb()));
                } else {
                    Integer parameterId = inputBlock.getParameterId();
                    if (parameterId != null && parameters[parameterId] != null) {
                        inputBlock.setParameter(parameters[parameterId].getParameter());
                        inputBlock.setDisplay(parameters[parameterId].getDisplay());
                        inputBlock.setMin(parameters[parameterId].getMin());
                        inputBlock.setMax(parameters[parameterId].getMax());
                        if (inputBlockList.size() == 1) {
                            DataBlock db = new DataBlock(parameters[parameterId].getMin(), null);
                            inputBlock.setInputValues(db);
                        }
                        inputBlock.setRefTable(parameters[parameterId].getParametersTable());
                    }
                }
            }
        }

        transmitInputBlocks();
    }

    private void transmitInputBlocks() {
        if (! inputBlockList.isEmpty()) {
            List<String> sysexList = selectedMaster.generateSysEx(inputBlockList);
// TODO            getSysexPane().getSysexResultPane().renderSysex(sysexList);
// TODO            getSysexPane().getSysexParametersPane().setParameters(selectedMaster, inputBlockList);
        }
    }

    // ACCESSORS
    public SysexPane getSysexPane() {
        return parent;
    }
}

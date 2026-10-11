package creative.scenes.sysexgen;

import creative.scenes.sysexgen.entity.EventType;
import creative.scenes.sysexgen.entity.SubEvent;
import creative.scenes.sysexgen.entity.SysexRoot;
import entity.sysex.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
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

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;

public class SysexGenSelectionPane extends GridPane {
    private static final Logger logger = LoggerFactory.getLogger(SysexGenSelectionPane.class);

    public SysexGenSelectionPane() {
        super();

        setId("SysexGenSelectionPane");

        setVgap(5);
        setPadding(new Insets(5));
    }

    private SysexGenPane parent;

    public SysexGenSelectionPane(SysexGenPane owner) {
        this();

        parent = owner;

        showPaneBorder(this, getColor("border", "green"));

        setPaneBackground(this);

        int totalColumns = 32;
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

        prepareData();
        buildPane();
    }

    private BooleanProperty sectionVisible = new SimpleBooleanProperty(false);
    private BooleanProperty effectVisible = new SimpleBooleanProperty(false);
    private BooleanProperty categoryVisible = new SimpleBooleanProperty(false);
    private BooleanProperty typeVisible = new SimpleBooleanProperty(false);

    private void hideNodes(BooleanProperty ...properties) {
        for (BooleanProperty property : properties) {
            if (property.get()) {
                property.set(false);
            }
        }
    }

    private ObjectProperty<SubEvent> selectedSubEvent = new SimpleObjectProperty<>();

    private void prepareData() {
        selectedSubEvent.addListener((observable, oldValue, newValue) -> {
            System.out.println("SELECTED SUBEVENT = " + newValue.getName());
        });
    }

    private SubEvent selectedEffect = null;

    private void buildPane() {
        final ComboBox<SubEvent> sectionsComboBox = new ComboBox<>();
        final ComboBox<SubEvent> effectComboBox = new ComboBox<>();
        final ComboBox<String> categoryComboBox = new ComboBox<>();
        final ComboBox<EventType> typeComboBox = new ComboBox<>();

        getChildren().clear();

        int row = 0;
        // ROW0 (sysex selection pane)
        Label sysexLabel = new Label("SysEx");
        add(sysexLabel, 0, row, 2, 1);

        // We start with the master combo
        ComboBox<SysexRoot> sysexComboBox = new ComboBox<>();
        sysexComboBox.getItems().addAll(SysexGenDefinitions.getSysexs());
        sysexComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(SysexRoot sysex) {
                // Return the field to display
                return (sysex != null) ? sysex.getName() : "";
            }

            @Override
            public SysexRoot fromString(String string) {
                // Not needed unless the ComboBox is editable
                return null;
            }
        });

        sysexComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            sectionVisible.setValue(newValue != null);

            if (newValue != null) {
                System.out.println("SELECTED SYSEX = " + newValue.getName());
                // fill Sections Combo
                sectionsComboBox.getItems().clear();
                sectionsComboBox.getItems().addAll(newValue.getSubEvents());

                hideNodes(effectVisible, categoryVisible, typeVisible);
            }
        });

        add(sysexComboBox, 2, row, 6, 1);

        row++;
        Label sectionLabel = new Label("Section");
        sectionLabel.visibleProperty().bind(sectionVisible);
        add(sectionLabel, 0, row, 2, 1);

        sectionsComboBox.visibleProperty().bind(sectionVisible);
        sectionsComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(SubEvent subEvent) {
                // Return the field to display
                return (subEvent != null) ? subEvent.getName() : "";
            }

            @Override
            public SubEvent fromString(String string) {
                // Not needed unless the ComboBox is editable
                return null;
            }
        });

        sectionsComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("SELECTED SECTION = " + newValue.getName());
                effectVisible.set(newValue.hasChildren());
                if (newValue.hasChildren()) {
                    System.out.println("SECTION HAS CHILDREN");
                    effectComboBox.getItems().clear();
                    effectComboBox.getItems().addAll(newValue.getChildren());
                } else {
                    System.out.println("SECTION HAS NO CHILDREN");
                    selectedSubEvent.set(newValue);
                }

                hideNodes(categoryVisible, typeVisible);
            }
        });
        add(sectionsComboBox, 2, row, 7, 1);

        Label effectLabel = new Label("Effect");
        effectLabel.visibleProperty().bind(effectVisible);
        add(effectLabel, 10, row, 2, 1);

        effectComboBox.visibleProperty().bind(effectVisible);
        effectComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(SubEvent subEvent) {
                // Return the field to display
                return (subEvent != null) ? subEvent.getName() : "";
            }

            @Override
            public SubEvent fromString(String string) {
                // Not needed unless the ComboBox is editable
                return null;
            }
        });

        effectComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            categoryVisible.set(newValue != null);

            if (newValue != null) {
                System.out.println("SELECTED EFFECT = " + newValue.getName());
                selectedEffect = newValue;
                categoryComboBox.getItems().clear();
                categoryComboBox.getItems().addAll(newValue.getCategories());

                hideNodes(typeVisible);
            }
        });

        add(effectComboBox, 12, row, 4, 1);

        Label categoryLabel = new Label("Category");
        categoryLabel.visibleProperty().bind(categoryVisible);
        add(categoryLabel, 16, row, 2, 1);

        categoryComboBox.visibleProperty().bind(categoryVisible);
        categoryComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String string) {
                // Return the field to display
                return (string != null) ? string : "";
            }

            @Override
            public String fromString(String string) {
                // Not needed unless the ComboBox is editable
                return null;
            }
        });

        categoryComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            typeVisible.set(newValue != null);

            if (newValue != null) {
                System.out.println("SELECTED CATEGORY = " + newValue);
                typeComboBox.getItems().clear();
                typeComboBox.getItems().addAll(selectedEffect.getTypesOfCategory(newValue));
            }
        });

        add(categoryComboBox, 19, row, 4, 1);

        Label typeLabel = new Label("Type");
        typeLabel.visibleProperty().bind(typeVisible);
        add(typeLabel, 24, row, 2, 1);

        typeComboBox.visibleProperty().bind(typeVisible);
        typeComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(EventType eventType) {
                // Return the field to display
                return (eventType != null) ? eventType.getName() : "";
            }

            @Override
            public EventType fromString(String string) {
                // Not needed unless the ComboBox is editable
                return null;
            }
        });

        typeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("SELECTED TYPE = " + newValue.getName());
            }
        });

        add(typeComboBox, 26, row, 4, 1);
    }

/*
    private Master selectedMaster;
    private List<String> masterCategories = new ArrayList<>();

    private String selectedCategory;

    private void buildPane() {
        logger.debug("[CM_SYSEX_GEN_SELECTION_PANE] Building {}", getId());

        getChildren().clear();

        int row = 0;
        // ROW0 (master selection pane)
        Label sysexLabel = new Label("SysEx");
        add(sysexLabel, 0, row);

        // We start with the master combo
        ComboBox<Master> masterComboBox = new ComboBox<>();
        masterComboBox.getItems().addAll(SysexGenHelper_Old.getMasters());
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
                selectedMaster = newValue;

                processMaster();

                buildPane();
            }
        });
        add(masterComboBox, 1, row, 2, 1);

        // There are categories available
        if (! masterCategories.isEmpty()) {
            row++;

            // display categories combo filled with available categories
            Label categoryLabel = new Label("Category");
            add(categoryLabel, 0, row);

            ComboBox<String> categoriesComboBox = new ComboBox<>();
            categoriesComboBox.getItems().addAll(masterCategories);
            add(categoriesComboBox, 1, row, 2, 1);

            if (selectedCategory != null) {
                categoriesComboBox.setValue(selectedCategory);
            }

            categoriesComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                selectedCategory = newValue;
                processCategory();

                buildPane();
            });

            // There are also types available
            if (!categoryTypeBlocks.isEmpty()) {

                // display the types combo box filled with avaiable types
                Label typeLabel = new Label("Type");
                add(typeLabel, 3, row);

                ComboBox<TypeBlock> typesComboBox = new ComboBox<>();
                typesComboBox.getItems().addAll(categoryTypeBlocks);
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
                add(typesComboBox, 4, row, 2, 1);

                // if there is a type selected, select it again in the combo box
                if (selectedTypeBlock != null) {
                    typesComboBox.setValue(selectedTypeBlock);
                }

                // what if we change the type ?
                typesComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        if (! newValue.equals(selectedTypeBlock)) {
                            selectedTypeBlock = newValue;
                            processType();

                            buildPane();
                        }
                    }
                });
            }
        }

        logger.debug("[CM_SYSEX_GEN_SELECTION_PANE] Built {}", getId());
    }

    List<ParameterChangeTable> masterParameterChangeTable = new ArrayList<>();
    List<TypeBlock> masterTypeBlocks = new ArrayList<>();
    ParameterChangeTable defaultTypeParameterChangeTable = null;
    TypeBlock defaultTypeBlock = null;
    TypeBlock selectedTypeBlock = null;

    private void processMaster() {
        // get available categories
        masterCategories = selectedMaster.getCategories();
        masterParameterChangeTable = selectedMaster.getParameterChangeTable();
        masterTypeBlocks = selectedMaster.getTypeBlocks();

        if (! masterTypeBlocks.isEmpty()) {
            defaultTypeParameterChangeTable = masterParameterChangeTable.stream().filter(pct -> "Type".equals(pct.getParameter())).findFirst().orElse(null);
            if (defaultTypeParameterChangeTable != null) {
                // We have a default Parameter Type, So we need to find the according TypeBlock later in types
                int msb = defaultTypeParameterChangeTable.getDefaultValues().getB1();
                int lsb = defaultTypeParameterChangeTable.getDefaultValues().getB2();

                defaultTypeBlock = masterTypeBlocks.stream().filter(tb -> tb.getMsb() == msb && tb.getLsb() == lsb).findAny().orElse(null);
                selectedTypeBlock = defaultTypeBlock;
                selectedCategory = defaultTypeBlock.getCategory();
            } else {
                selectedTypeBlock = masterTypeBlocks.get(0);
                categoryParameterChangeTable = masterParameterChangeTable;
                selectedCategory = null;
            }
        }

        processCategory();
    }

    private List<ParameterChangeTable> categoryParameterChangeTable = new ArrayList<>();
    private List<TypeBlock> categoryTypeBlocks = new ArrayList<>();

    private void processCategory() {
        if (selectedCategory != null) {
            categoryTypeBlocks = masterTypeBlocks.stream().filter(t -> t.getCategory() != null && t.getCategory().equals(selectedCategory)).toList();

            categoryParameterChangeTable = selectedMaster.getParameterChangeTable();
            boolean needParameters = categoryTypeBlocks.stream().anyMatch(tb -> tb.getParameters() != null);
            if (! needParameters) {
                categoryParameterChangeTable = categoryParameterChangeTable.stream().filter(t -> t.getParameter().contains("Type")).toList();
            }

            if (defaultTypeBlock != null) {
                if (defaultTypeBlock.getCategory().equals(selectedCategory)) {
                    selectedTypeBlock = defaultTypeBlock;
                } else {
                    if (!categoryTypeBlocks.isEmpty()) {
                        selectedTypeBlock = categoryTypeBlocks.get(0);
                    }
                }
            }
        }

        processType();
    }

    private List<InputBlock> inputBlockList = new ArrayList<>();

    private void processType() {
        if (selectedTypeBlock != null) {
            inputBlockList = new ArrayList<>();

            InputBlock.InputBlockBuilder inputBlockBuilder = new InputBlock.InputBlockBuilder();
            for (ParameterChangeTable pct : categoryParameterChangeTable) {
                String parameter = null;
                String display = null;
                boolean useMsbLsb = false;
                Integer parameterId = null;

                if (pct.getParameter().equals("Type")) {
                    parameter = "Type";
                    useMsbLsb = true;
                } else if (pct.getParameter().startsWith("Parameter")) {
                    String[] split = pct.getParameter().split(" ");
                    parameterId = Integer.parseInt(split[1]);
                } else {
                    parameter = pct.getParameter();
                    display = pct.getDescription();
                }

                int min = 0x0;
                int max = 0x7F;

                if (pct.hasRange()) {
                    min = pct.getRangeValues().getB1();
                    max = pct.getRangeValues().getB2();
                }

                InputBlock inputBlock = inputBlockBuilder.reset()
                        .withPosition(pct.getId())
                        .withNumBytes(pct.getNumBytes())
                        .withParameterId(parameterId)
                        .withParameter(parameter)
                        .withDisplay(display)
                        .withLsbMsb(useMsbLsb)
                        .withMinMax(min, max)
                        .build();

                if (useMsbLsb) {
                    inputBlock.setInputValues(new DataBlock(selectedTypeBlock.getMsb(), selectedTypeBlock.getLsb()));
                } else {
                    if (parameterId != null && selectedTypeBlock.getParameters() != null) {
                        ParameterBlock[] parameters = selectedTypeBlock.getParameters().getParameters();

                        if (parameters[parameterId] != null) {
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

                inputBlockList.add(inputBlock);
            }
        }

        forwardInputBlocks();
    }

    private void forwardInputBlocks() {
        if (! inputBlockList.isEmpty()) {
            List<String> sysexList = selectedMaster.generateSysEx(inputBlockList);
            getSysexPane().getSysexGenResultPane().renderSysex(sysexList);
            getSysexPane().getSysexGenParametersPane().setParameters(selectedMaster, inputBlockList);
        }
    }
*/
    // ACCESSORS
    public SysexGenPane getSysexPane() {
        return parent;
    }
}

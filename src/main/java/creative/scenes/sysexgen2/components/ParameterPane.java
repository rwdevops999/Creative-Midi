package creative.scenes.sysexgen2.components;

import creative.scenes.sysexgen2.entity.DataBlock;
import creative.scenes.sysexgen2.entity.InputBlock;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ParameterPane extends GridPane {
    public ParameterPane() {
        super();
    }

    public ParameterPane(InputBlock inputBlock, Consumer<InputBlock> consumer) {
        this();

        setPadding(new Insets(1));

        int columnSize = 5;
        int columns = 100 / columnSize;

        for (int i = 0; i < columns; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(columnSize);
            col.setHalignment(HPos.LEFT);
            getColumnConstraints().add(col);
        }

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.CENTER);
        getRowConstraints().add(rowConstraints);

        buildPane(inputBlock, consumer);
    }

    private final BiFunction<Integer, Integer, DataBlock> inputCalculator = new BiFunction<Integer, Integer, DataBlock>() {
        @Override
        public DataBlock apply(Integer value, Integer numOfBytes) {
            if (numOfBytes == 1) {
                return new DataBlock(null, value);
            }

            int highByte = (value >>> 8) & 0xFF;
            int lowByte = value & 0xFF;

            return new DataBlock(highByte, lowByte);
        }
    };

    private void buildPane(InputBlock inputBlock, Consumer<InputBlock> consumer) {
        getChildren().clear();

        Label parameter = new Label(inputBlock.getLabel());
        add(parameter, 0, 0, 3, 1);

        if (inputBlock.getDescription() != null) {
            Label display = new Label("(" + inputBlock.getDescription() + ")");
            add(display, 4, 0, 4, 1);
        }

        StringProperty sp = new SimpleStringProperty();

        Spinner<Integer> spinner = new Spinner<>(inputBlock.getRange().getMin(), inputBlock.getRange().getMax(),inputBlock.getValue().getValue());
        spinner.getValueFactory().valueProperty().addListener((observable, oldValue, newValue) -> {
            if (inputBlock.getDataTable() != null) {
                String[] pt = inputBlock.getDataTable().getEntries();
                if (pt != null) {
                    int parameterId = inputBlock.getParameter().getParameterId();

                    Label refLabel = new Label(pt[spinner.getValueFactory().getValue()]);
                    refLabel.setId("refLabel" + parameterId);
                    if (getChildren().removeIf(node -> ("refLabel" + parameterId).equals(node.getId()))) {
                        add(refLabel, 11, 0, 3, 1);
                    }
                }
            }

//            inputBlock.setValue(spinner.getValueFactory().getValue());
            // Set The Value
            inputBlock.setValue(inputCalculator.apply(spinner.getValueFactory().getValue(), inputBlock.getParameter().getNumBytes()));
            consumer.accept(inputBlock);
        });
        add(spinner, 8, 0, 2, 1);

        if (inputBlock.getDataTable() != null && inputBlock.getDataTable().getEntries() != null) {
            String[] pt = inputBlock.getDataTable().getEntries();

            sp.set(pt[spinner.getValueFactory().getValue()]);
            Label refLabel = new Label(pt[spinner.getValueFactory().getValue()]);
            refLabel.setId("refLabel" + inputBlock.getParameter().getParameterId());

            refLabel.textProperty().bindBidirectional(sp);
            add(refLabel, 11, 0, 3, 1);
        }
    }
}
package creative.scenes.sysex.component;

import entity.sysex.DataBlock;
import entity.sysex.InputBlock;
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

        setPadding(new Insets(1));
    }

    public ParameterPane(InputBlock inputBlock, Consumer<InputBlock> consumer) {
        this();

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
                return new DataBlock(value, null);
            }

            int highByte = (value >>> 8) & 0xFF;
            int lowByte = value & 0xFF;

            return new DataBlock(highByte, lowByte);
        }
    };

    private void buildPane(InputBlock inputBlock, Consumer<InputBlock> consumer) {
        getChildren().clear();

        Label parameter = new Label(inputBlock.getParameter());
        add(parameter, 0, 0, 3, 1);

        if (inputBlock.getDisplay() != null) {
            Label display = new Label("(" + inputBlock.getDisplay() + ")");
            add(display, 4, 0, 4, 1);
        }

        StringProperty sp = new SimpleStringProperty();

        Spinner<Integer> spinner = new Spinner<>(inputBlock.getMin(), inputBlock.getMax(), inputBlock.getMin());
        spinner.getValueFactory().valueProperty().addListener((observable, oldValue, newValue) -> {
            if (inputBlock.getRefTable() != null) {
                String[] pt = inputBlock.getRefTable().getDataBlock();
                Label refLabel = new Label(pt[spinner.getValueFactory().getValue()]);
                refLabel.setId("refLabel"+inputBlock.getParameterId());
                if (getChildren().removeIf(node -> ("refLabel"+inputBlock.getParameterId()).equals(node.getId()))) {
                    add(refLabel, 11, 0, 3, 1);
                }
            }

            // Set The Value
            inputBlock.setInputValues(inputCalculator.apply(spinner.getValueFactory().getValue(), inputBlock.getNumBytes()));
            consumer.accept(inputBlock);
        });
        add(spinner, 8, 0, 2, 1);

        if (inputBlock.getRefTable() != null) {
            String[] pt = inputBlock.getRefTable().getDataBlock();

            sp.set(pt[spinner.getValueFactory().getValue()]);
            Label refLabel = new Label(pt[spinner.getValueFactory().getValue()]);
            refLabel.setId("refLabel"+inputBlock.getParameterId());

            refLabel.textProperty().bindBidirectional(sp);
            add(refLabel, 11, 0, 3, 1);
        }
    }
}

package creative.scenes.test.test2;

import communication.CommunicationModel;
import entity.midi.NoteEntity;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ColorScheme;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static util.Util.calcNote;
import static util.Util.setPaneBackground;

public class Test2Pane extends HBox {
    private static final Logger logger = LoggerFactory.getLogger(Test2Pane.class);

    private record NamedColor(String name, Color color) {}

    private ObjectProperty<Paint> textFill = new SimpleObjectProperty<>(Color.RED);

    private List<NamedColor> getAllColors() {
        List<NamedColor> colors = new ArrayList<>();
        for (Field field : Color.class.getFields()) {
            if (Modifier.isStatic(field.getModifiers()) && field.getType() == Color.class) {
                try {
                    colors.add(new NamedColor(field.getName(), (Color) field.get(null)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return colors;
    }

    public Test2Pane() {
        super();

        setId("Test2");
        CommunicationModel.setStatus("Running Test2");

        setSpacing(10);
        setPaneBackground(this);

        runTest();
    }

    private void runTest() {
        logger.debug("[CM_TEST1_PANE] Executing {}", getId());

        // The testpane
        VBox colorTestPane = new VBox();
        Label testLabel = new Label("This is the testlabel");
        testLabel.textFillProperty().bind(textFill);
        colorTestPane.getChildren().add(testLabel);

        // The color select pane
        VBox colorSelectPane = new VBox();
        ObservableList<NamedColor> observableColors = FXCollections.observableArrayList(getAllColors());

        ComboBox<NamedColor> colorsBox = new ComboBox<>();
        colorsBox.setItems(observableColors);
        colorsBox.setConverter(new StringConverter<NamedColor>() {
            @Override
            public String toString(NamedColor color) {
                if (color == null) {
                    return null;
                }
                // Define how you want the text to appear
                return color.name;
            }

            @Override
            public NamedColor fromString(String string) {
                // Not strictly needed unless the ComboBox is editable
                return null;
            }
        });

        colorsBox.setOnAction(event -> {
            NamedColor selectedColor = colorsBox.getValue();
            System.out.println("Selected color: " + selectedColor.name);
            textFill.set(selectedColor.color);
//            textFill.setValue(selectedColor.color);
        });

        colorSelectPane.getChildren().add(colorsBox);

        getChildren().addAll(colorTestPane, colorSelectPane);

        logger.debug("[CM_TEST1_PANE] Executed {}", getId());
    }
}

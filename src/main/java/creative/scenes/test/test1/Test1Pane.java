package creative.scenes.test.test1;

import communication.CommunicationModel;
import creative.scenes.midi.convertor.MinPlusConvertor;
import creative.scenes.midi.convertor.PanningConvertor;
import creative.scenes.midi.convertor.SplitConvertor;
import creative.scenes.midi.data.ByteType;
import custom.components.OnOffSwitch;
import custom.components.midi.MidiEntrySelect;
import entity.midi.NoteEntity;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Util;
import util.properties.PropertyContainer;
import util.properties.PropertyType;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static util.ColorScheme.getColor;

public class Test1Pane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(Test1Pane.class);
    public Test1Pane() {
        super();

        setId("Test1");
        CommunicationModel.setStatus("Running Test1");

        setupTest();
        runTest();
    }

    private void setupTest() {
    }

    private void runTest() {
        logger.debug("[CM_TEST1_PANE] Executing {}", getId());

        MidiEntrySelect midiEntrySelect = new MidiEntrySelect(1);
        getChildren().add(midiEntrySelect);

        logger.debug("[CM_TEST1_PANE] Executed {}", getId());
    }
}

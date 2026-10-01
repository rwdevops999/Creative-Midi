package creative.scenes.playlist;

import custom.components.playlist.ScaleSelector;
import custom.components.playlist.SignatureSelector;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;
import static util.Util.setPaneHeightAsPercentage;

public class SongDetailsPane extends GridPane {
    private static final Logger logger = LoggerFactory.getLogger(SongDetailsPane.class);

    public SongDetailsPane() {
        super();

        setId("SongDetailsPane");

        setPadding(new Insets(5, 5, 10, 5));
        setVgap(5);

        int[] columnSizes = {20,5,16,5,16,38};
        for (int size: columnSizes) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(size);
            col.setHalignment(HPos.LEFT);
            getColumnConstraints().add(col);
        }
    }

    private PlaylistDetailsPane parent;
    public SongDetailsPane(PlaylistDetailsPane owner) {
        this();

        setPaneHeightAsPercentage(this, owner, 40);
        setPaneBackground(this);

        parent = owner;

//        showPaneBorder(this, getColor("border", "red", null));

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_SONG_DETAILS_PANE] Building {}", getId());

        int row = -1;

        // ROW 1 (Song Id)
        row++;
        setSongId(row);

        // ROW 2 (Song Name)
        row++;
        setSongName(row);

        // ROW 3 (Performer)
        row++;
        setPerformer(row);

        // ROW 4 (Tempo)
        row++;
        setTempo(row);

        // ROW 5 (Scale)
        row++;
        setScale(row);

        // ROW 6 (Time Signature)
        row++;
        setTimeSignature(row);

        logger.debug("[CM_SONG_DETAILS_PANE] Built {}", getId());
    }

    private final ObjectProperty<Integer> songIdProperty = new SimpleObjectProperty<>(PlaylistContainer.getNextSongId());
    private void setSongId(int row) {
        Label songIdLabel = new Label("Id");
        add(songIdLabel, 0, row, 1, 1);

        Spinner<Integer> songIdSpinner = new Spinner<>(1, 999, 1);
        songIdSpinner.setEditable(true);
        songIdSpinner.setPromptText("id ...");
        songIdSpinner.getValueFactory().valueProperty().bindBidirectional(songIdProperty);
        add(songIdSpinner, 2, row, 1, 1);
    }

    private final StringProperty songNameProperty = new SimpleStringProperty();
    private void setSongName(int row) {
        Label songNameLabel = new Label("Name");
        songNameLabel.setId("SongNameLabel");
        add(songNameLabel, 0, row, 1, 1);

        TextField songNameTextField = new TextField();
        songNameTextField.setPromptText("enter song name ...");
        songNameTextField.textProperty().bindBidirectional(songNameProperty);
        add(songNameTextField, 2, row, 4, 1);
    }

    private final StringProperty performerProperty = new SimpleStringProperty();
    private void setPerformer(int row) {
        Label performerLabel = new Label("Performer");
        add(performerLabel, 0, row, 1, 1);

        TextField performerTextField = new TextField();
        performerTextField.setPromptText("enter performer name ...");
        performerTextField.textProperty().bindBidirectional(performerProperty);
        add(performerTextField, 2, row, 4, 1);
    }

    private final ObjectProperty<Integer> tempoProperty = new SimpleObjectProperty<>(0);
    private void setTempo(int row) {
        Label tempoLabel = new Label("Tempo");
        add(tempoLabel, 0, row, 1, 1);

        Spinner<Integer> tempoSpinner = new Spinner<>(1, 999, 1);
        tempoSpinner.setEditable(true);
        tempoSpinner.setPromptText("tempo ...");
        tempoSpinner.getValueFactory().valueProperty().bindBidirectional(tempoProperty);
        add(tempoSpinner, 2, row, 1, 1);
    }

    private final StringProperty scaleProperty = new SimpleStringProperty(ScaleSelector.scales[0]);
    private final StringProperty pitchProperty = new SimpleStringProperty(ScaleSelector.pitches[0]);
    private void setScale(int row) {
        Label scaleLabel = new Label("Scale");
        add(scaleLabel, 0, row, 1, 1);

        ScaleSelector scaleSelector = new ScaleSelector(false);
        scaleSelector.scaleProperty.bindBidirectional(scaleProperty);
        scaleSelector.pitchProperty.bindBidirectional(pitchProperty);
        add(scaleSelector, 2, row, 6, 1);
    }

    private final ObjectProperty<Integer> beatsProperty = new SimpleObjectProperty<>(4);
    private final ObjectProperty<Integer> beatProperty = new SimpleObjectProperty<>(4);
    private void setTimeSignature(int row) {
        Label meterLabel = new Label("Meter");
        add(meterLabel, 0, row, 1, 1);

        SignatureSelector signatureSelector = new SignatureSelector();
        signatureSelector.setId("SignatureSelector");
        signatureSelector.bpmProperty.bindBidirectional(beatsProperty);
        signatureSelector.beatProperty.bindBidirectional(beatProperty);
        add(signatureSelector, 2, row, 3, 1);
    }

    // ACCESSORS
    public PlaylistDetailsPane getPlaylistDetailsPane() {
        return parent;
    }
}

package creative.scenes.playlist;

import creative.scenes.playlist.statemachine.PlaylistState;
import creative.scenes.sysex.SysexContainer;
import custom.components.playlist.ScaleSelector;
import custom.components.playlist.SignatureSelector;
import entity.playlist.Song;
import eventhandlers.ChangeHandler;
import javafx.application.Platform;
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
import javafx.scene.layout.Region;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

import java.awt.Container;
import java.util.function.BooleanSupplier;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.*;

public class SongDetailsPane extends GridPane {
    private static final Logger logger = LoggerFactory.getLogger(SongDetailsPane.class);

    private Song workingSong;

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

        setPaneHeightAsPercentage(this, owner, 45);
        setPaneBackground(this);

        parent = owner;

        showPaneBorder(this, getColor("border", "red", null));

        buildPane();
    }

    @Getter
    private final PlaylistStateMachine stateMachine = new PlaylistStateMachine(PlaylistState.EMPTY);

    private final StringProperty songNameProperty = new SimpleStringProperty();

    private final BooleanSupplier supplier = () -> {
        boolean result = false;

        // TODO set supplier action

        return result;
    };

    private final ChangeHandler songHasChangedHandler = () -> {
        Song originalSong = ApplicationInfo.getInstance().getCurrentSong();
        if (workingSong != null && ! workingSong.equals(originalSong)) {
            if (originalSong.getSongName().equals(songNameProperty.get()) && (PlaylistContainer.containsSong(songNameProperty.get()))) {
                stateMachine.transitionTo(PlaylistState.UPDATABLE, false, supplier);
            } else {
                stateMachine.transitionTo(PlaylistState.ADDABLE, false, supplier);
            }
        } else {
            stateMachine.undoStateWithSkips(stateMachine.getLastState(), supplier);
        }
    };

    private void buildPane() {
        logger.debug("[CM_SONG_DETAILS_PANE] Building {}", getId());

        if (workingSong == null) {
            return;
        }

        int row = -1;

        // ROW 1 (Song id)
        row++;
        setSongId(row);

        // ROW 2 (Song name)
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

        // ROW 6 (Time signature)
        row++;
        setTimeSignature(row);

        logger.debug("[CM_SONG_DETAILS_PANE] Built {}", getId());
    }

    private final ObjectProperty<Integer> songIdProperty = new SimpleObjectProperty<>(PlaylistContainer.getNextSongId());
    private final Label songIdLabel  = new Label("Id");;
    private final Spinner<Integer> songIdSpinner = new Spinner<>(1, 999, 1);

    private void setSongId(int row) {
        songIdSpinner.setEditable(true);
        songIdSpinner.setPromptText("id ...");
        songIdSpinner.getValueFactory().valueProperty().bindBidirectional(songIdProperty);
        songIdProperty.addListener((observable, oldValue, newValue) -> {
            workingSong.setSongId(newValue);
            songHasChangedHandler.handle();
        });
    }

    private final Label songNameLabel = new Label("Name");;
    private final TextField songNameTextField = new TextField();;

    private void setSongName(int row) {
        songNameTextField.setPromptText("enter song name ...");
        songNameTextField.textProperty().bindBidirectional(songNameProperty);
        songNameProperty.addListener((observable, oldValue, newValue) -> {
            workingSong.setSongName(newValue);
            songHasChangedHandler.handle();
            getPlaylistDetailsPane().getSongTitlePane().updateSongTitle(newValue);
        });
    }

    private final StringProperty performerProperty = new SimpleStringProperty();
    private final Label performerLabel = new Label("Performer");
    private final TextField performerTextField = new TextField();

    private void setPerformer(int row) {
        performerTextField.setPromptText("enter performer name ...");
        performerTextField.textProperty().bindBidirectional(performerProperty);
        performerProperty.addListener((observable, oldValue, newValue) -> {
            workingSong.getSongInfo().setPerformer(newValue);
            songHasChangedHandler.handle();
        });
    }

    private final ObjectProperty<Integer> tempoProperty = new SimpleObjectProperty<>(0);
    private final Label tempoLabel = new Label("Tempo");
    private final Spinner<Integer> tempoSpinner = new Spinner<>(1, 999, 1);
    private void setTempo(int row) {
        tempoSpinner.setEditable(true);
        tempoSpinner.setPromptText("tempo ...");
        tempoSpinner.getValueFactory().valueProperty().bindBidirectional(tempoProperty);
        tempoProperty.addListener((observable, oldValue, newValue) -> {
            workingSong.getSongInfo().setTempo(newValue);
            songHasChangedHandler.handle();
        });
    }

    private final StringProperty scaleProperty = new SimpleStringProperty(ScaleSelector.scales[0]);
    private final StringProperty pitchProperty = new SimpleStringProperty(ScaleSelector.pitches[0]);
    private final Label scaleLabel = new Label("Scale");
    private final ScaleSelector scaleSelector = new ScaleSelector(false);

    private void setScale(int row) {
        scaleSelector.scaleProperty.bindBidirectional(scaleProperty);
        scaleProperty.addListener((observable, oldValue, newValue) -> {
            workingSong.getSongInfo().getScale().setScale(newValue);
            songHasChangedHandler.handle();
        });
        scaleSelector.pitchProperty.bindBidirectional(pitchProperty);
        pitchProperty.addListener((observable, oldValue, newValue) -> {
            workingSong.getSongInfo().getScale().setPitch(newValue);
            songHasChangedHandler.handle();
        });
    }

    private final ObjectProperty<Integer> beatsProperty = new SimpleObjectProperty<>(4);
    private final ObjectProperty<Integer> beatProperty = new SimpleObjectProperty<>(4);

    private final Label meterLabel = new Label("Meter");
    private final SignatureSelector signatureSelector = new SignatureSelector();;
    private void setTimeSignature(int row) {
        signatureSelector.setId("SignatureSelector");
        signatureSelector.bpmProperty.bindBidirectional(beatsProperty);
        beatsProperty.addListener((observable, oldValue, newValue) -> {
            workingSong.getSongInfo().getSignature().setBeats(newValue);
            songHasChangedHandler.handle();
        });
        signatureSelector.beatProperty.bindBidirectional(beatProperty);
        beatProperty.addListener((observable, oldValue, newValue) -> {
            workingSong.getSongInfo().getSignature().setBeat(newValue);
            songHasChangedHandler.handle();
        });
    }

    private void setDatasource(Song song) {
        songIdProperty.set(song.getSongId());
        songNameProperty.set(song.getSongName());
        performerProperty.set(song.getSongInfo().getPerformer());
        tempoProperty.set(song.getSongInfo().getTempo());
        scaleProperty.set(song.getSongInfo().getScale().getScale());
        pitchProperty.set(song.getSongInfo().getScale().getPitch());
        beatsProperty.set(song.getSongInfo().getSignature().getBeats());
        beatProperty.set(song.getSongInfo().getSignature().getBeat());
    }

    private void addComponents() {
        getChildren().clear();

        int row = 0;
        add(songIdLabel, 0, row, 1, 1);
        add(songIdSpinner, 2, row, 1, 1);

        row++;
        add(songNameLabel, 0, row, 1, 1);
        add(songNameTextField, 2, row, 4, 1);

        row++;
        add(performerLabel, 0, row, 1, 1);
        add(performerTextField, 2, row, 4, 1);

        row++;
        add(tempoLabel, 0, row, 1, 1);
        add(tempoSpinner, 2, row, 1, 1);

        row++;
        add(scaleLabel, 0, row, 1, 1);
        add(scaleSelector, 2, row, 6, 1);

        row++;
        add(meterLabel, 0, row, 1, 1);
        add(signatureSelector, 2, row, 3, 1);

    }

    public void setSong(Song song) {
        if (song != null) {
            workingSong = song;

            getPlaylistDetailsPane().getSongTitlePane().updateSongTitle(song.getSongName());
            ApplicationInfo.getInstance().setCurrentSong(new Song(song));

            setDatasource(song);

            buildPane();
            addComponents();
        }
    }

    public void setState(PlaylistState state) {
        stateMachine.transitionTo(state, true, supplier);
    }

    // ACCESSORS
    public PlaylistDetailsPane getPlaylistDetailsPane() {
        return parent;
    }
}

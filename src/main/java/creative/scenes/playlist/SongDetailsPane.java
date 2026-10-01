package creative.scenes.playlist;

import creative.scenes.playlist.statemachine.PlaylistState;
import creative.scenes.sysex.SysexContainer;
import custom.components.playlist.ScaleSelector;
import custom.components.playlist.SignatureSelector;
import entity.playlist.Song;
import eventhandlers.ChangeHandler;
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
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

import java.util.function.BooleanSupplier;

import static util.Util.setPaneBackground;
import static util.Util.setPaneHeightAsPercentage;

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

//        showPaneBorder(this, getColor("border", "red", null));

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
/*        Song originalSong = ApplicationInfo.getInstance().getCurrentSong();
        if (workingSong != null && ! workingSong.equals(originalSong)) {
            ApplicationInfo.getInstance().setDirtySong(workingSong);

            if (originalSong.getSongName().equals(songNameProperty.get()) && (SysexContainer.containsSysex(songNameProperty.get()))) {
                stateMachine.transitionTo(PlaylistState.UPDATABLE, false, supplier);
            } else {
                stateMachine.transitionTo(PlaylistState.ADDABLE, false, supplier);
            }
        } else {
            ApplicationInfo.getInstance().setDirtySysex(null);
            stateMachine.undoStateWithSkips(stateMachine.getLastState(), supplier);
        }
 */
        System.out.println("Some Song value has changed");
        Song originalSong = ApplicationInfo.getInstance().getCurrentSong();
        if (workingSong != null && ! workingSong.equals(originalSong)) {
            System.out.println("Working song is dirty");
        } else {
            System.out.println("Working song is clean");
        }
    };

    private void buildPane() {
        logger.debug("[CM_SONG_DETAILS_PANE] Building {}", getId());

        if (workingSong == null) {
            return;
        }

        getChildren().clear();

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
    private void setSongId(int row) {
        Label songIdLabel = new Label("Id");
        add(songIdLabel, 0, row, 1, 1);

        Spinner<Integer> songIdSpinner = new Spinner<>(1, 999, 1);
        songIdSpinner.setEditable(true);
        songIdSpinner.setPromptText("id ...");
        songIdSpinner.getValueFactory().valueProperty().bindBidirectional(songIdProperty);
        songIdProperty.addListener((observable, oldValue, newValue) -> {
            workingSong.setSongId(newValue);
            songHasChangedHandler.handle();
        });
        add(songIdSpinner, 2, row, 1, 1);
    }

    private void setSongName(int row) {
        Label songNameLabel = new Label("Name");
        songNameLabel.setId("SongNameLabel");
        add(songNameLabel, 0, row, 1, 1);

        TextField songNameTextField = new TextField();
        songNameTextField.setPromptText("enter song name ...");
        songNameTextField.textProperty().bindBidirectional(songNameProperty);
        songNameProperty.addListener((observable, oldValue, newValue) -> {
            workingSong.setSongName(newValue);
            songHasChangedHandler.handle();
        });
        add(songNameTextField, 2, row, 4, 1);
    }

    private final StringProperty performerProperty = new SimpleStringProperty();
    private void setPerformer(int row) {
        Label performerLabel = new Label("Performer");
        add(performerLabel, 0, row, 1, 1);

        TextField performerTextField = new TextField();
        performerTextField.setPromptText("enter performer name ...");
        performerTextField.textProperty().bindBidirectional(performerProperty);
        performerProperty.addListener((observable, oldValue, newValue) -> {
            workingSong.getSongInfo().setPerformer(newValue);
            songHasChangedHandler.handle();
        });
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
        tempoProperty.addListener((observable, oldValue, newValue) -> {
            workingSong.getSongInfo().setTempo(newValue);
            songHasChangedHandler.handle();
        });
        add(tempoSpinner, 2, row, 1, 1);
    }

    private final StringProperty scaleProperty = new SimpleStringProperty(ScaleSelector.scales[0]);
    private final StringProperty pitchProperty = new SimpleStringProperty(ScaleSelector.pitches[0]);
    private void setScale(int row) {
        Label scaleLabel = new Label("Scale");
        add(scaleLabel, 0, row, 1, 1);

        ScaleSelector scaleSelector = new ScaleSelector(false);
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
        beatsProperty.addListener((observable, oldValue, newValue) -> {
            workingSong.getSongInfo().getSignature().setBeats(newValue);
            songHasChangedHandler.handle();
        });
        signatureSelector.beatProperty.bindBidirectional(beatProperty);
        beatProperty.addListener((observable, oldValue, newValue) -> {
            workingSong.getSongInfo().getSignature().setBeat(newValue);
            songHasChangedHandler.handle();
        });
        add(signatureSelector, 2, row, 3, 1);
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

    public void setSong(Song song) {
        workingSong = song;

        ApplicationInfo.getInstance().setCurrentSong(new Song(song));
        ApplicationInfo.getInstance().setDirtySong(null);

        setDatasource(song);

        buildPane();
    }

    public void setState(PlaylistState state) {
        stateMachine.transitionTo(state, true, supplier);
    }

    // ACCESSORS
    public PlaylistDetailsPane getPlaylistDetailsPane() {
        return parent;
    }
}

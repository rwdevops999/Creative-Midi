package creative.scenes.eventlist;

import creative.scenes.eventlist.panes.MidiInfoDurationPane;
import creative.scenes.eventlist.panes.MidiInfoFilenamePane;
import creative.scenes.eventlist.panes.MidiInfoTempoPane;
import creative.scenes.eventlist.panes.MidiInfoTimeSignaturPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Util;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneHeightAsPercentage;

public class MidiInfoPane extends HBox {
    private static final Logger logger = LoggerFactory.getLogger(MidiInfoPane.class);

    public MidiInfoPane() {
        super();

        setId("MidiInfoPane");

        setPadding(new Insets(2));
        setSpacing(5);
        setAlignment(Pos.CENTER_LEFT);
    }

    private EventsPane parent;
    public MidiInfoPane(EventsPane owner) {
        this();

        parent = owner;

//        showPaneBorder(this, getColor("border", "red"));
        setPaneHeightAsPercentage(this, parent, 6);

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_MIDI_INFO_PANE] Building {}", getId());

        MidiInfoFilenamePane filenamePane = new MidiInfoFilenamePane(this);
        getChildren().add(filenamePane);

        MidiInfoTempoPane tempoPane = new MidiInfoTempoPane(this);
        getChildren().add(tempoPane);

        MidiInfoDurationPane durationPane = new MidiInfoDurationPane(this);
        getChildren().add(durationPane);

        MidiInfoTimeSignaturPane signaturePane = new MidiInfoTimeSignaturPane(this);
        getChildren().add(signaturePane);

        logger.debug("[CM_MIDI_INFO_PANE] Built {}", getId());
    }

    public void setFilename(String name) {
        MidiInfoFilenamePane filenamePane = (MidiInfoFilenamePane) getChildren().filtered(n -> n instanceof MidiInfoFilenamePane).stream().findFirst().orElse(null);
        if (filenamePane != null) {
            filenamePane.setValue(FilenameUtils.removeExtension(name));
        }
    }

    public void setBPM(int bpm) {
        MidiInfoTempoPane tempoPane = (MidiInfoTempoPane) getChildren().filtered(n -> n instanceof MidiInfoTempoPane).stream().findFirst().orElse(null);
        if (tempoPane != null) {
            tempoPane.setValue(""+bpm);
        }
    }

    public void setDuration(String duration) {
        MidiInfoDurationPane durationPane = (MidiInfoDurationPane) getChildren().filtered(n -> n instanceof MidiInfoDurationPane).stream().findFirst().orElse(null);
        if (durationPane != null) {
            durationPane.setValue(duration);
        }
    }

    public void setTimeSignature(String timeSignature) {
        MidiInfoTimeSignaturPane signaturePane = (MidiInfoTimeSignaturPane) getChildren().filtered(n -> n instanceof MidiInfoTimeSignaturPane).stream().findFirst().orElse(null);
        if (signaturePane != null) {
            signaturePane.setValue(timeSignature);
        }
    }

    // ACCESSORS
    public EventsPane getEventsPane() {
        return parent;
    }
}

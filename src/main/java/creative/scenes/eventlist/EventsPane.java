package creative.scenes.eventlist;

import creative.scenes.eventlist.parser.*;
import creative.scenes.eventlist.parser.processor.MidiFileProcessor;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.Sequence;
import java.io.File;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;

public class EventsPane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(EventsPane.class);

    public EventsPane() {
        super();

        setId("EventsPane");
    }

    private EventlistPane parent;
    public EventsPane(EventlistPane owner) {
        this();

        parent = owner;

//        showPaneBorder(this, getColor("border", "red"));

        setPaneBackground(this);
        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_EVENTS_PANE] Building {}", getId());

        setTop(new MidiInfoPane(this));
        setCenter(new EventsDisplayPane(this));

        logger.debug("[CM_EVENTS_PANE] Built {}", getId());
    }

    public void processFile(File midiFile) {
        getMidiInfoPane().setFilename(midiFile.getName());

        MidifileParser midiParser = new MidifileParser(midiFile);
        MidiSequence customSequence = midiParser.getCustomSequence();

        try {
            // Convert our custom sequence to javax midi sequence
            Sequence sequence = MidiSequenceConverter.convertToStandardSequence(customSequence);

            // now we have out official midi sequence use it
            MBTCalculator.setPPQ(sequence.getResolution());
            MidifileHelper.setPPQ(sequence.getResolution());

            MidifileHelper.retrieveBpmAndTimeSignature(sequence);
            getMidiInfoPane().setBPM(MidifileHelper.getBPM());
            getMidiInfoPane().setTimeSignature(MidifileHelper.getTimeSignature().toString());
            MBTCalculator.setTimeSignature(MidifileHelper.getTimeSignature());
            getMidiInfoPane().setDuration(MidifileHelper.getDuration());

            MidiFileProcessor.processMidi(sequence);
            getEventsDisplayPane().setEventInfo(sequence, MidiFileProcessor.getEvents());
      } catch (Exception e) {
            logger.error("[CM_EVENTS_PANE] Exception. CAUSE: {}", e.getMessage());
        }
    }

    // ACCESSORS
    public EventlistPane getEventlistPane() {
        return parent;
    }

    public MidiInfoPane getMidiInfoPane() {
        return (MidiInfoPane) getTop();
    }

    public EventsDisplayPane getEventsDisplayPane() {
        return (EventsDisplayPane) getCenter();
    }
}

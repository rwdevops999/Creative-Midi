package creative.scenes.voice;

import creative.scenes.voice.util.SearchResultsPane;
import entity.voice.Patch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Util;

import java.util.ArrayList;
import java.util.List;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;
import static util.Util.setPaneWidthAsPercentage;

public class VoiceSearchResultsPane extends BorderPane {
    private static final Logger logger = LoggerFactory.getLogger(VoiceSearchResultsPane.class);

    public VoiceSearchResultsPane() {
        super();

        setId("VoiceSearchResultsPane");
    }

    private VoicePane parent;
    public VoiceSearchResultsPane(VoicePane owner, List<Patch> data) {
        this();

        parent = owner;

        showPaneBorder(this, getColor("test", "green"));
        setPaneWidthAsPercentage(this, parent, 70);
        setPaneBackground(this);

        buildPane();

        getSearchResultsPane().showResults(data);
    }

    private void buildPane() {
        logger.debug("[CM_VOICE_SEARCH_RESULT_PANE] Building {}", getId());

        setTop(new SpeakerPane(this));
        setCenter(new SearchResultsPane((this)));

        logger.debug("[CM_VOICE_SEARCH_RESULT_PANE] Built {}", getId());
    }

    public void showResults (List<Patch> data) {
//        List<Patch> newData = removeExcludes(data);

        getSearchResultsPane().showResults(data);
    }

    // ACCESSORS
    public VoicePane getVoicePane() {
        return parent;
    }

    public SpeakerPane getSpeakerPane() {
        return (SpeakerPane)getTop();
    }

    public SearchResultsPane getSearchResultsPane() {
        return (SearchResultsPane) getCenter();
    }
}

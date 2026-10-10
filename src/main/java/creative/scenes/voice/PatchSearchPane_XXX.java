package creative.scenes.voice;

import creative.scenes.voice.components.AddablePane;
import creative.scenes.voice.components.TitleSearchPane;
import creative.scenes.voice.util.VoiceFinder;
import entity.voice.Patch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static util.Util.setPaneBackground;
import static util.Util.setPaneHeightAsPercentage;

public class PatchSearchPane_XXX extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(PatchSearchPane_XXX.class);

    private List<AddablePane> addPanes = new ArrayList<>();

    public PatchSearchPane_XXX() {
        super();

        addPanes.add(new AddablePane(this, 0));
        addPanes.add(null);
        addPanes.add(null);
        addPanes.add(null);
        addPanes.add(null);

        setId("PatchSearchPane");

        setSpacing(5);
        setPadding(new Insets(5));
        setAlignment(Pos.TOP_LEFT);
    }

    VoiceSearchPane parent;

    public PatchSearchPane_XXX(VoiceSearchPane owner) {
        this();

        parent = owner;

        setPaneHeightAsPercentage(this, parent, 55);
        setPaneBackground(this);

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_PATCH_SEARCH_PANE] Building {}", getId());

        buildDynamicSearchPane();

        logger.debug("[CM_PATCH_SEARCH_PANE] Built {}", getId());
    }

    private void buildDynamicSearchPane() {
        TitleSearchPane titleSearchPane = new TitleSearchPane(this, "Patches");
        getChildren().add(titleSearchPane);

        Button searchButton = titleSearchPane.getSearchButton();

        for (AddablePane addPane : addPanes) {
            if (addPane != null) {
                addPane.addLinkedSearchButton(searchButton);
                getChildren().add(addPane);
            }
        }
    }

    public void addPane(int id) {
        for (int i = addPanes.size() - 2; i > id; i-- ) {
            AddablePane addPane = addPanes.get(i);
            if (addPane != null) {
                addPane.setButtonId(i+1);
            }
            addPanes.set(i+1, addPane);
        }

        if (id+1 < addPanes.size()) {
            addPanes.set(id+1, new AddablePane(this, id+1));
        }

        getChildren().clear();
        buildDynamicSearchPane();
    }

    public void removePane(int id) {
        if (id > 0) {
            for (int i = id; i < addPanes.size() - 1; i++) {
                AddablePane addPane = addPanes.get(i+1);
                if (addPane != null) {
                    addPane.setButtonId(i);
                }
                addPanes.set(i, addPane);
            }

            addPanes.set(addPanes.size()-1, null);

            getChildren().clear();
            buildDynamicSearchPane();
        }
    }

    public void handleSearch() {
        String voiceToSearch = addPanes.get(0).getInputValue();
        List<Patch> result = new ArrayList<>();

        if (voiceToSearch != null) {
            result = VoiceFinder.findVoice(voiceToSearch);

            for (int i = 1; i < addPanes.size(); i++) {
                if (addPanes.get(i) != null) {
                    String voiceName = addPanes.get(i).getInputValue();
                    if (voiceName != null) {
                        result = refine(result, voiceName.toLowerCase());
                    }
                }
            }

            VoiceSearchResultsPane resultsPane = getVoiceSearchPane().getVoicePane().getVoiceSearchResultsPane();
            resultsPane.showResults(result);
        }
    }

    private List<Patch> refine(List<Patch> previousResult, String voiceName) {
        List<Patch> result = new ArrayList<>();

        for (Patch patch : previousResult) {
            if (patch.getPatch().toLowerCase().contains(voiceName)) {
                result.add(patch);
            }
        }

        return result;
    }

    // ACCESSORS
    public VoiceSearchPane getVoiceSearchPane() {
        return parent;
    }
}

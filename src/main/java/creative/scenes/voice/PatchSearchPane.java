package creative.scenes.voice;

import creative.scenes.voice.components.AddablePane;
import creative.scenes.voice.components.TitleSearchPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ColorScheme;
import util.Util;

import java.util.ArrayList;
import java.util.List;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;
import static util.Util.setPaneHeightAsPercentage;

public class PatchSearchPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(PatchSearchPane.class);

    private List<AddablePane> addPanes = new ArrayList<>();

    public PatchSearchPane() {
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

    public PatchSearchPane(VoiceSearchPane owner) {
        this();

        parent = owner;

        showPaneBorder(this, getColor("test", "green"));
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

    }

    // ACCESSORS
    public VoiceSearchPane getVoiceSearchPane() {
        return parent;
    }
}

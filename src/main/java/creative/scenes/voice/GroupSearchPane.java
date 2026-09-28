package creative.scenes.voice;

import creative.scenes.voice.components.GroupPane;
import creative.scenes.voice.components.TitleSearchPane;
import creative.scenes.voice.util.VoiceFinder;
import entity.voice.Patch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

import java.util.ArrayList;
import java.util.List;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.setPaneBackground;
import static util.Util.setPaneHeightAsPercentage;

public class GroupSearchPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(GroupSearchPane.class);

    public GroupSearchPane() {
        super();

        setId("GroupSearchPane");

        setSpacing(5);
        setPadding(new Insets(5));
        setAlignment(Pos.TOP_LEFT);
    }

    VoiceSearchPane parent;
    private GroupPane groupPane;

    public GroupSearchPane(VoiceSearchPane owner) {
        this();

        parent = owner;

//        showPaneBorder(this, getColor("test", "red"));

        setPaneHeightAsPercentage(this, parent, 15);
        setPaneBackground(this);

        buildPane();
    }

    private void buildPane() {
        logger.debug("[CM_GROUP_SEARCH_PANE] Building {}", getId());

        TitleSearchPane  titleSearchPane = new TitleSearchPane(this, "Groups");
        getChildren().add(titleSearchPane);

        Button searchButton = titleSearchPane.getSearchButton();

        groupPane = new GroupPane(this);
        groupPane.addLinkedSearchButton(searchButton);
        getChildren().add(groupPane);

        logger.debug("[CM_GROUP_SEARCH_PANE] Built {}", getId());
    }

    public void handleSearch() {
        List<Patch> result = new ArrayList<>();

        String groupName = groupPane.getSelectedGroup();

        if (groupName != null) {
            result.addAll(VoiceFinder.getVoicesOfGroup(ApplicationInfo.getInstance().getInstrumentProvider().getGroups(), groupName));
        }

        VoiceSearchResultsPane resultsPane = getVoiceSearchPane().getVoicePane().getVoiceSearchResultsPane();
        resultsPane.showResults(result);
    }

    // ACCESSORS
    public VoiceSearchPane getVoiceSearchPane() {
        return parent;
    }
}

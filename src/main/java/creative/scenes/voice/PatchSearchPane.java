package creative.scenes.voice;

import creative.scenes.midi.provider.MidiProvider;
import creative.scenes.voice.components.AddablePane;
import creative.scenes.voice.components.TitleSearchPane;
import creative.scenes.voice.components.ValuePane;
import creative.scenes.voice.provider.InstrumentProvider;
import creative.scenes.voice.util.VoiceFinder;
import entity.voice.Patch;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApplicationInfo;

import java.util.ArrayList;
import java.util.List;

import static util.ColorScheme.getColor;
import static util.DummyUtil.showPaneBorder;
import static util.Util.*;

public class PatchSearchPane extends VBox {
    private static final Logger logger = LoggerFactory.getLogger(PatchSearchPane.class);

    private static final int TAB_PATCH = 0;
    private static final int TAB_BANK = 1;

    private List<AddablePane> addPanes = new ArrayList<>();
    private List<ValuePane> valuePanes = new ArrayList<>();

    private IntegerProperty selectedTabIndex = new SimpleIntegerProperty();
    private static int selectedTIndex = TAB_PATCH;

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

        setPaneHeightAsPercentage(this, parent, 55);
        setPaneBackground(this);

        buildPane();
        handleSearch();
    }

    private VBox tab1Content;

    private void buildPane() {
        logger.debug("[CM_PATCH_SEARCH_PANE] Building {}", getId());

        TabPane tabPane = new TabPane();
        setControlBackground(tabPane);
        tabPane.getSelectionModel().selectedIndexProperty().addListener((obs, oldIdx, newIdx) -> {
            if (selectedTabIndex.get() != newIdx.intValue()) {
                selectedTIndex = newIdx.intValue();
                selectedTabIndex.set(newIdx.intValue());
                handleSearch();
            }
        });

        selectedTabIndex.addListener((obs, oldIdx, newIdx) -> {
            if (tabPane.getSelectionModel().getSelectedIndex() != newIdx.intValue()) {
                tabPane.getSelectionModel().select(newIdx.intValue());
            }
        });

        Tab tab1 = new Tab("Patch");
        tab1Content = new VBox();
        buildDynamicSearchPane(tab1Content);
        tab1.setContent(tab1Content);

        Tab tab2 = new Tab("Bank");
        VBox tab2Content = new VBox();
        buildBankSearchPane(tab2Content);
        tab2.setContent(tab2Content);

        tabPane.getTabs().addAll(tab1, tab2);

        getChildren().addAll(tabPane);

        selectedTabIndex.set(selectedTIndex);

        logger.debug("[CM_PATCH_SEARCH_PANE] Built {}", getId());
    }

    private void buildBankSearchPane(VBox pane) {
        if (! pane.getChildren().isEmpty()) {
            pane.getChildren().clear();
        }

        pane.setSpacing(10);

        pane.setPadding(new Insets(5, 0, 0, 0));

        TitleSearchPane titleSearchPane = new TitleSearchPane(this, "Patch Values");
        pane.getChildren().add(titleSearchPane);

        Button searchButton = titleSearchPane.getSearchButton();

        ValuePane valuePane = new ValuePane(this, "Bank", "0");
        valuePane.addLinkedSearchButton(searchButton);
        valuePanes.add(valuePane);

        valuePane = new ValuePane(this, "MSB", "0");
        valuePane.addLinkedSearchButton(searchButton);
        valuePanes.add(valuePane);

        valuePane = new ValuePane(this, "LSB", "0");
        valuePane.addLinkedSearchButton(searchButton);
        valuePanes.add(valuePane);

        valuePane = new ValuePane(this, "PC", null);
        valuePane.addLinkedSearchButton(searchButton);
        valuePanes.add(valuePane);

        for (ValuePane p : valuePanes) {
            pane.getChildren().add(p);
        }

        setupValuePanes();
    }

    private static final int BANK_PANE = 0;
    private static final int MSB_PANE = 1;
    private static final int LSB_PANE = 2;
    private static final int PC_PANE = 3;

    private StringProperty bankChangedProperty = new SimpleStringProperty("" + 0);
    private StringProperty msbChangedProperty = new SimpleStringProperty("" + 0);
    private StringProperty lsbChangedProperty = new SimpleStringProperty(""+ 0);
    private StringProperty pcChangedProperty = new SimpleStringProperty();

    private boolean mute = false;

    private void setupValuePanes() {
        ValuePane bankPane = valuePanes.get(BANK_PANE);
        bankPane.addLinkedProperty(bankChangedProperty, "0");

        bankChangedProperty.addListener((obs, oldValue, newValue) -> {
            System.out.println("BANK VALUE CHANGED: " + newValue + " (mute) = " + mute);

            if (! mute) {
                mute = true;
                int bank;
                try {
                    bank = Integer.parseInt(bankChangedProperty.get());
                } catch (NumberFormatException e) {
                    bank = 0;
                    bankChangedProperty.set(""+bank);
                }

                ValuePane msbPane = valuePanes.get(MSB_PANE);
                msbPane.setInputValue("" + (bank >> 7));

                ValuePane lsbPane = valuePanes.get(LSB_PANE);
                lsbPane.setInputValue("" + (bank & 0x7F));
                mute = false;
            }
        });

        ValuePane msbPane = valuePanes.get(MSB_PANE);
        msbPane.addLinkedProperty(msbChangedProperty, "0");
        msbChangedProperty.addListener((obs, oldValue, newValue) -> {
            System.out.println("MSB VALUE CHANGED: " + newValue + " (mute) = " + mute);

            if (! mute) {
                mute = true;
                int msb;
                try {
                    msb = Integer.parseInt(msbChangedProperty.get());
                } catch (NumberFormatException e) {
                    msb = 0;
                    msbChangedProperty.set(""+msb);
                }
                int lsb;
                try {
                    lsb = Integer.parseInt(lsbChangedProperty.get());
                } catch (NumberFormatException e) {
                    lsb = 0;
                    lsbChangedProperty.set(""+lsb);
                }

                int bank = (msb << 7) | lsb;
                bankPane.setInputValue("" + bank);
                mute = false;
            }
        });

        ValuePane lsbPane = valuePanes.get(LSB_PANE);
        lsbPane.addLinkedProperty(lsbChangedProperty, "0");
        lsbChangedProperty.addListener((obs, oldValue, newValue) -> {
            System.out.println("LSB VALUE CHANGED: " + newValue + " (mute) = " + mute);
            if (! mute) {
                mute = true;
                int msb;
                try {
                    msb = Integer.parseInt(msbChangedProperty.get());
                } catch (NumberFormatException e) {
                    msb = 0;
                    msbChangedProperty.set(""+msb);
                }
                int lsb;
                try {
                    lsb = Integer.parseInt(lsbChangedProperty.get());
                } catch (NumberFormatException e) {
                    lsb = 0;
                    lsbChangedProperty.set(""+lsb);
                }

                int bank = (msb << 7) | lsb;
                bankPane.setInputValue("" + bank);
                mute = false;
            }
        });

        ValuePane pcPane = valuePanes.get(PC_PANE);
        pcPane.addLinkedProperty(pcChangedProperty, null);
        pcChangedProperty.addListener((obs, oldValue, newValue) -> {
            System.out.println("PC VALUE CHANGED: " + newValue + " (mute) = " + mute);
            if (! mute) {
                mute = true;
                int pc;
                try {
                    Integer.parseInt(pcChangedProperty.get());
                } catch (NumberFormatException e) {
                    pcChangedProperty.set(null);
                }
                mute = false;
            }
        });
    }

    private void buildDynamicSearchPane(VBox pane) {
        if (! pane.getChildren().isEmpty()) {
            pane.getChildren().clear();
        }

        pane.setPadding(new Insets(5, 0, 0, 0));

        TitleSearchPane titleSearchPane = new TitleSearchPane(this, "Patches");
        pane.getChildren().add(titleSearchPane);

        Button searchButton = titleSearchPane.getSearchButton();

        for (AddablePane addPane : addPanes) {
            if (addPane != null) {
                addPane.addLinkedSearchButton(searchButton);
                pane.getChildren().add(addPane);
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
//        buildDynamicSearchPane(tab1Content);
        buildPane();
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
            buildPane();
            handleSearch();
        }
    }

    public void handleSearch() {
        if (selectedTIndex == TAB_PATCH) {
            String voiceToSearch = addPanes.get(0).getInputValue();
            List<Patch> result = new ArrayList<>();

            if (voiceToSearch == null) {
                voiceToSearch = "";
            }

            // TODO remove this test
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
        } else if (selectedTIndex == TAB_BANK) {
            System.out.println("Searching on values");

            int bank = Integer.parseInt(bankChangedProperty.get());
            int msb = Integer.parseInt(msbChangedProperty.get());
            int lsb = Integer.parseInt(lsbChangedProperty.get());
            Integer pc = null;
            if (pcChangedProperty.get() != null) {
                pc = Integer.parseInt(pcChangedProperty.get());
            }

            System.out.println("Searching on values: BANK =" + bank
            + ", MSB = " + msb
            + ", LSB = " + lsb
            + ", PC = " + pc);

            Patch findPatch = new Patch();
            findPatch.setBank(bank);
//            findPatch.setMsb(msb);
//            findPatch.setLsb(lsb);
            findPatch.setPc(pc);

            List<Patch> result = new ArrayList<>();
            result = VoiceFinder.findVoice(findPatch);
            System.out.println("SearchRESULT: " + result.size());
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

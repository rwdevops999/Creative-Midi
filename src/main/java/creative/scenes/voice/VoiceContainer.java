package creative.scenes.voice;

import entity.voice.Patch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VoiceContainer {
    @Getter
    public final static ObservableSet<Patch> favorites = FXCollections.observableSet(new HashSet<>());

    public static void addAsFavorite(Patch patch) {
        favorites.add(patch);
    }

    public static boolean isFavorite(Patch patch) {
        return favorites.contains(patch);
    }
}

package system;

import entity.voice.Patch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class SysData extends SysDataBase {
    private String sysdataFilename = "sysdata.dat";

    // CHUNKS
    @Getter
    private final static ObservableSet<Patch> favorites = FXCollections.observableSet(new HashSet<>());

    // ACCESSORS
    public static void addAsFavorite(Patch patch) {
        favorites.add(patch);
    }

    public static boolean isFavorite(Patch patch) {
        return favorites.contains(patch);
    }

    @Override
    public void load() {
        System.out.println("LOAD SYSDATA");
    }

    @Override
    public void save() {
        System.out.println("SAVE SYSDATA");
    }
}

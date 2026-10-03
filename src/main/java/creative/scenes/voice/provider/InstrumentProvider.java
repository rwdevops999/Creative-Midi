package creative.scenes.voice.provider;

import entity.voice.Group;
import entity.voice.Patch;
import javafx.scene.layout.Pane;

import java.util.List;

public interface InstrumentProvider {
    String getSourceType(); // Returns "Yamaha Style" or ".ins File"
    List<Group> getGroups();
    default Patch findLinkedPatch(Patch patch) {
        return patch;
    };
    default Patch findPatch(int msb, int lsb, int pc) {
        return null;
    }
}

package creative.scenes.voice.provider;

import entity.voice.Group;

import java.util.List;

public interface InstrumentProvider {
    String getSourceType(); // Returns "Yamaha Style" or ".ins File"
    List<Group> getGroups();
}

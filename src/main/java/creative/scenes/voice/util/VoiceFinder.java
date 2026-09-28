package creative.scenes.voice.util;

import creative.scenes.voice.provider.InstrumentProvider;
import entity.voice.Group;
import entity.voice.Patch;

import java.util.ArrayList;
import java.util.List;

public class VoiceFinder {
    public static List<Patch> findVoice(InstrumentProvider instrumentProvider, String voiceToFind) {
        List<Patch> result = new ArrayList<>();

        if (voiceToFind != null) {
            voiceToFind = voiceToFind.toLowerCase();
        } else {
            voiceToFind = "";
        }

        for (Group group : instrumentProvider.getGroups()) {
            result.addAll(findVoiceInGroup(group, voiceToFind));
        }

        return result;
    }

    public static List<Patch> findVoiceInGroup(Group group, String voiceName) {
        List<Patch> result = new ArrayList<>();

        if (group.getGroupName().toLowerCase().contains(voiceName.toLowerCase())) {
            result.addAll(group.getPatches());
        } else {
            for (Patch patch : group.getPatches()) {
                if ((! result.contains(patch)) && (patch.getPatch().toLowerCase().contains(voiceName))) {
                    result.add(patch);
                }
            }
        }

        if (! group.getGroups().isEmpty()) {
            for (Group g : group.getGroups()) {
                result.addAll(findVoiceInGroup(g, voiceName));
            }
        }

        return result;
    }
}

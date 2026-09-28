package creative.scenes.voice.util;

import creative.scenes.voice.provider.InstrumentProvider;
import entity.voice.Group;
import entity.voice.Patch;
import util.ApplicationInfo;

import java.util.ArrayList;
import java.util.List;

public class VoiceFinder {
    public static List<Patch> findVoice(String voiceToFind) {
        List<Patch> result = new ArrayList<>();

        InstrumentProvider instrumentProvider = ApplicationInfo.getInstance().getInstrumentProvider();

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

    public static List<String> getGroupsNames(List<Group> groups, String baseName) {
        List<String> result = new ArrayList<>();

        for (Group group : groups) {
            String name = baseName + "/" + group.getGroupName();
            result.add(name.substring(1));
            if (! group.getGroups().isEmpty()) {
                result.addAll(getGroupsNames(group.getGroups(), name));
            }
        }

        return result;
    }

    public static List<Patch> getVoicesOfGroup(List<Group> groups, String groupName) {
        List<Patch> result = new ArrayList<>();
        for (Group group : groups) {
            if (group.getGroupName().toLowerCase().contains(groupName)) {
                result.addAll(group.getPatches());
                break;
            } else if (! group.getGroups().isEmpty()) {
                result.addAll(getVoicesOfGroup(group.getGroups(), groupName));
            }
        }

        return result;
    }
}

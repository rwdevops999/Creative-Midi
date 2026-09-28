package creative.scenes.voice.util;

import creative.scenes.voice.provider.InstrumentProvider;
import entity.voice.Group;
import entity.voice.Patch;
import util.ApplicationInfo;

import java.util.*;

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


    public static List<Patch> getVoices(List<Group> groups) {
        List<Patch> result = new ArrayList<>();

        for (Group group : groups) {
            result.addAll(group.getPatches());

            if (! group.getGroups().isEmpty()) {
                result.addAll(getVoices(group.getGroups()));
            }
        }

        return result;
    }

    public static Set<String> getVoiceTypes() {
        InstrumentProvider instrumentProvider = ApplicationInfo.getInstance().getInstrumentProvider();
        if (instrumentProvider.getSourceType() == "Yamaha") {
            List<Patch> result = getVoices(ApplicationInfo.getInstance().getInstrumentProvider().getGroups());
            Map<String, Integer> voiceTypes = new HashMap<>();

            for (Patch patch : result) {
                String patchName = patch.getPatch();
                int index = patchName.indexOf(" ");
                if (index != -1) {
                    String type = patchName.substring(0, index);
                    voiceTypes.merge(type, 1, Integer::sum);
                }
            }

            return voiceTypes.keySet();
        }

        return new HashSet<>();
    }
}

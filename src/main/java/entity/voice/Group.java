package entity.voice;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Group {
    private Integer index;
    private Group parent;
    private List<Group> groups = new ArrayList<>();
    private List<Patch> patches = new ArrayList<>();
    private String groupName = null;

    public Group() {
    }

    public Group (Integer index, String name, Group parent) {
        this();

        this.groupName = name;
        this.index = index;
        this.parent = parent;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append('[').append(this.groupName).append(']');
        if (!this.getGroups().isEmpty()) {
            sb.append(' ').append(this.getGroups());
        }

        return sb.toString();
    }
}

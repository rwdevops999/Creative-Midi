package entity.sysex;

import entity.AEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Sysex extends AEntity {
    private String name;        // the name of the midi
    private List<SysexContent> list;

    public Sysex() {
        super();

        name = "";
        list = new ArrayList<>();
    }

    public Sysex(String name) {
        this();
        this.name = name;
    }

    public Sysex(String name, List<SysexContent> list) {
        this();
        this.name = name;
        this.list = list;
    }

    public Sysex(Sysex other) {
        this.name = other.getName();
        this.list = new ArrayList<>(other.getList());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Sysex other)) {
            return false;
        }

        return Objects.equals(this.name, other.name) &&
                Objects.deepEquals(this.list, other.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, list);
    }
}

package entity.sysex;

import entity.AEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
}

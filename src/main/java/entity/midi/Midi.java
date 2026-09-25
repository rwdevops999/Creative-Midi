package entity.midi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Midi {
    private String name;

    public Midi() {
        this.name = "";
    }
}

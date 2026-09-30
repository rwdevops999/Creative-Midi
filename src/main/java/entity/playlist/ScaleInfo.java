package entity.playlist;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScaleInfo {
    private String scale;
    private String pitch;

    public ScaleInfo() {
        this.scale = "C";
        this.pitch = "major";
    }

    @Override
    public String toString() {
        return this.scale + (this.pitch.equals("minor") ? "m" : "");
    }

    public void set() {

    }
}

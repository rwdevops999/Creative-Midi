package entity.playlist;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignatureInfo {
    // beats / beat (e.g. 3/4)
    private Integer beats;
    private Integer beat;

    public SignatureInfo() {
        this.beats = 4;
        this.beat = 4;
    }

    @Override
    public String toString() {
        return this.beats + "/" + this.beat;
    }
}

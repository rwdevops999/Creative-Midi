package entity.playlist;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongInfo {
    private String performer;
    private Integer tempo;
    private ScaleInfo scale;
    private SignatureInfo signature;

    public SongInfo() {
        this.performer = "";
        this.tempo = 0;
        this.scale = new ScaleInfo();
        this.signature = new SignatureInfo();
    }

    @Override
    public String toString() {
        return
                "[" + System.lineSeparator() +
                        " - PERFORMER: " + this.performer + System.lineSeparator() +
                        " - TEMPO: " + this.tempo + " BPM" + System.lineSeparator() +
                        " - SCALE: " + this.scale.toString() + System.lineSeparator() +
                        " - SIGNATURE: " + this.signature.toString() + System.lineSeparator() +
                        "]";


    }
}

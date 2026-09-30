package entity.playlist;

import entity.AEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Song extends AEntity {
    private Integer songId;
    private String songName;
    private SongInfo songInfo;
    private List<Mapping> mappings;

    public Song(int nextId) {
        this.songId = nextId;
        this.songName = "";
        this.songInfo = new SongInfo();
        this.mappings = new ArrayList<>();
    }

    public Song() {
        this(1);
    }

    private String getAdditionalInfo() {
        String info = "";
        String mapping = "";

        if (songInfo != null) {
            info = songInfo.toString() + System.lineSeparator();
        }

        if (mappings != null && mappings.size() > 0) {
            mapping = "[MAPPINGS: " + System.lineSeparator() +
                    "   {" + System.lineSeparator();

            StringBuilder sb = new StringBuilder();
            for (Mapping m : mappings) {
                sb.append("      ... ").append(m.toString()).append(",").append(System.lineSeparator());
            }
            int index = sb.lastIndexOf(",");
            sb.setCharAt(index, ' ');
            String maps = sb.toString();
            mapping += maps;

            mapping += "   }" + System.lineSeparator() + "]";
        }

        return info + System.lineSeparator() + mapping;
    }

    @Override
    public String toString() {
        return "[SONG]: " + songName + System.lineSeparator() + getAdditionalInfo();
    }
}

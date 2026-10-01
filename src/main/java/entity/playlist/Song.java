package entity.playlist;

import entity.AEntity;
import entity.sysex.Sysex;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Song extends AEntity implements Serializable {
    private static final long serialVersionUID = 1L;

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

    public Song(Song other) {
        this.songId = other.getSongId();
        this.songName = other.getSongName();
        this.songInfo = new SongInfo(other.getSongInfo());
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

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Song other)) {
            return false;
        }

        return Objects.equals(this.songName, other.songName) &&
                this.songInfo.equals(other.songInfo) &&
                Objects.deepEquals(this.mappings, other.mappings) &&
                Objects.equals(this.songId, other.songId)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(songId, songName, songInfo, mappings);
    }

    public void removeMapping (Mapping mapping) {
        this.mappings.removeIf(m -> m.getReceive().equals(mapping.getReceive()) && m.getReply().equals(mapping.getReply()));
    }
}

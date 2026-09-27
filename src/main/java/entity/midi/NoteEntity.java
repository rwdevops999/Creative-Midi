package entity.midi;

import lombok.Getter;

@Getter
public class NoteEntity {
    private int id;
    private String name;

    public NoteEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        // Replace 'Note' with your actual class name
        NoteEntity other = (NoteEntity) obj;

        // Replace 'id' with your object's unique identifier (e.g., getId())
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        // Keeps hashCode consistent with equals
        return java.util.Objects.hash(id);
    }
}

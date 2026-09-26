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
}

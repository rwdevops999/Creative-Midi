package entity.playlist;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mapping {
    private String receive;
    private String reply;

    public Mapping(String receive, String reply) {
        this.receive = receive;
        this.reply = reply;
    }

    public Mapping() {
        this("", "");
    }

    @Override
    public String toString() {
        return this.receive + " -> " + this.reply;
    }
}

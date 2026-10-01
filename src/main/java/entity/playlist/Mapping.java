package entity.playlist;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Mapping implements Serializable {
    private static final long serialVersionUID = 1L;

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

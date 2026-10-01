package creative.scenes.playlist.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CommunicationInfo {
    private String name;
    private List<CommunicationInfo> replies;
    private byte[] data;

    public CommunicationInfo() {
        this.name = "";
        this.replies = new ArrayList<>();
        this.data = new byte[0];
    }

    public CommunicationInfo(String name) {
        this();
        this.name = name;
    }
}


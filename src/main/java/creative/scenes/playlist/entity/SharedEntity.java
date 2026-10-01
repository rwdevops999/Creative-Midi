package creative.scenes.playlist.entity;

import creative.panes.monitor.data.CommunicationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SharedEntity {
    private CommunicationType type;
    private String value;

    public SharedEntity(CommunicationType type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "<SHARED ENTITY> TYPE: " + type.name() + " VALUE: " + value;
    }
}

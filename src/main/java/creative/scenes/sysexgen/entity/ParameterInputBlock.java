package creative.scenes.sysexgen.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParameterInputBlock {
    private SubEvent subEvent;
    private Object parameterData;

    public ParameterInputBlock() {
        this.subEvent = null;
        this.parameterData = null;
    }
}

package creative.scenes.sysexgen.entity;

import entity.sysex.InputBlock;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ParameterInputBlock {
    private SubEvent subEvent;
    private List<InputBlock> inputBlocks;

    public ParameterInputBlock() {
        this.subEvent = null;
        this.inputBlocks = new ArrayList<>();
    }
}

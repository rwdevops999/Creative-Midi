package entity.sysex;

import java.util.List;

public interface IRoot {
    List<String> generateSysEx(List<InputBlock> inputBlocks);
}

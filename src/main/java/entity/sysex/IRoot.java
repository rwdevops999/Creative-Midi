package entity.sysex;

import java.util.List;

public interface IRoot {
    List<String> generateSysEx(String sysex, List<InputBlock> inputBlocks);
}

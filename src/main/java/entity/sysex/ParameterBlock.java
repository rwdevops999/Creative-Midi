package entity.sysex;

import lombok.Getter;

@Getter
public class ParameterBlock {
    String parameter;
    String display;
    Integer min;
    Integer max;
    ParametersTable parametersTable;

    public ParameterBlock(String parameter, String display, Integer min, Integer max, ParametersTable parametersTable) {
        this.parameter = parameter;
        this.display = display;
        this.min = min;
        this.max = max;
        this.parametersTable = parametersTable;
    }
}

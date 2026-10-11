package creative.scenes.sysexgen.entity;

import lombok.Getter;

@Getter
public class ParameterTableEntry {
    int id;
    String parameter;
    String display;
    Integer min;
    Integer max;
    ParameterDataTable dataTable;

    public ParameterTableEntry(int id, String parameter, String display, Integer min, Integer max, ParameterDataTable dataTable) {
        this.id = id;
        this.parameter = parameter;
        this.display = display;
        this.min = min;
        this.max = max;
        this.dataTable = dataTable;
    }
}

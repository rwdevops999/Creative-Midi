package creative.scenes.sysexgen2.entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.Getter;

public class InputBlock {
    @Getter
    private EventParameter parameter;

    private String label;
    private String description;

    private Range range;

    private DataTable dataTable;
    private Integer value;

    private IntegerProperty valuePoperty = new SimpleIntegerProperty();

    public InputBlock() {
        this.parameter = null;
        this.label = "";
        this.description = "";
        this.range = null;
        this.dataTable = null;
        this.value = null;
    }
}

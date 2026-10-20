package creative.scenes.sysexgen.entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputBlock {
    private Event event;
    private EventParameter parameter;

    private String label;
    private String description;

    private Range range;

    private DataTable dataTable;
    private DataBlock value;

    private IntegerProperty valuePoperty = new SimpleIntegerProperty();

    public InputBlock() {
        this.event = null;
        this.parameter = null;
        this.label = "";
        this.description = "";
        this.range = null;
        this.dataTable = null;
        this.value = null;
    }
}

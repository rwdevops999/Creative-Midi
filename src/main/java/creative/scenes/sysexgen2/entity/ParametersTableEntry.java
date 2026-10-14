package creative.scenes.sysexgen2.entity;

import lombok.Getter;
import lombok.Setter;

public class ParametersTableEntry {
    @Getter
    @Setter
    private int parameterId;

    @Getter
    @Setter
    private String label;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private int defaultValue;

    @Getter
    @Setter
    private Range range;

    private DataTable dataTable;

    public ParametersTableEntry() {
    }

    public ParametersTableEntry(ParametersTableEntryBuilder builder) {
        this();

        this.parameterId = builder.parameterId;
        this.label = builder.label;
        this.description = builder.description;
        this.defaultValue = builder.defaultValue;
        this.range = builder.range;
        this.dataTable = builder.dataTable;
    }

    public ParametersTableEntry(ParametersTableEntry other) {
        this();

        this.parameterId = other.parameterId;
        this.label = other.label;
        this.description = other.description;
        this.defaultValue = other.defaultValue;
        this.range = new Range(other.range);
        this.dataTable = new DataTable(other.dataTable);
    }

    public ParametersTableEntry(int index, ParametersTableEntry other) {
        this();

        this.parameterId = index;
        this.label = other.label;
        this.description = other.description;
        this.defaultValue = other.defaultValue;
        this.range = new Range(other.range);
        this.dataTable = new DataTable(other.dataTable);
    }

    public static class ParametersTableEntryBuilder {
        private int parameterId;
        private String label;
        private String description;
        private int defaultValue;
        private Range range;
        private DataTable dataTable;

        public ParametersTableEntryBuilder clear() {
            this.parameterId = -1;
            this.label = "";
            this.description = "";
            this.defaultValue = 0;
            this.range = new Range();
            this.dataTable = null;

            return this;
        }

        public ParametersTableEntryBuilder withParameterId(int id) {
            this.parameterId = id;
            return this;
        }

        public ParametersTableEntryBuilder withLabel(String label) {
            this.label = label;
            return this;
        }

        public ParametersTableEntryBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ParametersTableEntryBuilder withDefaultValue(int defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public ParametersTableEntryBuilder withRange(Range range) {
            this.range = range;
            return this;
        }

        public ParametersTableEntryBuilder withRange(int min, int max) {
            this.range = new Range(min, max);
            return this;
        }

        public ParametersTableEntryBuilder withDataTable(DataTable dataTable) {
            this.dataTable = dataTable;
            return this;
        }

        public ParametersTableEntry build() {
            return new ParametersTableEntry(this);
        }
    }
}

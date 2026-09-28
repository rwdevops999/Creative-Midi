package entity.sysex;

import lombok.Getter;

public class ParameterChangeTable {
    @Getter
    int id;
    @Getter
    int numBytes;
    @Getter
    String parameter;
    @Getter
    String description;
    @Getter
    DataBlock defaultValues;
    @Getter
    DataBlock rangeValues;

    public ParameterChangeTable(ParameterChangeTableBuilder builder) {
        this.id = builder.id;
        this.numBytes = builder.numBytes;
        this.parameter = builder.parameter;
        this.description = builder.description;
        this.defaultValues = builder.defaultValues;
        this.rangeValues = builder.rangeValues;
    }

    public boolean hasDefaultValues() {
        return defaultValues != null;
    };

    public boolean hasRange() {
        return rangeValues != null;
    };

    public static class ParameterChangeTableBuilder {
        int id;
        int numBytes;
        String parameter;
        String description;
        DataBlock defaultValues;
        DataBlock rangeValues;

        public ParameterChangeTableBuilder withId(int id) {
            this.id = id;
            return this;
        }

        public ParameterChangeTableBuilder withNumBytes(int numBytes) {
            this.numBytes = numBytes;
            return this;
        }

        public ParameterChangeTableBuilder withParameter(String parameter) {
            this.parameter = parameter;
            return this;
        }

        public ParameterChangeTableBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ParameterChangeTableBuilder withDefaultValues(Integer def1, Integer def2) {
            this.defaultValues = new DataBlock(def1, def2);
            return this;
        }

        public ParameterChangeTableBuilder withDefaultValues(Integer def1) {
            this.defaultValues = new DataBlock(def1, null);
            return this;
        }

        public ParameterChangeTableBuilder withRange(Integer range1, Integer range2) {
            this.rangeValues = new DataBlock(range1, range2);
            return this;
        }

        public ParameterChangeTableBuilder reset() {
            this.id = 0;
            this.numBytes = 0;
            this.parameter = null;
            this.description = null;
            this.defaultValues = null;
            this.rangeValues = new DataBlock(0x0, 0x7F);
            return this;
        }

        public ParameterChangeTable build() {
            return new ParameterChangeTable(this);
        }
    }
}

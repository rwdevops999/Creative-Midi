package creative.scenes.sysexgen.entity;

import lombok.Getter;

public class ParameterDataTable {
    String name;
    String description;
    @Getter
    String[] dataEntries;

    public ParameterDataTable(ParameterDataTableBuilder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.dataEntries = builder.dataEntries;
    }

    public static class ParameterDataTableBuilder {
        String name;
        String description;
        String[] dataEntries;

        public ParameterDataTableBuilder withSize(int size) {
            dataEntries = new String[size];
            return this;
        }

        public ParameterDataTableBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ParameterDataTableBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ParameterDataTableBuilder withEntries(String... values) {
            int index = 0;
            for (String value: values) {
                dataEntries[index++] = value;
            }
            return this;
        }

        public ParameterDataTableBuilder withEntry(int index, String value) {
            dataEntries[index] = value;
            return this;
        }

        public ParameterDataTableBuilder clear() {
            this.name = null;
            this.description = "";
            this.dataEntries = null;
            return this;
        }

        public ParameterDataTable build() {
            return new ParameterDataTable(this);
        }
    }
}

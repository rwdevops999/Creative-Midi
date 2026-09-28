package entity.sysex;

import lombok.Getter;

public class ParametersTable {
    String name;
    String description;
    @Getter
    String[] dataBlock;

    public ParametersTable(ParametersTableBuilder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.dataBlock = builder.dataBlock;
    }

    public static class ParametersTableBuilder {
        String name;
        String description;
        String[] dataBlock;

        public ParametersTableBuilder withSize(int size) {
            dataBlock = new String[size];
            return this;
        }

        public ParametersTableBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ParametersTableBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ParametersTableBuilder withValues(String... values) {
            int index = 0;
            for (String value: values) {
                dataBlock[index++] = value;
            }
            return this;
        }

        public ParametersTableBuilder reset() {
            this.name = null;
            this.dataBlock = null;
            return this;
        }

        public ParametersTable build() {
            return new ParametersTable(this);
        }
    }
}

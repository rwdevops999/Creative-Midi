package creative.scenes.sysexgen2.entity;

import lombok.Getter;

public class DataTable {
    @Getter
    String tableId;                 // USEFULL FOR TESTING...

    String tableDescription;
    String[] entries;

    public DataTable() {
        this.tableId = "";
        this.tableDescription = "";
        this.entries = null;
    }

    public DataTable(DataTableBuilder builder) {
        this();

        this.tableId = builder.tableId;
        this.tableDescription = builder.tableDescription;
        this.entries = builder.entries;
    }

    public DataTable(DataTable other) {
        this();

        if (other != null) {
            this.tableId = other.tableId;
            this.tableDescription = other.tableDescription;
            this.entries = other.entries;
        }
    }

    public static class DataTableBuilder {
        private String tableId;
        private String tableDescription;
        private String[] entries;

        public DataTableBuilder clear() {
            this.tableId = "";
            this.tableDescription = "";
            this.entries = null;

            return this;
        }

        // This must be called before adding entries
        public DataTableBuilder withSize(int size) {
            entries = new String[size];
            return this;
        }

        public DataTableBuilder withTableId(String value) {
            this.tableId = value;
            return this;
        }

        public DataTableBuilder withTableDescription(String value) {
            this.tableDescription = value;
            return this;
        }

        public DataTableBuilder withEntries(String ...values) {
            int index = 0;
            for (String value: values) {
                entries[index++] = value;
            }

            return this;
        }

        public DataTableBuilder withEntry(int index, String value) {
            entries[index] = value;

            return this;
        }

        public DataTable build() {
            return new DataTable(this);
        }
    }
}

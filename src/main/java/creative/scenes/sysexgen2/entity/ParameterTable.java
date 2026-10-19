package creative.scenes.sysexgen2.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ParameterTable {
    @Getter
    @Setter
    protected String identifier;    // USEFULL FOR TESTING...
    protected String name;
    protected String description;

    @Getter
    protected List<ParametersTableEntry> entries;

    public ParameterTable() {
        this.identifier = "";
        this.name = "";
        this.description = "";

        this.entries = new ArrayList<>();
    }

    public ParameterTable(ParameterTableBuilder builder) {
        this.identifier = builder.identifier;
        this.name = builder.name;
        this.description = builder.description;
        this.entries = builder.entries;
    }

    public ParameterTable(ParameterTable other) {
        this.identifier = other.identifier;
        this.name = other.name;
        this.description = other.description;
//        this.entries = new ArrayList<>(other.entries);

        List<ParametersTableEntry> newEntries = new ArrayList<>();
        for (ParametersTableEntry entry : other.entries) {
            newEntries.add(new ParametersTableEntry(entry));
        }

        this.entries = newEntries;
    }

    public boolean hasEntries() {
        return !entries.isEmpty();
    }

    public ParametersTableEntry getEntryOfParameterId(int parameterId) {
        for (ParametersTableEntry entry : entries) {
            if (entry.getParameterId() == parameterId) {
                return entry;
            }
        }

        return null;
    }

    public static class ParameterTableBuilder {
        private String identifier;
        private String name;
        private String description;
        private List<ParametersTableEntry> entries;

        public ParameterTableBuilder clear() {
            this.identifier = "";
            this.name = "";
            this.description = "";
            this.entries = new ArrayList<>();

            return this;
        }

        public ParameterTableBuilder withIdentifier(String id) {
            this.identifier = id;
            return this;
        }

        public ParameterTableBuilder withName(String value) {
            this.name = value;

            return this;
        }

        public ParameterTableBuilder withDescription(String value) {
            this.description = value;

            return this;
        }

        public ParameterTableBuilder withEntry(ParametersTableEntry entry) {
            this.entries.add(entry);

            return this;
        }

        public ParameterTableBuilder withEntries(ParametersTableEntry entry, String[] labels, int ...ids) {
            for (int i = 0; i < ids.length; i++) {
                ParametersTableEntry newEntry = new ParametersTableEntry(entry);

                if (labels != null && labels.length > i) {
                    newEntry.setLabel(labels[i]);
                }
                newEntry.setParameterId(ids[i]);
                this.entries.add(newEntry);
            }

            return this;
        }

        public ParameterTable build() {
            return new ParameterTable(this);
        }
    }
}

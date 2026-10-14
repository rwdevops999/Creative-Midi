package creative.scenes.sysexgen2.entity;

import lombok.Getter;

import java.util.*;

/**
 * Make sure that entries are filled in first
 */
public class MutableParameterTable {
    @Getter
    private ParameterTable parameterTable;

    private Map<Integer, ParameterTable> crossReferenceTables;

    public MutableParameterTable() {
    }

    public MutableParameterTable(MutableParameterTableBuilder builder) {
        this();

        this.parameterTable = builder.parameterTable;
        this.crossReferenceTables = builder.crossReferenceTables;
    }

    public boolean hasCrossReference(int parameterId) {
        return (this.crossReferenceTables != null) && (this.crossReferenceTables.containsKey(parameterId));
    }

    public ParameterTable getCrossReferenceTable(int parameterId) {
        Optional<Map.Entry<Integer, ParameterTable>> parameterTable = this.crossReferenceTables.entrySet().stream().filter(entry -> entry.getKey() == parameterId).findFirst();

        return parameterTable.map(Map.Entry::getValue).orElse((null));

    }

    public static class MutableParameterTableBuilder {
        private String label;
        private String description;
        private Range range;
        private Integer defaultValue;
        private ParameterTable parameterTable;

        private String identifier;
        private Map<Integer, ParameterTable> crossReferenceTables;

        public MutableParameterTableBuilder clear() {
            this.label = null;
            this.description = null;
            this.range = null;
            this.defaultValue = null;
            this.identifier = "";
            this.crossReferenceTables = new HashMap<Integer, ParameterTable>();

            return this;
        }

        public MutableParameterTableBuilder withIdentifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        public MutableParameterTableBuilder withLabel(String label) {
            this.label = label;
            return this;
        }

        public MutableParameterTableBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public MutableParameterTableBuilder withRange(Range range) {
            this.range = range;
            return this;
        }

        public MutableParameterTableBuilder withRange(int min, int max) {
            this.range = new Range(min, max);
            return this;
        }

        public MutableParameterTableBuilder withDefaultValue(int defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public MutableParameterTableBuilder crossReferenceTable(int parameterId, ParameterTable crossReferenceTable) {
            this.crossReferenceTables.put(parameterId, crossReferenceTable);

            return this;
        }

        public MutableParameterTableBuilder fromParameterTable(ParameterTable parameterTable) {
            this.parameterTable = parameterTable;

            if (parameterTable != null && parameterTable.hasEntries()) {
                parameterTable.setIdentifier(this.identifier);
                parameterTable.entries.stream().forEach(entry -> {
                    entry.setLabel(this.label != null ? this.label : entry.getLabel());
                    entry.setDescription(this.description != null ? this.description : entry.getDescription());
                    entry.setDefaultValue(this.defaultValue != null ? this.defaultValue : entry.getDefaultValue());
                    entry.setRange(this.range != null ? this.range : entry.getRange());
                });
            }

            return this;
        }

        public ParameterTable build() {
            return new MutableParameterTable(this).getParameterTable();
        }
    }
}

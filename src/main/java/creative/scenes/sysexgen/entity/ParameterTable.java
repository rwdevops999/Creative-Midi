package creative.scenes.sysexgen.entity;
import lombok.Getter;

import java.awt.image.renderable.ParameterBlock;

public class ParameterTable {
    String Name;
    @Getter
    ParameterTableEntry[] entries; // 17 entries as it goes from 1 .. 16

    public ParameterTable(ParameterTableBuilder builder) {
        this.Name = builder.Name;
        this.entries = builder.entries;
    }

    public static class ParameterTableBuilder {
        String Name;
        ParameterTableEntry[] entries; // 17 entries as it goes from 1 .. 16

        public ParameterTableBuilder() {
        }

        public ParameterTableBuilder withName(String name) {
            this.Name = name;
            return this;
        }

        public ParameterTableBuilder withParameterTableEntry(int id, String parameter, String display, int min, int max, ParameterDataTable table) {
            this.entries[id] = new ParameterTableEntry(id, parameter, display, min, max, table);
            return this;
        }

        public ParameterTableBuilder withParameterTableEntry(int id, String parameter, String display, int min, int max) {
            this.entries[id] = new ParameterTableEntry(id, parameter, display, min, max, null);
            return this;
        }

        public ParameterTableBuilder clear() {
            this.Name = null;
            this.entries = new ParameterTableEntry[17];
            return this;
        }

        public ParameterTable build() {
            return new ParameterTable(this);
        }
      }
}

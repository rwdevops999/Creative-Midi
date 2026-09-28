package entity.sysex;
import lombok.Getter;

public class ParameterList {
    String Name;
    @Getter
    ParameterBlock[] parameters; // 17 entries as it goes from 1 .. 16

    public ParameterList(ParameterListBuilder builder) {
        this.Name = builder.Name;
        this.parameters = builder.parameters;
    }

    public static class ParameterListBuilder {
        String Name;
        ParameterBlock[] parameters; // 17 entries as it goes from 1 .. 16

        public ParameterListBuilder withName(String name) {
            this.Name = name;
            return this;
        }

        public ParameterListBuilder withParameters(int id, String parameter, String display, int min, int max, ParametersTable table) {
            this.parameters[id] = new ParameterBlock(parameter, display, min, max, table);
            return this;
        }

        public ParameterListBuilder withParameters(int id, String parameter, String display, int min, int max) {
            this.parameters[id] = new ParameterBlock(parameter, display, min, max, null);
            return this;
        }

        public ParameterListBuilder reset() {
            this.Name = null;
            this.parameters = new ParameterBlock[17];
            return this;
        }

        public ParameterList build() {
            return new ParameterList(this);
        }
      }
}

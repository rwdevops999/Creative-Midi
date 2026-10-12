package creative.scenes.sysexgen.entity;

import creative.scenes.sysexgen.data.ParameterType;
import entity.sysex.DataBlock;
import lombok.Getter;
import lombok.Setter;

public class Parameter {
    @Getter
    private String name;
    private Integer subAddress;
    @Getter
    private int numBytes;
    @Getter
    private ParameterType type;
    private String parameterIdentifier;
    @Getter
    private String description;
    @Getter
    private DataBlock defaultValues;
    @Getter
    private DataBlock rangeValues;

    // this will contain the actual value
    @Getter
    @Setter
    private DataBlock value;

    public Parameter() {
        this.name = null;
        this.subAddress = null;
        this.numBytes = 1;                  // default 1 byte
        this.type = ParameterType.INLINE;   // default a parameters belonging to the sysex
        this.parameterIdentifier = null;    // a parameters identifier Number or a Name
        this.description = null;            // a description
        this.defaultValues = null;          // default value(s)
        this.rangeValues = null;          // default value(s)

        this.value = null;          // default value(s)
    }

    public Parameter(ParameterBuilder builder) {
        this();

        this.name = builder.name;
        this.subAddress = builder.subAddress;
        this.numBytes = builder.numBytes;
        this.type = builder.type;
        this.parameterIdentifier = builder.parameterIdentifier;
        this.description = builder.description;
        this.defaultValues = builder.defaultValues;
        this.rangeValues = builder.rangeValues;
    }

    public static class ParameterBuilder {
        private String name = null;
        private Integer subAddress = null;
        private int numBytes = 1;
        private ParameterType type = ParameterType.INLINE;
        private String parameterIdentifier = null;
        private String description = "";
        private DataBlock defaultValues = null;
        private DataBlock rangeValues = null;

        public ParameterBuilder clear() {
            this.name = null;
            this.subAddress = null;
            this.numBytes = 1;
            this.type = ParameterType.INLINE;
            this.parameterIdentifier = null;
            this.description = "";
            this.defaultValues = null;
            this.rangeValues = null;

            return (this);
        }

        public ParameterBuilder withName(String value) {
            this.name = value;
            return this;
        }

        public ParameterBuilder withSubAddress(Integer value) {
            this.subAddress = value;
            return this;
        }

        public ParameterBuilder withNumBytes(int value) {
            this.numBytes = value;
            return this;
        }

        public ParameterBuilder withType(ParameterType value) {
            this.type = value;
            return this;
        }

        public ParameterBuilder withIdentifier(String value) {
            this.parameterIdentifier = value;
            return this;
        }

        public ParameterBuilder withDescription(String value) {
            this.description = value;
            return this;
        }

        // It will be MSB and LSB
        public ParameterBuilder withDefaultValues(Integer val1, Integer val2) {
            this.defaultValues = new DataBlock(val1, val2);
            return this;
        }

        // It is only LSB
        public ParameterBuilder withDefaultValue(Integer value) {
            this.defaultValues = new DataBlock(null, value);
            return this;
        }

        // It will be MSB and LSB
        public ParameterBuilder withRangeValues(Integer val1, Integer val2) {
            this.rangeValues = new DataBlock(val1, val2);
            return this;
        }

        // It is only LSB
        public ParameterBuilder withRangeValue(Integer value) {
            this.rangeValues = new DataBlock(null, value);
            return this;
        }

        public Parameter build() { return new Parameter(this); }
    }
}

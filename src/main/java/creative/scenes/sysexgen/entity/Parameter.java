package creative.scenes.sysexgen.entity;

import creative.scenes.sysexgen.data.ParameterType;
import creative.scenes.sysexgen.data.RangeType;
import entity.sysex.DataBlock;
import entity.sysex.IntegerDataBlock;
import lombok.Getter;
import lombok.Setter;

import java.sql.DatabaseMetaData;

public class Parameter {
    @Getter
    private String name;
    private Integer subAddress;
    @Getter
    private int numBytes;
    @Getter
    private ParameterType type;
    @Getter
    private String parameterIdentifier;
    @Getter
    private String description;
    @Getter
    private DataBlock defaultValues;
    @Getter
    private IntegerDataBlock rangeValues;
    @Getter
    private RangeType rangeType;

    // this will contain the actual value
    @Getter
    @Setter
    private DataBlock value;

    @Getter
    private DataBlock eventValues = null;

    @Getter
    private boolean ignore = false;

    public Parameter() {
        this.name = null;
        this.subAddress = null;
        this.numBytes = 1;                  // default 1 byte
        this.type = ParameterType.INLINE;   // default a parameters belonging to the sysex
        this.parameterIdentifier = null;    // a parameters identifier Number or a Name
        this.description = null;            // a description
        this.defaultValues = null;          // default value(s)
        this.rangeValues = null;            // the range
        this.rangeType = RangeType.CONTINUOUS;
        this.eventValues = null;

        this.value = null;          // default value(s)
        this.ignore = false;
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
        this.rangeType = builder.rangeType;
        this.eventValues = builder.eventValues;
        this.ignore = builder.ignore;
    }

    public static class ParameterBuilder {
        private String name = null;
        private Integer subAddress = null;
        private int numBytes = 1;
        private ParameterType type = ParameterType.INLINE;
        private String parameterIdentifier = null;
        private String description = "";
        private DataBlock defaultValues = null;
        private IntegerDataBlock rangeValues = null;
        private RangeType rangeType = RangeType.CONTINUOUS;
        private DataBlock eventValues = null;
        private boolean ignore;

        public ParameterBuilder clear() {
            this.name = null;
            this.subAddress = null;
            this.numBytes = 1;
            this.type = ParameterType.INLINE;
            this.parameterIdentifier = null;
            this.description = "";
            this.defaultValues = null;
            this.eventValues = null;
            this.rangeValues = null;
            this.rangeType = RangeType.CONTINUOUS;
            this.ignore = false;

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

        public ParameterBuilder ignore() {
            this.ignore = true;
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
            this.defaultValues = new DataBlock(value >> 7, value & 0X7F);
            return this;
        }

        public ParameterBuilder withEventValues(Integer val1, Integer val2) {
            this.eventValues = new DataBlock(val1, val2);
            return this;
        }

        public ParameterBuilder withEventValue(Integer value) {
            this.eventValues = new DataBlock(value >> 7, value & 0x7F);
            return this;
        }

        // It will be MSB and LSB
        public ParameterBuilder withRangeValues(Integer val1, Integer val2) {
            this.rangeValues = new IntegerDataBlock(val1, val2);
            return this;
        }

        public ParameterBuilder withRangeType(RangeType value) {
            this.rangeType = value;
            return this;
        }

        // It is only LSB
        public Parameter build() { return new Parameter(this); }
    }
}

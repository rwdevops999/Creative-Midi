package creative.scenes.sysexgen2.entity;

import creative.scenes.sysexgen2.data.ParameterType;
import lombok.Getter;
import lombok.Setter;

public class EventParameter {
    @Getter
    private int parameterId;
    private int numBytes;

    @Getter
    private DataBlock value;

    @Getter
    private boolean ignore;

    private boolean useLowerOnly = false;

    @Getter
    private String address;

    @Getter
    private ParameterType parameterType;

    public EventParameter() {
        this.value = null;
    }

    @Getter
    private DisplayBlock displayBlock;

    public EventParameter(EventParameterBuilder builder) {
        this.parameterId = builder.parameterId;
        this.numBytes = builder.numBytes;
        this.ignore = builder.ignore;
        this.useLowerOnly = builder.useLowerOnly;
        this.address = builder.address;
        this.parameterType = builder.parameterType;
        this.displayBlock = builder.displayBlock;
    }

    public void setValue(DataBlock dataBlock) {
        this.value = dataBlock;
    }

    public void setValue(Integer value, boolean isLittleEndian) {
        this.value = new DataBlock(value, isLittleEndian);
    }

    public void setValue(Integer i1, Integer i2) {
        this.value = new DataBlock(i1,i2);
    }

    public void setValue(Integer i1, Integer i2, boolean isLittleEndian) {
        this.value = new DataBlock(i1,i2, isLittleEndian);
    }

    public String toSysex() {
        if (value == null) {
            System.out.println("is NULL");
        };

        return (address != null ? address + " " : "") + value.toHex(numBytes, useLowerOnly);
    }

    public String toSysex(DataBlock value) {
        return value.toHex(numBytes, useLowerOnly);
    }

    public static class EventParameterBuilder {
        private String name;        // maybe needed in a finder
        private int parameterId;
        private int numBytes;
        private boolean ignore = false;
        private boolean useLowerOnly = false;
        private String address;

        private ParameterType parameterType;
        private DisplayBlock displayBlock;

        public EventParameterBuilder clear() {
            this.name = "";
            this.parameterId = -1;
            this.numBytes = 1;
            this.ignore = false;
            this.useLowerOnly = false;
            this.address = null;
            this.parameterType = ParameterType.PARAMETER;
            this.displayBlock = null;

            return this;
        }

        public EventParameterBuilder withAddress(String address) {
            this.address = address;
            return this;
        }

        public EventParameterBuilder withDisplayblock(DisplayBlock block) {
            this.displayBlock = block;
            return this;
        }

        public EventParameterBuilder isIgnore(boolean ignore) {
            this.ignore = ignore;
            return this;
        }

        public EventParameterBuilder withParameterType(ParameterType type) {
            this.parameterType = type;
            return this;
        }

        public EventParameterBuilder withParameterId(Integer id) {
            this.parameterId = id;
            return this;
        }

        public EventParameterBuilder withNumBytes(Integer num) {
            this.numBytes = num;
            this.useLowerOnly = false;
            return this;
        }

        public EventParameterBuilder withNumBytes(Integer num, boolean useLowerOnly) {
            this.numBytes = num;
            this.useLowerOnly = useLowerOnly;
            return this;
        }

        public EventParameterBuilder withUseLowerOnly(boolean value) {
            this.useLowerOnly = value;
            return this;
        }

        public EventParameter build() {
            return new EventParameter(this);
        }
    }
}

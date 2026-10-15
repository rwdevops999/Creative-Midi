package creative.scenes.sysexgen2.entity;

import creative.scenes.sysexgen2.data.ParameterType;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

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

    private Function<Integer, String> callback;

    public EventParameter(EventParameterBuilder builder) {
        this.parameterId = builder.parameterId;
        this.numBytes = builder.numBytes;
        this.ignore = builder.ignore;
        this.useLowerOnly = builder.useLowerOnly;
        this.address = builder.address;
        this.parameterType = builder.parameterType;
        this.displayBlock = builder.displayBlock;
        if (this.displayBlock != null) {
            this.value = this.displayBlock.getDefaultValue();
        }
        this.callback = builder.callback;
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

    public void clearValue() {
        this.value = null;
    }

    public String toSysex() {
        if (value == null) {
            System.out.println("is NULL");
        };

        if (callback != null) {
            return callback.apply(value.getValue());
        }

        return (address != null ? address + " " : "") + value.toHex(numBytes, useLowerOnly);
    }

    public String toSysex(DataBlock value) {
        if (callback != null) {
            return callback.apply(value.getValue());
        }

        return value.toHex(numBytes, useLowerOnly);
    }

    public static class EventParameterBuilder {
        private int parameterId;
        private int numBytes;
        private boolean ignore = false;
        private boolean useLowerOnly = false;
        private String address;

        private ParameterType parameterType;
        private DisplayBlock displayBlock;

        private Function<Integer, String> callback;

        public EventParameterBuilder clear() {
            this.parameterId = -1;
            this.numBytes = 1;
            this.ignore = false;
            this.useLowerOnly = false;
            this.address = null;
            this.parameterType = ParameterType.PARAMETER;
            this.displayBlock = null;
            this.callback = null;

            return this;
        }

        public EventParameterBuilder withAddress(String address) {
            this.address = address;
            return this;
        }

        public EventParameterBuilder withCallback(Function<Integer, String> callback) {
            this.callback = callback;
            return this;
        }

        public EventParameterBuilder withDisplayblock(DisplayBlock block) {
            this.displayBlock = block;
            if (block.getDefaultValue() != null) {
            }
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

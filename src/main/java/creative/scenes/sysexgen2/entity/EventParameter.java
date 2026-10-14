package creative.scenes.sysexgen2.entity;

import lombok.Getter;
import lombok.Setter;

public class EventParameter {
    @Getter
    private String name;    // USEFULL FOR TESTING...

    @Getter
    private int parameterId;
    private int numBytes;

    @Getter
    private DataBlock value;

    @Getter
    private boolean ignore;

    private boolean useLowerOnly = false;

    @Getter
    private boolean fixed = false;
    @Getter
    private DataBlock fixedValue = null;

    private String label;

    public EventParameter() {
        this.value = null;
    }

    public EventParameter(EventParameterBuilder builder) {
        this.name = builder.name;
        this.parameterId = builder.parameterId;
        this.numBytes = builder.numBytes;
        this.ignore = builder.ignore;
        this.useLowerOnly = builder.useLowerOnly;
        this.fixed = builder.fixed;
        this.fixedValue = builder.fixedValue;
        this.label = builder.label; // should replace row entry label
    }

    public void setValue(Integer value) {
        this.value = new DataBlock(value);
    }

    public void setValue(Integer i1, Integer i2) {
        this.value = new DataBlock(i1,i2);
    }

    public String toSysex() {
        return value.toHex(numBytes, useLowerOnly);
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
        private boolean fixed = false;
        private DataBlock fixedValue = null;
        private String label;

        public EventParameterBuilder clear() {
            this.name = "";
            this.parameterId = -1;
            this.numBytes = 1;
            this.ignore = false;
            this.useLowerOnly = false;
            this.fixed = false;
            this.fixedValue = null;
            this.label = null;

            return this;
        }

        public EventParameterBuilder isFixed(int value) {
            this.fixed = true;
            this.fixedValue = new DataBlock(value);
            return this;
        }

        public EventParameterBuilder isIgnore(boolean ignore) {
            this.ignore = ignore;
            return this;
        }

        public EventParameterBuilder withName(String name) {
            this.name = name;
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

        public EventParameterBuilder withLabel(String value) {
            this.label = value;
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

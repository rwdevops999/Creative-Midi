package creative.scenes.sysexgen2.entity;

import lombok.Getter;
import lombok.Setter;

public class EventParameter {
    @Getter
    private String name;

    @Getter
    private int parameterId;
    private int numBytes;

    @Getter
    private DataBlock value;

    public EventParameter() {
        this.value = null;
    }

    public EventParameter(EventParameterBuilder builder) {
        this.name = builder.name;
        this.parameterId = builder.parameterId;
        this.numBytes = builder.numBytes;
    }

    public void setValue(Integer value) {
        this.value = new DataBlock(value);
    }

    public void setValue(Integer i1, Integer i2) {
        this.value = new DataBlock(i1,i2);
    }

    public String toSysex() {
        return value.toHex(numBytes);
    }

    public static class EventParameterBuilder {
        private String name;        // maybe needed in a finder
        private int parameterId;
        private int numBytes;

        public EventParameterBuilder clear() {
            this.name = "";
            this.parameterId = -1;

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
            return this;
        }

        public EventParameter build() {
            return new EventParameter(this);
        }
    }
}

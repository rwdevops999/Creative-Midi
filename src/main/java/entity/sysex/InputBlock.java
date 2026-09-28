package entity.sysex;

import lombok.Getter;
import lombok.Setter;

public class InputBlock {
    @Getter
    Integer position;
    @Getter
    Integer numBytes;
    @Getter
    Boolean useMsbLsb;
    @Getter
    Integer parameterId;
    @Getter
    @Setter
    String parameter;
    @Getter
    @Setter
    String display;
    @Getter
    @Setter
    DataBlock inputValues;

    @Getter
    @Setter
    Integer min = 0;
    @Getter
    @Setter
    Integer max = 0x7F;
    @Getter
    @Setter
    ParametersTable refTable = null;

    public void populate() {
    }

    public InputBlock(InputBlockBuilder builder) {
        this.position = builder.position;
        this.numBytes = builder.numBytes;
        this.useMsbLsb = builder.useMsbLsb;
        this.parameterId = builder.parameterId;
        this.parameter = builder.parameter;
        this.display = builder.display;
        this.inputValues = builder.inputValues;
        this.min = builder.min;
        this.max = builder.max;
    }

    public static class InputBlockBuilder {
        Integer position;
        Integer numBytes;
        Boolean useMsbLsb;
        Integer parameterId;
        String parameter;
        String display;
        DataBlock inputValues;
        Integer min;
        Integer max;

        public InputBlockBuilder reset() {
            position = null;
            numBytes = null;
            useMsbLsb = false;
            parameterId = null;
            parameter = null;
            display = null;
            inputValues = null;
            min = null;
            max = null;
            return this;
        }

        public InputBlockBuilder withPosition(Integer position) {
            this.position = position;
            return this;
        }

        public InputBlockBuilder withNumBytes(Integer numBytes) {
            this.numBytes = numBytes;
            return this;
        }

        public InputBlockBuilder withLsbMsb(Boolean bval) {
            this.useMsbLsb = bval;
            return this;
        }

        public InputBlockBuilder withParameterId(Integer parameterId) {
            this.parameterId = parameterId;
            return this;
        }

        public InputBlockBuilder withParameter(String parameter) {
            this.parameter = parameter;
            return this;
        }

        public InputBlockBuilder withDisplay(String display) {
            this.display = display;
            return this;
        }

        public InputBlockBuilder withInputValues(Integer b1, Integer b2) {
            this.inputValues = new DataBlock(b1, b2);
            return this;
        }

        public InputBlockBuilder withMinMax(Integer min, Integer max) {
            this.min = min;
            this.max = max;
            return this;
        }

        public InputBlock build() {
            return new InputBlock(this);
        }
    }
}

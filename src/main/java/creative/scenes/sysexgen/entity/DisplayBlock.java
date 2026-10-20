package creative.scenes.sysexgen.entity;

import lombok.Getter;

@Getter
public class DisplayBlock {
    private String label;
    private String description;
    private DataBlock defaultValue;
    private Range range;

    public DisplayBlock() {
    }

    public DisplayBlock(DisplayBlockBuilder builder) {
        this.label = builder.label;
        this.description = builder.description;
        this.defaultValue = builder.defaultValue;
        this.range = builder.range;
    }

    public static class DisplayBlockBuilder {
        private String label;
        private String description;
        private DataBlock defaultValue;
        private Range range;

        public DisplayBlockBuilder clear() {
            return this;
        }

        public DisplayBlockBuilder withLabel(String label) {
            this.label = label;
            return this;
        }

        public DisplayBlockBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public DisplayBlockBuilder withDefaultValue(DataBlock defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public DisplayBlockBuilder withDefaultValue(Integer value) {
            this.defaultValue = new DataBlock(value);
            return this;
        }

        public DisplayBlockBuilder withDefaultValues(Integer i1, Integer i2) {
            this.defaultValue = new DataBlock(i1, i2);
            return this;
        }

        public DisplayBlockBuilder withRange(Range range) {
            this.range = range;
            return this;
        }

        public DisplayBlockBuilder withRange(int min, int max) {
            this.range = new Range(min, max);
            return this;
        }

        public DisplayBlock build() {
            return new DisplayBlock(this);
        }
    }
}

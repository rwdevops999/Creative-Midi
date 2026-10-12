package creative.scenes.sysexgen.entity;

import entity.sysex.DataBlock;
import lombok.Getter;

public class EventType {
    @Getter
    private String category;
    @Getter
    private String name;
    private String description;
    @Getter
    private DataBlock value;
    @Getter
    private ParameterTable parameterTable;

    public EventType(EventTypeBuilder builder) {
        this.name = builder.name;
        this.category = builder.category;
        this.description = builder.description;
        this.value = builder.value;
        this.parameterTable = builder.parameterTable;
    }

    public static class EventTypeBuilder {
        private String name;
        private String category;
        private String description;
        private DataBlock value;
        private ParameterTable parameterTable;

        public EventTypeBuilder clear() {
            this.name = null;
            this.category = null;
            this.description = null;
            this.value = null;
            this.parameterTable = null;

            return this;
        }

        public EventTypeBuilder withName(String value) {
            this.name = value;
            return this;
        }

        public EventTypeBuilder withCategory(String value) {
            this.category = value;
            return this;
        }

        public EventTypeBuilder withDescription(String value) {
            this.description = value;
            return this;
        }

        public EventTypeBuilder withValues(int value1, int value2) {
            this.value = new DataBlock(value1, value2);

            return this;
        }

        public EventTypeBuilder withValue(int value) {
            this.value = new DataBlock((value >> 7), (value & 0x7F));

            return this;
        }

        public EventTypeBuilder withParameterTable(ParameterTable value) {
            this.parameterTable = value;

            return this;
        }

        public EventType build() {
            return new EventType(this);
        }
    }
}

package creative.scenes.sysexgen2.entity;

import lombok.Getter;

public class EventType {
    @Getter
    private DataBlock eventValue;

    @Getter
    private ParameterTable parameterTable;

    @Getter
    private String category;

    @Getter
    private String name;            // USEFULL FOR TESTING...

    public EventType() {
    }

    public EventType(String category, String name) {
        this();

        this.category = category;
        this.name = name;
    }

    public EventType(EventTypeBuilder builder) {
        this(builder.category, builder.name);

        this.eventValue = builder.eventValue;
        this.parameterTable = builder.parameterTable;
        this.category = builder.category;
    }

    public static class EventTypeBuilder {
        private DataBlock eventValue;
        private ParameterTable parameterTable;
        private String category;
        private String name;

        public EventTypeBuilder clear() {
            this.eventValue = null;
            this.parameterTable = null;
            this.category = "";
            this.name = "";

            return this;
        }

        public EventTypeBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public EventTypeBuilder withCategory(String category) {
            this.category = category;
            return this;
        }

        public EventTypeBuilder withEventValue(DataBlock eventValue) {
            this.eventValue = eventValue;
            return this;
        }

        public EventTypeBuilder withEventValue(Integer value) {
            // Here the integer is split into MSB and LSB
            this.eventValue = new DataBlock(value);
            return this;
        }

        public EventTypeBuilder withEventValues(Integer i1, Integer i2) {
            // Here the integers are converted to byte
            this.eventValue = new DataBlock(i1, i2);
            return this;
        }

        public EventTypeBuilder withParameterTable(ParameterTable parameterTable) {
            this.parameterTable = parameterTable;
            return this;
        }

        public EventType build() {

            return new EventType(this);
        }
    }
}

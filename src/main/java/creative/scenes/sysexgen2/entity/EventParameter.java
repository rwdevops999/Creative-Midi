package creative.scenes.sysexgen2.entity;

import creative.scenes.sysexgen2.data.ParameterType;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
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

    @Getter
    @Setter
    private EventParameter linkedEventParameter;
    private Map<DataBlock, DataBlock> linkedValuesMap;

    private List<EventParameter> linkedEventParameterList;

    public EventParameter() {
        this.value = null;
        this.linkedValuesMap = new HashMap<>();
        this.linkedEventParameter = null;
        linkedEventParameterList = null;
    }

    @Getter
    private DisplayBlock displayBlock;

    private Function<Integer, String> callback;

    public EventParameter(EventParameterBuilder builder) {
        this();
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
        if (builder.linkedEventParameter != null) {
            if (linkedEventParameterList == null) {
                linkedEventParameterList = new ArrayList<>();
            }

            builder.linkedEventParameter.setLinkedEventParameter(this);
            linkedEventParameterList.add(builder.linkedEventParameter);
        };
    }

    public void setLinkedValue(DataBlock value) {
        if (linkedValuesMap.containsKey(this.value)) {
            linkedValuesMap.remove(this.value);
        }

        linkedValuesMap.put(this.value, value);
    }

    public EventParameter findLinkedEventParameterById(Integer identifier) {
        if (! linkedEventParameterList.isEmpty()) {
            return linkedEventParameterList.stream().filter(e -> e.getParameterId() == identifier).findFirst().orElse(null);
        }

        return null;
    }

    private void handleLinkedValue(DataBlock value) {
        if (linkedEventParameter != null) {
            linkedEventParameter.setLinkedValue(value);
        }
    }

    public void setValue(DataBlock dataBlock) {
        this.value = dataBlock;
        handleLinkedValue(this.value);
    }

    public void setValue(Integer value, boolean isLittle) {
        this.value = new DataBlock(value, isLittle);
        handleLinkedValue(this.value);
    }

    public void setValue(Integer i1, Integer i2) {
        this.value = new DataBlock(i1,i2);
        handleLinkedValue(this.value);
    }

    public void setValue(Integer i1, Integer i2, boolean isLittle) {
        this.value = new DataBlock(i1,i2, isLittle);
        handleLinkedValue(this.value);
    }

    public void clearValue() {
        this.value = null;
    }

    public String toSysex() {
        if (linkedValuesMap != null) {
            StringBuilder sb = new StringBuilder();

            for (Map.Entry<DataBlock, DataBlock> entry : linkedValuesMap.entrySet()) {
                sb
                        .append(entry.getKey().toHex(numBytes))
                        .append(" ")
                        .append(entry.getValue().toHex(numBytes))
                        .append(" ");
            }

            return sb.toString().trim();
        }

        if (callback != null && value != null) {
            return callback.apply(value.getValue());
        }

        if (value != null) {
            return (address != null ? address + " " : "") + value.toHex(numBytes, useLowerOnly);
        }

        return ""; // CHECK Normally we should never come here
    }

/*    public String toSysex(DataBlock value) {
        if (callback != null) {
            return callback.apply(value.getValue());
        }

        return value.toHex(numBytes, useLowerOnly);
    }
*/
    public static class EventParameterBuilder {
        private int parameterId;
        private int numBytes;
        private boolean ignore = false;
        private boolean useLowerOnly = false;
        private String address;

        private ParameterType parameterType;
        private DisplayBlock displayBlock;

        private Function<Integer, String> callback;
        private EventParameter linkedEventParameter;

        public EventParameterBuilder clear() {
            this.parameterId = -1;
            this.numBytes = 1;
            this.ignore = false;
            this.useLowerOnly = false;
            this.address = null;
            this.parameterType = ParameterType.PARAMETER;
            this.displayBlock = null;
            this.callback = null;
            this.linkedEventParameter = null;

            return this;
        }

        public EventParameterBuilder withAddress(String address) {
            this.address = address;
            return this;
        }

        public EventParameterBuilder withLinkedEventParameter(EventParameter linkedParameter) {
            this.linkedEventParameter = linkedParameter;
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

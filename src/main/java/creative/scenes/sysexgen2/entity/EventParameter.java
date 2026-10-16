package creative.scenes.sysexgen2.entity;

import creative.scenes.sysexgen2.data.ParameterType;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.Function;

import static util.Constants.BIG_ENDIAN;
import static util.Constants.LITTLE_ENDIAN;

public class EventParameter {
    @Getter
    private String name;

    @Getter
    private int endian = BIG_ENDIAN;

    @Getter
    @Setter
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
    private Map<Integer, DataBlock> linkedValuesMap;

    private List<EventParameter> linkedEventParameterList;

    @Getter
    private Integer controllerId = null;

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

        this.name = builder.name;

        this.parameterId = builder.parameterId;
        this.numBytes = builder.numBytes;
        this.ignore = builder.ignore;
        this.useLowerOnly = builder.useLowerOnly;
        this.address = builder.address;
        this.parameterType = builder.parameterType;
        this.endian = builder.endian;
        this.displayBlock = builder.displayBlock;
        if (this.displayBlock != null) {
            this.value = this.displayBlock.getDefaultValue();
        }
        this.callback = builder.callback;

        this.controllerId = builder.controllerId;

        if (builder.linkedEventParameterList != null) {
            if (linkedEventParameterList == null) {
                linkedValuesMap = new HashMap<>();
            }

            linkedEventParameterList = builder.linkedEventParameterList;
            linkedEventParameterList.forEach(ep -> ep.setLinkedEventParameter(this));
        };
    }

    public void setLinkedValue(Integer id, DataBlock value) {
        linkedValuesMap.remove(id);
        linkedValuesMap.put(id, value);
    }

    public EventParameter findLinkedEventParameterByName(String name) {
        if (! linkedEventParameterList.isEmpty()) {
            return linkedEventParameterList.stream().filter(e -> name.equals(e.getName())).findFirst().orElse(null);
        }

        return null;
    }

/*    public EventParameter findLinkedEventParameterByControllerId(Integer controllerId) {
        if (! linkedEventParameterList.isEmpty()) {
            return linkedEventParameterList.stream().filter(e -> e.getControllerId() == controllerId).findFirst().orElse(null);
        }

        return null;
    }
*/

    private void handleLinkedValue(Integer controllerId, DataBlock value) {
        if (controllerId == null) {
            return;
        }

        if (value != null) {
            if (linkedEventParameter != null) {
                linkedEventParameter.setLinkedValue(controllerId, value);
            } else {
                setLinkedValue(controllerId, value);
            }
        }
    }

    public void setValue(DataBlock dataBlock) {
        if (dataBlock != null) {
            this.value = dataBlock;
            handleLinkedValue(controllerId, this.value);
        }
    }

    public void setValue(Integer value) {
        this.value = new DataBlock(value);
        handleLinkedValue(controllerId, this.value);
    }

    public void setValue(Integer i1, Integer i2) {
        this.value = new DataBlock(i1,i2);
        handleLinkedValue(controllerId, this.value);
    }

    public void clearValue() {
        this.value = null;
        handleLinkedValue(parameterId, this.value);
    }

    public String toSysex() {
        if (linkedValuesMap != null && ! linkedValuesMap.isEmpty()) {
            StringBuilder sb = new StringBuilder();

            for (Map.Entry<Integer, DataBlock> entry : linkedValuesMap.entrySet()) {
                sb
                        .append(String.format("%02X", entry.getKey()))
                        .append(" ")
                        .append(entry.getValue().toHex(numBytes, this.endian))
                        .append(" ");
            }

            return sb.toString().trim();
        }

        if (callback != null && value != null) {
            return callback.apply(value.getValue());
        }

        if (value != null) {
            return (address != null ? address + " " : "") + value.toHex(numBytes, useLowerOnly, this.endian);
        }

        return ""; // CHECK Normally we should never come here
    }

    public static class EventParameterBuilder {
        private String name;
        private int parameterId;
        private int numBytes;
        private boolean ignore = false;
        private boolean useLowerOnly = false;
        private String address;
        private int endian = BIG_ENDIAN;

        private ParameterType parameterType;
        private DisplayBlock displayBlock;

        private Function<Integer, String> callback;
        private List<EventParameter> linkedEventParameterList = new ArrayList<>();

        private Integer controllerId;

        public EventParameterBuilder clear() {
            this.parameterId = -1;
            this.numBytes = 1;
            this.ignore = false;
            this.useLowerOnly = false;
            this.address = null;
            this.parameterType = ParameterType.PARAMETER;
            this.displayBlock = null;
            this.callback = null;
            this.linkedEventParameterList = new ArrayList<>();
            this.name = "";
            this.endian = BIG_ENDIAN;
            this.controllerId = null;

            return this;
        }

        public EventParameterBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public EventParameterBuilder withAddress(String address) {
            this.address = address;
            return this;
        }

        public EventParameterBuilder withEndian(int endian) {
            this.endian = endian;
            return this;
        }

        public EventParameterBuilder withControllerId(int controllerId) {
            this.controllerId = controllerId;
            return this;
        }

        public EventParameterBuilder withLinkedEventParameter(EventParameter linkedParameter) {
            this.linkedEventParameterList.add(linkedParameter);
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

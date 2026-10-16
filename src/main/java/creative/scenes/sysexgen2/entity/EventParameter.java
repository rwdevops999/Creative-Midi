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

    private List<EventParameter> linkedEventParameterList;

    private Map<String, List<String>> linkedResultList;

    @Getter
    private Integer controllerId = null;

    public EventParameter() {
        this.value = null;
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
            linkedEventParameterList = builder.linkedEventParameterList;
            linkedEventParameterList.forEach(ep -> ep.setLinkedEventParameter(this));
            linkedResultList = new HashMap<>();
        };
    }

    public boolean hasLinkedParameters() {
        return linkedEventParameterList != null && !linkedEventParameterList.isEmpty();
    }

    public void setLinkedValue(Integer id, DataBlock value) {
//        linkedValuesMap.remove(id);
//        linkedValuesMap.put(id, value);
    }

    public EventParameter findLinkedEventParameterByName(String name) {
        if (! linkedEventParameterList.isEmpty()) {
            return linkedEventParameterList.stream().filter(e -> name.equals(e.getName())).findFirst().orElse(null);
        }

        return null;
    }

    public void setValueInternal(Integer value) {
        this.value = new DataBlock(value);
    }

    public void clearValue() {
        this.value = null;
    }

    public void addLinkedValue(String strvalue) {
        String key = String.format("%02X %s", controllerId, value.toHex(numBytes, endian));

        if (linkedResultList!= null && linkedResultList.containsKey(key)) {
            List<String> entries = linkedResultList.get(key);
            if (! entries.contains(strvalue)) {
                entries.add(strvalue);  // is this by reference ???
            }
        }
    }

    private void handleLinkedValues() {
        String key = String.format("%02X %s", controllerId, value.toHex(numBytes, endian));
        if (linkedEventParameterList != null) {
            if (! linkedResultList.containsKey(key)) {
                linkedResultList.put(key, new ArrayList<>());
                for (EventParameter ep : linkedEventParameterList) {
                    ep.clearValue();
                }
            } else {
                List<String> linkedValues = linkedResultList.get(key);
                for (String keyValueCombination : linkedValues) {
                    String[] splitted = keyValueCombination.split(" ");
                    int cid = Integer.parseInt(splitted[0]);
                    int value = Integer.parseInt(splitted[1]);

                    for (EventParameter ep : linkedEventParameterList) {
                        if (ep.getControllerId() == cid) {
                            ep.setValueInternal(value);
                        } else {
                            ep.clearValue();
                        }
                    }
                }
            }
        }

        if (linkedEventParameter != null) {
            linkedEventParameter.addLinkedValue(key);
        }
    }

    public void setValue(DataBlock dataBlock) {
        if (dataBlock != null) {
            this.value = dataBlock;
            handleLinkedValues();
        }
    }

    public void setValue(Integer value) {
        this.value = new DataBlock(value);
        handleLinkedValues();
    }

    public void setValue(Integer i1, Integer i2) {
        this.value = new DataBlock(i1,i2);
        handleLinkedValues();
    }

    public String toSysex() {
        StringBuilder sb = new StringBuilder();

        if (value != null) {
            if (linkedEventParameterList != null) {
                List<String> keylist = linkedResultList.keySet().stream().sorted().toList();
                for (String key : keylist) {
                    List<String> values = linkedResultList.get(key).stream().sorted().toList();

                    if (! values.isEmpty() ) {
                        sb.append(key).append(" ");
                        for (String value : values) {
                            sb.append(value).append(" ");
                        }
                    }
                }

                System.out.println("CHECK");

/*                String key = String.format("%02X %s", controllerId, value.toHex(numBytes, endian));

                List<String> possibleSysexs = linkedResultList.get(key);
                if (! possibleSysexs.isEmpty()) {
                    sb.append(key).append(" ");
                }

                for (String possibleSysex : possibleSysexs) {
                    sb.append(possibleSysex).append(" ");
                }
*/
                return sb.toString().trim();
            }

            if (callback != null) {
                sb.append(callback.apply(value.getValue()));
            } else {
                sb.append(address != null ? address + " " : "").append(value.toHex(numBytes, useLowerOnly, this.endian));
            }
        }

        return sb.toString();
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
        private List<EventParameter> linkedEventParameterList = null;

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
            this.linkedEventParameterList = null;
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
            if (linkedEventParameterList == null) {
                linkedEventParameterList = new ArrayList<>();
            }

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

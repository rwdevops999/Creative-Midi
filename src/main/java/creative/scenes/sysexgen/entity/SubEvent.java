package creative.scenes.sysexgen.entity;

import creative.scenes.sysexgen.data.ParameterType;
import entity.sysex.DataBlock;
import entity.sysex.InputBlock;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class SubEvent {
    @Setter
    private Object parent;
    @Getter
    private String name;
    private String sysex;
    @Getter
    private List<Parameter> parameters;
    @Getter
    private List<EventType> eventTypes;
    @Getter
    private List<SubEvent> children;

    public SubEvent(SubEventBuilder builder) {
        this.name = builder.name;
        this.sysex = builder.sysex;
        this.parameters = builder.parameters;
        this.eventTypes = builder.eventTypes;
        this.children = builder.children;
        if (this.children != null) {
            this.children.forEach(child -> child.setParent(this));
        }
    }

    public String createSysex() {
        StringBuilder sysexBuilder = new StringBuilder();

        if (this.parent instanceof SysexRoot sysexRoot) {
            return sysexRoot.buildSysex(this.sysex);
        }

        return "TO BE CREATED";
    }

    public List<String> createSysexList (List<InputBlock> inputList) {
        List<String> sysexList = new ArrayList<>();

        if (parent instanceof SysexRoot sysexRoot) {
            // TODO pass the list?
            return sysexRoot.generateSysEx(this.sysex, inputList);
        }

        return sysexList;
    }

    public boolean hasChildren() {
        return this.children != null && !this.children.isEmpty();
    }

    public List<String> getCategories() {
        return eventTypes.stream().map(EventType::getCategory).distinct().toList();
    }

    public List<EventType> getTypesOfCategory(String category) {
        return eventTypes.stream().filter(type -> type.getCategory().equals(category)).toList();
    }

    public Parameter findDefaultParameter() {
        Parameter result = null;

        if (parameters != null && ! parameters.isEmpty()) {
            result = parameters.stream().filter(parameter -> parameter.getType().equals(ParameterType.TYPE)).findFirst().orElse(null);
        }

        return result;
    }

    public EventType findDefaultEventType(DataBlock values) {
        return eventTypes.stream().filter(eventType -> eventType.getValue().getB1().equals(values.getB1()) && eventType.getValue().getB2().equals(values.getB2())).findFirst().orElse(null);
    }

    public EventType findLinkedEventType(DataBlock values) {
        //        return eventTypes.stream().filter(eventType -> eventType.getValue().getB1().equals(values.getB1()) && eventType.getValue().getB2().equals(values.getB2())).findFirst().orElse(null);
        for (EventType eventType : eventTypes) {
            DataBlock db1 = eventType.getValue();
            DataBlock db2 = values;
        }

        return eventTypes.stream().filter(eventType -> eventType.getValue().getB1().equals(values.getB1()) && eventType.getValue().getB2().equals(values.getB2())).findFirst().orElse(null);
    }

    public static class SubEventBuilder {
        private String name;
        private String sysex;
        private List<Parameter> parameters;
        private List<EventType> eventTypes;
        private List<SubEvent> children;

        public SubEventBuilder clear() {
            this.name = null;
            this.sysex = null;
            this.parameters = new ArrayList<>();
            this.eventTypes = new ArrayList<>();
            this.children = new ArrayList<>();

            return this;
        }

        public SubEventBuilder withName(String value) {
            this.name = value;
            return this;
        }

        public SubEventBuilder withSysex(String value) {
            this.sysex = value;
            return this;
        }

        public SubEventBuilder withParameter(Parameter param) {
            if (this.parameters == null) {
                this.parameters = new ArrayList<>();
            }

            this.parameters.add(param);

            return this;
        }

        public SubEventBuilder withEventType(EventType value) {
            if (this.eventTypes == null) {
                this.eventTypes = new ArrayList<>();
            }

            this.eventTypes.add(value);

            return this;
        }

        public SubEventBuilder addChild(SubEvent value) {
            if (this.children == null) {
                this.children = new ArrayList<>();
            }

            this.children.add(value);

            return this;
        }

        public SubEvent build() {
            return new SubEvent(this);
        }
    }

}

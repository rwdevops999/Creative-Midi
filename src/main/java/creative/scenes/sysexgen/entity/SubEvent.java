package creative.scenes.sysexgen.entity;

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
    private List<Parameter> parameters;
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

    public boolean hasChildren() {
        return this.children != null && !this.children.isEmpty();
    }

    public List<String> getCategories() {
        return eventTypes.stream().map(EventType::getCategory).distinct().toList();
    }

    public List<EventType> getTypesOfCategory(String category) {
        return eventTypes.stream().filter(type -> type.getCategory().equals(category)).toList();
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
            this.parameters = null;
            this.eventTypes = null;
            this.children = null;

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

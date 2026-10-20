package creative.scenes.sysexgen.entity;

import creative.scenes.sysexgen.data.ParameterType;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Event implements IEvent {
    @Setter
    @Getter
    private Event parent;

    @Getter
    private String name;    // This comes in a combo box

    private String sysex;

    @Getter
    private List<Event> children = new ArrayList<>();
    private DataBlock defaultValue;

    @Getter
    private List<EventParameter> parameters;
    private List<EventType> eventTypes;

    private boolean inline = true;

    @Getter
    @Setter
    private Event selectedEvent;

    public Event() {
        this.eventTypes = new ArrayList<>();
        this.children = new ArrayList<>();
        this.parameters = new ArrayList<>();
        this.sysex = "";

        this.selectedEvent = null;
    }

    public Event(String name) {
        this();
        if (this.name == null || this.name.isEmpty()) {
            this.name = name;
        }
    }

    public Event(EventBuilder builder) {
        this(builder.name);

        this.sysex = builder.sysex;

        this.children.addAll(builder.children);
        this.children.stream().filter(c -> c.getParent() == null).forEach(c -> c.setParent(this));

        this.defaultValue = builder.defaultValue;

        this.eventTypes.addAll(builder.eventTypes);
        this.parameters.addAll(builder.parameters);
        postProcess(this.parameters);

        this.inline = builder.inline;
    }

    public EventType getFirstEventTypeOfCategory(String category) {
        return eventTypes.stream().filter(et -> category.equals(et.getCategory())).findFirst().orElse(null);
    }

    private void postProcess(List<EventParameter> parameters) {
        for (EventParameter parameter : parameters) {
            if (ParameterType.TYPE.equals(parameter.getParameterType())) {
                parameter.setValue(defaultValue);
            } else {
                int parameterId = parameter.getParameterId();

                if (hasEventTypes()) {
                    EventType eventType = findEventTypeFromDefaultValue();

                    ParameterTable pt = eventType.getParameterTable();
                    for (ParametersTableEntry entry : pt.getEntries()) {
                        if (entry.getParameterId() == parameterId) {
                            parameter.setValue(new DataBlock(entry.getDefaultValue()));
                        }
                    }
                } else {
                    parameter.setValue(defaultValue);
                }
            }
        }
    }

    public void setDefaultValue(DataBlock value) {
        this.defaultValue = value;
    }

    public void setDefaultValue(Integer value) {
        this.defaultValue = new DataBlock(value);
    }

    public void setDefaultValues(Integer i1, Integer i2) {
        this.defaultValue = new DataBlock(i1, i2);
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public boolean hasEventTypes() {
        return ((eventTypes != null) && (!eventTypes.isEmpty()));
    }

    public void addChild(Event child) {
        child.setParent(this);

        children.add(child);
    }

    public boolean hasParameters() {
        return !parameters.isEmpty();
    }

    public EventParameter findEventParameterByName(String name) {
        EventParameter parameter = null;

        if (hasParameters()) {
            if (name != null && !name.isEmpty()) {
                parameter = this.parameters.stream().filter(ep -> name.equals(ep.getName())).findFirst().orElse(null);
            }
        }

        return parameter;
    }

    public EventParameter findEventParameterById(int id) {
        EventParameter parameter = null;

        if (hasParameters()) {
            parameter = this.parameters.stream().filter(ep -> ep.getParameterId() == id).findFirst().orElse(null);
        }

        return parameter;
    }

    private boolean hasParent() {
        return parent != null;
    }

    public Event findEvent(String name) {
        Event result = null;

        if (hasChildren()) {
            result = children.stream().filter(e -> name.equals(e.getName())).findAny().orElse(null);
            if (result == null) {
                for (Event child : children) {
                    result = child.findEvent(name);
                    if (result != null) {
                        break;
                    }
                }
            }
        }

        return result;
    }

    public List<String> createSysexStrings() {
        List<String> sysexStrings = new ArrayList<>();

        if (this.hasParent()) {
            List<String> parentSysexStrings = new ArrayList<>(this.parent.createSysexStrings());
            parentSysexStrings.replaceAll(s -> s.length() == 0 ? this.sysex : s + extend(this.sysex));
            sysexStrings.addAll(parentSysexStrings);
        } else {
            sysexStrings.add(this.sysex);
        }

        List<String> parameterSysexList = new ArrayList<>();
        if (hasEventTypes()) {
            EventType eventType = findEventTypeFromDefaultValue();
            if (eventType != null) {


                ParameterTable parameterTable = eventType.getParameterTable();
                if (parameterTable != null) {
                    for (ParametersTableEntry entry :parameterTable.getEntries()) {
                        int parameterId = entry.getParameterId();
                        if (hasParameters()) {
                            EventParameter parameter = findEventParameterById(parameterId);
                            if (parameter != null) {
                                if (!parameter.isIgnore()) {
//                                    if (parameter.getValue() != null) {
                                        parameterSysexList.add(parameter.toSysex());
//                                    }
                                } else {
                                    parameterSysexList.add("00");
                                }
                            }
                        }
                    }

                    // handle Type Parameters
                    if (hasParameters()) {
                        EventParameter typeParameter = parameters.stream().filter(p -> p.getParameterType().equals(ParameterType.TYPE)).findAny().orElse(null);
                        if (typeParameter != null && typeParameter.getValue() != null) {
                            parameterSysexList.add(typeParameter.toSysex());
                        }
                    }
                }
            }
        }

        // Handle Free Parameters
        if (hasParameters()) {
            // handle Free Parameters
            List<EventParameter> freeParameters = parameters.stream().filter(p -> p.getParameterType().equals(ParameterType.FREE)).toList();
            if (!freeParameters.isEmpty()) {
                freeParameters.forEach(p -> {
                    // QUESTION: Can free parameters be ignored ?
                    if (p.getValue() != null) {
                        parameterSysexList.add(p.toSysex());
                    }
                });
            }
        }


        if (! parameterSysexList.isEmpty() ) {
            if (! inline ) {
                if (!sysexStrings.isEmpty()) {
                    String baseSysex = sysexStrings.get(0);

                    sysexStrings.clear();
                    for (String sysex : parameterSysexList) {
                        sysexStrings.add(baseSysex + extend(sysex));
                    }
                }
            } else {
                for (String parameterSysex : parameterSysexList) {
                    sysexStrings.replaceAll(s -> s.length() == 0 ? parameterSysex : s + extend(parameterSysex));
                }
            }
        }

        return sysexStrings.stream().sorted().toList();
    }

    private String sortStringInternally(String input) {
        return input.chars()
                .sorted()
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());
    }

    public List<String> complete(List<String> sysexStrings) {
        sysexStrings.replaceAll(s -> SYSEX_START + extend(s) + extend(SYSEX_END));

        return sysexStrings;
    }

    /**
     * Find the EventType where defaultValue references to.
     *
     * @return the EventType oor null (if not found)
     */
    public EventType findEventTypeFromDefaultValue() {
        EventType result = null;

        if (hasEventTypes()) {
            result = eventTypes.stream().filter(et1 -> et1.getEventValue() != null).filter(et2 -> et2.getEventValue().getValue().equals(defaultValue.getValue())).findFirst().orElse(null);
        }

        return result;
    }

    /**
     * Get a list of all categories
     *
     * @return a list of categorynames or an empty list
     */
    public List<String> getEventTypeCategories() {
        List<String> result = new ArrayList<>();

        if (hasEventTypes()) {
            result = eventTypes.stream().map(EventType::getCategory).distinct().toList();
        }

        return result;
    }

    /**
     * Get the list of EventTypes of the category
     *
     * @param category the category name where we want to get the EventTypes of.
     *
     * @return a List of event types or an empty list
     */
    public List<EventType> getEventTypesOfCategory(@NotNull String category) {
        List<EventType> result = new ArrayList<>();

        if (hasEventTypes()) {
            result = eventTypes.stream().filter(et -> category.equals(et.getCategory())).toList();
        }
        return result;
    }
    public static class EventBuilder {
        private String name;
        private String sysex;
        private DataBlock defaultValue; // is used to find the eventType
        private List<Event> children;
        private List<EventType> eventTypes;
        private List<EventParameter> parameters;
        private boolean inline;

        public EventBuilder clear() {
            this.name = "";
            this.sysex = "";
            this.defaultValue = null;
            this.children = new ArrayList<>();
            this.eventTypes = new ArrayList<>();
            this.parameters = new ArrayList<>();
            this.inline = true;

            return this;
        }

        public EventBuilder withName(String value) {
            this.name = value;
            return this;
        }

        public EventBuilder isNotInline() {
            this.inline = false;
            return this;
        }

        public EventBuilder withSysex(String value) {
            this.sysex = value;
            return this;
        }

        public EventBuilder withDefaultValue(DataBlock value) {
            this.defaultValue = value;
            return this;
        }

        public EventBuilder withDefaultValue(Integer value) {
            this.defaultValue = new DataBlock(value);
            return this;
        }

        public EventBuilder withDefaultValues(Integer i1, Integer i2) {
            this.defaultValue = new DataBlock(i1, i2);
            return this;
        }

        public EventBuilder withEventType(EventType value) {
            this.eventTypes.add(value);
            return this;
        }

        public EventBuilder withEventParameter(EventParameter parameter) {
            this.parameters.add(parameter);
            return this;
        }

        public EventBuilder withChildren(List<Event> values) {
            this.children = values;

            return this;
        }

        public EventBuilder withChild(Event value) {
            this.children.add(value);

            return this;
        }

        public Event build() {
            return new Event(this);
        }
    }
}

package creative.scenes.eventlist.data;

import java.util.*;

public class EventKey {
    private static List<EventKeyValue> keys = new ArrayList<>();

    private static List<EventKeyValue> values = new ArrayList<>();

    static {
        keys.addAll(Arrays.asList(EventKeyValue.values()));
    }

    public EventKey(EventKeyValue ...values) {
        this.values.addAll(Arrays.asList(values));
    }

    public boolean hasValue(EventKeyValue target) {
        return this.values.contains(target);
    }

    public void addValue(EventKeyValue value) {
        if (! hasValue(value)) {
            this.values.add(value);
        }
    }

    public EventKeyValue getFirstElement() {
        return this.values.get(0);
    }

    public EventKeyValue getLastElement() {
        return this.values.get(this.values.size() - 1);
    }

    public static List<EventKeyValue> getEvents(EventKeyValue type) {
        return keys.stream().filter(k -> (! k.name().equals(type.name())) && (k.name().contains(type.name()))).toList();
    }
}

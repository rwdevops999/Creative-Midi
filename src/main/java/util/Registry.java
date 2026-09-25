package util;

import javafx.scene.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Registry {
    private final static Map<String, BiConsumer<Node, Object>> consumerRegistry = new HashMap<>();
    private final static Map<String, Node> nodeRegistry = new HashMap<>();

    public static void register(String name, Node node, BiConsumer<Node, Object> consumer) {
        nodeRegistry.put(name, node);
        consumerRegistry.put(name, consumer);
    }

    public static void unregister(String name) {
        nodeRegistry.remove(name);
        consumerRegistry.remove(name);
    }

    public static void publish(String name, Object data) {
        if (nodeRegistry.containsKey(name)) {
            consumerRegistry.get(name).accept(nodeRegistry.get(name), data);
        }
    }
}

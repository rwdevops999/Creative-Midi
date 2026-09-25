package util;

import javafx.scene.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Registry {
    private final static Map<String, BiConsumer<Node, Object>> consumerregistry = new HashMap<>();
    private final static Map<String, Node> noderegistry = new HashMap<>();

    public static void register(String name, Node node, BiConsumer<Node, Object> consumer) {
        noderegistry.put(name, node);
        consumerregistry.put(name, consumer);
    }

    public static void unregister(String name) {
        noderegistry.remove(name);
        consumerregistry.remove(name);
    }

    public static void publish(String name, Object data) {
        if (noderegistry.containsKey(name)) {
            consumerregistry.get(name).accept(noderegistry.get(name), data);
        }
    }
}

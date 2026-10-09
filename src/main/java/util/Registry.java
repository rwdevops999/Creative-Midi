package util;

import javafx.scene.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Registry {
    private final static Map<String, List<Node>> nodeRegistry = new HashMap<>();
    private final static Map<String, List<BiConsumer<Node, Object>>> consumerRegistry = new HashMap<>();

    public static void register(String name, Node node, BiConsumer<Node, Object> consumer) {
        System.out.println("REGISTER");
        if (! nodeRegistry.containsKey(name)) {
            nodeRegistry.put(name, new ArrayList<>());
            consumerRegistry.put(name, new ArrayList<>());
        }

        nodeRegistry.get(name).add(node);
        consumerRegistry.get(name).add(consumer);
    }

    public static void unregister(String name) {
        if (nodeRegistry.containsKey(name)) {
            nodeRegistry.remove(name);
            consumerRegistry.remove(name);
        }
    }

    public static void publish(String name, Object data, Boolean deviceSet) {
        List<Node> nodes = nodeRegistry.get(name);
        List<BiConsumer<Node, Object>> consumers = consumerRegistry.get(name);

        if (nodes != null && consumers != null) {
            int size = Math.min(nodes.size(), consumers.size());
            for (int i = 0; i < size; i++) {
                Node node = nodes.get(i);
                BiConsumer<Node, Object> consumer = consumers.get(i);

                consumer.accept(node, data);
            }
        }
    }
}

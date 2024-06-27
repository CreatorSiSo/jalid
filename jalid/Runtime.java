package jalid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class Runtime {
    public <T> Signal<T> createSignal(T value) {
        nodes.put(nextId, new ReactiveNode(nextId, value));
        return new Signal<T>(nextId++, this);
    }

    <T> void update(int nodeId, UnaryOperator<T> updateFn) {
        ReactiveNode node = nodes.get(nodeId);
        node.value = updateFn.apply((T) node.value);

        for (Integer subscriberId : subscribers.get(nodeId)) {
            update(subscriberId, updateFn);
        }

        node.state = ReactiveNode.State.Dirty;
    }

    <T> T get(int nodeId) {
        return (T) nodes.get(nodeId).value;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " {\n"
                + "  nodes: [\n"
                + "    " + nodes.entrySet()
                        .stream()
                        .map(entry -> entry.getKey() + " = "
                                + entry.getValue()
                                        .toString()
                                        .lines()
                                        .collect(Collectors.joining("\n    ")))
                        .collect(Collectors.joining(",\n    "))
                + "\n  ],\n"
                + "  nextId: " + nextId + "\n"
                + "}";
    }

    int nextId = 1;
    HashMap<Integer, ReactiveNode> nodes = new HashMap<>();
    HashMap<Integer, ArrayList<Integer>> subscribers = new HashMap<>();
}

class ReactiveNode {
    <T> ReactiveNode(int id, T value) {
        this.id = id;
        this.state = State.Clean;
        this.value = value;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " {\n"
                + "  id: " + id + ",\n"
                + "  state: " + state + ",\n"
                + "  value: " + value + ",\n"
                + "}";
    }

    enum State {
        Clean,
        Dirty,
    }

    int id;
    State state;
    Object value;
}

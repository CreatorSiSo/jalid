package jalid;

import java.util.HashMap;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class Runtime {
    public <T> Signal<T> createSignal(T value) {
        nodes.put(nextId, new ReactiveNode(nextId, value));
        return new Signal<T>(nextId++, this);
    }

    <T> void update(int id, UnaryOperator<T> updateFn) {
        ReactiveNode node = nodes.get(id);
        node.value = updateFn.apply((T) node.value);
        node.state = ReactiveNode.State.Dirty;
    }

    <T> T get(int id) {
        return (T) nodes.get(id).value;
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

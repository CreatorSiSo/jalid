package jalid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Runtime {
    public <T> Signal<T> createSignal(T value) {
        nodes.put(nextId, ReactiveNode.signal(nextId, value));
        return new Signal<T>(nextId++, this);
    }

    public <T> void createEffect(Runnable fn) {
        ReactiveNode node = ReactiveNode.effect(nextId, fn);
        nodes.put(nextId, node);
        this.registerProperty(new ScopeProperty(nextId++, ScopePropertyType.Effect));
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

    void registerProperty(ScopeProperty property) {
        ArrayList<ScopeProperty> entry = this.properties.getOrDefault(property.id(), new ArrayList<>());
        entry.add(property);
    }

    /*
     * Returns new value of node.
     */
    <T> T updateValue(int nodeId, Function<T, T> fn) {
        ReactiveNode node = nodes.get(nodeId);
        T result = fn.apply((T) node.value);
        node.value = result;

        this.markDirty(nodeId);
        this.runEffects();

        return result;
    }

    void markDirty(int nodeId) {
    }

    void runEffects() {
    }

    <T> T get(int nodeId) {
        return (T) nodes.get(nodeId).value;
    }

    int nextId = 1;
    HashMap<Integer, ReactiveNode> nodes = new HashMap<>();
    HashMap<Integer, ArrayList<Integer>> subscribers = new HashMap<>();
    HashMap<Integer, ArrayList<ScopeProperty>> properties = new HashMap<>();
}

class ReactiveNode {
    public static <T> ReactiveNode signal(int id, T value) {
        return new ReactiveNode(id, value, Type.Signal, null);
    }

    public static ReactiveNode effect(int id, Runnable fn) {
        return new ReactiveNode(id, null, Type.Effect, fn);
    }

    public void run() {
        if (this.fn != null) {
            this.fn.run();
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " {\n"
                + "  id: " + id + ",\n"
                + "  state: " + state + ",\n"
                + "  value: " + value + ",\n"
                + "  type: " + type + ",\n"
                + "  fn: " + fn + ",\n"
                + "}";
    }

    private <T> ReactiveNode(int id, T value, Type type, Runnable fn) {
        this.id = id;
        this.state = State.Clean;
        this.value = value;
        this.type = type;
        this.fn = fn;
    }

    private enum State {
        Clean,
        Dirty,
    }

    private enum Type {
        Signal,
        Effect,
    }

    private int id;
    private State state;
    Object value;
    private Type type;
    private Runnable fn;
}

enum ScopePropertyType {
    Signal,
    Effect,
}

record ScopeProperty(int id, ScopePropertyType type) {
}

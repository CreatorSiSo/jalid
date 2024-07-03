package jalid;

import java.util.HashSet;
import java.util.function.Function;

public class Signal<T> extends ReactiveNode {
    public static <T> Signal<T> create(T value) {
        Signal<T> result = new Signal<>();
        result.value = value;
        return result;
    }

    public T get() {
        this.subscribe();
        return this.value;
    }

    public void set(T value) {
        this.update(prev -> value);
    }

    public void update(Function<T, T> fn) {
        this.value = fn.apply(value);

        for (Effect sub : subscriptions) {
            sub.execute();
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    private void subscribe() {
        if (context.size() == 0) {
            return;
        }
        Effect running = context.getLast();
        subscriptions.add(running);
        running.dependencies.add(subscriptions);
    }

    private Signal() {
        super();
    }

    private T value;
    private HashSet<Effect> subscriptions = new HashSet<>();
}

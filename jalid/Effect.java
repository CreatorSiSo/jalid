package jalid;

import java.util.HashSet;

public class Effect extends ReactiveNode {
    public static Effect create(Runnable fn) {
        Effect result = new Effect(fn);
        result.execute();
        return result;
    }

    public void execute() {
        cleanup();
        context.add(this);
        try {
            this.fn.run();
        } finally {
            context.removeLast();
        }
    }

    private void cleanup() {
        for (HashSet<Effect> dep : dependencies) {
            dep.remove(this);
        }
        this.dependencies.clear();
    }

    private Effect(Runnable fn) {
        super();
        this.fn = fn;
    }

    Runnable fn;
    HashSet<HashSet<Effect>> dependencies = new HashSet<>();
}

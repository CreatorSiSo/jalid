package jalid;

public class Signal<T> {
    public Signal(int id, Runtime owner) {
        this.id = id;
        this.owner = owner;
    }

    public void set(T value) {
        owner.update(id, prev -> value);
    }

    public T get() {
        return owner.get(id);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + id + ")";
    }

    private int id;
    private Runtime owner;
}

package test;

import jalid.Effect;
import jalid.Signal;

public class Reactive {
    public static void main(String[] args) {
        Signal<Boolean> bool = Signal.create(true);
        Signal<String> title = Signal.create("Window Title");

        Effect.create(() -> {
            System.out.println("bool = " + bool.get());
            System.out.println("title = " + title.get());
        });

        bool.set(false);
        bool.update(prev -> prev ? false : true);
        title.update(prev -> prev.replace("Window", "Changed"));
    }
}

package test;

import jalid.Runtime;
import jalid.Signal;

public class HelloWindow {
    public static void main(String[] args) {
        Runtime runtime = new Runtime();
        Signal<Boolean> boolSignal = runtime.createSignal(true);
        runtime.createSignal("Window Title");
        System.out.println(runtime);
        System.out.println("boolSignal = " + boolSignal);
        System.out.println("bool = " + boolSignal.get());
        boolSignal.set(false);
        System.out.println("boolSignal = " + boolSignal);
        System.out.println("bool = " + boolSignal.get());
        System.out.println(runtime);
    }
}

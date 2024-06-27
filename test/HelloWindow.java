package test;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import jalid.Runtime;
import jalid.Signal;

public class HelloWindow {
    public static void main(String[] args) {
        Runtime runtime = new Runtime();

        Frame frame = new Frame("Hello World");
        frame.setVisible(true);

        Signal<Dimension> size = runtime.createSignal(new Dimension(300, 500));
        runtime.createEffect(() -> frame.setSize(size.get()));
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                size.set(frame.getSize());
                System.out.println(size.get());
            }
        });

        System.err.println("test");

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}

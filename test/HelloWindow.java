package test;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HelloWindow {
    public static void main(String[] args) {
        Frame frame = new Frame("Hello World");
        frame.setVisible(true);
        frame.setSize(300, 500);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}

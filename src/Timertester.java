import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Timertester {
    public static void main(String[] args) {
        int delay= 100; // milliseconds
        ActionListener taskPerformer= new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                System.out.println("testing");
            }
        };
        Timer t= new Timer(delay, taskPerformer);
        t.start();
    }
}
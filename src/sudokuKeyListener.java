import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

public class sudokuKeyListener implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {
        /*Object ob= e.getSource();
        if (ob instanceof JTextField) {
            JTextField jTF= (JTextField) ob;
            jTF.setText("");
        }*/

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Object ob= e.getSource();
        if (ob instanceof JTextField) {
            JTextField tf= (JTextField) ob;
            String value= tf.getText();
            if (e.getKeyChar() >= '1' && e.getKeyChar() <= '9') {
                tf.setEditable(true);
                tf.setText("");
            } else {
                tf.setEditable(false);
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
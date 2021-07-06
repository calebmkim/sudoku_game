import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

public class sudokuListener extends MouseInputAdapter {

    @Override
    public void mousePressed(MouseEvent e) {
        Object ob= e.getSource();
        if (ob instanceof SudokuSquare) {
            ((SudokuSquare) ob).setClicked();
        }
    }

}
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SudokuSquare extends JPanel {
    sudokuKeyListener myListener= new sudokuKeyListener();
    JTextField myArea= new JTextField();

    boolean isPermanent;
    boolean isClicked;
    private static Font permaFont= new Font("Serif", Font.BOLD, 25);
    private static Font tempFont= new Font("Serif", Font.PLAIN, 25);
    private boolean paused= false;

    int value;
    private int row;
    private int col;
    public static final int HEIGHT= 70;  // height and
    public static final int WIDTH= 70;   // width of square

    private JComboBox numberChoice= new JComboBox();
    private JTextField numberInput= new JTextField("", 1);

    boolean hasPaintedSol;

    public SudokuSquare(int value, int row, int col, boolean permanent) {
        isClicked= false;
        isPermanent= permanent;
        this.value= value;
        this.row= row;
        this.col= col;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        numberInput.addKeyListener(new sudokuKeyListener());
        add(numberInput, BorderLayout.CENTER);
        numberInput.setFont(tempFont);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isPermanent) {
            numberInput.setVisible(false);
            g.setFont(permaFont);
        } else {
            numberInput.setVisible(true);
        }

        g.setColor(Color.black);
        g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

        // draw value
        /*if (value != 0) {
            if (isPermanent) {
                g.drawString("" + value, WIDTH / 2 - 5, 5 + HEIGHT / 2);
            } else {
                numberChoice.setSelectedIndex(value - 1);
            }
        }*/
        if (isPermanent) {
            g.drawString("" + value, WIDTH / 2 - 5, 5 + HEIGHT / 2);
        }
        if (!paused) {
            if (!isPermanent) {
                value= getTextInput();
            }
        }

        // setting bolded lines to draw
        BasicStroke stroke= new BasicStroke();
        Graphics2D g2= (Graphics2D) g;
        g2.setStroke(new BasicStroke(7));

        // lines to visualize the 3x3 squares
        if (row % 3 == 2) {
            g2.draw(new Line2D.Float(0, HEIGHT, WIDTH, HEIGHT));
        }
        if (col % 3 == 2) {
            g2.draw(new Line2D.Float(WIDTH, 0, WIDTH, HEIGHT));
        }

    }

    /*@Override
    public void paint(Graphics g) {
        if (isPermanent) {
            g.setFont(permaFont);
        } else {
            g.setFont(tempFont);
        }

        // System.out.println("painting");

        g.setColor(Color.black);
        g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

        // draw value
        if (value != 0) {
            g.drawString("" + value, WIDTH / 2 - 5, 5 + HEIGHT / 2);
        }
        // setting bolded lines to draw
        BasicStroke stroke= new BasicStroke();
        Graphics2D g2= (Graphics2D) g;
        g2.setStroke(new BasicStroke(7));

        // lines to visualize the 3x3 squares
        if (row % 3 == 2) {
            g2.draw(new Line2D.Float(0, HEIGHT, WIDTH, HEIGHT));
        }
        if (col % 3 == 2) {
            g2.draw(new Line2D.Float(WIDTH, 0, WIDTH, HEIGHT));
        }

        if (isClicked) {
            g2.draw(new Line2D.Float(HEIGHT, 0, HEIGHT, HEIGHT));
            g2.draw(new Line2D.Float(0, HEIGHT, HEIGHT, HEIGHT));
            g2.draw(new Line2D.Float(0, 0, 0, HEIGHT));
            g2.draw(new Line2D.Float(0, 0, HEIGHT, 0));
        }

    }*/

    public void setClicked() {
        isClicked= true;
    }

    public int getTextInput() {
        try {
            int val= Integer.parseInt(numberInput.getText());
            if (val >= 1 && val <= 10) { return val; }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public void changeValueReset(int newVal) {
        value= newVal;
        if (!isPermanent) {
            numberInput.setVisible(true);
        } else {
            numberInput.setVisible(false);
        }
        numberInput.setText("");
    }

    /** Changes the value of the sudokuSquare to int newVal. boolean visualize determines whether
     * the user sees the value gets changed immediately or not */
    public void changeValue(int newVal, boolean visualize) {
        if (newVal != 0) {
            numberInput.setText("" + newVal);
        } else {
            numberInput.setText("");
        }
        if (visualize) numberInput.paintImmediately(numberInput.getVisibleRect());
    }

    public void changeValueRepaint(int newVal) {
        // System.out.println("actually repaint");
        if (!isPermanent) value= newVal;
    }

    public void setPause(boolean b) {
        paused= b;
    }
}
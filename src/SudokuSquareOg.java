import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SudokuSquareOg extends JPanel {
    sudokuKeyListener myListener= new sudokuKeyListener();
    JTextField myArea= new JTextField();

    boolean isPermanent;
    boolean isClicked;
    private static Font permaFont= new Font("Serif", Font.BOLD, 25);
    private static Font tempFont= new Font("Serif", Font.PLAIN, 25);

    int value;
    private int row;
    private int col;
    public static final int HEIGHT= 70;  // height and
    public static final int WIDTH= 70;   // width of square

    private JComboBox numberOptions= new JComboBox();

    boolean hasPaintedSol;

    public SudokuSquareOg(int value, int row, int col, boolean permanent) {
        isClicked= false;
        isPermanent= permanent;
        this.value= value;
        this.row= row;
        this.col= col;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // numberChoice.addActionListener(e -> { changeValue(e); });
        /*numberChoice.addItem("1");
        numberChoice.addItem("2");
        numberChoice.addItem("3");
        numberChoice.addItem("4");
        numberChoice.addItem("5");
        numberChoice.addItem("6");
        numberChoice.addItem("7");
        numberChoice.addItem("8");
        numberChoice.addItem("9");*/

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isPermanent) {
            g.setFont(permaFont);
        } else {
            g.setFont(tempFont);
        }

        // System.out.println("is painting");

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

        hasPaintedSol= true;
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

    public void changeValue(int newVal) {
        value= newVal;
    }

    public void changeValueRepaint(int newVal) {
        // System.out.println("actually repaint");
        if (!isPermanent) value= newVal;
        repaint();
    }
}
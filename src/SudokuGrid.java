import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SudokuGrid extends JFrame {

    private String nytURL= "https://www.nytimes.com/puzzles/sudoku/";
    private static String difficulty= "easy";

    static SudokuSquare[][] grid;
    private static Box gridBox= new Box(BoxLayout.Y_AXIS);
    static int[][] sudokuArray= new int[9][9];

    private static JPanel winScreen= new JPanel();
    static Popup p;
    static PopupFactory pf= new PopupFactory();

    private static JButton solveButton= new JButton("Solve (Visualize Algorithm) ");
    private static JButton solveQuickButton= new JButton("Solve (Quickly)");
    private static JComboBox safeButton= new JComboBox();
    private static JComboBox difficultyButton= new JComboBox();

    private static Box buttonBox= new Box(BoxLayout.Y_AXIS);

    static boolean hasWon= false;
    static boolean inTransition= false;
    private static boolean safeMode= true;

    public static void main(String[] args) {
        int[] scrapedArray= scrapeArray();
        buildPuzzle(scrapedArray);
        // buildBasicPuzzle();
        SudokuGrid gui= new SudokuGrid();
        int prevRow= 0;
        int prevCol= 0;

        while (true) {
            while (!hasWon && !inTransition) {
                for (int r= 0; r < 9; r++ ) {
                    for (int c= 0; c < 9; c++ ) {
                        sudokuArray[r][c]= grid[r][c].value;
                        if (checkHasWon()) {
                            hasWon= true;
                            p.show();
                        }
                    }
                }
            }
        }
    }

    /** Sets all of the squares to paused so that their value no longer is tied to their jText
     * input */
    public static void pauseSquares(boolean pause) {
        for (int r= 0; r < 9; r++ ) {
            for (int c= 0; c < 9; c++ ) {
                grid[r][c].setPause(pause);
            }
        }
    }

    /** Scrapes the 81 int array from the NYT suodku page */
    public static void buildPuzzle(int[] input) {
        for (int r= 0; r < 9; r++ ) {
            for (int c= 0; c < 9; c++ ) {
                int index= r * 9 + c;
                sudokuArray[r][c]= input[index];
            }
        }
    }

    /** Scrapes the 81 int array from the NYT sudoku page */
    public static int[] scrapeArray() {
        String url= "https://www.nytimes.com/puzzles/sudoku/" + difficulty;
        Document doc= null;
        try {
            doc= Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element gameData= null;
        Elements game= doc.select("script[type = text/javascript]");
        for (Element element : game) {
            if (element.data().contains("gameData")) {
                gameData= element;
            }
        }
        String stringData= gameData.data();
        int start= stringData.indexOf("gameData = ") + 11;
        String gameString= stringData.substring(start);
        JSONObject gameJson= null;
        try {
            gameJson= new JSONObject(gameString);
        } catch (JSONException err) {}
        JSONArray gameArray= gameJson.getJSONObject(difficulty).getJSONObject("puzzle_data")
            .getJSONArray("puzzle");
        int[] gameIntArray= new int[81];
        int count= 0;
        for (Object o : gameArray) {
            int addVal= (Integer) o;
            gameIntArray[count]= addVal;
            count++ ;
        }
        return gameIntArray;
    }

    /** Calls solve slowly */
    public static void callSolve(ActionEvent e) {
        inTransition= true;
        clearBoard();
        boolean canSolve= solve();
        inTransition= false;
        if (!canSolve) {
            System.out.println("cannot find a solution");
        }
    }

    /** Prints the current sudoku Array (used mostly for debugging) */
    public static void printSudokuArray() {
        for (int row= 0; row < 9; row++ )// Cycles through rows
        {
            for (int col= 0; col < 9; col++ )// Cycles through columns
            {
                System.out.printf("%5d", sudokuArray[row][col]); // change the %5d to however much
                                                                 // space you want
            }
            System.out.println(); // Makes a new row
        }
    }

    /** Calls solve quickly */
    public static void callSolveQuick(ActionEvent e) {
        inTransition= true;
        pauseSquares(true);
        clearBoard();
        boolean canSolve= solveQuickly();
        if (!canSolve) {
            System.out.println("cannot find a solution");
        } else {
            for (int r= 0; r < 9; r++ ) {
                for (int c= 0; c < 9; c++ ) {
                    grid[r][c].changeValue(sudokuArray[r][c], false);
                }
            }
            inTransition= false;
            pauseSquares(false);
        }

    }

    /** Clears the board of all user input (i.e all the non-permanent squares) */
    public static void clearBoard() {
        for (int r= 0; r < 9; r++ ) {
            for (int c= 0; c < 9; c++ ) {
                if (!grid[r][c].isPermanent) {
                    sudokuArray[r][c]= 0;
                    grid[r][c].changeValue(0, true);
                    grid[r][c].repaint();
                }
            }
        }
    }

    /** Solves the puzzle quickly, without allowing user to visualize the algorithm. */
    public static boolean solveQuickly() {
        for (int r= 0; r < 9; r++ ) {
            for (int c= 0; c < 9; c++ ) {
                if (sudokuArray[r][c] == 0) {
                    for (int pos= 1; pos < 10; pos++ ) {
                        if (isSafe(r, c, pos)) {
                            sudokuArray[r][c]= pos;
                            // grid[r][c].changeValue(pos, false);
                            // grid[r][c].repaint();
                            if (solveQuickly()) {
                                return true;
                            } else {
                                sudokuArray[r][c]= 0;
                                // grid[r][c].changeValue(0, false);
                                // grid[r][c].repaint();
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /** Solves the puzzle in a way that allows the user to see the algorithm "thinking". */
    public static boolean solve() {
        for (int r= 0; r < 9; r++ ) {
            for (int c= 0; c < 9; c++ ) {
                if (sudokuArray[r][c] == 0) {
                    for (int pos= 1; pos < 10; pos++ ) {
                        if (isSafe(r, c, pos)) {
                            sudokuArray[r][c]= pos;
                            grid[r][c].changeValue(pos, true);
                            if (solve()) {
                                return true;
                            } else {
                                sudokuArray[r][c]= 0;
                                grid[r][c].changeValue(0, true);
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /** Builds the 9x9 grid */
    public static void buildGrid() {
        grid= new SudokuSquare[9][9];
        for (int r= 0; r < 9; r++ ) {
            Box row= new Box(BoxLayout.X_AXIS);
            for (int c= 0; c < 9; c++ ) {
                int val= sudokuArray[r][c];
                boolean isPermanent= val != 0;
                grid[r][c]= new SudokuSquare(val, r, c, isPermanent);
                row.add(grid[r][c]);
            }
            gridBox.add(row);
        }
    }

    /** Builds the screen that pops up when you win */
    public static void buildWinScreen() {
        p= pf.getPopup(gridBox, winScreen, 180, 100);

        JLabel winText= new JLabel("You won! What level do you want to try next?");
        winText.setFont(new Font("Verdana", Font.PLAIN, 25));
        winScreen.add(winText, BorderLayout.NORTH);

        Box winChoices= new Box(BoxLayout.X_AXIS);
        JButton easyButton= new JButton(" Easy");
        JButton medButton= new JButton("Medium");
        JButton hardButton= new JButton("Hard");
        JButton quitButton= new JButton("Quit");
        winChoices.add(easyButton);
        winChoices.add(medButton);
        winChoices.add(hardButton);
        winChoices.add(quitButton);
        easyButton.addActionListener(e -> callEasy(e));
        medButton.addActionListener(e -> callMed(e));
        hardButton.addActionListener(e -> callHard(e));
        quitButton.addActionListener(e -> callQuit(e));

        winScreen.add(winChoices, BorderLayout.SOUTH);

    }

    /** Adds the Buttons that let you change difficulty and solve */
    public static void addButtons() {
        buttonBox.add(solveButton);
        buttonBox.add(solveQuickButton);
        buttonBox.add(difficultyButton);
        difficultyButton.addItem("Easy");
        difficultyButton.addItem("Medium");
        difficultyButton.addItem("Hard");

        // Adding action listener so button/ combobox does something when pressed/changed
        solveButton.addActionListener(e -> callSolve(e));
        solveQuickButton.addActionListener(a -> callSolveQuick(a));
        safeButton.addActionListener(e -> { changeSafeState(e); });
        difficultyButton.addActionListener(e -> { changeDifficulty(e); });
    }

    /** Constructs the game */
    public SudokuGrid() {
        super("Sudoku");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // building the 9x9 grid
        buildGrid();
        // build what pops up when you win
        buildWinScreen();
        // Adds the Buttons
        addButtons();
        // Adding the Boxes
        add(gridBox, BorderLayout.CENTER);
        add(buttonBox, BorderLayout.EAST);
        pack();
        setVisible(true);
    }

    /** Creates a new game, including scraping the NYT array, repainting the grid. */
    private static void makeNewGame() {
        inTransition= true;
        pauseSquares(true);
        int[] scrapedArray= scrapeArray();
        buildPuzzle(scrapedArray);
        repaintGrid();
        pauseSquares(false);
        inTransition= false;
        hasWon= false;
    }

    /** Creates a new easy game, and hides the pop-up win screen */
    private static void callEasy(ActionEvent e) {
        difficulty= "easy";
        difficultyButton.setSelectedIndex(0);
        makeNewGame();
        p.hide();
        p= pf.getPopup(gridBox, winScreen, 180, 100);
    }

    /** Creates a new medium game, and hides the pop-up win screen */
    private static void callMed(ActionEvent e) {
        difficulty= "medium";
        difficultyButton.setSelectedIndex(1);
        makeNewGame();
        p.hide();
        p= pf.getPopup(gridBox, winScreen, 180, 100);

    }

    /** Creates a new hard game, and hides the pop-up win screen */
    private static void callHard(ActionEvent e) {
        difficulty= "hard";
        difficultyButton.setSelectedIndex(2);
        makeNewGame();
        p.hide();
        p= pf.getPopup(gridBox, winScreen, 180, 100);
    }

    /** Quits the game */
    private static void callQuit(ActionEvent e) {
        System.exit(0);
    }

    /** Repaints the grid according to sudokuArray, including changing permenacy of squares */
    private static void repaintGrid() {
        for (int r= 0; r < 9; r++ ) {
            for (int c= 0; c < 9; c++ ) {
                boolean perm= sudokuArray[r][c] != 0;
                grid[r][c].isPermanent= perm;
                grid[r][c].changeValueReset(sudokuArray[r][c]);
                grid[r][c].repaint();
            }
        }
    }

    /** Changes the difficulty level, and makes a new game if the user has chosen a new
     * difficulty */
    private static void changeDifficulty(ActionEvent e) {
        String prevDifficulty= difficulty;
        if (difficultyButton.getSelectedItem().equals("Easy")) {
            difficulty= "easy";
        } else if (difficultyButton.getSelectedItem().equals("Medium")) {
            difficulty= "medium";
        } else if (difficultyButton.getSelectedItem().equals("Hard")) {
            difficulty= "hard";
        }
        if (!difficulty.equals(prevDifficulty)) {
            makeNewGame();
        }
    }

    /** Changes boolean safeMode to true or false, depending on what the user has selected */
    private static void changeSafeState(ActionEvent e) {
        if (safeButton.getSelectedItem().equals("Help")) {
            safeMode= true;
        } else {
            safeMode= false;
        }
    }

    /** Returns true if game is won, false otherwise */
    public static boolean checkHasWon() {
        for (int r= 0; r < 9; r++ ) {
            for (int c= 0; c < 9; c++ ) {
                int currentVal= sudokuArray[r][c];
                if (sudokuArray[r][c] == 0 || !uniqueOverall(r, c, currentVal)) return false;
            }
        }
        return true;
    }

    /** Returns true if int target is unique to its row, column, and quadrant. Precondition: r and c
     * must be 0-8. Target must be 1-9 */
    private static boolean uniqueOverall(int r, int c, int target) {
        return uniqueToRow(r, c, target) && uniqueToCol(r, c, target) &&
            uniqueToQuadrant(r, c, target);
    }

    /** Returns true if int target appears either a) 0 times in the row identified byrc or b)
     * appears only 1 time at the specified row and column. Precondition: r and c must be 0-8.
     * Target must be 1-9 */
    private static boolean uniqueToRow(int r, int c, int target) {
        for (int i= 0; i < 9; i++ ) {
            if (sudokuArray[r][i] == target && i != c) return false;
        }
        return true;
    }

    /** Returns true if int target appears either a) 0 times in the col identified by c or b)
     * appears only 1 time at the specified row and column. Precondition: r and c must be 0-8.
     * Target must be 1-9 */
    private static boolean uniqueToCol(int r, int c, int target) {
        for (int i= 0; i < 9; i++ ) {
            if (sudokuArray[i][c] == target && i != r) return false;
        }
        return true;
    }

    /** Returns true if int target appears either a) 0 times in the quadrant identified by row r and
     * col c or b) appears only 1 time at the specified row and column. Precondition: r and c must
     * be 0-8. Target must be 1-9 */
    private static boolean uniqueToQuadrant(int r, int c, int target) {
        int rowStarter= r / 3 * 3;
        int colStarter= c / 3 * 3;
        for (int ro= rowStarter; ro <= rowStarter + 2; ro++ ) {
            for (int co= colStarter; co <= colStarter + 2; co++ ) {
                if (sudokuArray[ro][co] == target && (ro != r || co != c)) return false;
            }
        }
        return true;
    }

    /** Returns true int target is safe to place in row r and col c. Precondition: r and c must be
     * 0-8. Target must be 1-9 */
    public static boolean isSafe(int r, int c, int target) {
        return notInRow(r, target) && notInCol(c, target) && notInQuadrant(r, c, target);
    }

    /** Returns true if int target is not already in row r. Precondition: r and c must be 0-8.
     * Target must be 1-9 */
    private static boolean notInRow(int r, int target) {
        for (int i= 0; i < 9; i++ ) {
            if (sudokuArray[r][i] == target) return false;
        }
        return true;
    }

    /** Returns true if int target is not already in column c. Precondition: r and c must be 0-8.
     * Target must be 1-9 */
    private static boolean notInCol(int c, int target) {
        for (int i= 0; i < 9; i++ ) {
            if (sudokuArray[i][c] == target) return false;
        }
        return true;
    }

    /** Returns true if int target is not already in the quadrant specified by row r and column c. r
     * and c are numbered 0-8. Based off r and c, we know which quadrant we should be looking in.
     * Precondition: r and c must be 0-8. Target must be 1-9 */
    private static boolean notInQuadrant(int r, int c, int target) {
        int rowStarter= r / 3 * 3;
        int colStarter= c / 3 * 3;
        for (int ro= rowStarter; ro <= rowStarter + 2; ro++ ) {
            for (int co= colStarter; co <= colStarter + 2; co++ ) {
                if (sudokuArray[ro][co] == target) return false;
            }
        }
        return true;
    }

    /** Builds a basic puzzle */
    public static void buildBasicPuzzle() {
        sudokuArray[2][0]= 3;
        sudokuArray[0][3]= 3;
        sudokuArray[1][3]= 2;
        sudokuArray[0][6]= 9;
        sudokuArray[2][6]= 6;
        sudokuArray[0][7]= 4;
        sudokuArray[1][7]= 1;

        sudokuArray[3][1]= 1;
        sudokuArray[3][2]= 5;
        sudokuArray[5][1]= 6;
        sudokuArray[3][3]= 7;
        sudokuArray[3][5]= 8;
        sudokuArray[4][5]= 4;
        sudokuArray[3][8]= 4;

        sudokuArray[6][2]= 1;
        sudokuArray[8][2]= 7;
        sudokuArray[8][3]= 9;
        sudokuArray[6][6]= 3;
        sudokuArray[7][7]= 7;
        sudokuArray[8][7]= 6;
        sudokuArray[6][8]= 8;
        sudokuArray[7][8]= 1;
        sudokuArray[7][3]= 4;
    }

}
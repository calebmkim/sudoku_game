public class SudokuMath {
    /** Returns true if int target is unique to its row, column, and quadrant. Precondition: r and c
     * must be 0-8. Target must be 1-9 */
    private static boolean uniqueOverall(int r, int c, int target, int[][] sudokuArray) {
        return uniqueToRow(r, c, target, sudokuArray) && uniqueToCol(r, c, target, sudokuArray) &&
            uniqueToQuadrant(r, c, target, sudokuArray);
    }

    /** Returns true if int target appears either a) 0 times in the row identified byrc or b)
     * appears only 1 time at the specified row and column. Precondition: r and c must be 0-8.
     * Target must be 1-9 */
    private static boolean uniqueToRow(int r, int c, int target, int[][] sudokuArray) {
        for (int i= 0; i < 9; i++ ) {
            if (sudokuArray[r][i] == target && i != c) return false;
        }
        return true;
    }

    /** Returns true if int target appears either a) 0 times in the col identified by c or b)
     * appears only 1 time at the specified row and column. Precondition: r and c must be 0-8.
     * Target must be 1-9 */
    private static boolean uniqueToCol(int r, int c, int target, int[][] sudokuArray) {
        for (int i= 0; i < 9; i++ ) {
            if (sudokuArray[i][c] == target && i != r) return false;
        }
        return true;
    }

    /** Returns true if int target appears either a) 0 times in the quadrant identified by row r and
     * col c or b) appears only 1 time at the specified row and column. Precondition: r and c must
     * be 0-8. Target must be 1-9 */
    private static boolean uniqueToQuadrant(int r, int c, int target, int[][] sudokuArray) {
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
    public static boolean isSafe(int r, int c, int target, int[][] sudokuArray) {
        return notInRow(r, target, sudokuArray) && notInCol(c, target, sudokuArray) &&
            notInQuadrant(r, c, target, sudokuArray);
    }

    /** Returns true if int target is not already in row r. Precondition: r and c must be 0-8.
     * Target must be 1-9 */
    private static boolean notInRow(int r, int target, int[][] sudokuArray) {
        for (int i= 0; i < 9; i++ ) {
            if (sudokuArray[r][i] == target) return false;
        }
        return true;
    }

    /** Returns true if int target is not already in column c. Precondition: r and c must be 0-8.
     * Target must be 1-9 */
    private static boolean notInCol(int c, int target, int[][] sudokuArray) {
        for (int i= 0; i < 9; i++ ) {
            if (sudokuArray[i][c] == target) return false;
        }
        return true;
    }

    /** Returns true if int target is not already in the quadrant specified by row r and column c. r
     * and c are numbered 0-8. Based off r and c, we know which quadrant we should be looking in.
     * Precondition: r and c must be 0-8. Target must be 1-9 */
    private static boolean notInQuadrant(int r, int c, int target, int[][] sudokuArray) {
        int rowStarter= r / 3 * 3;
        int colStarter= c / 3 * 3;
        for (int ro= rowStarter; ro <= rowStarter + 2; ro++ ) {
            for (int co= colStarter; co <= colStarter + 2; co++ ) {
                if (sudokuArray[ro][co] == target) return false;
            }
        }
        return true;
    }

}
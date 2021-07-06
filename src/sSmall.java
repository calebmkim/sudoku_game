import java.util.ArrayList;
import java.util.HashMap;

public class sSmall {
    static int size= 4;
    public static int[][] s= new int[size][size];
    static Heap<HeapEntry> eHeap= new Heap<>(true);
    static HashMap<entry, entry> eMap= new HashMap<>();

    public static void main(String[] args) {
        buildBasicPuzzle();
        printPuzzle();

        // fillHeap();

        System.out.println("" + solve());
        System.out.println("\n");
        printPuzzle();
    }

    public static boolean solve() {
        System.out.println("");
        printPuzzle();
        for (int r= 0; r < size; r++ ) {
            for (int c= 0; c < size; c++ ) {
                if (s[r][c] == 0) {
                    for (int pos= 1; pos < size + 1; pos++ ) {
                        if (isSafe(r, c, pos)) {
                            s[r][c]= pos;
                            if (solve()) {
                                return true;
                            } else {
                                s[r][c]= 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isSafe(int r, int c, int target) {
        return notInRow(r, target) && notInCol(c, target) && notInQuadrant(r, c, target);
    }

    public static void fillHeap() {
        for (int r= 0; r <= 8; r++ ) {
            for (int c= 0; c <= 8; c++ ) {
                if (s[r][c] == 0) {
                    ArrayList<Integer> pVals= getPossibleVals(r, c);
                    HeapEntry sol= new HeapEntry(r, c, pVals);
                    eHeap.insert(sol, pVals.size());
                }
            }
        }
    }

    private static ArrayList<Integer> getPossibleVals(int r, int c) {
        ArrayList<Integer> result= new ArrayList<>();
        for (int i= 1; i <= 9; i++ ) {
            if (notInRow(r, i) && notInCol(c, i) && notInQuadrant(r, c, i)) {
                result.add(i);
            }
        }
        return result;
    }

    private static boolean notInRow(int r, int target) {
        for (int i= 0; i < size; i++ ) {
            if (s[r][i] == target) return false;
        }
        return true;
    }

    private static boolean notInCol(int c, int target) {
        for (int i= 0; i < size; i++ ) {
            if (s[i][c] == target) return false;
        }
        return true;
    }

    private static boolean notInQuadrant(int r, int c, int target) {
        int cellSize= (int) Math.sqrt(size);
        System.out.println(cellSize);
        int rowStarter= r / cellSize * cellSize;
        int colStarter= c / cellSize * cellSize;
        System.out.println(rowStarter);
        System.out.println(colStarter);
        for (int ro= rowStarter; ro < rowStarter + cellSize; ro++ ) {
            for (int co= colStarter; co < colStarter + cellSize; co++ ) {
                if (s[ro][co] == target) return false;
            }
        }
        return true;
    }

    private static void printPuzzle() {
        for (int r[] : s) {
            for (int c : r) {
                if (c == 0) System.out.print(" ? ");
                else {
                    System.out.print(" " + c);
                }
            }
            System.out.println("");
        }
    }

    private static void addStartEntry(int r, int c, int v) {
        s[r][c]= v;
        // eMap.put(new entry(r, c, v), null);
    }

    public static void buildBasicPuzzle() {
        addStartEntry(0, 3, 1);
        addStartEntry(1, 0, 2);
        addStartEntry(2, 3, 4);
        addStartEntry(3, 0, 3);

        addStartEntry(0, 0, 4);
        addStartEntry(0, 1, 3);
        addStartEntry(0, 2, 2);
        addStartEntry(1, 1, 1);

    }

    private static class entry {
        int row;
        int col;
        int val;

        entry(int r, int c, int v) {
            row= r;
            col= c;
            val= v;
        }
    }

    private static class HeapEntry implements Comparable {
        private int r;
        private int c;
        private ArrayList<Integer> pVals;

        HeapEntry(int r, int c, ArrayList<Integer> vals) {
            this.r= r;
            this.c= c;
            pVals= vals;
        }

        @Override
        public int compareTo(Object o) {
            HeapEntry comp= (HeapEntry) o;
            return pVals.size() - comp.pVals.size();
        }

    }
}
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class s {
    public static int[][] s1= new int[9][9];
    static Heap<HeapEntry> eHeap= new Heap<>(true);
    static HashMap<entry, entry> eMap= new HashMap<>();

    public static void main(String[] args) {
        String url= "https://www.nytimes.com/puzzles/sudoku/medium";
        /*String content= null;
        URLConnection connection= null;
        try {
            connection= new URL("http://www.google.com").openConnection();
            Scanner scanner= new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content= scanner.next();
            scanner.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/

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
        JSONArray gameArray= gameJson.getJSONObject("medium").getJSONObject("puzzle_data")
            .getJSONArray("puzzle");
        int[] gameIntArray= new int[81];
        int count= 0;
        for (Object o : gameArray) {
            int addVal= (Integer) o;
            gameIntArray[count]= addVal;
            count++ ;
        }
        // Elements game= doc.select("div[class = pz-game-screen]");
        // System.out.println(game);

        /*buildBasicPuzzle();
        printPuzzle();
        fillHeap();
        
        System.out.println("" + solve());
        System.out.println("\n");
        printPuzzle();*/
    }

    public static boolean solve() {
        for (int r= 0; r < 9; r++ ) {
            for (int c= 0; c < 9; c++ ) {
                if (s1[r][c] == 0) {
                    for (int pos= 1; pos < 10; pos++ ) {
                        if (isSafe(r, c, pos)) {
                            s1[r][c]= pos;
                            if (solve()) {
                                return true;
                            } else {
                                s1[r][c]= 0;
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
                if (s1[r][c] == 0) {
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
        for (int i= 0; i < 9; i++ ) {
            if (s1[r][i] == target) return false;
        }
        return true;
    }

    private static boolean notInCol(int c, int target) {
        for (int i= 0; i < 9; i++ ) {
            if (s1[i][c] == target) return false;
        }
        return true;
    }

    private static boolean notInQuadrant(int r, int c, int target) {
        int rowStarter= r / 3 * 3;
        int colStarter= c / 3 * 3;
        for (int ro= rowStarter; ro <= rowStarter + 2; ro++ ) {
            for (int co= colStarter; co <= colStarter + 2; co++ ) {
                if (s1[ro][co] == target) return false;
            }
        }
        return true;
    }

    private static void printPuzzle() {
        for (int r[] : s1) {
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
        s1[r][c]= v;
        eMap.put(new entry(r, c, v), null);
    }

    public static void buildBasicPuzzle() {
        addStartEntry(2, 0, 3);
        addStartEntry(0, 3, 3);
        addStartEntry(1, 3, 2);
        addStartEntry(0, 6, 9);
        addStartEntry(2, 6, 6);
        addStartEntry(0, 7, 4);
        addStartEntry(1, 7, 1);

        addStartEntry(3, 1, 1);
        addStartEntry(3, 2, 5);
        addStartEntry(5, 1, 6);
        addStartEntry(3, 3, 7);
        addStartEntry(3, 5, 8);
        addStartEntry(4, 5, 4);
        addStartEntry(3, 8, 4);

        addStartEntry(6, 2, 1);
        addStartEntry(8, 2, 7);
        addStartEntry(8, 3, 9);
        addStartEntry(6, 6, 3);
        addStartEntry(7, 7, 7);
        addStartEntry(8, 7, 6);
        addStartEntry(6, 8, 8);
        addStartEntry(7, 8, 1);
        addStartEntry(7, 3, 4);
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
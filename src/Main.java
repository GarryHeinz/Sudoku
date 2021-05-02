import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        /*int[][] field1 = new int[][]{
                {5,8,6,0,3,1,0,7,0},
                {2,0,7,8,6,0,5,1,3},
                {0,1,0,7,0,5,2,0,6},
                {0,2,8,0,0,4,3,6,1},
                {6,0,4,9,1,3,7,2,0},
                {0,3,1,6,2,0,0,9,5},
                {4,0,5,0,8,2,0,3,7},
                {1,7,0,4,9,6,8,0,2},
                {0,6,2,3,5,0,1,0,9},
        };*/
        /*int[][] field1 = new int[][]{
                {0,0,0,6,0,0,0,0,0},
                {6,0,0,3,0,5,4,0,0},
                {5,0,4,0,0,0,0,0,1},
                {3,0,1,5,0,8,7,0,0},
                {9,8,0,7,0,2,0,1,3},
                {0,0,7,1,0,6,8,0,2},
                {2,0,0,0,0,0,3,0,7},
                {0,0,8,4,0,3,0,0,9},
                {0,0,0,0,0,9,0,0,0},
        };*/
        int[][] field1 = new int[][]{
                {0,0,0,0,0,0,0,4,8},
                {0,9,8,2,0,0,0,0,7},
                {0,0,0,4,3,0,0,0,6},
                {9,8,0,0,0,0,0,0,0},
                {0,0,0,3,1,0,0,0,0},
                {7,0,0,0,0,0,0,9,4},
                {0,0,9,0,0,0,6,0,0},
                {0,0,0,7,0,2,0,0,0},
                {5,0,3,0,0,4,8,0,0},
        };
        System.out.println("Starte Benchmark...");
        SudokuSolver s = new SudokuSolver(field1);
        s.printFiled();
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        int it = s.solve();
        s.printFiled();
    }
}

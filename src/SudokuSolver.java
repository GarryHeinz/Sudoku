import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class SudokuSolver {

    private final int SIZE = 3;
    private int[][] field = new int[SIZE * SIZE][SIZE * SIZE];
    private int[][][][] subfield = new int[SIZE][SIZE][SIZE][SIZE];

    private List<List<Set<Integer>>> possible = new ArrayList<>();

    public SudokuSolver(int[][] f){
        for(int i = 0; i < f.length;i++){
            possible.add(new ArrayList<>());
            for(int j = 0; j < f[i].length;j++){
                this.field[i][j] = f[i][j];
                possible.get(i).add(new HashSet<>());
                possible.get(i).get(j).addAll(Arrays.asList(new Integer[]{1,2,3,4,5,6,7,8,9}));
            }
        }
    }

    public int[][] getField(){
        return this.field;
    }
    public Set<Integer> possibleFor(int col, int row){
        Set<Integer> possible = new HashSet<>();
        possible.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9));
        if(this.field[col][row] != 0){
            return new HashSet<Integer>();
        }
        for(int c = 0; c <this.field.length;c++){
            if(this.field[c][row] != 0){
                possible.remove(this.field[c][row]);
            }
        }

        for(int r = 0;r < this.field[col].length;r++){
            if(this.field[col][r] != 0){
                possible.remove(this.field[col][r]);
            }
        }
        for(int i = 0; i< this.subfield[col/3][row/3].length;i++){
            for(int j = 0; j< this.subfield[col/3][row/3][i].length;j++){
                if(this.subfield[col/3][row/3][i][j] != 0){
                    possible.remove(this.subfield[col/3][row/3][i][j]);
                }
            }
        }
        this.updateSubfield();


        return possible;
    }

    private void updateSubfield(){
        /*
        [][][]
        [][][] ->
        [][][]
         */
        for(int i = 0; i< this.field.length;i++){
            for(int j = 0; j< this.field[i].length;j++){
                int subfieldI =(i/3) % 3;
                int subfieldJ = (j/3) % 3;
                int innerI = i % 3;
                int innerJ = j % 3;
                this.subfield[subfieldI][subfieldJ][innerI][innerJ] = this.field[i][j];
               // System.out.printf("i %d, j %d, SubI %d, SubJ %d, InnerI %d, InnerJ %d \n",i,j,subfieldI,subfieldJ,innerI,innerJ);
               /*System.out.printf("i %d,",i);
                System.out.printf("j %d,",j);
                System.out.printf("SubI %d,",subfieldI);
                System.out.printf("InnerI %d,",innerI);
                System.out.printf("SubJ %d,",subfieldJ);
                System.out.printf("InnerJ %d,",innerJ);
                System.out.println();*/
            }
        }
    }

    private boolean isSolved(){
        for(int i = 0; i< this.field.length;i++){
            for(int j =0 ; j< this.field[i].length;j++){
                if(this.field[i][j] == 0){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkFiled(){

        for(int i = 0;i < field.length;i++){

            Set<Integer> curCol = new HashSet<Integer>();
            curCol.addAll(Arrays.asList(new Integer[]{1,2,3,4,5,6,7,8,9}));
            Set<Integer> curRow = new HashSet<Integer>();
            curRow.addAll(Arrays.asList(new Integer[]{1,2,3,4,5,6,7,8,9}));

            for(int j = 0; j< field[i].length;j++){
                if(!curCol.remove(this.field[i][j]) || !curRow.remove(this.field[j][i])){
                    System.out.printf("i %d, j %d\n",i,j);
                    return false;
                }
            }
        }
        this.updateSubfield();
        for(int subI = 0; subI < this.subfield.length;subI++){
            for(int subJ = 0; subJ < this.subfield[subI].length;subJ++){

                Set<Integer> curSubfield = new HashSet<>();
                curSubfield.addAll(Arrays.asList(new Integer[]{1,2,3,4,5,6,7,8,9}));
                for(int i = 0; i< this.subfield[subI][subJ].length;i++){
                    for(int j = 0; j < this.subfield[subI][subJ][i].length;j++){
                        if(!curSubfield.remove(this.subfield[subI][subJ][i][j])){
                            System.out.printf("SubfieldI %d, SubfieldJ %d\n",subI,subJ);
                            System.out.printf("i %d, j %d\n",i,j);
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void updatePossible(){
        for(int i = 0; i< this.field.length;i++){
            for(int j = 0; j<this.field[i].length;j++){
                Set<Integer> p =this.possible.get(i).get(j);
                p.removeIf((e) ->e == e);
                p.addAll(this.possibleFor(i,j));
            }
        }
    }

    private boolean isOnlyOptionInSub(int val, int col, int row){
        assert(this.possibleFor(col,row).contains(val));
        this.updateSubfield();
        int subI = col/3;
        int subJ = row/3;
        int options = 0;
        for(int i = 0; i < this.subfield[subI][subJ].length;i++){
            for(int j = 0; j< this.subfield[subI][subJ][i].length;j++){
                if(this.possibleFor(subI*3 + i,subJ*3 +j).contains(val)){
                    options++;
                }
            }
        }
        return options==1;
    }

    public int solve(){
        int changes = 0;
        int iteration = 0;
        Scanner s = new Scanner(System.in);
        boolean goOn = true;
        while(!this.isSolved() && goOn){
            changes = 0;
            iteration++;
            //System.out.printf("Iteration %d going on...\n",iteration);
            for(int i = 0; i< field.length;i++){
                for(int j = 0; j < this.field[i].length;j++){
                    if(field[i][j] == 0){
                        Set<Integer> pos = this.possibleFor(i,j);
                        //System.out.println(i+","+j+" = "+pos);
                        if(pos.size() ==1){
                            this.field[i][j] = pos.toArray(new Integer[1])[0];
                            changes++;
                        }
                    }

                }
            }

            for(int i = 0; i < this.field.length;i++){
                for(int j = 0; j< this.field[i].length;j++){
                    Iterator<Integer> it = possibleFor(i,j).iterator();
                    boolean changed = false;
                    while(it.hasNext() && !changed){
                        int opt = it.next();
                        if(this.isOnlyOptionInSub(opt,i,j)){
                            this.field[i][j] = opt;
                            changed = true;
                            changes++;
                        }
                    }
                }
            }
            //System.out.printf("Changes = %d\n",changes);
            if(changes == 0){
                this.printFiled();
                //System.out.println("Keine änderung gemacht, Aber feld noch nicht gelöst. Weitermachen (J/N)?");
                    if(s.next().toLowerCase().equals("n")){
                        goOn = false;
                    }


            }
        }
        if(!this.checkFiled()){
            throw new RuntimeException("ERROR! FIELD NOT VALID");
        }
        return iteration;
    }

    public void printFiled(){
        System.out.println("-------------------------------------------------");
        for(int i = 0; i< field.length;i++){
            System.out.print(i+"     ");
            for(int j = 0; j<field[i].length;j++){
                if(j %3 == 0 ){
                    System.out.print("| ");
                }
                if(field[i][j] == 0){
                    System.out.print("{"+this.possibleFor(i,j).size()+"} ");
                }else{
                    System.out.print(" "+field[i][j] + "  ");
                }
            }
            System.out.print("| ");
            System.out.println();
            if(((i+1) %3) == 0){
                //2*9 + 3*3
                System.out.println("-------------------------------------------------");
            }
        }
        showPanel();
    }

    public void showPanel(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,600);

        JPanel main = new JPanel(new GridLayout(3,3));
        main.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
        main.setBackground(Color.BLACK);
        JPanel[][] panels = new JPanel[3][3];
        for (int i = 0; i < panels.length; i++) {
            for (int j = 0; j < panels[i].length; j++) {
                panels[i][j] = new JPanel(new GridLayout(3, 3, 1, 1));
                panels[i][j].setBackground(Color.black);
                panels[i][j].setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
                main.add(panels[i][j]);
            }
        }
        JTextField[][] fieldGrid = new JTextField[9][9];
        for (int row = 0; row < fieldGrid.length; row++) {
            for (int col = 0; col < fieldGrid[row].length; col++) {
                JTextField field = new JTextField(2);
                field.setHorizontalAlignment(JTextField.CENTER);
                field.setFont(field.getFont().deriveFont(Font.BOLD, 32f));

                fieldGrid[row][col] = field;
                int i = row / 3;
                int j = col / 3;
                panels[i][j].add(fieldGrid[row][col]);
            }
        }
        for(int i = 0; i<fieldGrid.length;i++){
            for(int j = 0; j< fieldGrid[i].length;j++){
                if(this.field[i][j] != 0){
                    fieldGrid[i][j].setText(String.valueOf(this.field[i][j]));
                    fieldGrid[i][j].addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {

                        }

                        @Override
                        public void mousePressed(MouseEvent e) {

                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {

                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            String me = ((JTextField)e.getComponent()).getText();
                            for(int i = 0; i < fieldGrid.length;i++){
                                for(int j = 0; j< fieldGrid[i].length;j++){

                                    if(fieldGrid[i][j].getText().equals(me)){
                                        fieldGrid[i][j].setBackground(Color.CYAN);
                                    }
                                }
                            }
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {

                            for(int i = 0; i < fieldGrid.length;i++){
                                for(int j = 0; j< fieldGrid[i].length;j++){
                                    fieldGrid[i][j].setBackground(Color.WHITE);
                                }
                            }
                        }
                    });
                }else{
                    fieldGrid[i][j].setText("_");
                    fieldGrid[i][j].setToolTipText(this.possibleFor(i,j).toString());
                }
            }
        }

        frame.add(main);
        frame.setVisible(true);
    }

}

package main;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;


class Minefield extends GridPane {

    final int hSize;
    final int vSize;
    private final int mines;


    int remainingMines;
    int opened = 0;
    int clicks = 0;

    Position[][] grid;


    Minefield(int hSize, int vSize, int mines, Main main){

        this.hSize = hSize;
        this.vSize = vSize;
        this.mines  = mines;

        grid = new Position[hSize][vSize];

        remainingMines = mines;


        this.setPrefSize(hSize * main.size + main.size, vSize * main.size + main.size);
        this.setHgap(main.size);
        this.setVgap(main.size);
        this.setPadding(new Insets(10));

        List<Integer> randomMines = new ArrayList<>();

        for (int i = 0; i < mines; i++ ){

            int a = (int) (Math.random()* hSize * vSize);
            while(randomMines.contains(a)) a = (int) (Math.random()* hSize * vSize);
            randomMines.add(a);

        }

        for(int y = 0; y < vSize; y++){
            for(int x = 0; x < hSize; x++){
                Position position = new Position(x,y, randomMines.contains(y* vSize +x), this, main);

                grid[x][y] = position;
                this.getChildren().add(position);
            }
        }

    }

    void incRemainingMines(){remainingMines++;}

    void decRemainingMines(){remainingMines--;}

    boolean allEmptyFieldsUncovered(){return opened >= (hSize * vSize) - mines;}
}

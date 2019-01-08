package main;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;


class Position extends Pane {

    private int line, column;
    private boolean hasMine;
    private boolean isOpen = false;
    private Minefield field;
    private Main main;

    private ImageView picture = new ImageView();
    private Image flag = new Image("Flag.png");
    private Image empty = new Image("Empty.png");
    private boolean flagIsSet = false;


    Position(int line, int column, boolean hasMine, Minefield field, Main main){

        this.line = line;
        this.column = column;
        this.hasMine = hasMine;
        this.field = field;
        this.main = main;

        picture.setImage(empty);
        picture.setFitHeight(main.size);
        picture.setFitWidth(main.size);

        getChildren().add(picture);

        setTranslateX(line * main.size);
        setTranslateY(column * main.size);

        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.SECONDARY)
                    return;
                main.smile1.setImage(new Image("Smiley2.png"));
            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                main.smile1.setImage(new Image("Smiley1.png"));
            }
        });

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {

                if(me.getButton() == MouseButton.SECONDARY) {
                    if(flagIsSet) {

                        field.incRemainingMines();
                        main.mineCounter.setText(String.valueOf(field.remainingMines));
                        picture.setImage(empty);
                        flagIsSet = false;
                        isOpen = false;
                    }else{
                        field.decRemainingMines();
                        main.mineCounter.setText(String.valueOf(field.remainingMines));
                        picture.setImage(flag);
                        flagIsSet = true;
                        isOpen = true;


                    }

                }
                else uncover();
            }
        });

    }

    void uncover(){

        if(!main.runTimer){

            main.startTimer();

            if(hasMine){
                main.restartAtPosition(line, column);
                return;
            }

        }

        field.clicks++;

        if(isOpen) return;

        if(hasMine) {

            loose();
            return;
        }

        isOpen = true;
        field.opened++;
        uncoverPosition();

        if(field.allEmptyFieldsUncovered())
            win();

        if(numberOfNeighbours() < 1){
            getNeighbours(this).forEach(Position::uncover);
        }
    }

    private void win(){

        uncoverAll();
        main.runTimer = false;
        main.smile1.setImage(new Image("Smiley3.png"));
        Highscore highscore = new Highscore(main.time,main.mines);

    }

    private void loose(){

        uncoverAll();
        this.picture.setImage(new Image("Mine2.png"));
        main.runTimer = false;
        main.smile1.setImage(new Image("Smiley4.png"));

    }

    private void uncoverAll(){
        for(int y = 0; y < field.vSize; y++) {
            for (int x = 0; x < field.hSize; x++) {
                field.grid[x][y].uncoverPosition();
            }
        }
    }

    private void uncoverPosition(){

        if(this.hasMine) this.picture.setImage(new Image("Mine1.png"));

        else if(this.flagIsSet) this.picture.setImage(new Image("NoMine.png"));

        else this.picture.setImage(new Image(numberOfNeighbours() + ".png" ));

    }

    private int numberOfNeighbours(){return (int) getNeighbours(this).stream().filter(t -> t.hasMine).count();}

    private List<Position> getNeighbours(Position position){
        List<Position> neighbours = new ArrayList<>();

        int[] points = new int[]{
                -1,-1,
                -1,0,
                -1,1,
                0,-1,
                0,1,
                1,-1,
                1,0,
                1,1
        };

        for (int i = 0; i < points.length;i++){
            int dx = points[i];
            int dy = points[++i];

            int newX = position.line + dx;
            int newY = position.column + dy;

            if(newX >= 0 && newX < field.hSize
                    && newY >= 0 && newY < field.vSize){
                neighbours.add(field.grid[newX][newY]);
            }
        }

        return neighbours;
    }


}

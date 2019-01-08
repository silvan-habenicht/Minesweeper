package main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



class Highscore {

    private int time;
    private String name;
    private int listSize = 10;
    private String[][] highscoreList = new String[listSize][2];
    private String path;
    private boolean isNewHighscore;
    private Font font = Font.loadFont(
            this.getClass().getClassLoader().getResource("cmunrm.ttf").toExternalForm(), 12);

    Highscore(int time, int mines){

        this.time = time;
        path = this.getClass().getClassLoader().getResource("") + "/highscore/list"  + mines + ".txt";

        getHighscoreList();

        showHighscoreDialog();

    }


    private void newHighscoreList(){


        try {

            highscoreList[listSize-1][0] = name;
            highscoreList[listSize-1][1] = String.valueOf(time);

            for(int i = listSize-2; i >= 0 ; i--){

                if(highscoreList[i][1].equals("")){

                    highscoreList[i][0] = highscoreList[i+1][0];
                    highscoreList[i][1] = highscoreList[i+1][1];
                    highscoreList[i+1][0] = "";
                    highscoreList[i+1][1] = "";

                }

                else if(Integer.parseInt(highscoreList[i+1][1]) < Integer.parseInt(highscoreList[i][1])){

                    String n = highscoreList[i+1][0];
                    String s = highscoreList[i+1][1];
                    highscoreList[i+1][0] = highscoreList[i][0];
                    highscoreList[i+1][1] = highscoreList[i][1];
                    highscoreList[i][0] = n;
                    highscoreList[i][1] = s;


                }


            }

        }catch (NumberFormatException e){
            System.err.println("Number-Format");}

        List<String> list = new ArrayList<>();

        for (int z = 0; z < listSize; z++) {
            for (int s = 0; s < 2; s++ ){

                    list.add(highscoreList[z][s]);

            }

        }
        Path p = Paths.get(path);

        try {
            Files.write(p, list);
        }catch (IOException e){System.err.println("Write-Fail");}

    }

    private void getHighscoreList() {

        Path p = Paths.get(path);
        try {
            List<String> list = Files.readAllLines(p);
            if(list.size() < listSize*2) isNewHighscore = true;

            int i = 0;

            for (int z = 0; z < listSize; z++) {
                for (int s = 0; s < 2; s++ ){


                    if(i > list.size()-1 || list.size() == 0)
                        highscoreList[z][s] = "";

                    else highscoreList[z][s] = list.get(i++);

                }
                
            }
            try {
                for(int j = listSize-1; j > 0 ; j--){

                    if(highscoreList[j][1].equals("")) isNewHighscore = true;

                    else if(time < Integer.parseInt(highscoreList[j][1])) {

                        isNewHighscore = true;

                    }

                }

            }catch (NumberFormatException e){System.err.println("Number-Format");}
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void showHighscoreDialog() {

        Stage stage = new Stage();

        Label title = new Label("Highscores");
        if(isNewHighscore)
            title.setText("New Highscore!");
        title.setPadding(new Insets(10,10,10,10));
        title.setFont(Font.loadFont(
                this.getClass().getClassLoader().getResource("cmunbx.ttf").toExternalForm(), 20));

        Text[] names = new Text[listSize];

        Text[] scores = new Text[listSize];

        for (int z = 0; z < listSize; z++) {

            names[z] = new Text();
            scores[z] = new Text();

            if(highscoreList[z][0] != null) {
                names[z].setText(highscoreList[z][0]);
                names[z].setFont(font);
                scores[z].setText(highscoreList[z][1]);
                scores[z].setFont(font);
            }

        }

        VBox namesBox = new VBox(names);
        VBox scoresBox = new VBox(scores);

        HBox table = new HBox(20,namesBox,scoresBox);
        table.setAlignment(Pos.CENTER);
        table.setPadding(new Insets(20,10,20,10));

        TextField text = new TextField("Enter your name");
        text.setMaxWidth(200);
        text.setFont(font);

        VBox textField = new VBox(text);
        textField.setPadding(new Insets(10,10,10,10));
        textField.setAlignment(Pos.BOTTOM_CENTER);

        Button ok = new Button("Ok");
        ok.setFont(font);
        Button cancel = new Button("Cancel");
        cancel.setFont(font);

        HBox okCancel = new HBox(30,cancel, ok);
        okCancel.setAlignment(Pos.CENTER);
        okCancel.setPadding(new Insets(10,10,50,10));

        VBox root = new VBox(title, table, textField, okCancel);
        root.setAlignment(Pos.BOTTOM_CENTER);

        Scene scene = new Scene(root, 300,400);

        stage.setScene(scene);
        stage.setTitle("Winner!");

        cancel.setOnAction(event -> {stage.close();});

        ok.setOnAction(event -> {

            name = text.getText();

            if(isNewHighscore && name != null) newHighscoreList();

            stage.close();});

        stage.show();

    }

}

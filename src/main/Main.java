package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;



public class Main extends Application {


    private int zoom = 2;
    int size = 20;
    volatile int time = 0;
    volatile boolean runTimer = false;


    private Label timeCounter = new Label(String.valueOf(time));
    ImageView smile1 = new ImageView(new Image("Smiley1.png"));
    Label mineCounter = new Label();

    private RadioButton btnEasy = new RadioButton("Easy");
    private RadioButton btnModerate = new RadioButton("Moderate");
    private RadioButton btnHard = new RadioButton("Hard");

    private RadioButton one = new RadioButton("1x");
    private RadioButton two = new RadioButton("2x");

    private int hSize = 9;
    private int vSize = 9;
    int mines = 1;

    private double windowWidth = 250;
    private double windowHeight = 350;


    private Minefield field;
    private HBox gameContent;
    private BorderPane root;
    private Scene scene;
    private Stage stage;

    Thread t;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Font font1 = Font.loadFont(this.getClass().getClassLoader().getResource("cmunrm.ttf").toExternalForm(), 12);
        Font font2 = Font.loadFont(this.getClass().getClassLoader().getResource("digital-7.ttf").toExternalForm(), 40);

        /** Top Pane */

        Button btnSmile = new Button();
        btnSmile.setOnAction(event -> restart());
        smile1.setFitWidth(2* size);
        smile1.setFitHeight(2* size);
        btnSmile.setGraphic(smile1);

        mineCounter.setMinWidth(40);
        mineCounter.setFont(font2);
        mineCounter.setTextFill(Color.RED);
        timeCounter.setMinWidth(40);
        timeCounter.setFont(font2);
        timeCounter.setTextFill(Color.RED);


        HBox.setMargin(mineCounter, new Insets(10));
        HBox.setMargin(btnSmile, new Insets(10));
        HBox.setMargin(timeCounter, new Insets(10));


        BorderPane topx = new BorderPane();
        topx.setLeft(mineCounter);
        topx.setCenter(btnSmile);
        topx.setRight(timeCounter);




        /** Bottom Pane */

        ToggleGroup group = new ToggleGroup();

        btnEasy.setToggleGroup(group);
        btnEasy.setFont(font1);
        btnModerate.setToggleGroup(group);
        btnModerate.setFont(font1);
        btnHard.setToggleGroup(group);
        btnHard.setFont(font1);
        btnEasy.setSelected(true);

        HBox check = new HBox(10, btnEasy, btnModerate, btnHard);
        check.setPadding(new Insets(10));
        check.setAlignment(Pos.CENTER);

        ToggleGroup group1 = new ToggleGroup();
        one.setToggleGroup(group1);
        one.setFont(font1);
        two.setToggleGroup(group1);
        two.setFont(font1);
        one.setSelected(true);

        HBox factor = new HBox(10, one, two);

        factor.setAlignment(Pos.CENTER);

        VBox bottom = new VBox(10,check,factor);

        /** Center Pane */

        btnEasy.setOnAction(e ->{hSize = 9;vSize = 9;mines = 10;windowWidth = 250;windowHeight = 350;restart();
            if(size > 20) {size /= zoom; enlarge();}});
        btnModerate.setOnAction(e ->{hSize = 16; vSize = 16; mines = 40; windowWidth = 390; windowHeight = 490; restart();
            if(size > 20) {size /= zoom; enlarge();}});
        btnHard.setOnAction(e ->{hSize = 30; vSize = 16; mines = 99; windowWidth = 650; windowHeight = 490; restart();
            if(size > 20) {size /= zoom; enlarge();}});

        one.setOnAction(e -> downsize());
        two.setOnAction(e -> enlarge());


        makeField();


        /** Root Pane */

        root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setBottom(bottom);
        root.setTop(topx);
        root.setCenter(gameContent);


        scene = new Scene(root);

        stage = primaryStage;
        stage.setTitle("Minesweeper");

        launchStage();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if(runTimer) runTimer= false;
            }
        });

    }

    private void launchStage() {

        stage.setScene(scene);
        stage.setResizable(true);

        stage.setWidth(windowWidth);
        stage.setHeight(windowHeight);

        stage.hide();
        stage.show();

    }

    private void makeField(){

        field = new Minefield(hSize,vSize, mines, this);
        gameContent = new HBox(field);
        gameContent.setAlignment(Pos.CENTER);
        mineCounter.setText(String.valueOf(mines));

    }

    void startTimer(){

        runTimer = true;

        t = new Thread(() -> {

                while (runTimer) {
                    Platform.runLater(() -> {
                        timeCounter.setText(String.valueOf(time));
                        time++;
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

        });
        t.start();

    }


    private void restart(){

        if(runTimer) runTimer = false;

        time = 0;
        timeCounter.setText("0");
        smile1.setImage(new Image("Smiley1.png"));

        makeField();

        root.setCenter(gameContent);

        launchStage();

    }


    void restartAtPosition(int x, int y){

        restart();

        this.field.grid[x][y].uncover();


    }

    private void enlarge(){

        size *= zoom; windowWidth *= zoom; windowHeight *= zoom; restart();

    }

    private void downsize(){

        size /= zoom; windowWidth /= zoom; windowHeight /= zoom; restart();

    }


    public static void main(String[] args) {
        launch(args);
    }
}

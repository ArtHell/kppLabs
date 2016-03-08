package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;

public class ArtRico extends Application {
    Pane gameroot = new Pane();
    Scene scene;
    ArrayList<StaticObject> bricks;              // массив кирпичей
    Player player;                               // игрок
    Ball ball;                                   // шар
    StaticObject leftWall;                       // левая стена
    StaticObject rightWall;                      // правая стена
    StaticObject top;                            // верхняя граница
    StaticObject bottom;                         // нижняя граница
    static int score;                            // очки
    Label scoreLabel;                            // лэйбл для очков
    static HashSet<String> currentlyActiveKeys;  // массив нажатых клавиш
    static HashSet<String> lastKey;
    boolean pause;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ArtRik");
        scene = new Scene(createContent());
        prepareActionHandlers();
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now)
            {
                if (ball.isGameWon()) {
                    pause = true;
                    Button button = new Button("Completed! Your score is " + score);
                    button.setLayoutX(600);
                    button.setLayoutY(240);
                    button.setPrefSize(160,80);
                    button.setOnAction(e -> {
                        pause = false;
                        button.setVisible(false);
                    });
                    ball.gameWon = false;
                    restart();
                    gameroot.getChildren().add(button);
                }
                if (lastKey.contains("SPACE") ) {

                    if(pause == false) {
                        pause = true;
                    }
                    else {
                        pause = false;
                    }
                }
                lastKey.clear();
                if(!pause) {
                    update();
                }

            }
        };
        timer.start();
    }

    public Parent createContent() {
        pause = false;
        lastKey = new HashSet<String>();
        gameroot.setPrefSize(1280,640);
        Rectangle bg = new Rectangle(1280,640,Color.GREY);
        player = new Player(570,570,140,20,2,Color.ORANGE,Color.BEIGE,10,1270);
        player.setSpeed(10);
        ball = new Ball(630,550,20,20,2,Color.ORANGE,Color.BEIGE,10,1270,30,640);
        ball.setSpeedX(5);
        ball.setSpeedY(5);
        ball.setPlayer(player);
        ball.setFlagsInFalse();
        leftWall = new StaticObject(0,0,10,640,2,Color.ORANGE,Color.BEIGE);
        rightWall = new StaticObject(1270,0,10,640,2,Color.ORANGE,Color.BEIGE);
        rightWall = new StaticObject(1270,0,10,640,2,Color.ORANGE,Color.BEIGE);
        bottom = new StaticObject(0,640,1280,20,2,Color.ORANGE,Color.BEIGE);
        top = new StaticObject(0,0,1280,30,2,Color.ORANGE,Color.BEIGE);
        score = 0;
        scoreLabel = new Label("Score : " + score);
        scoreLabel.setLayoutX(40);
        scoreLabel.setLayoutY(0);
        scoreLabel.setFont(javafx.scene.text.Font.font(20));
        gameroot.getChildren().addAll(bg,player,ball,leftWall,rightWall,bottom,top,scoreLabel);
        bricks = new ArrayList<>();
        createBricks();
        return gameroot;
    }
    private void createBricks() {
        for(StaticObject b : bricks) {
            b.setVisible(false);
        }
        bricks.clear();
        for (int i = 0; i < 5; i++) {
            for(int j = 0; j < 20; j++) {
                StaticObject brick = new StaticObject(j*60+50,i*60+40,40,40,2,Color.ORANGE,Color.BEIGE);
                bricks.add(brick);
                gameroot.getChildren().add(brick);
            }
        }
        // Другие варианты карты...
        /*for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 3; j++) {
                StaticObject brick = new StaticObject(240 + j*300, 100, 200, 200, 2, Color.ORANGE, Color.BEIGE);
                bricks.add(brick);
                gameroot.getChildren().add(brick);
            }
        }*/

        /*for (int i = 0; i < 2; i++) {
            for(int j = 0; j < 14; j++) {
                StaticObject brick = new StaticObject(j*60+10 + 5*j ,i*60+40,5*j,40,2,Color.ORANGE,Color.BEIGE);
                bricks.add(brick);
                gameroot.getChildren().add(brick);
            }
        }*/

        ball.setBricks(bricks);
    }
    public void restart() {
        createBricks();
        score = 0;
        player.setTranslateX(600);
        player.setTranslateY(570);
        player.setSpeed(10);
        ball.setTranslateX(630);
        ball.setTranslateY(550);
        ball.setSpeedX(5);
        ball.setSpeedY(5);
        ball.gameOver = false;
        ball.setFlagsInFalse();
    }

    private void update() {
        if (currentlyActiveKeys.contains("LEFT"))
        {
            player.moveLeft();
        }
        if (currentlyActiveKeys.contains("RIGHT"))
        {
            player.moveRight();
        }
        if(ball.isGameOver()) {
            restart();
        }
        else {
            ball.move();
        }
        scoreLabel.setText("Score : " + score);
    }

    private  void prepareActionHandlers()
    {
        // use a set so duplicates are not possible
        currentlyActiveKeys = new HashSet<String>();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                currentlyActiveKeys.add(event.getCode().toString());
                lastKey.add(event.getCode().toString());
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                currentlyActiveKeys.remove(event.getCode().toString());
            }
        });
    }
    public static void main(String[] args) {
        launch(args);
    }
}

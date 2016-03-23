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
    Pane gameRoot;
    Pane menuRoot;
    Scene scene;
    ArrayList<SimpleObject> bricks;              // массив кирпичей
    Player player;                               // игрок
    Ball ball;                                   // шар
    SimpleObject leftWall;                       // левая стена
    SimpleObject rightWall;                      // правая стена
    SimpleObject top;                            // верхняя граница
    SimpleObject bottom;                         // нижняя граница
    static int score;                            // очки
    Label scoreLabel;                            // лэйбл для очков
    Bot bot;                                     // бот
    static HashSet<String> currentlyActiveKeys;  // массив нажатых клавиш
    static HashSet<String> lastKey;
    boolean pause;
    AnimationTimer timer;


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ArtRik");
        showMenu(primaryStage);
    }

    private void startGame(Stage primaryStage, int mode){
        scene = new Scene(createContent(mode));
        prepareActionHandlers();
        primaryStage.setScene(scene);
        primaryStage.show();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now)
            {
                if (ball.isGameWon()) {
                    showMessage("You won! Your score is: ");
                }
                if(ball.isGameOver()) {
                    showMessage("You lose! Your score is: ");
                }
                if (lastKey.contains("SPACE") ) {

                    if (pause == false) {
                        pause = true;
                    } else {
                        pause = false;
                    }
                }
                if (lastKey.contains("ESCAPE") ) {
                    timer.stop();
                    showMenu(primaryStage);
                }
                lastKey.clear();
                if(!pause) {
                    update();
                }

            }
        };
        timer.start();
    }

    private Parent createContent(int mode) {
        pause = true;
        gameRoot = new Pane();
        gameRoot.setPrefSize(1280,640);
        Rectangle bg = new Rectangle(1280,640,Color.GREY);
        leftWall = new SimpleObject(0,0,10,640,2,Color.ORANGE,Color.BEIGE);
        rightWall = new SimpleObject(1270,0,10,640,2,Color.ORANGE,Color.BEIGE);
        rightWall = new SimpleObject(1270,0,10,640,2,Color.ORANGE,Color.BEIGE);
        bottom = new SimpleObject(0,640,1280,20,2,Color.ORANGE,Color.BEIGE);
        top = new SimpleObject(0,0,1280,30,2,Color.ORANGE,Color.BEIGE);
        score = 0;
        scoreLabel = new Label("Score : " + score);
        scoreLabel.setLayoutX(40);
        scoreLabel.setLayoutY(0);
        scoreLabel.setFont(javafx.scene.text.Font.font(20));
        if(mode == 0){
            player = new Player(500,570,280,20,2,Color.ORANGE,Color.BEIGE,10,1270,10);
            ball = new Ball(635,550,20,20,2,Color.ORANGE,Color.BEIGE,10,1270,30,640,5,5);
        }
        if(mode == 1){
            player = new Player(540,570,200,20,2,Color.ORANGE,Color.BEIGE,10,1270,10);
            ball = new Ball(635,550,20,20,2,Color.ORANGE,Color.BEIGE,10,1270,30,640,5,5);
        }
        if(mode == 2){
            player = new Player(570,570,140,20,2,Color.ORANGE,Color.BEIGE,10,1270,10);
            ball = new Ball(635,550,20,20,2,Color.ORANGE,Color.BEIGE,10,1270,30,640,10,10);
        }

        ball.setPlayer(player);
        ball.setFlagsInFalse();
        bot = new Bot(player, ball);
        gameRoot.getChildren().addAll(bg,player,ball,leftWall,rightWall,bottom,top,scoreLabel);
        bricks = new ArrayList<>();
        createBricks(15,8,80,40);
        return gameRoot;
    }

    private void createBricks(int colomn, int raw, int w, int h) {
        for(SimpleObject b : bricks) {
            b.setVisible(false);
        }
        bricks.clear();
        for (int i = 0; i < raw; i++) {
            for(int j = 0; j < colomn; j++) {
                SimpleObject brick = new SimpleObject(j*(w)+40,i*(h)+65,w,h,2,Color.ORANGE,Color.BEIGE);
                bricks.add(brick);
                gameRoot.getChildren().add(brick);
            }
        }
        ball.setBricks(bricks);
    }
    private void restart() {
        gameRoot.getChildren().remove(bricks);
        createBricks(15,8,80,40);
        score = 0;
        player.setTranslateX(570);
        player.setTranslateY(570);
        ball.setTranslateX(635);
        ball.setTranslateY(550);
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
        //bot.Execute();
        ball.move();
        scoreLabel.setText("Score : " + score);
    }

    private  void prepareActionHandlers()
    {
        // use a set so duplicates are not possible
        currentlyActiveKeys = new HashSet<String>();
        lastKey = new HashSet<String>();
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
    private void showMessage (String msg) {
        pause = true;
        Button button = new Button(msg + score);
        button.setLayoutX(510);
        button.setLayoutY(240);
        button.setPrefSize(260,80);
        button.setOnAction(e -> {
            pause = false;
            button.setVisible(false);
            gameRoot.getChildren().remove(button);
        });
        ball.gameWon = false;
        ball.gameOver = false;
        restart();
        gameRoot.getChildren().add(button);
    }
    private void showMenu(Stage primaryStage){
        menuRoot = new Pane();
        menuRoot.setPrefSize(1280,640);
        Rectangle bg = new Rectangle(1280,640,Color.GREY);
        Button startButton = new Button("Start game");
        startButton.setLayoutX(510);
        startButton.setLayoutY(140);
        startButton.setPrefSize(260,80);
        startButton.setOnAction(e -> {
            startButton.setVisible(false);
            Button[] button = new Button[4];
            for(int i = 0; i < 4; i++) {
                button[i] = new Button();
                button[i].setLayoutX(510);
                button[i].setLayoutY(i*100+140);
                button[i].setPrefSize(260,80);
                menuRoot.getChildren().add(button[i]);
            }
            button[0].setText("Easy");
            button[1].setText("Medium");
            button[2].setText("Hard");
            button[3].setText("Back");
            button[0].setOnAction(event -> {
                startGame(primaryStage, 0);
            });
            button[1].setOnAction(event -> {
                startGame(primaryStage, 1);
            });
            button[2].setOnAction(event -> {
                startGame(primaryStage, 2);
            });
            button[3].setOnAction(event -> {
                for(Button b : button) {
                    b.setVisible(false);
                    startButton.setVisible(true);
                }
            });
        });
        menuRoot.getChildren().addAll(bg,startButton);
        scene = new Scene(menuRoot);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}

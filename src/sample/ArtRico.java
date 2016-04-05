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
  final int SCENE_WIDTH = 1280;
  final int SCENE_HEIGHT = 640;
  final int WALL_WIDTH = 10;
  final int STROKE_WIDTH = 2;
  final int TOP_HEIGHT = 30;
  final int BOTTOM_HEIGHT = 10;
  final int PLAYER_HEIGHT = 20;
  final int PLAYER_POS_Y = 570;
  final int PLAYER_WIDTH_EASY = 280;
  final int PLAYER_WIDTH_MEDIUM = 200;
  final int PLAYER_WIDTH_HARD = 200;
  final int PLAYER_SPEED_EASY = 10;
  final int PLAYER_SPEED_MEDIUM = 10;
  final int PLAYER_SPEED_HARD = 20;
  final int BALL_SPEED_EASY = 5;
  final int BALL_SPEED_MEDIUM = 5;
  final int BALL_SPEED_HARD = 10;
  final int BALL_SIZE = 20;
  final int BRICKS_IN_RAW = 15;
  final int BRICKS_IN_COLOMN = 8;
  final int BRICKS_HEIGHT = 40;
  final int BRICKS_WIDTH = 80;
  final int BUTTON_WIDTH = 260;
  final int BUTTON_HEIGHT = 80;

  Pane gameRoot;
  Pane menuRoot;
  Scene scene;
  ArrayList<SimpleObject> bricks;
  Player player;
  Ball ball;
  SimpleObject leftWall;
  SimpleObject rightWall;
  SimpleObject top;
  SimpleObject bottom;
  static int score;
  Label scoreLabel;
  Bot bot;
  static HashSet<String> currentlyActiveKeys;
  static HashSet<String> lastKey;
  boolean pause;
  AnimationTimer timer;


  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("ArtRik");
    showMenu(primaryStage);
  }

  private void startGame(Stage primaryStage, int mode) {
    scene = new Scene(createContent(mode));
    prepareActionHandlers();
    primaryStage.setScene(scene);
    primaryStage.show();

    timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        if (ball.isGameWon()) {
          showMessage("You won! Your score is: ");
        }
        if (ball.isGameOver()) {
          showMessage("You lose! Your score is: ");
        }
        if (lastKey.contains("SPACE")) {
          if (pause == false) {
            pause = true;
          } else {
            pause = false;
          }
        }
        if (lastKey.contains("ESCAPE")) {
          gameRoot.getChildren().clear();
          timer.stop();
          showMenu(primaryStage);
        }
        if (lastKey.contains("B")) {
          if (bot.isOn()) {
            bot.setOff();
          } else {
            bot.setOn();
          }
        }
        lastKey.clear();
        if (!pause) {
          update();
        }
      }
    };
    timer.start();
  }

  private Parent createContent(int mode) {
    pause = true;
    gameRoot = new Pane();
    gameRoot.setPrefSize(SCENE_WIDTH, SCENE_HEIGHT);
    Rectangle bg = new Rectangle(SCENE_WIDTH, SCENE_HEIGHT, Color.GREY);
    leftWall = new SimpleObject(0, 0, WALL_WIDTH, SCENE_HEIGHT,
        STROKE_WIDTH, Color.ORANGE, Color.BEIGE);
    rightWall = new SimpleObject(SCENE_WIDTH - WALL_WIDTH, 0, WALL_WIDTH,
        SCENE_WIDTH, STROKE_WIDTH, Color.ORANGE, Color.BEIGE);
    bottom = new SimpleObject(0, SCENE_HEIGHT, SCENE_WIDTH, BOTTOM_HEIGHT,
        STROKE_WIDTH, Color.ORANGE, Color.BEIGE);
    top = new SimpleObject(0, 0, SCENE_WIDTH, TOP_HEIGHT,
        STROKE_WIDTH, Color.ORANGE, Color.BEIGE);
    score = 0;
    scoreLabel = new Label("Score : " + score);
    scoreLabel.setLayoutX(40);
    scoreLabel.setLayoutY(0);
    scoreLabel.setFont(javafx.scene.text.Font.font(20));

    switch (mode) {
      case 0: {
        player = new Player(SCENE_WIDTH / 2 - PLAYER_WIDTH_EASY / 2,
            PLAYER_POS_Y, PLAYER_WIDTH_EASY, PLAYER_HEIGHT,
            STROKE_WIDTH, Color.ORANGE, Color.BEIGE,
            WALL_WIDTH, SCENE_WIDTH - WALL_WIDTH, PLAYER_SPEED_EASY);
        ball = new Ball(SCENE_WIDTH / 2 - BALL_SIZE / 2, PLAYER_POS_Y - BALL_SIZE,
            BALL_SIZE, BALL_SIZE, STROKE_WIDTH, Color.ORANGE,
            Color.BEIGE, WALL_WIDTH, SCENE_WIDTH - WALL_WIDTH,
            TOP_HEIGHT, SCENE_HEIGHT, BALL_SPEED_EASY);
        break;
      }
      case 1: {
        player = new Player(SCENE_WIDTH / 2 - PLAYER_WIDTH_MEDIUM / 2,
            PLAYER_POS_Y, PLAYER_WIDTH_MEDIUM,
            PLAYER_HEIGHT, STROKE_WIDTH, Color.ORANGE, Color.BEIGE,
            WALL_WIDTH, SCENE_WIDTH - WALL_WIDTH, PLAYER_SPEED_MEDIUM);
        ball = new Ball(SCENE_WIDTH / 2 - BALL_SIZE / 2, PLAYER_POS_Y - BALL_SIZE,
            BALL_SIZE, BALL_SIZE, STROKE_WIDTH, Color.ORANGE,
            Color.BEIGE, WALL_WIDTH, SCENE_WIDTH - WALL_WIDTH,
            TOP_HEIGHT, SCENE_HEIGHT, BALL_SPEED_MEDIUM);
        break;
      }
      case 2: {
        player = new Player(SCENE_WIDTH / 2 - PLAYER_WIDTH_HARD / 2,
            PLAYER_POS_Y, PLAYER_WIDTH_HARD, PLAYER_HEIGHT,
            STROKE_WIDTH, Color.ORANGE, Color.BEIGE,
            WALL_WIDTH, SCENE_WIDTH - WALL_WIDTH, PLAYER_SPEED_HARD);
        ball = new Ball(SCENE_WIDTH / 2 - BALL_SIZE / 2,
            PLAYER_POS_Y - BALL_SIZE, BALL_SIZE,
            BALL_SIZE, STROKE_WIDTH, Color.ORANGE,
            Color.BEIGE, WALL_WIDTH, SCENE_WIDTH - WALL_WIDTH,
            TOP_HEIGHT, SCENE_HEIGHT, BALL_SPEED_HARD);
        break;
      }

    }

    ball.setPlayer(player);
    ball.setFlagsInFalse();
    bot = new Bot(player, ball);
    gameRoot.getChildren().addAll(bg, player, ball, leftWall, rightWall,
        bottom, top, scoreLabel);
    bricks = new ArrayList<>();
    createBricks(BRICKS_IN_RAW, BRICKS_IN_COLOMN, BRICKS_WIDTH, BRICKS_HEIGHT);
    return gameRoot;
  }

  private void createBricks(int colomn, int raw, int w, int h) {
    gameRoot.getChildren().removeAll(bricks);
    bricks.clear();
    for (int i = 0; i < raw; i++) {
      for (int j = 0; j < colomn; j++) {
        SimpleObject brick = new SimpleObject(j * w + 40, i * h + 75,
            w, h, STROKE_WIDTH, Color.ORANGE, Color.BEIGE);
        bricks.add(brick);

      }
    }
    gameRoot.getChildren().addAll(bricks);
    ball.setBricks(bricks);
  }

  private void restart() {
    gameRoot.getChildren().removeAll(bricks);
    createBricks(BRICKS_IN_RAW, BRICKS_IN_COLOMN, BRICKS_WIDTH, BRICKS_HEIGHT);
    score = 0;
    player.setTranslateX(SCENE_WIDTH / 2 - player.getWidth() / 2 + STROKE_WIDTH / 2);
    player.setTranslateY(PLAYER_POS_Y);
    ball.setTranslateX(SCENE_WIDTH / 2 - BALL_SIZE / 2);
    ball.setTranslateY(PLAYER_POS_Y - BALL_SIZE);
    ball.gameOver = false;
    ball.setFlagsInFalse();
  }

  private void update() {
    if (bot.isOn()) {
      bot.Execute();
    } else if (currentlyActiveKeys.contains("LEFT")) {
      player.moveLeft();
    } else if (currentlyActiveKeys.contains("RIGHT")) {
      player.moveRight();
    }
    ball.move();
    scoreLabel.setText("Score : " + score);
  }

  private void prepareActionHandlers() {
    // use a set so duplicates are not possible
    currentlyActiveKeys = new HashSet<String>();
    lastKey = new HashSet<String>();
    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        currentlyActiveKeys.add(event.getCode().toString());
        lastKey.add(event.getCode().toString());
      }
    });
    scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        currentlyActiveKeys.remove(event.getCode().toString());
      }
    });
  }

  private void showMessage(String msg) {
    pause = true;
    Button button = new Button(msg + score);
    button.setLayoutX(510);
    button.setLayoutY(240);
    button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
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

  private void showMenu(Stage primaryStage) {
    menuRoot = new Pane();
    menuRoot.setPrefSize(SCENE_WIDTH, SCENE_HEIGHT);
    Rectangle bg = new Rectangle(SCENE_WIDTH, SCENE_HEIGHT, Color.GREY);
    Button[] button1 = new Button[4];

    for (int i = 0; i < 4; i++) {
      button1[i] = new Button();
      button1[i].setLayoutX(510);
      button1[i].setLayoutY(i * 100 + 140);
      button1[i].setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
      menuRoot.getChildren().add(button1[i]);
    }

    button1[0].setText("NEW GAME");
    button1[1].setText("SETTINGS");
    button1[2].setText("ABOUT");
    button1[3].setText("EXIT");

    button1[0].setOnAction(e -> {
      menuRoot.getChildren().removeAll(button1);
      Button[] button2 = new Button[4];
      for (int i = 0; i < 4; i++) {
        button2[i] = new Button();
        button2[i].setLayoutX(510);
        button2[i].setLayoutY(i * 100 + 140);
        button2[i].setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        menuRoot.getChildren().add(button2[i]);
      }
      button2[0].setText("Easy");
      button2[1].setText("Medium");
      button2[2].setText("Hard");
      button2[3].setText("Back");
      button2[0].setOnAction(event -> {
        menuRoot.getChildren().clear();
        startGame(primaryStage, 0);
      });
      button2[1].setOnAction(event -> {
        menuRoot.getChildren().clear();
        startGame(primaryStage, 1);
      });
      button2[2].setOnAction(event -> {
        menuRoot.getChildren().clear();
        startGame(primaryStage, 2);
      });
      button2[3].setOnAction(event -> {
        menuRoot.getChildren().removeAll(button2);
        menuRoot.getChildren().addAll(button1);
      });
    });

    button1[3].setOnAction(event -> {
      System.exit(0);
    });

    scene = new Scene(menuRoot);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

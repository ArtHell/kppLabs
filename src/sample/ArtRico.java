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

public class ArtRico extends Application
    implements Constants, MoveConstants {
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
  String replay;
  char gameMode;
  char replayMode;
  boolean replayNow;
  int replayIterator;



  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("ArtRik");
    showMenu(primaryStage);
  }

  private void startGame(Stage primaryStage, char mode) {
    scene = new Scene(createContent(mode));
    prepareActionHandlers();
    primaryStage.setScene(scene);
    primaryStage.show();

    timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        if (ball.isGameWon()) {
          if(replayNow){
            gameRoot.getChildren().clear();
            timer.stop();
            showMenu(primaryStage);
          } else{
            replay = mode + replay;
            Serializer.saveReplay(REPLAY_FILE,replay);
            replay = "";
            showMessage("You won! Your score is: ");
          }
        }
        if (ball.isGameOver()) {
          if(replayNow){
            gameRoot.getChildren().clear();
            timer.stop();
            showMenu(primaryStage);
          } else {
            replay = mode + replay;
            Serializer.saveReplay(REPLAY_FILE, replay);
            replay = "";
            showMessage("You lose! Your score is: ");
          }
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

  private Parent createContent(char mode) {
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
      case 'e': {
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
      case 'm': {
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
      case 'h': {
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
    char direction;
    if(replayNow) {
      switch(replay.toCharArray()[replayIterator]) {
        case MOVED_LEFT: player.moveLeft(); break;
        case MOVED_RIGHT: player.moveRight(); break;
        default: break;
      }
      replayIterator++;
    } else {
      if (bot.isOn()) {
        direction = bot.Execute();
      } else if (currentlyActiveKeys.contains("LEFT")) {
        direction = player.moveLeft();
      } else if (currentlyActiveKeys.contains("RIGHT")) {
        direction = player.moveRight();
      } else {
        direction = NOT_MOVED;
      }
      replay += direction;
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
    replayNow = false;
    replay = "";
    menuRoot = new Pane();
    menuRoot.setPrefSize(SCENE_WIDTH, SCENE_HEIGHT);
    Rectangle bg = new Rectangle(SCENE_WIDTH, SCENE_HEIGHT, Color.GREY);
    Button[] mainMenuButtons = new Button[MENU_SIZE];

    for (int i = 0; i < MENU_SIZE; i++) {
      mainMenuButtons[i] = new Button();
      mainMenuButtons[i].setLayoutX(510);
      mainMenuButtons[i].setLayoutY(i * 100 + 140);
      mainMenuButtons[i].setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
      menuRoot.getChildren().add(mainMenuButtons[i]);
    }

    mainMenuButtons[0].setText("NEW GAME");
    mainMenuButtons[1].setText("SETTINGS");
    mainMenuButtons[2].setText("ABOUT");
    mainMenuButtons[3].setText("REPLAY");
    mainMenuButtons[4].setText("EXIT");

    mainMenuButtons[0].setOnAction(e -> {
      menuRoot.getChildren().removeAll(mainMenuButtons);
      Button[] newGameMenuButtons = new Button[4];
      for (int i = 0; i < NEW_GAME_MENU_SIZE; i++) {
        newGameMenuButtons[i] = new Button();
        newGameMenuButtons[i].setLayoutX(510);
        newGameMenuButtons[i].setLayoutY(i * 100 + 140);
        newGameMenuButtons[i].setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        menuRoot.getChildren().add(newGameMenuButtons[i]);
      }
      newGameMenuButtons[0].setText("Easy");
      newGameMenuButtons[1].setText("Medium");
      newGameMenuButtons[2].setText("Hard");
      newGameMenuButtons[3].setText("Back");
      newGameMenuButtons[0].setOnAction(event -> {
        menuRoot.getChildren().clear();
        gameMode = EASY_MODE;
        startGame(primaryStage, EASY_MODE);
      });
      newGameMenuButtons[1].setOnAction(event -> {
        menuRoot.getChildren().clear();
        gameMode = MEDIUM_MODE;
        startGame(primaryStage, MEDIUM_MODE);
      });
      newGameMenuButtons[2].setOnAction(event -> {
        menuRoot.getChildren().clear();
        gameMode = HARD_MODE;
        startGame(primaryStage, HARD_MODE);
      });
      newGameMenuButtons[3].setOnAction(event -> {
        menuRoot.getChildren().removeAll(newGameMenuButtons);
        menuRoot.getChildren().addAll(mainMenuButtons);
      });
    });

    mainMenuButtons[3].setOnAction(event -> {
      replay = Serializer.loadReplay(REPLAY_FILE);
      if(!replay.isEmpty()) {
        replayNow = true;
        replayMode = replay.toCharArray()[0];
        replayIterator = 1;
        menuRoot.getChildren().clear();
        startGame(primaryStage, replayMode);
      }

    });

    mainMenuButtons[4].setOnAction(event -> {
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

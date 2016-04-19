package sample;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashSet;


public class Client extends Thread implements Constants {
  Pane gameRoot;
  Scene scene;
  Player player;
  Ball ball;
  ArrayList<SimpleObject> bricks;
  SimpleObject leftWall;
  SimpleObject rightWall;
  SimpleObject top;
  SimpleObject bottom;
  Rectangle background;
  Label scoreLabel;
  Server server;
  Game game;
  boolean working;

  char direction;
  HashSet<String> lastKey;

  public Client(char gameMode, Game game) {
    super();
    server = new Server(gameMode, this);
    this.bricks = new ArrayList<>();
    this.game = game;
    working = true;
    start();
  }

  @Override
  public void run() {
    try {
      sleep(100000);
    } catch (InterruptedException e) {
      scene = new Scene(initGame());
      prepareActionHandlers();
      server.interrupt();
    }
    while (working) {
      try {
        sleep(10000);
      } catch (InterruptedException e) {
        return;
      }
    }
  }

  private Pane initGame() {
    gameRoot = new Pane();
    gameRoot.setPrefSize(SCENE_WIDTH, SCENE_HEIGHT);
    background = new Rectangle(SCENE_WIDTH, SCENE_HEIGHT, Color.GREY);
    scoreLabel = new Label();
    scoreLabel.setLayoutX(40);
    scoreLabel.setLayoutY(0);
    scoreLabel.setFont(javafx.scene.text.Font.font(20));
    gameRoot.getChildren().add(background);
    gameRoot.getChildren().addAll(bricks);
    gameRoot.getChildren().addAll(player, ball, leftWall, rightWall, top, bottom);
    gameRoot.getChildren().add(scoreLabel);
    return gameRoot;
  }

  public void setContent(Player player, Ball ball,
                         ArrayList<SimpleObject> bricks, SimpleObject leftWall,
                         SimpleObject rightWall, SimpleObject top,
                         SimpleObject bottom) {
    this.player = player;
    this.ball = ball;
    this.bricks = bricks;
    this.leftWall = leftWall;
    this.rightWall = rightWall;
    this.top = top;
    this.bottom = bottom;
  }

  /**
   *
   */
  private void prepareActionHandlers() {
    lastKey = new HashSet<>();
    direction = 'n';
    scene.setOnKeyPressed(event -> {
      switch (event.getCode().toString()) {
        case "LEFT":
          direction = 'l';
          break;
        case "RIGHT":
          direction = 'r';
          break;
        case "SPACE":
          server.animationSwitch();
          break;
        case "B":
          server.bot.switchMode();
          break;
        case "ESCAPE":
          server.working = false;
          game.exit();
          this.working = false;
      }
      lastKey.add(event.getCode().toString());
    });
    scene.setOnKeyReleased(event -> {
      direction = 'n';
    });
  }

  public void resetBricks() {
    gameRoot.getChildren().removeAll(bricks);
  }

  public void setBricks(ArrayList<SimpleObject> bricks) {
    gameRoot.getChildren().addAll(bricks);
  }

  public char getDirection() {
    return direction;
  }
}

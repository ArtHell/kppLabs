package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Server extends Thread implements Constants, MoveConstants {
  Player player;
  Ball ball;
  ArrayList<SimpleObject> bricks;
  SimpleObject leftWall;
  SimpleObject rightWall;
  SimpleObject top;
  SimpleObject bottom;
  char gameMode;
  char direction;
  boolean pause;
  Bot bot;
  boolean working;

  String replay;
  boolean replayNow;
  int replayIterator;

  AnimationTimer timer;
  Client client;

  public Server(char gameMode, Client client) {
    super();
    working = true;
    if (gameMode == REPLAY_MODE) {
      replay = Serializer.loadReplay(REPLAY_FILE);
      if (!replay.isEmpty()) {
        replayNow = true;
        this.gameMode = replay.toCharArray()[0];
        replayIterator = 1;
      }
    } else {
      replayNow = false;
      this.gameMode = gameMode;
      replay = "" + gameMode;
    }

    this.client = client;
    start();
  }

  @Override
  public void run() {
    createContent();
    client.setContent(player, ball, bricks, leftWall, rightWall, top, bottom);
    client.interrupt();
    try {
      sleep(100000);
    } catch (InterruptedException e) {
      timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
          if(working) {
            update();
          }
          else {
            timer.stop();
          }
        }
      };
      pause = false;
      timer.start();
    }
  }

  private void createContent() {
    leftWall = new SimpleObject(0, 0, WALL_WIDTH, SCENE_HEIGHT,
        STROKE_WIDTH, Color.ORANGE, Color.BEIGE);
    rightWall = new SimpleObject(SCENE_WIDTH - WALL_WIDTH, 0, WALL_WIDTH,
        SCENE_WIDTH, STROKE_WIDTH, Color.ORANGE, Color.BEIGE);
    bottom = new SimpleObject(0, SCENE_HEIGHT, SCENE_WIDTH, BOTTOM_HEIGHT,
        STROKE_WIDTH, Color.ORANGE, Color.BEIGE);
    top = new SimpleObject(0, 0, SCENE_WIDTH, TOP_HEIGHT,
        STROKE_WIDTH, Color.ORANGE, Color.BEIGE);

    switch (gameMode) {
      case EASY_MODE: {
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
      case MEDIUM_MODE: {
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
      case HARD_MODE: {
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
    bricks = new ArrayList<>();
    createBricks(BRICKS_IN_RAW, BRICKS_IN_COLOMN, BRICKS_WIDTH, BRICKS_HEIGHT);
  }

  /**
   * @param colomn
   * @param raw
   * @param width
   * @param height
   */
  private void createBricks(int colomn, int raw, int width, int height) {
    bricks.clear();
    for (int i = 0; i < raw; i++) {
      for (int j = 0; j < colomn; j++) {
        SimpleObject brick = new SimpleObject(j * width + 40, i * height + 75,
            width, height, STROKE_WIDTH, Color.ORANGE, Color.BEIGE);
        bricks.add(brick);

      }
    }
    ball.setBricks(bricks);
  }

  private void update() {
    if (ball.isGameWon()) {
      restart();
    }
    if (ball.isGameOver()) {
      restart();
    }
    if (replayNow) {
      direction = replay.toCharArray()[replayIterator];
      replayIterator++;
    } else if (bot.isOn()) {
      direction = bot.execute();
      replay += direction;
    } else {
      direction = client.getDirection();
      replay += direction;
    }
    if (direction == 'l') {
      player.moveLeft();
    } else if (direction == 'r') {
      player.moveRight();
    }
    ball.move();
    client.scoreLabel.setText("Score: " + ball.score);
  }

  private void restart() {
    if (replayNow) {
      replayIterator = 1;
    } else {
      Serializer.saveReplay(REPLAY_FILE, replay);
      replay = "" + gameMode;
    }
    client.resetBricks();
    createBricks(BRICKS_IN_RAW, BRICKS_IN_COLOMN, BRICKS_WIDTH, BRICKS_HEIGHT);
    player.setTranslateX(SCENE_WIDTH / 2 - player.getWidth() / 2 + STROKE_WIDTH / 2);
    player.setTranslateY(PLAYER_POS_Y);
    ball.setTranslateX(SCENE_WIDTH / 2 - BALL_SIZE / 2);
    ball.setTranslateY(PLAYER_POS_Y - BALL_SIZE);
    ball.gameWon = false;
    ball.gameOver = false;
    ball.setFlagsInFalse();
    client.setBricks(bricks);
  }

  public void animationSwitch() {
    if (pause) {
      timer.start();
      pause = false;
    } else {
      timer.stop();
      pause = true;
    }

  }
}
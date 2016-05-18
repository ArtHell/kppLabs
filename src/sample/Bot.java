package sample;

import java.util.Random;

/**
 * program can play without player
 */
public class Bot implements Constants {
  final int timeOfMoving = 2000;
  Player player;
  Ball ball;
  int time;
  boolean on;

  /**
   * @param player
   * @param ball
   */
  public Bot(Player player, Ball ball) {
    this.player = player;
    this.ball = ball;
    time = 0;
    on = false;
  }

  /**
   * @return
   */
  public char execute() {
    Random toMove = new Random();
    if (toMove.nextBoolean() && toMove.nextBoolean() && toMove.nextBoolean()) {
      if (time == timeOfMoving * 2) {
        time = 0;
      }
      time++;
      if (time < timeOfMoving) {
        if (player.getTranslateX() + player.getWidth() / 2
            < ball.getTranslateX() + ball.getWidth()) {
          return RIGHT_MOVE;
        } else {
          return LEFT_MOVE;
        }
      } else {
        if (player.getTranslateX() + player.getWidth() / 2
            < ball.getTranslateX()) {
          return RIGHT_MOVE;
        } else {
          return LEFT_MOVE;
        }
      }
    } else {
      return NOT_MOVE;
    }
  }

  /**
   * @return
   */
  public boolean isOn() {
    return on;
  }

  public void switchMode() {
    if (on) {
      on = false;
    } else {
      on = true;
    }
  }
}

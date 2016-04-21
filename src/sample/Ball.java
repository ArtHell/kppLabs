package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class Ball extends SimpleObject {
  Player player;
  ArrayList<SimpleObject> bricks;
  ArrayList<Integer> indexs;
  double speed, kx, ky;
  double leftBorder;
  double rightBorder;
  double topBorder;
  double bottomBorder;
  boolean gameOver;
  boolean gameWon;
  boolean leftCollision;
  boolean rightCollision;
  boolean topCollision;
  boolean bottomCollision;
  boolean playerCollision;
  boolean topCover;
  boolean bottomCover;
  int score;

  /**
   * @param x
   * @param y
   * @param w
   * @param h
   * @param sw
   * @param fillColor
   * @param strokeColor
   * @param lb
   * @param rb
   * @param tb
   * @param bb
   * @param speed
   */
  public Ball(double x, double y, double w, double h, double sw,
              Color fillColor, Color strokeColor,
              double lb, double rb, double tb,
              double bb, double speed) {
    super(x, y, w, h, sw, fillColor, strokeColor);
    gameOver = false;
    gameWon = false;
    leftBorder = lb;
    rightBorder = rb;
    topBorder = tb;
    bottomBorder = bb;
    indexs = new ArrayList<>();
    bricks = new ArrayList<>();
    this.speed = speed;
    ky = 1;
    kx = 0;
  }

  void checkLeftWallCollision(){
    if (this.getTranslateX() + speed * kx <= leftBorder) {
      leftCollision = true;
    }
  }

  void checkRightWallCollision(){
    if (this.getTranslateX() + this.getWidth() + speed * kx
        >= rightBorder) {
      rightCollision = true;
    }
  }

  void checkTopCollision(){
    if (this.getTranslateY() + speed * ky <= topBorder) {
      topCollision = true;
    }
  }

  void checkBottomCollision(){
    if (this.getTranslateY() + this.getHeight() + speed * ky
        >= bottomBorder) {

      gameOver = true;
    }
  }

  void checkPlayerCollisionLeft(){
    if (ky > 0) {
      if (this.getTranslateX() + this.getWidth()
          > player.getTranslateX() &&
          this.getTranslateX() < player.getTranslateX()
              + player.getWidth()) {
        if (this.getTranslateY() <= player.getTranslateY() &&
            this.getTranslateY() + this.getHeight()
                >= player.getTranslateY()) {
          playerCollision = true;
        }
      }
    }
  }

  void checkPlayerCollisionRight(){
    if (kx > 0) {
      if (this.getTranslateY() + this.getHeight()
          > player.getTranslateY() &&
          this.getTranslateY() < player.getTranslateY()
              + player.getHeight()) {
        if (this.getTranslateX() <= player.getTranslateX() &&
            this.getTranslateX() + this.getWidth()
                >= player.getTranslateX()) {
          rightCollision = true;
        }
      }
    }
  }

  void checkPlayerCollisionTop(){
    if (kx < 0) {
      if (this.getTranslateY() + this.getHeight()
          > player.getTranslateY() &&
          this.getTranslateY() < player.getTranslateY()
              + player.getHeight()) {
        if (this.getTranslateX() <= player.getTranslateX()
            + player.getWidth() &&
            this.getTranslateX() + this.getWidth()
                >= player.getTranslateX()
                + player.getWidth()) {
          leftCollision = true;
        }
      }
    }
  }

  void checkPlayerCollision(){
    checkPlayerCollisionLeft();
    checkPlayerCollisionRight();
    checkPlayerCollisionTop();
  }

  void checkBrickCollisionBottom(){
    for (int i = 0; i < bricks.size(); i++) {
      SimpleObject brick = bricks.get(i);
      if (this.getTranslateX() + this.getWidth()
          > brick.getTranslateX() &&
          this.getTranslateX() < brick.getTranslateX()
              + brick.getWidth()) {
        if (this.getTranslateY() <= brick.getTranslateY() &&
            this.getTranslateY() + this.getHeight()
                >= brick.getTranslateY()) {
          if (ky > 0) {
            bottomCollision = true;
          } else {
            bottomCover = true;
          }
          indexs.add(i);
        }
      }
    }
  }

  void checkBrickCollisionTop(){
    for (int i = 0; i < bricks.size(); i++) {
      SimpleObject brick = bricks.get(i);
      if (this.getTranslateX() + this.getWidth()
          > brick.getTranslateX() &&
          this.getTranslateX() < brick.getTranslateX()
              + brick.getWidth()) {
        if (this.getTranslateY() <= brick.getTranslateY()
            + brick.getHeight() &&
            this.getTranslateY() + this.getHeight()
                >= brick.getTranslateY()
                + brick.getHeight()) {
          if (ky < 0) {
            topCollision = true;
          } else {
            topCover = true;
          }
          indexs.add(i);
        }
      }
    }
  }

  void checkBrickCollisionRight(){
    if (kx < 0) {
      for (int i = 0; i < bricks.size(); i++) {
        SimpleObject brick = bricks.get(i);
        if (this.getTranslateY() + this.getHeight()
            > brick.getTranslateY() &&
            this.getTranslateY() < brick.getTranslateY()
                + brick.getHeight()) {
          if (this.getTranslateX() <= brick.getTranslateX()
              + brick.getWidth() &&
              this.getTranslateX() + this.getWidth()
                  >= brick.getTranslateX()
                  + brick.getWidth()) {
            leftCollision = true;
            indexs.add(i);
          }
        }
      }
    }
  }

  void checkBrickCollisionLeft(){
    if (kx > 0) {
      for (int i = 0; i < bricks.size(); i++) {
        SimpleObject brick = bricks.get(i);
        if (this.getTranslateY() + this.getHeight()
            > brick.getTranslateY() &&
            this.getTranslateY() < brick.getTranslateY()
                + brick.getHeight()) {
          if (this.getTranslateX() <= brick.getTranslateX() &&
              this.getTranslateX() + this.getWidth()
                  >= brick.getTranslateX()) {
            leftCollision = true;
            indexs.add(i);
          }
        }
      }
    }
  }

  void checkBrickCollision(){
    checkBrickCollisionBottom();
    checkBrickCollisionTop();
    checkBrickCollisionLeft();
    checkBrickCollisionRight();
  }

  /**
   *
   */
  public void checkCollision() {
    checkLeftWallCollision();
    checkRightWallCollision();
    checkTopCollision();
    checkBottomCollision();
    checkPlayerCollision();
    checkBrickCollision();
  }

  void destroyBricks(){
    Set ixs = new HashSet(indexs);
    indexs.clear();
    indexs.addAll(ixs);
    indexs.sort(Comparator.naturalOrder());
    for (int k = 0; indexs.size() > 0; k++) {
      SimpleObject b = bricks.get(indexs.get(0) - k);
      b.setVisible(false);
      bricks.remove(b);
      indexs.remove(0);
      score++;
    }
  }

  void moveBall(){
    setTranslateX(getTranslateX() + speed * kx);
    setTranslateY(getTranslateY() + speed * ky);
  }

  /**
   *
   */
  public void move() {
    checkCollision();
    if ((topCollision && !bottomCover) ^ (bottomCollision && !topCover)) {
      ky = -ky;
    } else if (leftCollision ^ rightCollision) {
      kx = -kx;
    }
    if (playerCollision) {
      double k;
      k = (this.getTranslateX() + this.getWidth() / 2
          - player.getTranslateX()
          - player.getWidth() / 2) / (player.getWidth() / 2);
      kx = k;
      ky = -0.8 - (1 - Math.abs(k));
    }
    destroyBricks();
    if (bricks.size() == 0) {
      gameWon = true;
    }
    moveBall();
    setFlagsInFalse(); // reset flags
  }

  /**
   * @param bricks
   */
  public void setBricks(ArrayList<SimpleObject> bricks) {
    this.bricks.clear();
    score = 0;
    this.bricks.addAll(bricks);
  }

  /**
   * @param player
   */
  public void setPlayer(Player player) {
    this.player = player;
  }

  /**
   * @return
   */
  public boolean isGameOver() {
    return gameOver;
  }

  /**
   * @return
   */
  public boolean isGameWon() {
    return gameWon;
  }

  /**
   *
   */
  public void setFlagsInFalse() {
    leftCollision = false;
    rightCollision = false;
    topCollision = false;
    bottomCollision = false;
    playerCollision = false;
    topCover = false;
    bottomCover = false;
  }
}

package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Platform of player, can move left and right
 */
public class Player extends SimpleObject {
  double speed;
  double leftBorder;
  double rightBorder;

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
   * @param speed
   */
  public Player(double x, double y, double w, double h, double sw,
                Color fillColor, Color strokeColor,
                double lb, double rb, double speed) {
    super(x, y, w, h, sw, fillColor, strokeColor);
    leftBorder = lb;
    rightBorder = rb;
    this.speed = speed;
  }

  public void moveLeft() {
    if (this.getTranslateX() > leftBorder) {
      setTranslateX(this.getTranslateX() - speed);
    }
  }

  public void moveRight() {
    if (this.getTranslateX() + this.getWidth() < rightBorder) {
      setTranslateX(this.getTranslateX() + speed);
    }
  }
}

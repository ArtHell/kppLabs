package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends SimpleObject implements MoveConstants {
  double speed;
  double leftBorder;
  double rightBorder;

  public Player(double x, double y, double w, double h, double sw,
                Color fillColor, Color strokeColor,
                double lb, double rb, double speed) {
    super(x, y, w, h, sw, fillColor, strokeColor);
    leftBorder = lb;
    rightBorder = rb;
    this.speed = speed;
  }

  public char moveLeft() {
    if (this.getTranslateX() <= leftBorder) {
      return NOT_MOVED;
    }
    setTranslateX(this.getTranslateX() - speed);
    return MOVED_LEFT;
  }

  public char moveRight() {
    if (this.getTranslateX() + this.getWidth() >= rightBorder) {
      return NOT_MOVED;
    }
    setTranslateX(this.getTranslateX() + speed);
    return MOVED_RIGHT;
  }
}
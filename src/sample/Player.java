package sample;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends Pane{
    Rectangle rect;
    double speed;
    double leftBorder;
    double rightBorder;
    public Player(double x, double y, double w, double h, double sw, Color fc,Color sc, double lb, double rb) {
        rect = new Rectangle(w,h,fc);
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.getChildren().add(rect);
        rect.setStrokeWidth(sw);
        rect.setStroke(sc);
        leftBorder = lb;
        rightBorder = rb;
    }
    public void moveLeft() {
        if(this.getTranslateX() <= leftBorder) {
            return;
        }
        setTranslateX(this.getTranslateX() - speed);
    }
    public void moveRight() {
        if(this.getTranslateX() + this.getWidth() >= rightBorder) {
            return;
        }
        setTranslateX(this.getTranslateX() + speed);
    }
    public double getSpeed() {
        return speed;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }
}

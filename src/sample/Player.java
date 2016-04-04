package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends SimpleObject {
	Rectangle rect;
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

	public void moveLeft() {
		if (this.getTranslateX() <= leftBorder) {
			return;
		}
		setTranslateX(this.getTranslateX() - speed);
	}

	public void moveRight() {
		if (this.getTranslateX() + this.getWidth() >= rightBorder) {
			return;
		}
		setTranslateX(this.getTranslateX() + speed);
	}
}

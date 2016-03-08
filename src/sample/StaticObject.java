package sample;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class StaticObject extends Pane {
    Rectangle rect;

    public StaticObject(double x, double y, double w, double h, double sw, Color fillColor,Color strokeColor) {
        rect = new Rectangle(w,h,fillColor);
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.getChildren().add(rect);
        rect.setStrokeWidth(sw);
        rect.setStroke(strokeColor);
    }
}
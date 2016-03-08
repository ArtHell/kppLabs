package sample;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Ball extends Pane {
    Player player;
    ArrayList<StaticObject> bricks;
    ArrayList<Integer> indexs;
    Rectangle rect;
    double speedX, kx;
    double speedY, ky;
    double leftBorder;
    double rightBorder;
    double topBorder;
    double bottomBorder;
    boolean gameOver;
    boolean gameWon;
    boolean lc;
    boolean rc;
    boolean tc;
    boolean bc;
    boolean pc;
    public Ball(double x, double y, double w, double h, double sw, Color fillColor, Color strokeColor, double lb, double rb, double tb, double bb) {
        rect = new Rectangle(w,h,fillColor);
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.getChildren().add(rect);
        rect.setStrokeWidth(sw);
        rect.setStroke(strokeColor);
        gameOver = false;
        gameWon = false;
        leftBorder = lb;
        rightBorder = rb;
        topBorder = tb;
        bottomBorder = bb;
        indexs = new ArrayList<>();
        bricks = new ArrayList<>();
        ky = 1;
        kx = 0;
    }
    public void checkCollision(){
        if (this.getTranslateX() + speedX*kx <= leftBorder) {
            lc = true;
        }
        if (this.getTranslateX() + this.getWidth() + speedX*kx >= rightBorder) {
            rc = true;
        }
        if (this.getTranslateY() + speedY*ky <= topBorder) {
            tc = true;
        }
        if (this.getTranslateY() + this.getHeight() + speedY*ky >= bottomBorder) {

            gameOver = true;
        }
        // коллизия с игроком низом
        if(ky > 0) {
            if (this.getTranslateX() + this.getWidth() + speedX * kx > player.getTranslateX() && this.getTranslateX() + speedX * kx < player.getTranslateX() + player.getWidth()) {
                if (this.getTranslateY() + speedY * ky <= player.getTranslateY() && this.getTranslateY() + this.getHeight() + speedY * ky >= player.getTranslateY()) {
                    pc = true;
                }
            }
        }
        // коллизия с игроком правым боком
        if(kx > 0) {
            if (this.getTranslateY() + this.getHeight() + speedY * ky > player.getTranslateY() && this.getTranslateY() + speedY * ky < player.getTranslateY() + player.getHeight()) {
                if (this.getTranslateX() + speedX * kx <= player.getTranslateX() && this.getTranslateX() + this.getWidth() + speedX * kx >= player.getTranslateX()) {
                    rc = true;
                }
            }
        }
        // коллизия с игроком левым боком
        if(kx < 0) {
            if (this.getTranslateY() + this.getHeight() + speedY * ky > player.getTranslateY() && this.getTranslateY() + speedY * ky < player.getTranslateY() + player.getHeight()) {
                if (this.getTranslateX() + speedX * kx <= player.getTranslateX() + player.getWidth() && this.getTranslateX() + this.getWidth() + speedX * kx >= player.getTranslateX() + player.getWidth()) {
                    lc = true;
                }
            }
        }
        // коллизия с кирпичом низом
        if(ky > 0) {
            for (int i = 0; i < bricks.size(); i++) {
                StaticObject brick = bricks.get(i);
                if (this.getTranslateX() + this.getWidth() + speedX * kx > brick.getTranslateX() && this.getTranslateX() + speedX * kx < brick.getTranslateX() + brick.getWidth()) {
                    if (this.getTranslateY() + speedY * ky <= brick.getTranslateY() && this.getTranslateY() + this.getHeight() + speedY * ky >= brick.getTranslateY()) {
                        bc = true;
                        indexs.add(i);
                    }
                }
            }
        }
        // коллизия с кирпичом верхом
        if(ky < 0) {
            for (int i = 0; i < bricks.size(); i++) {
                StaticObject brick = bricks.get(i);
                if (this.getTranslateX() + this.getWidth() + speedX * kx > brick.getTranslateX() && this.getTranslateX() + speedX * kx < brick.getTranslateX() + brick.getWidth()) {
                    if (this.getTranslateY() + speedY * ky <= brick.getTranslateY() + brick.getHeight() && this.getTranslateY() + this.getHeight() + speedY * ky >= brick.getTranslateY() + brick.getHeight()) {
                        tc = true;
                        indexs.add(i);
                    }
                }
            }
        }
        // коллизия с кирпичом правым боком
        if(kx > 0) {
            for (int i = 0; i < bricks.size(); i++) {
                StaticObject brick = bricks.get(i);
                if (this.getTranslateY() + this.getHeight() + speedY * ky > brick.getTranslateY() && this.getTranslateY() + speedY * ky < brick.getTranslateY() + brick.getHeight()) {
                    if (this.getTranslateX() + speedX * kx <= brick.getTranslateX() && this.getTranslateX() + this.getWidth() + speedX * kx >= brick.getTranslateX()) {
                        lc = true;
                        indexs.add(i);
                    }
                }
            }
        }
        // коллизия с кирпичом левым боком
        if(kx < 0) {
            for (int i = 0; i < bricks.size(); i++) {
                StaticObject brick = bricks.get(i);
                if (this.getTranslateY() + this.getHeight() + speedY * ky > brick.getTranslateY() && this.getTranslateY() + speedY * ky < brick.getTranslateY() + brick.getHeight()) {
                    if (this.getTranslateX() + speedX * kx <= brick.getTranslateX() + brick.getWidth() && this.getTranslateX() + this.getWidth() + speedX * kx >= brick.getTranslateX() + brick.getWidth()) {
                        lc = true;
                        indexs.add(i);
                    }
                }
            }
        }
    }
    public void move() {
        checkCollision(); // проверка коллизий, установка флагов
        // изменение скорости в зависимости от флагов
        if (tc ^ bc) {
            ky = -ky;
        }
        else if(lc ^ rc) {
            kx = -kx;
        }
        if (pc) {
            double k;
            k = (this.getTranslateX()+this.getWidth()/2-player.getTranslateX()-player.getWidth()/2) / (player.getWidth()/2);
            kx = k;
            ky = -0.8 - (1 - Math.abs(k));
        }
        // уничтожение кирпичей
        Set ixs = new HashSet(indexs);
        indexs.clear();
        indexs.addAll(ixs);
        for(int k = 0; indexs.size() > 0; k++) {
            StaticObject b = bricks.get(indexs.get(0) - k);
            b.setVisible(false);
            bricks.remove(b);
            indexs.remove(0);
            ArtRico.score += 1;
        }
        if(bricks.size() == 0) {
            gameWon = true;
        }

        // перемещение мяча
        setTranslateX(getTranslateX() + speedX*kx);
        setTranslateY(getTranslateY() + speedY*ky);
        setFlagsInFalse(); // сброс флагов
    }
    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }
    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }
    public void setBricks(ArrayList<StaticObject> bricks) {
        for(StaticObject b : this.bricks) {
            b.setVisible(false);
        }
        this.bricks.clear();
        this.bricks.addAll(bricks);
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public boolean isGameOver() {
        return gameOver;
    }
    public boolean isGameWon() {
        return gameWon;
    }
    public void setFlagsInFalse() {
        lc = false;
        rc = false;
        tc = false;
        bc = false;
        pc = false;
    }

}

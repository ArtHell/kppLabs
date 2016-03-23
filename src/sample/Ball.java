package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Ball extends SimpleObject {
    Player player;
    ArrayList<SimpleObject> bricks;
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
    boolean lc, rc, tc, bc, pc;

    public Ball(double x, double y, double w, double h, double sw, Color fillColor, Color strokeColor,
                double lb, double rb, double tb, double bb, double speedX, double SpeedY) {
        super(x, y, w, h, sw, fillColor, strokeColor);
        gameOver = false;
        gameWon = false;
        leftBorder = lb;
        rightBorder = rb;
        topBorder = tb;
        bottomBorder = bb;
        indexs = new ArrayList<>();
        bricks = new ArrayList<>();
        this.speedX = speedX;
        this.speedY = SpeedY;
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
        // collision with player
        if(ky > 0) {
            if (this.getTranslateX() + this.getWidth()  > player.getTranslateX() &&
                    this.getTranslateX() < player.getTranslateX() + player.getWidth()) {
                if (this.getTranslateY() <= player.getTranslateY() &&
                        this.getTranslateY() + this.getHeight() >= player.getTranslateY()) {
                    pc = true;
                }
            }
        }
		// collision with player
        if(kx > 0) {
            if (this.getTranslateY() + this.getHeight() > player.getTranslateY() &&
                    this.getTranslateY()  < player.getTranslateY() + player.getHeight()) {
                if (this.getTranslateX()  <= player.getTranslateX() &&
                        this.getTranslateX() + this.getWidth()  >= player.getTranslateX()) {
                    rc = true;
                }
            }
        }
		// collision with player
        if(kx < 0) {
            if (this.getTranslateY() + this.getHeight()  > player.getTranslateY() &&
                    this.getTranslateY()  < player.getTranslateY() + player.getHeight()) {
                if (this.getTranslateX()  <= player.getTranslateX() + player.getWidth() &&
                        this.getTranslateX() + this.getWidth()  >= player.getTranslateX() + player.getWidth()) {
                    lc = true;
                }
            }
        }
		// collision with brick
        if(ky > 0) {
            for (int i = 0; i < bricks.size(); i++) {
                SimpleObject brick = bricks.get(i);
                if (this.getTranslateX() + this.getWidth()  > brick.getTranslateX() &&
                        this.getTranslateX()  < brick.getTranslateX() + brick.getWidth()) {
                    if (this.getTranslateY()  <= brick.getTranslateY() &&
                            this.getTranslateY() + this.getHeight()  >= brick.getTranslateY()) {
                        bc = true;
                        indexs.add(i);
                    }
                }
            }
        }
		// collision with brick
        if(ky < 0) {
            for (int i = 0; i < bricks.size(); i++) {
                SimpleObject brick = bricks.get(i);
                if (this.getTranslateX() + this.getWidth()  > brick.getTranslateX() &&
                        this.getTranslateX()  < brick.getTranslateX() + brick.getWidth()) {
                    if (this.getTranslateY()  <= brick.getTranslateY() + brick.getHeight() &&
                            this.getTranslateY() + this.getHeight()  >= brick.getTranslateY() + brick.getHeight()) {
                        tc = true;
                        indexs.add(i);
                    }
                }
            }
        }
		// collision with brick
        if(kx > 0) {
            for (int i = 0; i < bricks.size(); i++) {
                SimpleObject brick = bricks.get(i);
                if (this.getTranslateY() + this.getHeight()  > brick.getTranslateY() &&
                        this.getTranslateY()  < brick.getTranslateY() + brick.getHeight()) {
                    if (this.getTranslateX()  <= brick.getTranslateX() &&
                            this.getTranslateX() + this.getWidth()  >= brick.getTranslateX()) {
                        lc = true;
                        indexs.add(i);
                    }
                }
            }
        }
		// collision with brick
        if(kx < 0) {
            for (int i = 0; i < bricks.size(); i++) {
                SimpleObject brick = bricks.get(i);
                if (this.getTranslateY() + this.getHeight()  > brick.getTranslateY() &&
                        this.getTranslateY()  < brick.getTranslateY() + brick.getHeight()) {
                    if (this.getTranslateX()  <= brick.getTranslateX() + brick.getWidth() &&
                            this.getTranslateX() + this.getWidth()  >= brick.getTranslateX() + brick.getWidth()) {
                        lc = true;
                        indexs.add(i);
                    }
                }
            }
        }
    }

    public void move() {
        checkCollision();
        if (tc ^ bc) {
            ky = -ky;
        }
        else if(lc ^ rc) {
            kx = -kx;
        }
        if (pc) {
            double k;
            k = (this.getTranslateX()+this.getWidth()/2-player.getTranslateX()
                    -player.getWidth()/2) / (player.getWidth()/2);
            kx = k;
            ky = -0.8 - (1 - Math.abs(k));
        }
        // destroying bricks
        Set ixs = new HashSet(indexs);
        indexs.clear();
        indexs.addAll(ixs);
        indexs.sort(Comparator.naturalOrder());
        for(int k = 0; indexs.size() > 0; k++) {
            SimpleObject b = bricks.get(indexs.get(0) - k);
            b.setVisible(false);
            bricks.remove(b);
            indexs.remove(0);
            ArtRico.score += 1;
        }
        if(bricks.size() == 0) {
            gameWon = true;
        }

        // move ball
        setTranslateX(getTranslateX() + speedX*kx);
        setTranslateY(getTranslateY() + speedY*ky);
        setFlagsInFalse(); // reset flags
    }

    public void setBricks(ArrayList<SimpleObject> bricks) {
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

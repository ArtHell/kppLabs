package sample;

public class Bot {
    Player player;
    Ball ball;
    int time;
    public Bot(Player player, Ball ball) {
        this.player = player;
        this.ball = ball;
        time = 0;
    }
    public void Execute() {
        if(time < 1850){
            if (player.getTranslateX() + player.getWidth()/2 < ball.getTranslateX() + ball.getWidth()) {
                player.moveRight();
            }
            else {
                player.moveLeft();
            }
        }
        if(time > 1850) {
            if (player.getTranslateX() + player.getWidth()/2 < ball.getTranslateX()) {
                player.moveRight();
            }
            else {
                player.moveLeft();
            }
        }
        if (time == 4000) {
            time = 0;
        }
        time++;
    }
}

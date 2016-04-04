package sample;

public class Bot {
	final int timeOfMoving = 2000;
	Player player;
	Ball ball;
	int time;
	boolean on;

	public Bot(Player player, Ball ball) {
		this.player = player;
		this.ball = ball;
		time = 0;
		on = false;
	}

	public void Execute() {
		if (time < timeOfMoving) {
			if (player.getTranslateX() + player.getWidth() / 2
							< ball.getTranslateX() + ball.getWidth()) {
				player.moveRight();
			} else {
				player.moveLeft();
			}
		}
		if (time > timeOfMoving) {
			if (player.getTranslateX() + player.getWidth() / 2
							< ball.getTranslateX()) {
				player.moveRight();
			} else {
				player.moveLeft();
			}
		}
		if (time == timeOfMoving * 2) {
			time = 0;
		}
		time++;
	}

	public boolean isOn() {
		return on;
	}

	public void setOn() {
		on = true;
	}

	public void setOff() {
		on = false;
	}
}
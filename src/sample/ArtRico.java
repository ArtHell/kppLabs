package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;

public class ArtRico extends Application {
	final int sceneWidth = 1280;
	final int sceneHeight = 640;
	final int wallWidth = 10;
	final int strokeWidth = 2;
	final int topHeight = 30;
	final int bottomHeight = 10;
	final int playerHeight = 20;
	final int playerPosY = 570;
	final int playerWidthEasy = 280;
	final int playerWidthMedium = 200;
	final int playerWidthHard = 200;
	final int playerSpeedEasy = 10;
	final int playerSpeedMedium = 10;
	final int playerSpeedHard = 20;
	final int ballSpeedEasy = 5;
	final int ballSpeedMedium = 5;
	final int ballSpeedHard = 10;
	final int ballSize = 20;
	final int bricksN = 15;
	final int bricksM = 8;
	final int bricksHeight = 40;
	final int bricksWidth = 80;
	final int buttonWidth = 260;
	final int buttonHeight = 80;

	String exitButton;
	String pauseButton;
	String botButton;
	String turnLeftButton;
	String turnRightButton;

	Pane gameRoot;
	Pane menuRoot;
	Scene scene;
	ArrayList<SimpleObject> bricks;
	Player player;
	Ball ball;
	SimpleObject leftWall;
	SimpleObject rightWall;
	SimpleObject top;
	SimpleObject bottom;
	static int score;
	Label scoreLabel;
	Bot bot;
	static HashSet<String> currentlyActiveKeys;
	static HashSet<String> lastKey;
	boolean pause;
	AnimationTimer timer;


	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("ArtRik");
		showMenu(primaryStage);
	}

	private void startGame(Stage primaryStage, int mode) {
		scene = new Scene(createContent(mode));
		prepareActionHandlers();
		primaryStage.setScene(scene);
		primaryStage.show();

		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (ball.isGameWon()) {
					showMessage("You won! Your score is: ");
				}
				if (ball.isGameOver()) {
					showMessage("You lose! Your score is: ");
				}
				if (lastKey.contains("SPACE")) {
					if (pause == false) {
						pause = true;
					} else {
						pause = false;
					}
				}
				if (lastKey.contains("ESCAPE")) {
					gameRoot.getChildren().clear();
					timer.stop();
					showMenu(primaryStage);
				}
				if (lastKey.contains("B")) {
					if (bot.isOn()) {
						bot.setOff();
					} else {
						bot.setOn();
					}
				}
				lastKey.clear();
				if (!pause) {
					update();
				}
			}
		};
		timer.start();
	}

	private Parent createContent(int mode) {
		pause = true;
		gameRoot = new Pane();
		gameRoot.setPrefSize(1280, 640);
		Rectangle bg = new Rectangle(sceneWidth, sceneHeight, Color.GREY);
		leftWall = new SimpleObject(0, 0, wallWidth, sceneHeight,
						strokeWidth, Color.ORANGE, Color.BEIGE);
		rightWall = new SimpleObject(sceneWidth - wallWidth, 0, wallWidth,
						sceneWidth, strokeWidth, Color.ORANGE, Color.BEIGE);
		bottom = new SimpleObject(0, sceneHeight, sceneWidth, bottomHeight,
						strokeWidth, Color.ORANGE, Color.BEIGE);
		top = new SimpleObject(0, 0, sceneWidth, topHeight,
						strokeWidth, Color.ORANGE, Color.BEIGE);
		score = 0;
		scoreLabel = new Label("Score : " + score);
		scoreLabel.setLayoutX(40);
		scoreLabel.setLayoutY(0);
		scoreLabel.setFont(javafx.scene.text.Font.font(20));

		switch (mode) {
			case 0: {
				player = new Player(sceneWidth / 2 - playerWidthEasy / 2,
								playerPosY, playerWidthEasy, playerHeight,
								strokeWidth, Color.ORANGE, Color.BEIGE,
								wallWidth, sceneWidth - wallWidth, playerSpeedEasy);
				ball = new Ball(sceneWidth / 2 - ballSize / 2, playerPosY - ballSize,
								ballSize, ballSize, strokeWidth, Color.ORANGE,
								Color.BEIGE, wallWidth, sceneWidth - wallWidth,
								topHeight, sceneHeight, ballSpeedEasy);
				break;
			}
			case 1: {
				player = new Player(sceneWidth / 2 - playerWidthMedium / 2,
								playerPosY, playerWidthMedium,
								playerHeight, strokeWidth, Color.ORANGE, Color.BEIGE,
								wallWidth, sceneWidth - wallWidth, playerSpeedMedium);
				ball = new Ball(sceneWidth / 2 - ballSize / 2, playerPosY - ballSize,
								ballSize, ballSize, strokeWidth, Color.ORANGE,
								Color.BEIGE, wallWidth, sceneWidth - wallWidth,
								topHeight, sceneHeight, ballSpeedMedium);
				break;
			}
			case 2: {
				player = new Player(sceneWidth / 2 - playerWidthHard / 2,
								playerPosY, playerWidthHard, playerHeight,
								strokeWidth, Color.ORANGE, Color.BEIGE,
								wallWidth, sceneWidth - wallWidth, playerSpeedHard);
				ball = new Ball(sceneWidth / 2 - ballSize / 2,
								playerPosY - ballSize, ballSize,
								ballSize, strokeWidth, Color.ORANGE,
								Color.BEIGE, wallWidth, sceneWidth - wallWidth,
								topHeight, sceneHeight, ballSpeedHard);
				break;
			}

		}

		ball.setPlayer(player);
		ball.setFlagsInFalse();
		bot = new Bot(player, ball);
		gameRoot.getChildren().addAll(bg, player, ball, leftWall, rightWall,
						bottom, top, scoreLabel);
		bricks = new ArrayList<>();
		createBricks(bricksN, bricksM, bricksWidth, bricksHeight);
		return gameRoot;
	}

	private void createBricks(int colomn, int raw, int w, int h) {
		gameRoot.getChildren().removeAll(bricks);
		bricks.clear();
		for (int i = 0; i < raw; i++) {
			for (int j = 0; j < colomn; j++) {
				SimpleObject brick = new SimpleObject(j * w + 40, i * h + 75,
								w, h, strokeWidth, Color.ORANGE, Color.BEIGE);
				bricks.add(brick);

			}
		}
		gameRoot.getChildren().addAll(bricks);
		ball.setBricks(bricks);
	}

	private void restart() {
		gameRoot.getChildren().removeAll(bricks);
		createBricks(bricksN, bricksM, bricksWidth, bricksHeight);
		score = 0;
		player.setTranslateX(sceneWidth / 2 - player.getWidth() / 2 + strokeWidth / 2);
		player.setTranslateY(playerPosY);
		ball.setTranslateX(sceneWidth / 2 - ballSize / 2);
		ball.setTranslateY(playerPosY - ballSize);
		ball.gameOver = false;
		ball.setFlagsInFalse();
	}

	private void update() {
		if (bot.isOn()) {
			bot.Execute();
		} else if (currentlyActiveKeys.contains("LEFT")) {
			player.moveLeft();
		} else if (currentlyActiveKeys.contains("RIGHT")) {
			player.moveRight();
		}
		ball.move();
		scoreLabel.setText("Score : " + score);
	}

	private void prepareActionHandlers() {
		// use a set so duplicates are not possible
		currentlyActiveKeys = new HashSet<String>();
		lastKey = new HashSet<String>();
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				currentlyActiveKeys.add(event.getCode().toString());
				lastKey.add(event.getCode().toString());
			}
		});
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				currentlyActiveKeys.remove(event.getCode().toString());
			}
		});
	}

	private void showMessage(String msg) {
		pause = true;
		Button button = new Button(msg + score);
		button.setLayoutX(510);
		button.setLayoutY(240);
		button.setPrefSize(buttonWidth, buttonHeight);
		button.setOnAction(e -> {
			pause = false;
			button.setVisible(false);
			gameRoot.getChildren().remove(button);
		});
		ball.gameWon = false;
		ball.gameOver = false;
		restart();
		gameRoot.getChildren().add(button);
	}

	private void showMenu(Stage primaryStage) {
		menuRoot = new Pane();
		menuRoot.setPrefSize(sceneWidth, sceneHeight);
		Rectangle bg = new Rectangle(sceneWidth, sceneHeight, Color.GREY);
		Button[] button1 = new Button[4];

		for (int i = 0; i < 4; i++) {
			button1[i] = new Button();
			button1[i].setLayoutX(510);
			button1[i].setLayoutY(i * 100 + 140);
			button1[i].setPrefSize(buttonWidth, buttonHeight);
			menuRoot.getChildren().add(button1[i]);
		}

		button1[0].setText("NEW GAME");
		button1[1].setText("SETTINGS");
		button1[2].setText("ABOUT");
		button1[3].setText("EXIT");

		button1[0].setOnAction(e -> {
			menuRoot.getChildren().removeAll(button1);
			Button[] button2 = new Button[4];
			for (int i = 0; i < 4; i++) {
				button2[i] = new Button();
				button2[i].setLayoutX(510);
				button2[i].setLayoutY(i * 100 + 140);
				button2[i].setPrefSize(buttonWidth, buttonHeight);
				menuRoot.getChildren().add(button2[i]);
			}
			button2[0].setText("Easy");
			button2[1].setText("Medium");
			button2[2].setText("Hard");
			button2[3].setText("Back");
			button2[0].setOnAction(event -> {
				menuRoot.getChildren().clear();
				startGame(primaryStage, 0);
			});
			button2[1].setOnAction(event -> {
				menuRoot.getChildren().clear();
				startGame(primaryStage, 1);
			});
			button2[2].setOnAction(event -> {
				menuRoot.getChildren().clear();
				startGame(primaryStage, 2);
			});
			button2[3].setOnAction(event -> {
				menuRoot.getChildren().removeAll(button2);
				menuRoot.getChildren().addAll(button1);
			});
		});

		button1[3].setOnAction(event -> {
			System.exit(0);
		});

		scene = new Scene(menuRoot);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}

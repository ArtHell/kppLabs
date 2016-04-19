package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Game extends Application {
  char gameMode;
  Stage gameStage;
  Scene scene;
  Client client;

  public Game(char gameMode) {
    this.gameMode = gameMode;
    this.gameStage = new Stage();
    start(gameStage);
  }

  public void start(Stage gameStage) {
    client = new Client(gameMode, this);
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
    }
    scene = client.scene;
    gameStage.setScene(scene);
    gameStage.show();

  }

  public void exit() {
    gameStage.close();
  }
}

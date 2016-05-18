package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * main class of application
 */
public class ArtRico extends Application
    implements Constants {
  Pane menuRoot;
  Scene scene;
  char gameMode;
  Button[] mainMenuButtons;
  Button[] newGameMenuButtons;

  /**
   * @param primaryStage
   */
  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("ArtRico");
    showMenu(primaryStage);
  }

  /**
   * create buttons for main menu
   */
  void createMainMenuButtons() {
    mainMenuButtons = new Button[MENU_SIZE];

    for (int i = 0; i < MENU_SIZE; i++) {
      mainMenuButtons[i] = new Button();
      mainMenuButtons[i].setLayoutX(SCENE_WIDTH / 2 - BUTTON_WIDTH / 2);
      mainMenuButtons[i].setLayoutY(i * (BUTTON_HEIGHT + BUTTON_BORDER)
          + BUTTON_BORDER);
      mainMenuButtons[i].setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
      menuRoot.getChildren().add(mainMenuButtons[i]);
    }

    mainMenuButtons[0].setText("NEW GAME");
    mainMenuButtons[1].setText("SETTINGS");
    mainMenuButtons[2].setText("STATISTICS");
    mainMenuButtons[3].setText("REPLAY");
    mainMenuButtons[4].setText("EXIT");

    setMainMenuHandlers();
  }

  /**
   * set event handlers for main menu
   */
  void setMainMenuHandlers() {
    mainMenuButtons[0].setOnAction(e -> {
      menuRoot.getChildren().removeAll(mainMenuButtons);
      createNewGameMenuButtons();

    });

    mainMenuButtons[2].setOnAction(event -> {
      new Statistics();
    });

    mainMenuButtons[3].setOnAction(event -> {
      gameMode = REPLAY_MODE;
      startGame(gameMode);
    });

    mainMenuButtons[4].setOnAction(event -> {
      System.exit(0);
    });
  }

  /**
   * create buttons for new game menu
   */
  void createNewGameMenuButtons() {
    newGameMenuButtons = new Button[4];
    for (int i = 0; i < NEW_GAME_MENU_SIZE; i++) {
      newGameMenuButtons[i] = new Button();
      newGameMenuButtons[i].setLayoutX(SCENE_WIDTH / 2 - BUTTON_WIDTH / 2);
      newGameMenuButtons[i].setLayoutY(i * (BUTTON_HEIGHT + BUTTON_BORDER)
          + BUTTON_BORDER);
      newGameMenuButtons[i].setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
      menuRoot.getChildren().add(newGameMenuButtons[i]);
    }
    newGameMenuButtons[0].setText("Easy");
    newGameMenuButtons[1].setText("Medium");
    newGameMenuButtons[2].setText("Hard");
    newGameMenuButtons[3].setText("Back");
    setNewGameMenuHandlers();
  }

  /**
   * set event handlers for new game menu
   */
  void setNewGameMenuHandlers() {
    newGameMenuButtons[0].setOnAction(event -> {
      gameMode = EASY_MODE;
      startGame(gameMode);
    });
    newGameMenuButtons[1].setOnAction(event -> {
      gameMode = MEDIUM_MODE;
      startGame(gameMode);
    });
    newGameMenuButtons[2].setOnAction(event -> {
      gameMode = HARD_MODE;
      startGame(gameMode);
    });
    newGameMenuButtons[3].setOnAction(event -> {
      menuRoot.getChildren().removeAll(newGameMenuButtons);
      menuRoot.getChildren().addAll(mainMenuButtons);
    });
  }

  /**
   * show menu
   *
   * @param primaryStage
   */
  private void showMenu(Stage primaryStage) {
    menuRoot = new Pane();
    menuRoot.setPrefSize(SCENE_WIDTH, SCENE_HEIGHT);
    createMainMenuButtons();
    scene = new Scene(menuRoot);
    scene.getStylesheets().add(ArtRico.class.getResource(STYLE_FILE)
        .toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * start new game in another window
   *
   * @param gameMode
   */
  private void startGame(char gameMode) {
    new Game(gameMode);
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    launch(args);
  }
}

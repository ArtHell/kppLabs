package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

/**
 * generate new window and make client and server sides
 */
public class Game extends Application implements Constants {
  Pane menuRoot;
  char gameMode;
  Stage gameStage;
  Scene scene;
  Client client;
  String replayFile;
  int currentIter;
  String[] fileNames;
  ArrayList<ReplayInfo> replays;

  public Game(char gameMode) {
    this.gameMode = gameMode;
    this.gameStage = new Stage();
    start(gameStage);
  }

  public void start(Stage gameStage) {
    if (gameMode == REPLAY_MODE) {
      replays = new ArrayList<>();
      showReplayMenu();
    } else {
      startGame();
    }
  }

  public void startGame() {
    client = new Client(gameMode, this);
    try {
      Thread.sleep(START_DELAY);
    } catch (InterruptedException e) {
    }
    scene = client.scene;
    scene.getStylesheets().add(Game.class.getResource(STYLE_FILE)
        .toExternalForm());
    gameStage.setScene(scene);
    gameStage.setOnCloseRequest(e -> {
      if (client != null) {
        client.server.working = false;
        client.working = false;
      }
    });
    gameStage.show();
  }

  void makeReplayInfo() {
    replays.clear();
    for (int i = 0; i < fileNames.length; i++) {
      ReplayInfo replayInfo = new ReplayInfo(fileNames[i]);
      replays.add(replayInfo);
    }
  }

  private void showReplayMenu() {
    currentIter = 0;
    menuRoot = new Pane();
    menuRoot.setPrefSize(SCENE_WIDTH, SCENE_HEIGHT);
    File folder = new File(RESOURCE_FOLDER);
    fileNames = folder.list();
    if (fileNames == null) {
      return;
    }

    makeReplayInfo();

    GridPane gridPane = new GridPane();
    gridPane.setLayoutX(GRID_INDENTS);
    gridPane.setLayoutY(GRID_INDENTS);
    gridPane.setGridLinesVisible(true);
    menuRoot.getChildren().add(gridPane);
    addLabels(gridPane);
    scene = new Scene(menuRoot);
    scene.getStylesheets().add(Game.class.getResource("style.css")
        .toExternalForm());

    gridPane.setOnMouseClicked(e -> {
      int fileNumber = ((int) e.getY()) / GRID_LABEL_HEIGHT - 1;
      if (fileNumber != -1 && fileNumber < NOTES_ON_SCREEN) {
        menuRoot.getChildren().remove(gridPane);
        replayFile = RESOURCE_FOLDER
            + replays.get(fileNumber + currentIter).getReplayName();
        startGame();
      } else if (fileNumber == -1) {
        ReplayInfo.sortBy = (int) e.getX() * 5 / (SCENE_WIDTH -
            GRID_INDENTS * 2);
        replays.sort(ReplayInfo::compareTo);
        addLabels(gridPane);
      }
    });
    scene.setOnKeyPressed(event -> {
      switch (event.getCode().toString()) {
        case "ESCAPE": {
          exit();
          break;
        }
        case "T": {
          sortTest();
          break;
        }
        case "UP": {
          if (currentIter != 0) {
            currentIter -= NOTES_ON_SCREEN;
            addLabels(gridPane);
          }
          break;
        }
        case "DOWN": {
          if (currentIter < replays.size()) {
            currentIter += NOTES_ON_SCREEN;
            addLabels(gridPane);
          }
          break;
        }
      }
    });
    gameStage.setScene(scene);
    gameStage.show();
  }

  public String getReplayFile() {
    return replayFile;
  }

  public void addTopLabels(GridPane gridPane) {
    for (int i = 0; i < 5; i++) {
      Label topLabel = new Label();
      topLabel.setText(TOP_LABEL_CONTENT[i]);
      topLabel.setPrefWidth((SCENE_WIDTH - GRID_INDENTS * 2) / 5);
      topLabel.setPrefHeight(GRID_LABEL_HEIGHT);
      gridPane.add(topLabel, i, 0);
    }
  }

  public void addNameLabel(GridPane gridPane, int i) {
    Label replayNameLabel = new Label();
    replayNameLabel.setText(replays.get(i).getReplayName());
    replayNameLabel.setPrefWidth((SCENE_WIDTH - GRID_INDENTS * 2) / 5);
    replayNameLabel.setPrefHeight(GRID_LABEL_HEIGHT);
    gridPane.add(replayNameLabel, 0, i + 1);
  }

  public void addModeLabel(GridPane gridPane, int i) {
    Label replayModeLabel = new Label();
    replayModeLabel.setText(replays.get(i).getReplayMode());
    replayModeLabel.setPrefWidth((SCENE_WIDTH - GRID_INDENTS * 2) / 5);
    replayModeLabel.setPrefHeight(GRID_LABEL_HEIGHT);
    gridPane.add(replayModeLabel, 1, i + 1);
  }

  public void addTimeLabel(GridPane gridPane, int i) {
    Label gameTimeLabel = new Label();
    gameTimeLabel.setText(Integer.toString(replays.get(i).getGameTime()));
    gameTimeLabel.setPrefWidth((SCENE_WIDTH - GRID_INDENTS * 2) / 5);
    gameTimeLabel.setPrefHeight(GRID_LABEL_HEIGHT);
    gridPane.add(gameTimeLabel, 2, i + 1);
  }

  public void addLeftMovingTimeLabel(GridPane gridPane, int i) {
    Label leftMovingTimeLabel = new Label();
    leftMovingTimeLabel.setText(Integer.toString(replays.get(i)
        .getLeftMovingTime()));
    leftMovingTimeLabel.setPrefWidth((SCENE_WIDTH - GRID_INDENTS * 2) / 5);
    leftMovingTimeLabel.setPrefHeight(GRID_LABEL_HEIGHT);
    gridPane.add(leftMovingTimeLabel, 3, i + 1);
  }

  public void addRightMovingTimeLabel(GridPane gridPane, int i) {
    Label rightMovingTimeLabel = new Label();
    rightMovingTimeLabel.setText(Integer.toString(replays.get(i)
        .getRightMovingTime()));
    rightMovingTimeLabel.setPrefWidth((SCENE_WIDTH - GRID_INDENTS * 2) / 5);
    rightMovingTimeLabel.setPrefHeight(GRID_LABEL_HEIGHT);
    gridPane.add(rightMovingTimeLabel, 4, i + 1);
  }


  public void addLabels(GridPane gridPane) {
    gridPane.getChildren().clear();
    addTopLabels(gridPane);
    for (int i = currentIter; i < currentIter + NOTES_ON_SCREEN && i < replays.size(); i++) {
      addNameLabel(gridPane, i);
      addModeLabel(gridPane, i);
      addTimeLabel(gridPane, i);
      addLeftMovingTimeLabel(gridPane, i);
      addRightMovingTimeLabel(gridPane, i);
    }
  }

  void sortTest() {
    long traceTime;
    Algorithm algorithm = new Algorithm();
    JavaSort javaSort = new JavaSort();
    int[] array = new int[fileNames.length];
    for (int i = 0; i < fileNames.length; i++) {
      array[i] = replays.get(i).getGameTime();
    }
    System.out.println("Time of sorting:");
    traceTime = algorithm.testSort(array);
    System.out.println("Scala: " + traceTime);
    traceTime = javaSort.testSort(array);
    System.out.println("Java:  " + traceTime);
  }

  public void exit() {
    if (client != null) {
      client.server.working = false;
      client.working = false;
    }
    gameStage.close();
  }
}

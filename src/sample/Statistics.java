package sample;/**
 * Created by Artsiom Amialchuk on 21.04.2016.
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

/**
 * new window contain statistics of all played games
 */
public class Statistics extends Application implements Constants {
  Stage stage;
  Pane root;
  ArrayList<ReplayInfo> replays;
  int allTimePlayed;
  int allTimeMovedRight;
  int allTimeMovedLeft;
  int allTimeNotMoved;

  public Statistics() {
    replays = new ArrayList<>();
    allTimePlayed = 0;
    allTimeMovedRight = 0;
    allTimeMovedLeft = 0;
    allTimeNotMoved = 0;
    this.stage = new Stage();
    start(stage);
  }

  void initStatistics() {

    File folder = new File(RESOURCE_FOLDER);
    String[] fileNames = folder.list();

    if (fileNames == null) {
      return;
    }
    for (int i = 0; i < fileNames.length; i++) {
      ReplayInfo replayInfo = new ReplayInfo(fileNames[i]);
      replays.add(replayInfo);
    }

    for (int i = 0; i < replays.size(); i++) {
      allTimePlayed += replays.get(i).getGameTime();
      allTimeMovedRight += replays.get(i).getRightMovingTime();
      allTimeMovedLeft += replays.get(i).getLeftMovingTime();
    }

    allTimeNotMoved = allTimePlayed - allTimeMovedLeft - allTimeMovedRight;
  }

  @Override
  public void start(Stage stage) {
    initStatistics();
    Button[] buttons = new Button[4];
    for (int i = 0; i < 4; i++) {
      buttons[i] = new Button();
      buttons[i].setTranslateY(STATISTICS_LABEL_INDENTS + i
          * (STATISTICS_LABEL_INDENTS + STATISTICS_LABEL_HEIGHT));
      buttons[i].setPrefWidth(STATISTICS_LABEL_WIDTH);
      buttons[i].setPrefHeight(STATISTICS_LABEL_HEIGHT);
      buttons[i].setTranslateX(SCENE_WIDTH / 2 - STATISTICS_LABEL_WIDTH / 2);
    }
    buttons[0].setText("Time played: " + allTimePlayed);
    buttons[1].setText("Time moved right: " + allTimeMovedRight);
    buttons[2].setText("Time moved left: " + allTimeMovedLeft);
    buttons[3].setText("Time not moved: " + allTimeNotMoved);
    root = new Pane();
    root.setPrefSize(SCENE_WIDTH, SCENE_HEIGHT);
    root.getChildren().addAll(buttons);
    Scene scene = new Scene(root);
    stage.setScene(scene);
    scene.getStylesheets().add(Game.class.getResource("style.css")
        .toExternalForm());
    stage.show();
    scene.setOnKeyPressed(event -> {
      if (event.getCode().toString() == "ESCAPE") {
        stage.close();
      }
    });
  }
}

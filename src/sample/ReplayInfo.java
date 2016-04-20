package sample;

public class ReplayInfo implements Constants, Comparable<ReplayInfo> {
  String replayName;
  String replay;
  String replayMode;
  int rightMovingTime;
  int leftMovingTime;
  int gameTime;
  static int sortBy;

  ReplayInfo(String replayName) {
    this.replayName = replayName;
    replay = Serializer.loadReplay(RESOURSE_FOLDER + replayName);
    switch (replay.toCharArray()[0]) {
      case 'e':
        replayMode = "EASY";
        break;
      case 'm':
        replayMode = "MEDIUM";
        break;
      case 'h':
        replayMode = "HARD";
        break;
    }
    rightMovingTime = 0;
    leftMovingTime = 0;
    gameTime = replay.length() - 1;
    for (int i = 1; i <= gameTime; i++) {
      char currentSymbol = replay.toCharArray()[i];
      switch (currentSymbol) {
        case 'l':
          leftMovingTime++;
          break;
        case 'r':
          rightMovingTime++;
          break;
      }
    }
  }

  @Override
  public int compareTo(ReplayInfo o) {
    int result = 0;
    switch (sortBy) {
      case 0:
        result = Integer.compare(new Integer(replayName), new Integer(o.replayName));
        break;
      case 1:
        result = replayMode.compareTo(o.replayMode);
        break;
      case 2:
        result = Integer.compare(gameTime, o.gameTime);
        break;
      case 3:
        result = Integer.compare(leftMovingTime, o.leftMovingTime);
        break;
      case 4:
        result = Integer.compare(rightMovingTime, o.rightMovingTime);
        break;
    }
    return result;
  }

  public int getRightMovingTime() {
    return rightMovingTime;
  }

  public String getReplayMode() {
    return replayMode;
  }

  public int getLeftMovingTime() {
    return leftMovingTime;
  }

  public int getGameTime() {
    return gameTime;
  }

  public String getReplayName() {
    return replayName;
  }

}

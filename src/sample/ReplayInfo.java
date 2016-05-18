package sample;

/**
 * class store information about replays
 */
public class ReplayInfo implements Constants, Comparable<ReplayInfo> {
  String replayName;
  String replay;
  String replayMode;
  int rightMovingTime;
  int leftMovingTime;
  int gameTime;
  static int sortBy;

  /**
   * constructor
   *
   * @param replayName
   */
  ReplayInfo(String replayName) {
    this.replayName = replayName;
    replay = Serializer.loadReplay(RESOURCE_FOLDER + replayName);
    switch (replay.toCharArray()[0]) {
      case EASY_MODE:
        replayMode = "EASY";
        break;
      case MEDIUM_MODE:
        replayMode = "MEDIUM";
        break;
      case HARD_MODE:
        replayMode = "HARD";
        break;
    }
    rightMovingTime = 0;
    leftMovingTime = 0;
    gameTime = replay.length() - 1;
    replay.toCharArray();
    Algorithm algorithm = new Algorithm();
    leftMovingTime = algorithm.countLeftMove(replay.toCharArray());
    rightMovingTime = algorithm.countRightMove(replay.toCharArray());
  }

  /**
   * realize comparable interface
   *
   * @param o
   * @return
   */
  @Override
  public int compareTo(ReplayInfo o) {
    int result = 0;
    switch (sortBy) {
      case 0:
        result = Integer.compare(new Integer(replayName),
            new Integer(o.replayName));
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

  /**
   * getter for right moving time
   *
   * @return
   */
  public int getRightMovingTime() {
    return rightMovingTime;
  }

  /**
   * getter for replay mode
   *
   * @return
   */
  public String getReplayMode() {
    return replayMode;
  }

  /**
   * getter for left moving time
   *
   * @return
   */
  public int getLeftMovingTime() {
    return leftMovingTime;
  }

  /**
   * getter for game time
   *
   * @return
   */
  public int getGameTime() {
    return gameTime;
  }

  /**
   * getter for replay name
   *
   * @return
   */
  public String getReplayName() {
    return replayName;
  }

}

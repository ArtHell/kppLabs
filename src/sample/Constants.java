package sample;

/**
 * storage of constants
 */
interface Constants {
  int SCENE_WIDTH = 640;
  int SCENE_HEIGHT = 640;
  int WALL_WIDTH = 10;
  int STROKE_WIDTH = 2;
  int TOP_HEIGHT = 30;
  int BOTTOM_HEIGHT = 10;
  int PLAYER_HEIGHT = 20;
  int GRID_INDENTS = 20;
  int GRID_LABEL_HEIGHT = 30;
  int PLAYER_POS_Y = 570;
  char EASY_MODE = 'e';
  char MEDIUM_MODE = 'm';
  char HARD_MODE = 'h';
  char REPLAY_MODE = 'r';
  char LEFT_MOVE = 'l';
  char RIGHT_MOVE = 'r';
  char NOT_MOVE = 'n';
  int NOTES_ON_SCREEN = 19;
  int PLAYER_WIDTH_EASY = 280;
  int PLAYER_WIDTH_MEDIUM = 200;
  int PLAYER_WIDTH_HARD = 200;
  int PLAYER_SPEED_EASY = 10;
  int PLAYER_SPEED_MEDIUM = 10;
  int PLAYER_SPEED_HARD = 20;
  int BALL_SPEED_EASY = 5;
  int BALL_SPEED_MEDIUM = 5;
  int BALL_SPEED_HARD = 10;
  int BALL_SIZE = 20;
  int BRICKS_IN_RAW = 7;
  int BRICKS_IN_COLOMN = 8;
  int BRICKS_HEIGHT = 40;
  int BRICKS_WIDTH = 80;
  int BUTTON_WIDTH = 260;
  int BUTTON_HEIGHT = 80;
  int BUTTON_BORDER = 20;
  int MENU_SIZE = 5;
  int NEW_GAME_MENU_SIZE = 4;
  int STATISTICS_LABEL_INDENTS = 20;
  int STATISTICS_LABEL_HEIGHT = 130;
  int STATISTICS_LABEL_WIDTH = 320;
  int WAIT_TIME = 100000;
  int START_DELAY = 500;
  String[] TOP_LABEL_CONTENT = {"REPLAY", "MODE", "PLAYED", "MOVED LEFT"
      , "MOVED RIGHT"};
  String RESOURCE_FOLDER = "Resource\\";
  String STYLE_FILE = "style.css";
}

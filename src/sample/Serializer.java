package sample;

import java.io.*;
import java.util.Date;

/**
 * save and load replays from Resource folder
 */
public class Serializer implements Constants {
  /**
   * save replay with unique name
   *
   * @param string
   */
  public static void saveReplay(String string) {
    File folder = new File(RESOURCE_FOLDER);
    String[] fileNames = folder.list();
    int max = 0;
    if (fileNames != null) {
      try {
        for (int i = 0; i < fileNames.length; i++) {
          Integer temp = new Integer(fileNames[i]);
          if (temp > max) {
            max = temp;
          }
        }
        max++;
      } catch (NumberFormatException e) {
      }
    } else {
      max = 1;
    }

    String fileName = RESOURCE_FOLDER + Integer.toString(max);
    File file = new File(fileName);
    try (FileWriter writer = new FileWriter(file, false)) {
      writer.write(string);
      writer.flush();
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
  }

  /**
   * load replay by name
   *
   * @param fileName
   * @return
   */
  public static String loadReplay(String fileName) {
    File file = new File(fileName);
    String replay = "";
    try (FileReader reader = new FileReader(file)) {
      char[] buffer = new char[(int) file.length()];
      reader.read(buffer);
      replay = new String(buffer);
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    } finally {
      return replay;
    }
  }
}

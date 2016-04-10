package sample;

import java.io.*;

public class Serializer {

  public static void saveReplay(String fileName, String string) {
    File file = new File(fileName);
    try(FileWriter writer = new FileWriter(file, false)) {
      writer.write(string);
      writer.flush();
    }
    catch(IOException ex) {
      System.out.println(ex.getMessage());
    }
  }

  public static String loadReplay(String fileName) {
    File file = new File(fileName);
    String replay = "";
    try(FileReader reader = new FileReader(file))
    {
      char[] buffer = new char[(int)file.length()];
      reader.read(buffer);
      replay = new String(buffer);
    }
    catch(IOException ex){
      System.out.println(ex.getMessage());
    }
    finally {
      return replay;
    }
  }
}

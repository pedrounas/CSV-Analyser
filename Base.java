import java.util.*;
import java.io.*;

class Base extends Helper {
  public static void FileParser(Scanner stdin) {
    String file,line;
    BufferedReader name = null;
    int rows = 0;
    System.out.println("Insira o ficheiro a ler (incluindo a extensao): ");
    file = stdin.next();
    try {
      name = new BufferedReader(new FileReader(file));
      while((line = name.readLine()) != null) {
        String[] words = line.split(",");
        for (int i = 0; i < words.length; i++) {
          dataArray[rows][i] = words[i];
        }
        rows++;
        nColumns = words.length;
      }
      nRows = rows;
    }catch(FileNotFoundException e) {
      e.printStackTrace();
    } catch(IOException e) {
      e.printStackTrace();
    } finally {
      if (name != null) {
        try {
          name.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
}
  public static void main(String[] args) {
    Scanner stdin = new Scanner(System.in);
    boolean visted;
    FileParser(stdin);
    for (int i = 0; i < nRows; i++) {
      for (int j = 0; j < nColumns; j++) {
        System.out.print(dataArray[i][j] + " ");
      }
      System.out.println();
    }
    System.out.println(initialEntropy()) ;
    columnGain();
  }
}

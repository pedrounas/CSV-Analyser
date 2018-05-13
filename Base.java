import java.util.*;
import java.io.*;

class Base extends Helper {
  static boolean[] visitedAttributes;
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
public static void imprimirSpace(int size){
  for (int i=0; i<size*2;i++){
    System.out.print(" ");
  }
}

public static int checkMax() {
  int target = -1;
  double max = 0.0;
  Arrays.fill(gain,0.0);
  columnGain(gain);
  for(int i = 1; i < gain.length; i++) {
      System.out.print(gain[i] + "   ");
      if (gain[i] > max && !visitedAttributes[i]) {
        max = gain[i];
        target = i;
      }

  }
  System.out.println();
  if(target ==-1){
    return -1;
  }
  visitedAttributes[target] = true;
  return target;
}

public static LinkedList<String> ID3(int target, String[] examples, String[][] atributes, LinkedList<String> tree, int iteractions){
  iteractions++;
  int nProb;
  tree.addLast(atributes[0][target]);
  imprimirSpace(iteractions);
    System.out.println("-" + atributes[0][target] + "-");
    for (int i = 0; i < diffClasses.length;i++) {
      nProb = 0;
      for (int j = 0; j < examples.length;j++) {
        if (partialProbability(target,i,examples[j])==1.0)
          nProb++;
        }
    if (examples.length == nProb) {
      for (int t = 0; t < examples.length; t++) {
        imprimirSpace(iteractions);
        System.out.println(examples[t] + ": " + diffClasses[i] + " " + nProb);
      }
      return tree;
    }
  }
  boolean finishedExamples[] = new boolean[examples.length];
  Arrays.fill(finishedExamples, false);
  for(int i=0;i<diffClasses.length;i++){
    for(int j=0; j<examples.length;j++){
      if(finishedExamples[j]==false){
        if(partialProbability(target,i,examples[j])==1){
          int counter= calcCounter(target,examples[j]);
          tree.addLast(diffClasses[i]);
          finishedExamples[j]=true;
          imprimirSpace(iteractions);
          System.out.println(examples[j] + ": " + diffClasses[i] + " " + counter);
          checkIfFinished(target,examples[j]);
        }
      }
    }
  }
  for(int i=0;i<examples.length;i++){
    if(finishedExamples[i]==false){
      int nmax=checkMax();
      if(nmax==-1){
        return tree;
      }
      imprimirSpace(iteractions);
      System.out.println(examples[i]);
      ID3(nmax, differentValues(nmax), atributes, tree, iteractions);
    }
  }
  return tree;
}
  public static void main(String[] args) {
    Scanner stdin = new Scanner(System.in);
    FileParser(stdin);
    //ISTO É PARA TESTAR O PARSER
    /*for (int i = 0; i < nRows; i++) {
      for (int j = 0; j < nColumns; j++) {
        System.out.print(dataArray[i][j] + " ");
      }
      System.out.println();
    }*/

    //System.out.println(initialEntropy());
    gain = new double[nColumns-1];
    visitedAttributes = new boolean[nColumns-1];
    finished = new boolean[nRows];
    Arrays.fill(visitedAttributes,false);
    Arrays.fill(finished,false);
    diffClasses = differentValues(nColumns-1);
    gain = columnGain(gain);
    //ISTO É PARA TESTAR O COLUMN GAIN
      for (int i = 1; i < nColumns - 1; i++) {
      System.out.println(gain[i]);
    }
    int target = checkMax();
    if (target!= -1)
      visitedAttributes[target] = true;
    LinkedList<String> tree = new LinkedList<String>();
    //System.out.println(max + " " + target);
    ID3(target,differentValues(target),dataArray,tree,-1);
  }
}

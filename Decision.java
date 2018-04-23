import java.util.*;
import java.io.*;

class Decision extends Calculate {

  public static boolean[] attributesVisited;

  /* Ler dados do arquivo csv */
  public static void lerInput(Scanner in) {
    // Variaveis
    String csvFile, csvFileAux;
    BufferedReader br = null;
    String line = "";
    //Definir separador
    String cvsSplitBy = ",";
    //Ler arquivo csv
    System.out.println("Insira o nome do seu arquivo .csv, exemplo: 'restaurant'");
    csvFileAux = in.next();
    csvFile = csvFileAux + ".csv";
    //csvFile = "restaurant.csv";

    try {
      int i=0;
      br = new BufferedReader(new FileReader(csvFile));
      while((line = br.readLine()) != null) {
        String[] dados = line.split(cvsSplitBy);
        //Imprime dados;
        for(int j=0; j<dados.length; j++)
          table[i][j] = dados[j];
        i++;
        limitColumn = dados.length;
      }
      limitLinha = i;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

  }

  /* Restablecer valores de visitar[] */
  public static boolean[] refreshVisitar(int column, String value, boolean[] visitar) {
    int i;

    //Novos valores
    for(i=1; i<limitLinha; i++){
      if(table[i][column].equals(value))
        visitar[i] = false;
    }

    return visitar;
  }


  /* Imprimir espaços */
  public static void imprimirSpace(int space) {
    for(int i=0; i<space; i++){
      System.out.print(" ");
    }
  }


  //Algortimo de decisao
  public static LinkedList<String> ID3 (LinkedList<String> tree, String[] examples, int targetAttribute, String[][] attributes, boolean[] visitar, int space) {
    //Variaveis
    int countProb;
    int i,j,k;
    int counter;

    tree.addLast(attributes[0][targetAttribute]);
    attributesVisited[targetAttribute] = true;
    imprimirSpace(space);
    System.out.println("<" + attributes[0][targetAttribute] + ">"); // Imprimir atributo


    //Se todos os examples[] sao da mesma classValue
    for(i=0; i<classValue.length; i++) {
      countProb = 0;
      for(j=0; j<examples.length; j++) {
        if(calculateProbParcial(targetAttribute, examples[j], i, visitar) == 1){
          countProb++;
        }
      }
      if(countProb == examples.length){
        for(k=0; k<countProb; k++){
          imprimirSpace(space+4);
          System.out.println(examples[k] + ": " + classValue[i] + " (" + countProb + ")");
        }
        return tree;
      }
    }

   /*************************************************************************************************************************/

    //Se todos os examples nao sao da mesma classe
    boolean examplesVisited[] = new boolean[examples.length]; //Marcar todos os exemplos como nao lidos
    for(k=0; k<examples.length; k++){
        examplesVisited[k] = false;
    }

    // Calcular os valores que pertencem a uma clase
    for(i=0; i<classValue.length; i++) {
      for(j=0; j<examples.length; j++) {
        if(examplesVisited[j] == false){
          if(calculateProbParcial(targetAttribute, examples[j], i, visitar) == 1){
            counter = calculateCounter(targetAttribute, examples[j], i,  visitar);
            tree.addLast(classValue[i]);
            imprimirSpace(space+4);
            System.out.println(examples[j] + ": " + classValue[i] + " (" + counter + ")");
            examplesVisited[j] = true;
            visitar = refreshVisitar(targetAttribute, examples[j], visitar);
          }
        }
      }
    }

    //Calcular os valores que nao pertencem a uma clase
    for(i=0; i<examples.length; i++){
      if (examplesVisited[i] == false){
        //Variaveis
        double maiorGanho =-1;
        int posRoot=-1;

        calculateGanho(visitar);

        //Calcular qual vai ser o proximo atributo a calcular
        for(j=1; j<limitColumn-1; j++) {
            if(attributesVisited[j] != true && ganhoTable[j]>maiorGanho){
              maiorGanho = ganhoTable[j];
              posRoot = j;
            }
          }
        if(maiorGanho == -1)
          return tree;
        imprimirSpace(space+4);
        System.out.println(examples[i] + ": ");
        ID3(tree, lookValues(posRoot, visitar), posRoot, attributes, visitar, space+8);

      }
    }

    return tree;
  }


  /* MAIN */
  public static void main (String[] args) {
    //Variaveis
    Scanner in = new Scanner(System.in);
    int posRoot =-1, i;
    boolean visitar[];
    double maiorGanho =-1;

    lerInput(in);
    System.out.println();
    visitar = new boolean[limitLinha];
    for(i=0; i<limitLinha; i++)
      visitar[i] = true;

    classValue = lookValues(limitColumn-1, visitar);

    calculateGanho(visitar);

    //Escolher root Inicial
    for(i=1; i<limitColumn-1; i++) {
      if(ganhoTable[i]>maiorGanho){
        maiorGanho = ganhoTable[i];
        posRoot = i;
      }
    }


    attributesVisited = new boolean[limitColumn];
    for (i=0; i<limitColumn; i++){
      attributesVisited[i] = false;
    }

    LinkedList <String> tree = new LinkedList<String>();
    ID3(tree, lookValues(posRoot, visitar), posRoot, table, visitar, 0);

    System.out.println();
    //System.out.println(tree);

  }
}

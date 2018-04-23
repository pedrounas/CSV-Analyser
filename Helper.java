import java.util.*;
import java.io.*;

class Helper {
  public static int nColumns;
  public static int nRows;
  public static String[][] dataArray = new String[500][500];

  public static double initialEntropy() { // Should be done
    double entropy,plus,minus;
    int nYes = 0, nNo = 0;

    for (int i = 1; i < nRows; i++) {
      if (dataArray[i][nColumns-1].equals("No") || dataArray[i][nColumns-1].equals("no"))
        nNo++;
    }

    for (int i = 1; i < nRows; i++) {
      if (dataArray[i][nColumns-1].equals("Yes") || dataArray[i][nColumns-1].equals("yes"))
        nYes++;
    }

    minus = (double)nNo/(double)(nRows-1);
    plus = (double)nYes/(double)(nRows-1);

    entropy = -(plus*(Math.log(plus)/Math.log(2))) - (minus*(Math.log(minus)/Math.log(2)));
    if (Double.isNaN(entropy)) return 0;
    else return entropy;
  }

  public static String[] differentValues(int column) {
  String[] values = new String[nRows-1];
  int nTypes = 0;
  int total = 0;
  int test = 0;
  Arrays.fill(values,"null");
  for (int i = 1; i < nRows; i++) {
    for (int j = 0; j < total+1; j++) {
      if (dataArray[i][column].equals(values[j]))
        test = 1;
    }
    if (test == 0) {
      values[total] = dataArray[i][column];
      total++;
    }
    test = 0;
  }
  String[] cleanValues = new String[total];
  cleanValues = clearString(values,total);
  return cleanValues;
}

  public static String[] clearString(String[] values, int n) {
    String[] aux = new String[n];
    for (int i = 0; i < n; i++) {
      aux[i] = values[i];
    }
    return aux;
  }

}

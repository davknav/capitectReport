package davidlum.capitectReport.converter;

import java.util.Comparator;

public class AssetClassComparator implements Comparator<String> {
  @Override
  public int compare(String ac1, String ac2) {
    // Stocks go first, then Bonds, then Cash, then sort reverse-alphabetically so "US" comes before "Non-US"
    int retval = toNumber(ac1) - toNumber(ac2);
    if (retval == 0) {
      retval = ac2.compareTo(ac1);
    }
    return retval;
  }

  private static int toNumber(String ac) {
    if (ac.endsWith("Stock")) {
      return 0;
    }
    if (ac.endsWith("Bond")) {
      return 1;
    }
    if (ac.contains("Cash")) {
      return 2;
    }
    return 3;
  }
}

/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package ukkonen_scs_finder;

import alphabet.LanguageParameterFactory;
import java.util.List;
import alphabet.LanguageParameter;

/**
 *
 * @author : Markus Walder
 * @since : 26.12.2020, Sa.
 */
public class SCSTester {

  int totalSize = 0;
  List<String> testKeys;
  LanguageParameter params;

  private SCSTester(int numTestCases, LanguageParameter params, int minStringLength,
      int maxStringLength) {
    this.params = params;
    testKeys = RandomStringGenerator
                   .generateRandomStrings(params, numTestCases, minStringLength, maxStringLength);
    testKeys.forEach(s -> totalSize += s.length());
  }

  public boolean evaluateSuperstring(String result) {
    System.out.println("Tried to find SCS");
    System.out.println("Compression factor is: " + (double) totalSize / (double) result.length());
    System.out
        .println("Points are: " + (double) (totalSize - result.length()) / (double) totalSize);
    System.out.println();
    for (String key : testKeys) {
      if (!result.contains(key)) {
        System.out.println("Test failed for string - " + result);
        System.out.println("Test failed with key - " + key);
        return false;
      }
    }
    return true;
  }

  public static boolean testRandomlyGeneratedSCS(int numTestCases, LanguageParameter params,
      int minStringLength, int maxStringLength) {

    SCSTester tester = new SCSTester(numTestCases, params, minStringLength, maxStringLength);
    UkkonensSCSFinder builder = UkkonensSCSFinder.createFromKeys(tester.testKeys);

    return tester.evaluateSuperstring(builder.getSCS());
  }

  //Uses Default Language Parameter
  public static boolean testRandomlyGeneratedSCS(int numTestCases, int minStringLength,
      int maxStringLength) {

    SCSTester tester = new SCSTester(numTestCases,
        LanguageParameterFactory.defaultParameter, minStringLength, maxStringLength);
    UkkonensSCSFinder builder = UkkonensSCSFinder.createFromKeys(tester.testKeys);

    return tester.evaluateSuperstring(builder.getSCS());
  }

  public static void main(String[] args) {
    testRandomlyGeneratedSCS(500, 2, 5);
  }
}

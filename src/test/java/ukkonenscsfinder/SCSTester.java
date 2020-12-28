/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package ukkonenscsfinder;

import alphabet.LanguageParameterFactory;
import java.util.List;
import alphabet.LanguageParameter;

/**
 * SCSTester is a utility class to help in testing which randomly generates a lot of
 * strings using {@link RandomStringGenerator}. It then uses {@link UkkonenSCSFinder} to generate
 * an approximate superstring for the set of keywords and tests whether the superstring is valid
 * (contains all keywords).
 *
 * @author Markus Walder
 * @since 26.12.2020, Sa.
 */
class SCSTester {

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

  boolean evaluateSuperstring(String result) {
    System.out.println("Tried to find SCS");
    System.out.println("Compression factor is: " + (double) totalSize / (double) result.length());
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

  static boolean testRandomlyGeneratedSCS(int numTestCases, LanguageParameter params,
      int minStringLength, int maxStringLength) {

    SCSTester tester = new SCSTester(numTestCases, params, minStringLength, maxStringLength);
    UkkonenSCSFinder builder = UkkonenSCSFinder.createFromKeys(tester.testKeys);

    return tester.evaluateSuperstring(builder.getSCS());
  }

  //Uses Default Language Parameter
  static boolean testRandomlyGeneratedSCS(int numTestCases, int minStringLength,
      int maxStringLength) {

    SCSTester tester = new SCSTester(numTestCases,
        LanguageParameterFactory.defaultParameter, minStringLength, maxStringLength);
    UkkonenSCSFinder builder = UkkonenSCSFinder.createFromKeys(tester.testKeys);

    return tester.evaluateSuperstring(builder.getSCS());
  }
}

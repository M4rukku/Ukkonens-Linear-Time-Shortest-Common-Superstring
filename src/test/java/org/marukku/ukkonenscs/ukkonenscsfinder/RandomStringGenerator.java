/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package org.marukku.ukkonenscs.ukkonenscsfinder;

import org.marukku.ukkonenscs.alphabet.LanguageParameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Statically generates lists of random strings given an org.marukku.ukkonenscs.alphabet for use in testing..
 *
 * @author Markus Walder
 * @since 26.12.2020, Sa.
 */
class RandomStringGenerator {

  static List<String> generateRandomStrings(LanguageParameter params,
      int numStringsToGenerate, int minStringLength, int maxStringLength) {

    List<Character> alphabet = params.getAlphabet();
    //Build Strings of Size 2 - 5
    List<String> testSet = new ArrayList<>(numStringsToGenerate);

    StringBuilder builder = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < numStringsToGenerate; i++) {
      builder.delete(0, builder.length());

      int size = random.nextInt(maxStringLength - minStringLength) + minStringLength;
      for (int j = 0; j < size; j++) {
        builder.append(alphabet.get(random.nextInt(alphabet.size())));
      }
      testSet.add(builder.toString());
    }
    return testSet;
  }
}

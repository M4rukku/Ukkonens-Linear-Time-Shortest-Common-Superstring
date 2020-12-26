/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package alphabet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author : Markus Walder
 * @since : 26.12.2020, Sa.
 */
public class LanguageParameterFactory {
  // Static Factory Methods
  public static LanguageParameters createLanguageParametersFromAlphabet(List<Character> alphabet) {
    //First get the five characters and build a mapper from the chars to 0-4
    HashMap<Character, Integer> alphabetMapper = new HashMap<>();
    int count = 0;
    for (Character c : alphabet) {
      alphabetMapper.put(c, count);
      count++;
    }
    return new LanguageParameters(alphabetMapper.size(), alphabetMapper::get,
        new ArrayList<>(alphabetMapper.keySet()));
  }

  public static LanguageParameters createLanguageParametersFromParams
      (int alphabetSize, Function<Character, Integer> mapper, List<Character> alphabet) {
    return new LanguageParameters(alphabetSize, mapper, alphabet);
  }

  public static LanguageParameters createLanguageParametersFromKeys(List<String> keys) {
    //First get the five characters and build a mapper from the chars to 0-4
    HashMap<Character, Integer> alphabetMapper = new HashMap<>();
    int count = 0;
    for (String name : keys) {
      for (char c : name.toCharArray()) {
        if (!alphabetMapper.containsKey(c)) {
          alphabetMapper.put(c, count);
          count++;
        }
      }
    }
    return new LanguageParameters(alphabetMapper.size(), alphabetMapper::get,
        new ArrayList<>(alphabetMapper.keySet()));
  }

  // Static default Languages
  private static final List<Character> lowerCaseStandardAlphabet =
      "abcdefghijklmnopqrstuvwxyz".chars().mapToObj(c -> (char) c).collect(Collectors.toList());

  public static LanguageParameters defaultParameter =
      new LanguageParameters(26, myChar -> myChar - 'a', lowerCaseStandardAlphabet);

}

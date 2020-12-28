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
 * Factory to create LanguageParameters.
 *
 * @author : Markus Walder
 * @since : 26.12.2020, Sa.
 */
public class LanguageParameterFactory {

  /**
   * Static factory method that creates a language based on the alphabet alone. It will generate a
   * mapping function using a HashMap.
   *
   * @param alphabet List of Characters that define our language
   * @return the created LanguageParameter
   */
  // Static Factory Methods
  public static LanguageParameter createLanguageParametersFromAlphabet(List<Character> alphabet) {
    //First get the five characters and build a mapper from the chars to 0-4
    HashMap<Character, Integer> alphabetMapper = new HashMap<>();
    int count = 0;
    for (Character c : alphabet) {
      alphabetMapper.put(c, count);
      count++;
    }
    return new LanguageParameter(alphabetMapper.size(), alphabetMapper::get,
        new ArrayList<>(alphabetMapper.keySet()));
  }

  /**
   * Static Factory method that creates a language based on the alphabet, and a mapper function.
   * This is preferred to createLanguageParametersFromAlphabet whenever there is a simple mapping
   * function (i.e. character - 'a') that is fast to calculate. This is especially important for
   * small alphabets.
   *
   * @param mapper   a mapping function that maps all characters in alphabet to an integer in
   *                 range(0, alphabetSize-1)
   * @param alphabet a List of Characters that defines our alphabet
   * @return the created LanguageParameter
   */
  public static LanguageParameter createLanguageParametersFromParams(
      Function<Character, Integer> mapper, List<Character> alphabet) {
    return new LanguageParameter(alphabet.size(), mapper, alphabet);
  }

  /**
   * Static factory method that creates a {@link LanguageParameter} based on the sample of words we
   * give it. It extracts the characters of the alphabet by iterating over all characters used in
   * wordSample. Attention is needed when using this method because it requires the wordSample to
   * contain all characters in the alphabet. In general, it is discouraged to use this method for a
   * more general case.
   *
   * @param wordSample List of Strings, a set of sample words of our language
   * @return the LanguageParameter created
   */
  public static LanguageParameter createLanguageParametersFromKeys(List<String> wordSample) {
    //First get the five characters and build a mapper from the chars to 0-4
    HashMap<Character, Integer> alphabetMapper = new HashMap<>();
    int count = 0;
    for (String name : wordSample) {
      for (char c : name.toCharArray()) {
        if (!alphabetMapper.containsKey(c)) {
          alphabetMapper.put(c, count);
          count++;
        }
      }
    }
    return new LanguageParameter(alphabetMapper.size(), alphabetMapper::get,
        new ArrayList<>(alphabetMapper.keySet()));
  }

  // Static default Languages
  private static final List<Character> lowerCaseStandardAlphabet =
      "abcdefghijklmnopqrstuvwxyz".chars().mapToObj(c -> (char) c).collect(Collectors.toList());

  /**
   * The default language defining the lower case English alphabet "abc...xyz".
   */
  public static LanguageParameter defaultParameter =
      new LanguageParameter(26, myChar -> myChar - 'a', lowerCaseStandardAlphabet);

}

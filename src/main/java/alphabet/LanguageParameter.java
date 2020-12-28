/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package alphabet;

import java.util.List;
import java.util.function.Function;

/*
 * Stores global language parameters (alphabet characters, mapping function) needed in both
 * AhoCorasick and Ukkonen's Algorithm.
 *
 * @author Markus Walder
 * @since 26.12.2020, Sa.
 */
public class LanguageParameter {

  private int alphabetSize;
  private Function<Character, Integer> mapper;
  private List<Character> alphabet;

  LanguageParameter(int alphabetSize, Function<Character, Integer> mapper,
      List<Character> alphabet) {
    this.alphabetSize = alphabetSize;
    this.mapper = mapper;
    this.alphabet = alphabet;
  }

  /**
   * map maps an input char to an integer. The mapping function must be injective and map all chars
   * in alphabet to the integer range (0, alphabetSize-1). We need it to facilitate array access in
   * AC's goto function.
   *
   * @param input Character which we want to map to an int
   * @return the Integer that maps to the Character in our predefined mapping
   */
  //Accessors
  public int map(char input) {
    return mapper.apply(input);
  }

  /**
   * getAlphabet returns all characters in our alphabet.
   *
   * @return a list containing all characters of our alphabet
   */
  public List<Character> getAlphabet() {
    return alphabet;
  }

  public int getAlphabetSize() {
    return alphabetSize;
  }
}

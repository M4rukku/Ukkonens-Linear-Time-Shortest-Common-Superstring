package alphabet;

import java.util.*;
import java.util.function.Function;

/*
 * Stores global parameters needed in both ac_string_matcher.AhoCorasick and Ukkonen's Algorithm
 * Can be sent to them via dependency injection
 */
public class LanguageParameters {

  private int alphabetSize;
  private Function<Character, Integer> mapper;
  private List<Character> alphabet;

  LanguageParameters(int alphabetSize, Function<Character, Integer> mapper,
      List<Character> alphabet) {
    this.alphabetSize = alphabetSize;
    this.mapper = mapper;
    this.alphabet = alphabet;
  }

  //Accessors
  public int map(char input) {
    return mapper.apply(input);
  }

  public List<Character> getAlphabet(){
    return alphabet;
  }

  public int getSize(){return alphabetSize;}
}

/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package actrie;

import alphabet.LanguageParameter;
import java.util.ArrayList;
import java.util.List;
import trienodes.ACTrieNode;

/**
 * StringDictionaryMatcher implements the basic use case of the {@link AhoCorasickTrie}. It finds
 * all matches of the keywords used in its construction in the text body.
 *
 * Example Usage: <br>
 *   StringDictionaryMatcher matcher = StringDictionaryMatcher.createFromParameters(
 *         LanguageParameterFactory.defaultParameter, dictionary); <br>
 *   List[Match] matches = matcher.matchText(text); <br>
 *
 * @author Markus Walder
 * @since 26.12.2020, Sa.
 */
public class KeywordTextMatcher {

  private AhoCorasickTrie<ACTrieNode> stringMatcher;

  private KeywordTextMatcher(LanguageParameter parameters, List<String> keywords) {
    stringMatcher = AhoCorasickTrieFactory
                        .createAhoCorasickTrieFromParams(keywords, parameters);
  }

  /**
   * Creates a new {@link KeywordTextMatcher} from a {@link LanguageParameter} and the keywords
   * we want to match.
   *
   * @param parameters the {@link LanguageParameter} we are using
   * @param keywords   the keywords we want to match with
   * @return the new {@link KeywordTextMatcher}
   */
  public static KeywordTextMatcher createFromParameters(LanguageParameter parameters,
      List<String> keywords) {
    return new KeywordTextMatcher(parameters, keywords);
  }

  /**
   * Returns all {@link Match}es of the keywords we found in the text.
   *
   * @param text the text body we want to search
   * @return a list of {@link Match}es found
   */
  public List<Match> matchText(String text) {
    ACTrieNode currentNode = stringMatcher.rootNode;
    List<Match> matches = new ArrayList<>();

    char[] charArray = text.toCharArray();
    for (int i = 0; i < charArray.length; i++) {
      char c = charArray[i];
      currentNode = currentNode.getDFATransition(c);

      for (String output : currentNode.output) {
        matches.add(new Match(output, i + 1 - output.length(), i));
      }
    }

    return matches;
  }
}

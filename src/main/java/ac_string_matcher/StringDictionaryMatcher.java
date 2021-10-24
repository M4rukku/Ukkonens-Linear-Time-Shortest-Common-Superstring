/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package ac_string_matcher;

import alphabet.LanguageParameters;
import java.util.ArrayList;
import java.util.List;
import trie_nodes.ACTrieNode;
import trie_nodes.ACTrieNodeFactory;
import trie_nodes.AbstractACNodeFactory;

public class StringDictionaryMatcher<T extends ACTrieNode> {

  private AhoCorasickTrie<T> stringMatcher;

  private StringDictionaryMatcher(LanguageParameters parameters, List<String> dictionary,
      AbstractACNodeFactory<T> factory) {
    stringMatcher = AhoCorasickTrieFactory
                        .createAhoCorasickTrieFromParamsWithNodeFactory(dictionary, parameters,
                            factory);

  }

  public static StringDictionaryMatcher<ACTrieNode> createFromParameters(
      LanguageParameters parameters,
      List<String> dictionary) {
    return new StringDictionaryMatcher<>(parameters, dictionary, new ACTrieNodeFactory());
  }

  public static <E extends ACTrieNode> StringDictionaryMatcher<E> createFromParametersWithCustomNodeFactory(
      LanguageParameters parameters, List<String> dictionary, AbstractACNodeFactory<E> factory) {
    return new StringDictionaryMatcher<>(parameters, dictionary, factory);
  }

  public List<Match> matchText(String text) {
    ACTrieNode currentNode = stringMatcher.rootNode;
    List<Match> returnList = new ArrayList<>();

    char[] charArray = text.toCharArray();
    for (int i = 0; i < charArray.length; i++) {
      char c = charArray[i];
      currentNode = currentNode.getDFATransition(c);

      for (String output : currentNode.output) {
        returnList.add(new Match(output, i + 1 - output.length(), i));
      }
    }

    return returnList;
  }
}

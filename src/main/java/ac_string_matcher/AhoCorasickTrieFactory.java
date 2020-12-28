/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package ac_string_matcher;

import alphabet.LanguageParameterFactory;
import alphabet.LanguageParameter;
import java.util.List;
import trie_nodes.ACTrieNode;
import trie_nodes.ACTrieNodeFactory;
import trie_nodes.AbstractACNodeFactory;

/**
 *
 * @author : Markus Walder
 * @since : 26.12.2020, Sa.
 */
public class AhoCorasickTrieFactory {

  //Default ACTNodes
  public static AhoCorasickTrie<ACTrieNode> createAhoCorasickTrieFromParams(List<String> keys,
      LanguageParameter parameters) {
    return new AhoCorasickTrie<>(keys, parameters, new ACTrieNodeFactory());
  }

  //Custom Class Nodes
  public static <T extends ACTrieNode> AhoCorasickTrie<T> createAhoCorasickTrieFromParamsWithNodeFactory(
      List<String> keys,
      LanguageParameter parameters, AbstractACNodeFactory<T> factory) {
    return new AhoCorasickTrie<>(keys, parameters, factory);
  }

  public static <T extends ACTrieNode> AhoCorasickTrie<T> getAhoCorasickTrieFromKeysWithNodeFactory(
      List<String> keys, AbstractACNodeFactory<T> factory) {

    return new AhoCorasickTrie<>(keys,
        LanguageParameterFactory.createLanguageParametersFromKeys(keys),
        factory);
  }
}

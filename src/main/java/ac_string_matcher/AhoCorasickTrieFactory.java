/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package ac_string_matcher;

import alphabet.LanguageParameter;
import java.util.List;
import trie_nodes.ACTrieNode;
import trie_nodes.ACTrieNodeFactory;
import trie_nodes.AbstractACNodeFactory;

/**
 * A Factory to create AhoCorasickTries.
 *
 * @author Markus Walder
 * @since 26.12.2020, Sa.
 */
public class AhoCorasickTrieFactory {

  /**
   * Creates the AhoCorasickTrie using a default ({@link ACTrieNode}) to create the nodes.
   *
   * @param keys       List of Strings - the keys we want to use to build the {@link
   *                   AhoCorasickTrie}
   * @param parameters a {@link LanguageParameter} defining the language used for the keys
   * @return the new AhoCorasickTrie
   */
  public static AhoCorasickTrie<ACTrieNode> createAhoCorasickTrieFromParams(List<String> keys,
      LanguageParameter parameters) {
    return new AhoCorasickTrie<>(keys, parameters, new ACTrieNodeFactory());
  }


  /**
   * Creates the AhoCorasickTrie using a custom node type. The custom node type will be defined
   * using an {@link AbstractACNodeFactory}.
   *
   * @param keys       List of Strings - the keys we want to use to build the {@link
   *                   AhoCorasickTrie}
   * @param parameters a {@link LanguageParameter} defining the language used for the keys
   * @param factory    An implementation of {@link AbstractACNodeFactory} defining the creation of
   *                   new nodes.
   * @param <T>        The node type created by the factory.
   * @return the new AhoCorasickTrie
   */
  public static <T extends ACTrieNode> AhoCorasickTrie<T>
      createAhoCorasickTrieFromParamsWithNodeFactory(
        List<String> keys,
        LanguageParameter parameters, AbstractACNodeFactory<T> factory) {
    return new AhoCorasickTrie<>(keys, parameters, factory);
  }
}

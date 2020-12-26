/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package trie_nodes;

import alphabet.LanguageParameters;

/**
 * The {@link AbstractACNodeFactory} defines the interface NodeFactories need to implement in order
 * to create suffix trie nodes that may be used in the AhoCorasickStringMatcher DFA. We need an
 * abstract Factory because we might want to add functionality to our standard nodes (i.e. in
 * Ukkonens Algorithm)
 * <p>
 * T represents the NodeType that extends ACNodeFactories.AhoCorasickTrieNode - used to add
 * functionality to the string matcher
 *
 * @author : Markus Walder
 * @since : 26.12.2020, Sa.
 */
public interface AbstractACNodeFactory<T extends ACTrieNode> {

  /**
   * A simple factory method that takes a set of default parameters and then returns a new Suffix
   * Trie Node based on it.
   *
   * @param parameters  The {@link LanguageParameters} that define our underlying Alphabet
   * @param isEndOfWord Indicates whether this Suffix Trie Node represents the end of a word
   * @param parentChar  Character - that tells us the character of our parent
   * @return Returns a new Trie Node
   */
  T createFromDefaultValues(
      LanguageParameters parameters, boolean isEndOfWord, Character parentChar);
}

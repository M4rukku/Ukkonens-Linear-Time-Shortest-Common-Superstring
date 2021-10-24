/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package org.marukku.ukkonenscs.trienodes;

import org.marukku.ukkonenscs.alphabet.LanguageParameter;

/**
 * The {@link AbstractACNodeFactory} defines the interface NodeFactories need to implement in order
 * to create suffix trie nodes that may be used in the AhoCorasickStringMatcher DFA. We need an
 * abstract Factory because we might want to add functionality to our standard nodes (i.e. in
 * Ukkonens Algorithm).
 *
 * <p>T represents the NodeType that extends ACNodeFactories.AhoCorasickTrieNode - ensures that
 * typing works as expected.
 *
 * @author Markus Walder
 * @since 26.12.2020, Sa.
 */
public interface AbstractACNodeFactory<T extends ACTrieNode> {

  /**
   * A simple factory method that takes a set of default parameters and then returns a new Suffix
   * Trie Node based on it.
   *
   * @param parameters  The {@link LanguageParameter} that define our underlying Alphabet
   * @param isEndOfWord Boolean - Indicates whether this Suffix Trie Node represents the end of a
   *                    word
   * @param parentChar  Character - tells us which character leads to this node from the parent
   * @return Returns a new Trie Node
   */
  T createFromDefaultValues(
      LanguageParameter parameters, boolean isEndOfWord, Character parentChar);
}

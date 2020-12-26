/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package trie_nodes;

import alphabet.LanguageParameters;
import trie_nodes.AbstractACNodeFactory;
import trie_nodes.UkkonenTrieNode;

/**
 * Creates {@link UkkonenTrieNode}s, which support the implementation details needed for Ukkonens
 * Linear Time SCS Finder.
 */
public class UkkonenTrieNodeFactory implements AbstractACNodeFactory<UkkonenTrieNode> {

  /**
   * A simple factory method that takes a set of default parameters and then returns a new Suffix
   * Trie Node supporting Ukkonens' Algorithm based on it.
   *
   * @param parameters  The {@link LanguageParameters} that define our underlying Alphabet
   * @param isEndOfWord Indicates whether this Suffix Trie Node represents the end of a word
   * @param parentChar  Character - that tells us the character of our parent
   * @return Returns a new {@link UkkonenTrieNode} from the given Parameters
   */
  @Override
  public UkkonenTrieNode createFromDefaultValues(
      LanguageParameters parameters, boolean isEndOfWord, Character parentChar) {
    return new UkkonenTrieNode(parameters, isEndOfWord, parentChar);
  }
}

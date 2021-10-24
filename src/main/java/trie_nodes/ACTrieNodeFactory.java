package trie_nodes;

import alphabet.LanguageParameters;
import trie_nodes.ACTrieNode;
import trie_nodes.AbstractACNodeFactory;

public class ACTrieNodeFactory implements AbstractACNodeFactory<ACTrieNode> {

  /**
   * A simple factory method that takes a set of default parameters and then returns an {@link
   * ACTrieNode} to the caller. This is the most basic (default Factory).
   *
   * @param parameters  The {@link LanguageParameters} that define our underlying Alphabet
   * @param isEndOfWord Indicates whether this Suffix Trie Node represents the end of a word
   * @param parentChar  Character - that tells us the character of our parent
   * @return Returns a new basic {@link ACTrieNode}
   */
  @Override
  public ACTrieNode createFromDefaultValues(
      LanguageParameters parameters, boolean isEndOfWord, Character parentChar) {
    return new ACTrieNode(parameters, isEndOfWord, parentChar);
  }
}
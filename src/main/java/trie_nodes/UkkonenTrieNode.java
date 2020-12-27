/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package trie_nodes;

import java.util.*;
import alphabet.LanguageParameters;

/**
 * @author : Markus Walder
 * @since : 26.12.2020, Sa.
 */
public class UkkonenTrieNode extends ACTrieNode {

  public int depth;
  public List<Integer> supportedKeys;
  public UkkonenTrieNode bfsSuccessor;

  /**
   * pCandidate contains all indices i, such that this node is on the Failure path starting from the
   * node representing the end of string i. We still need to check for each node in pCandidate
   * whether the hamilton path already contains an overlap (xi, xj) for any j. If it doesn't, it
   * means that this node can form an overlap with string i in pCandidate.
   */
  public LinkedList<Integer> pCandidate = new LinkedList<>();

  protected UkkonenTrieNode(LanguageParameters parameters, boolean isEndOfWord, char pch) {
    super(parameters, isEndOfWord, pch);
    supportedKeys = new ArrayList<>();
  }

  public UkkonenTrieNode getNextNode(char input) {
    return (UkkonenTrieNode) successorNodes.get(parameters.map(input));
  }

  public void setNextNode(char input, UkkonenTrieNode node) {
    successorNodes.set(parameters.map(input), node);
  }

  public Iterator<UkkonenTrieNode> iterator() {
    return successorNodes.stream().map(node -> (UkkonenTrieNode) node).iterator();
  }

  public UkkonenTrieNode getFail() {
    return (UkkonenTrieNode) super.getFail();
  }
}

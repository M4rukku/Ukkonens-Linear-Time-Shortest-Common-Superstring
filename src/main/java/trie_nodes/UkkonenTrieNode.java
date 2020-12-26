/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package trie_nodes;

import java.util.*;
import alphabet.LanguageParameters;

public class UkkonenTrieNode extends ACTrieNode {

  public int depth;
  public List<Integer> supportedKeys;
  public UkkonenTrieNode bfsSuccessor;

  //sOnFailureFromFiAndNotHContainsNoOverlap between i and any j in supportedKeys
  public Deque<Integer> pCandidate = new ArrayDeque<>();

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

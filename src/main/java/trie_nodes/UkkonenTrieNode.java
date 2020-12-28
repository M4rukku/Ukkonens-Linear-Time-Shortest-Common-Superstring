/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package trie_nodes;

import alphabet.LanguageParameters;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * UkkonenTrieNode is a subclass of ACTrieNode which adds functionality (depth, supporters,
 * bfsSuccessor) to the superclass in order to enable the use of Ukkonens Algorithm.
 *
 * @author Markus Walder
 * @since 26.12.2020, Sa.
 */
public class UkkonenTrieNode extends ACTrieNode {

  public int depth;
  public List<Integer> supportedKeys;
  public UkkonenTrieNode bfsSuccessor;

  /**
   * pCandidate represents all strings with a suffix overlapping with the prefix of this node's
   * substring with which we can still merge.
   *
   * <p>Therefore, pCandidate contains all indices i, such that this node is on the Failure path
   * starting from the node representing the end of string i. We still need to check for each node
   * in pCandidate whether the hamilton path already contains an overlap (xi, xj) for any j during
   * construction. If it doesn't, it means that this node can form an overlap with string i.
   */
  public LinkedList<Integer> pCandidate = new LinkedList<>();

  protected UkkonenTrieNode(LanguageParameters parameters, boolean isEndOfWord, char pch) {
    super(parameters, isEndOfWord, pch);
    supportedKeys = new ArrayList<>();
  }

  /**
   * Returns the next node in the Aho-Corasick String Matcher Trie, note that this is different to
   * getDFATransition. It will return null, if there is no node reachable using input. This function
   * does not handle any transitions to the failure node.
   *
   * @param input Character used to determine next node
   * @return UkkonenTrieNode the node reachable using input or null if there is no reachable node
   */
  public UkkonenTrieNode getNextNode(char input) {
    return (UkkonenTrieNode) successorNodes.get(parameters.map(input));
  }

  public void setNextNode(char input, UkkonenTrieNode node) {
    successorNodes.set(parameters.map(input), node);
  }

  /**
   * Returns an iterator over all successor nodes casted to UkkonenTrieNodes.
   *
   * @return Iterator of UkkonenTrieNodes over all successors
   */
  public Iterator<UkkonenTrieNode> iterator() {
    return successorNodes.stream().map(node -> (UkkonenTrieNode) node).iterator();
  }

  /**
   * Gets the first node on the failure path starting from this node. This node is the node
   * representing the longest prefix string matching the suffix of the string represented by our
   * current node.
   *
   * @return UkkonenTrieNode the fail node
   */
  public UkkonenTrieNode getFail() {
    return (UkkonenTrieNode) super.getFail();
  }
}

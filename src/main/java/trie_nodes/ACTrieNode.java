/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package trie_nodes;

import alphabet.LanguageParameter;
import java.util.ArrayList;
import java.util.List;


/**
 * The basic Aho-Corasick supporting Trie Node - It keeps track of all transitions, failure paths
 * and can be used to create a DFA.
 *
 * <p>Note on the implementation: The goto function (successorNodes) maps characters to integers to
 * ACTrieNodes. We do this to make use of fast array access in small alphabets. It scales badly for
 * large alphabets because we need to keep a list the size of the alphabet with each node. It would
 * be more efficient to switch to an implementation based on HashMap[Character, Integer] in such
 * cases.
 *
 * @author Markus Walder
 * @since 26.12.2020, Sa.
 */
public class ACTrieNode {

  public LanguageParameter parameters;
  protected List<ACTrieNode> successorNodes; //Null means we need to check Fail
  protected ACTrieNode fail;

  public boolean isEndOfWord;
  public char parentChar;

  public List<String> output = new ArrayList<>();


  protected ACTrieNode(LanguageParameter parameters, boolean isEndOfWord, char pch) {
    int alphabetSize = parameters.getAlphabetSize();
    this.parameters = parameters;

    this.successorNodes = new ArrayList<>(alphabetSize);
    this.dfaTransitions = new ArrayList<>(alphabetSize);

    for (int i = 0; i < alphabetSize; i++) {
      successorNodes.add(null);
      dfaTransitions.add(null);
    }

    this.isEndOfWord = isEndOfWord;
    parentChar = pch;
  }

  //TRIE FUNCTIONS

  /**
   * Returns the next node in the Aho-Corasick String Matcher Trie, note that this is different to
   * getDFATransition. It will return null, if there is no node reachable using input. This function
   * does not handle any transitions to the failure node.
   *
   * @param input Character used to determine next node
   * @return ACTrieNode the node reachable using input or null if there is no reachable node
   */
  public ACTrieNode getNextNode(char input) {
    return successorNodes.get(parameters.map(input));
  }

  public void setNextNode(char input, ACTrieNode node) {
    successorNodes.set(parameters.map(input), node);
  }

  /**
   * Gets the first node on the failure path starting from this node. This node is the node
   * representing the longest prefix string matching the suffix of the string represented by our
   * current node.
   *
   * @return ACTrieNode the fail node
   */
  public ACTrieNode getFail() {
    return fail;
  }

  public void setFail(ACTrieNode fail) {
    this.fail = fail;
  }


  /**
   * Utility method that returns all (even null) successors in the AC-Trie.
   *
   * @return List[ACTrieNode] a list of all successor tree nodes.
   */
  public List<ACTrieNode> getSuccessorNodes() {
    return successorNodes;
  }

  //DFS FUNCTIONS
  private List<ACTrieNode> dfaTransitions;

  /**
   * Returns the next node in the constructed Aho-Corasick DFA, note that this is different to
   * getNextNode which returns the next node in the Trie. The DFA is more efficient at matching
   * strings because it combines the transition and failure function.
   *
   * @param input Character used to determine next node
   * @return ACTrieNode the node reachable using input or null if there is no reachable node
   */
  public ACTrieNode getDFATransition(char input) {
    return dfaTransitions.get(parameters.map(input));
  }

  public void setDFATransition(char input, ACTrieNode node) {
    dfaTransitions.set(parameters.map(input), node);
  }

  /**
   * Returns all reachable nodes of the DFA node.
   *
   * @return List[ACTrieNode] the nodes reachable from this node
   */
  public List<ACTrieNode> getAllPossibleDFATransitions() {
    return dfaTransitions;
  }

  //Utility method that says whether node is a leaf in the ac_string_matcher.AhoCorasick Trie
  // different from end of word!

  protected boolean leafComputed = false;
  protected boolean leaf = false;

  /**
   * Utility method that returns whether the node is a leaf in the Aho-Corasick Trie.
   *
   * @return Boolean is leaf in AC Graph
   */
  public boolean isLeafInAhoCorasickGraph() {
    if (leafComputed) {
      return leaf;
    } else {
      leafComputed = true;
      for (ACTrieNode nextState : successorNodes) {
        if (nextState != null) {
          leaf = false;
          return false;
        }
      }
      leaf = true;
      return true;
    }
  }
}
/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package actrie;

import alphabet.LanguageParameter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import trienodes.ACTrieNode;
import trienodes.AbstractACNodeFactory;


/**
 * Represents and builds an AhoCorasickTrie. This class takes a set of keywords, a {@link
 * LanguageParameter} and a NodeFactory and then builds the Trie + the DFA. <br>It is built to make
 * extension easy: We can inject different node factories and then build algorithms on the exposed
 * list of trie nodes.
 *
 * <p>The implementation is based on the original paper: https://dl.acm.org/doi/10.1145/360825.360855
 *
 * @param <nodeType> nodeType is a subclass of ACTrieNode. It ensures typing works as expected.
 * @author Markus Walder
 * @since 26.12.2020, Sa.
 */
public class AhoCorasickTrie<nodeType extends ACTrieNode> {

  private List<String> keys;
  private LanguageParameter parameters;
  private AbstractACNodeFactory<nodeType> nodeConstructorFactory;

  public List<nodeType> trieNodes = new ArrayList<>();
  public nodeType rootNode;


  AhoCorasickTrie(List<String> keys, LanguageParameter parameters,
      AbstractACNodeFactory<nodeType> nodeConstructorFactory) {
    this.keys = keys;
    this.parameters = parameters;
    this.nodeConstructorFactory = nodeConstructorFactory;

    //Test whether constructor fits!
    this.rootNode = nodeConstructorFactory
                        .createFromDefaultValues(parameters, false, '$');
    trieNodes.add(rootNode);

    createTrie();
  }


  private void createTrie() {
    defineSuccessorFunction(keys);
    calculateFailureFunction();
    buildDFA();
  }

  /**
   * Takes the keys and creates the basic Trie based on them. It defines the successor function and
   * basic trie node structure needed for the AC algorithm.
   *
   * @param keys a List of String containing the keys we want to match with later
   */
  private void defineSuccessorFunction(List<String> keys) {
    for (String key : keys) {
      ACTrieNode current = rootNode;

      int charIndex = 0;
      char[] keyChars = key.toCharArray();

      while (charIndex < keyChars.length
                 && (current.getNextNode(keyChars[charIndex]) != null)) {

        current = current.getNextNode(keyChars[charIndex]);
        charIndex++;
      }

      for (int ind = charIndex; ind < keyChars.length; ind++) {

        char currentChar = keyChars[ind];
        boolean isLeaf = (ind == (keyChars.length - 1));

        nodeType newNode = nodeConstructorFactory
                               .createFromDefaultValues(parameters, isLeaf, currentChar);

        if (isLeaf) {
          newNode.output.add(key);
        }

        current.setNextNode(currentChar, newNode);
        current = newNode;
        trieNodes.add(newNode);
      }
    }

    // Set all unused paths to go back to the root node
    for (int go = 0; go < rootNode.getSuccessorNodes().size(); go++) {
      if (rootNode.getSuccessorNodes().get(go) == null) {
        rootNode.getSuccessorNodes().set(go, rootNode);
      }
    }
  }

  /**
   * Calculates the failure function for each node. (See Paper)
   */
  private void calculateFailureFunction() {

    rootNode.setFail(rootNode);
    Deque<ACTrieNode> bfsQueue = new ArrayDeque<>();

    for (ACTrieNode child : rootNode.getSuccessorNodes()) {
      if (child != rootNode) {
        bfsQueue.add(child);
        child.setFail(rootNode);
      }
    }

    while (!bfsQueue.isEmpty()) {
      ACTrieNode curParentState = bfsQueue.poll();

      for (ACTrieNode childState : curParentState.getSuccessorNodes()) {
        if (childState == null) {
          continue;
        }

        bfsQueue.add(childState);
        ACTrieNode failureState = curParentState.getFail();
        char path = childState.parentChar;

        //Terminates because root will never fail
        while (failureState.getNextNode(path) == null) {
          failureState = failureState.getFail();
        }

        childState.setFail(failureState.getNextNode(path));
        childState.output.addAll(childState.getFail().output);
      }
    }

  }

  /**
   * Reduces the connections to failure nodes to a new transition function. (See Paper)
   */
  private void buildDFA() {
    Deque<ACTrieNode> bfsQueue = new ArrayDeque<>();

    for (char c : parameters.getAlphabet()) {
      rootNode.setDFATransition(c, rootNode.getNextNode(c));
      if (rootNode.getNextNode(c) != rootNode) {
        bfsQueue.add(rootNode.getNextNode(c));
      }
    }

    while (!bfsQueue.isEmpty()) {
      ACTrieNode curNode = bfsQueue.poll();
      for (char c : parameters.getAlphabet()) {
        if (curNode.getNextNode(c) != null) {
          bfsQueue.add(curNode.getNextNode(c));
          curNode.setDFATransition(c, curNode.getNextNode(c));
        } else {
          curNode.setDFATransition(c, curNode.getFail().getDFATransition(c));
        }
      }
    }
  }
}

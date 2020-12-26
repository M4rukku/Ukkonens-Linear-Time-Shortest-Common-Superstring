package trie_nodes;

import alphabet.LanguageParameters;
import java.util.ArrayList;
import java.util.List;


/**
 * The basic Aho-Corasick supporting Trie Node - It keeps track of all transitions, failure paths
 * and can be used to create the DFA.
 */
public class ACTrieNode {

  protected LanguageParameters parameters;
  protected List<ACTrieNode> successorNodes; //Null Represents Fail
  protected ACTrieNode fail;

  public boolean isEndOfWord = false;
  public char parentChar;

  public List<String> output = new ArrayList<>();


  protected ACTrieNode(LanguageParameters parameters, boolean isEndOfWord, char pch) {
    int alphabetSize = parameters.getSize();
    this.parameters = parameters;

    this.successorNodes = new ArrayList<>(alphabetSize);
    this.transitions = new ArrayList<>(alphabetSize);

    for (int i = 0; i < alphabetSize; i++) {
      successorNodes.add(null);
      transitions.add(null);
    }

    this.isEndOfWord = isEndOfWord;
    parentChar = pch;
  }

  //TRIE STUFF

  public ACTrieNode getNextNode(char input) {
    return successorNodes.get(parameters.map(input));
  }

  public void setNextNode(char input, ACTrieNode node) {
    successorNodes.set(parameters.map(input), node);
  }

  public ACTrieNode getFail() {
    return fail;
  }

  public void setFail(ACTrieNode fail) {
    this.fail = fail;
  }

  //DFS STUFF

  private final List<ACTrieNode> transitions;

  public ACTrieNode getTransitions(char input) {
    return transitions.get(parameters.map(input));
  }

  public void setTransitions(char input, ACTrieNode node) {
    transitions.set(parameters.map(input), node);
  }

  //Utility method that says whether node is a leaf in the ac_string_matcher.AhoCorasick Trie
  // different from end of word!

  protected boolean leafComputed = false;
  protected boolean leaf = false;

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

  public List<ACTrieNode> getSuccessorNodes() {
    return successorNodes;
  }

  public void setSuccessorNodes(List<ACTrieNode> successorNodes) {
    this.successorNodes = successorNodes;
  }
}
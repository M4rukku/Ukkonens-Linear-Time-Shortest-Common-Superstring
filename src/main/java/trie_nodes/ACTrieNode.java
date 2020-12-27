/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package trie_nodes;

import alphabet.LanguageParameters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * The basic Aho-Corasick supporting Trie Node - It keeps track of all transitions, failure paths
 * and can be used to create the DFA.
 *
 * @author : Markus Walder
 * @since : 26.12.2020, Sa.
 */
public class ACTrieNode {

  public LanguageParameters parameters;
  protected List<ACTrieNode> successorNodes; //Null Represents Fail
  protected ACTrieNode fail;

  public boolean isEndOfWord = false;
  public char parentChar;

  public List<String> output = new ArrayList<>();


  protected ACTrieNode(LanguageParameters parameters, boolean isEndOfWord, char pch) {
    int alphabetSize = parameters.getSize();
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

  private List<ACTrieNode> dfaTransitions;

  public ACTrieNode getDFATransition(char input) {
    return dfaTransitions.get(parameters.map(input));
  }

  public void setDFATransition(char input, ACTrieNode node) {
    dfaTransitions.set(parameters.map(input), node);
  }

  public List<ACTrieNode> getAllPossibleDFATransitions() {
    return dfaTransitions;
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
}
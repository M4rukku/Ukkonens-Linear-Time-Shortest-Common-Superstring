package ac_string_matcher;

import trie_nodes.ACTrieNode;
import java.util.*;
import alphabet.LanguageParameters;
import trie_nodes.AbstractACNodeFactory;

/*
 * Implementation of the AhoCorasick Algorithm
 * Takes a set of strings and builds a Trie from the given data
 * It exposes - The Trie Nodes, The Root for further processing with Ukkonen's Algorithm and a simple pattern matcher
 * You can also pass in a node that takes - language.LanguageParameters,
 */
public class AhoCorasickTrie<NodeType extends ACTrieNode> {

  private List<String> keys;
  private LanguageParameters parameters;
  private AbstractACNodeFactory<NodeType> nodeConstructorFactory;

  public List<NodeType> trieNodes = new ArrayList<>();
  public NodeType rootNode;


  AhoCorasickTrie(List<String> keys, LanguageParameters parameters,
      AbstractACNodeFactory<NodeType> nodeConstructorFactory) {
    this.keys = keys;
    this.parameters = parameters;
    this.nodeConstructorFactory = nodeConstructorFactory;

    //Test whether constructor fits!
    this.rootNode = nodeConstructorFactory
                        .createFromDefaultValues(parameters, false, '$');
    trieNodes.add(rootNode);

    preprocessTrie();
  }




  private void preprocessTrie() {
    buildSuccessorFunction(keys);
    calculateFailureFunction();
    buildDFA();
  }

  private void buildSuccessorFunction(List<String> keys) {
    for (String key : keys) {
      ACTrieNode current = rootNode;

      int charIndex = 0;
      char[] keyChars = key.toCharArray();

      while (charIndex < keyChars.length &&
                 (current.getNextNode(keyChars[charIndex]) != null)) {

        current = current.getNextNode(keyChars[charIndex]);
        charIndex++;
      }

      for (int ind = charIndex; ind < keyChars.length; ind++) {

        char currentChar = keyChars[ind];
        boolean isLeaf = (ind == (keyChars.length - 1));

        NodeType newNode = nodeConstructorFactory
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

  private void buildDFA() {
    Deque<ACTrieNode> bfsQueue = new ArrayDeque<>();

    for (char c : parameters.getAlphabet()) {
      rootNode.setTransitions(c, rootNode.getNextNode(c));
      if (rootNode.getNextNode(c) != rootNode) {
        bfsQueue.add(rootNode.getNextNode(c));
      }
    }

    while (!bfsQueue.isEmpty()) {
      ACTrieNode curNode = bfsQueue.poll();
      for (char c : parameters.getAlphabet()) {
        if (curNode.getNextNode(c) != null) {
          bfsQueue.add(curNode.getNextNode(c));
          curNode.setTransitions(c, curNode.getNextNode(c));
        } else {
          curNode.setTransitions(c, curNode.getFail().getTransitions(c));
        }
      }
    }
  }

  public Set<String> patternMatch(String text) {
    ACTrieNode currentNode = rootNode;
    Set<String> returnSet = new HashSet<>();

    for (char c : text.toCharArray()) {
      currentNode = currentNode.getTransitions(c);
      returnSet.addAll(currentNode.output);
    }

    return returnSet;
  }

  public static void main(String[] args) {
//        ac_string_matcher.AhoCorasick trie = createAhoCorasickTrieFromParams(List.of("hello", "world", "is", "leiwand"),
    AhoCorasickTrie<ACTrieNode> trie = AhoCorasickTrieFactory.getAhoCorasickTrieFromKeys(
        List.of("DD", "B99", "aac", "caDcD", "acaB", "D9"));
//                language.LanguageParameters.defaultParameter);
    System.out.println(trie.patternMatch("sjkldfhelloalksdjis"));
  }
}

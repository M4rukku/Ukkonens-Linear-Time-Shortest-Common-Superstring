/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package ukkonen_scs_finder;


import ac_string_matcher.AhoCorasickTrie;
import ac_string_matcher.AhoCorasickTrieFactory;
import alphabet.LanguageParameterFactory;
import alphabet.LanguageParameters;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import trie_nodes.UkkonenTrieNode;
import trie_nodes.UkkonenTrieNodeFactory;


/**
 * An implementation of Esko Ukkonen's linear time algorithm for finding approximate shortest common
 * superstrings. It tries to find the longest Hamiltonian Path in the weighted overlap graph of our
 * keys via a greedy heuristic. This path corresponds to an approximate SCS. We compute the greedy
 * heuristic fast using the AhoCorasick String Matcher.
 *
 * <p>Example Usage:
 * List[String] keys = List.of("aki", "ele", "kiki", "kira", "lea"); UkkonensSCSFinder finder =
 * UkkonensSCSFinder.createFromKeys(keys); String scs = finder.getSCS();
 *
 * <p>Some Notes on the Implementation and the Algorithm:
 *
 * <p>Our AhoCorasick algorithm uses arrays to define the goto function. That means that our
 * algorithm scales badly in terms of space for large alphabets. (One can try to change ACTrieNode
 * to use Maps with defaults instead.)
 *
 * <p>The basic idea of Ukkonens Algorithm is defined in the following Theorem: Let strings xi and
 * xj be in R and let xi represent state s. The maximal overlap between xi and xj is of length k if
 * and only if k = d (t) where t is the first state on the failure transition path from s such that
 * j is in L(t).
 *
 * <p>Theorem 4 suggests how the greedy heuristics can be implemented using the AC machine. To find
 * the pairwise overlaps in descending order, follow all failure paths starting from the states
 * represented by the strings in R. All paths have to be followed simultaneously and the longest
 * available overlap has to be selected at each stage. This suggests an implementation as follows.
 * Traverse the states of the AC machine in reversed breadth-first order. At each state we have to
 * know which of the failure paths pass through that state. Each passing path represents an overlap.
 * Choose any overlap that is not forbidden by earlier selections.
 *
 * <p>The algorithm is described in detail here: https://link.springer.com/article/10.1007/BF01840391.
 *
 * @author : Markus Walder
 * @since : 26.12.2020, Sa.
 **/
public class UkkonensSCSFinder {

  UkkonenTrieNode rootNode;
  UkkonenTrieNode firstNodeInReverseBFSOrder;
  List<UkkonenTrieNode> allNodes;

  private List<String> keys;
  /**
   * stringIndexToRepresentingNode is a map between a string in keys and an UkkonenTrieNode
   * (connected via index). A key is associated with a node whenether the node represents the end of
   * the string and the string is contained within the REDUCED graph. This is important since we
   * will use it to define the reduced graph in the latter parts of the algorithm.
   */
  private List<UkkonenTrieNode> stringIndexToRepresentingNode;
  /**
   * representingNodeToStringIndex is the inverse of stringIndexToRepresentingNode.
   */
  private Map<UkkonenTrieNode, Integer> representingNodeToStringIndex;

  //Keep track of Hamilton Path Building
  /**
   * forbidden is false at index i, if our current hamiltonPath does not yet contain any overlap
   * ending at the node representing key i, otherwise it is true. (It is also true, if the string is
   * not part of the reduced graph.)
   */
  private List<Boolean> forbidden;
  private List<Edge<Integer, Integer>> hamiltonPath;
  /**
   * firstStringInComponent at position i is j, iff the directed path component containing i starts
   * with j.
   */
  private List<Integer> firstStringInComponent;
  /**
   * lastStringInComponent at position i is j, iff the directed path component containing i ends
   * with j.
   */
  private List<Integer> lastStringInComponent;

  private UkkonensSCSFinder(List<String> keys, LanguageParameters params) {
    this.keys = keys;

    //Inject the UkkonenTrieNodeFactory instead of the ACTrieNodeFactory
    AhoCorasickTrie<UkkonenTrieNode> newTrie =
        AhoCorasickTrieFactory
            .createAhoCorasickTrieFromParamsWithNodeFactory(
                keys, params, new UkkonenTrieNodeFactory());

    allNodes = newTrie.trieNodes;
    rootNode = newTrie.rootNode;

    hamiltonPath = new ArrayList<>();
    stringIndexToRepresentingNode = new ArrayList<>(keys.size());
    forbidden = new ArrayList<>(keys.size());
    firstStringInComponent = new ArrayList<>(keys.size());
    lastStringInComponent = new ArrayList<>(keys.size());

    for (int i = 0; i < keys.size(); i++) {
      stringIndexToRepresentingNode.add(rootNode);
      forbidden.add(false);
      firstStringInComponent.add(-1);
      lastStringInComponent.add(-1);
    }
    representingNodeToStringIndex = new HashMap<>();

    preprocessTrie();
    greedilyBuildHamiltonPath();
  }

  //Static Factory Methods

  /**
   * Creates an UkkonenSCSFinder from the list of words for which we want to generate an approximate
   * SCS. The {@link LanguageParameters} will be automatically generated from the keys. See {@link
   * LanguageParameterFactory}.
   *
   * @param keys a list of strings for which we want to generate a SCS
   * @return an instance of UkkonensSCSFinder for our parameters
   */
  public static UkkonensSCSFinder createFromKeys(List<String> keys) {
    return new UkkonensSCSFinder(keys,
        LanguageParameterFactory.createLanguageParametersFromKeys(keys));
  }

  /**
   * Creates an UkkonenSCSFinder from the list of words for which we want to generate an approximate
   * SCS and the paramters of the underlying language.
   *
   * @param keys   a list of strings for which we want to generate a SCS
   * @param params language paramters that define the language of the words used in keys
   * @return an instance of UkkonensSCSFinder for our parameters
   */
  public static UkkonensSCSFinder createFromParams(List<String> keys, LanguageParameters params) {
    return new UkkonensSCSFinder(keys, params);
  }

  //Augment AC Machine and find Trie

  /**
   * Preprocesses the AC Machine to add the functionality we need for Ukkonens' algorithm. In
   * detail, it does the following:
   *
   * <p>It calculates the depth d(s) and node supporters L(s); it reduces the graph (eliminates
   * unneeded substrings, i.e. her when we have both her and herself) by changing
   * stringIndexToRepresentingNode; it calculates the reverse bfs ordering of states and assigns
   * each nodes its successor; it stores the starting points of the failure paths (all string ends
   * in the reduced graph)
   */
  private void preprocessTrie() {
    // Reduces Graph, Calculates depth + supporters
    // stringIndexToStartOfFailurePath is extremely important -- it represents our reduced graph,
    // since we will only process those strings where this value is not the root

    //Init E
    allNodes.forEach(ukkonenTrieNode -> representingNodeToStringIndex.put(ukkonenTrieNode, -1));

    for (int i = 0; i < keys.size(); i++) {
      UkkonenTrieNode state = rootNode;
      rootNode.supportedKeys.add(i);
      char[] currentString = keys.get(i).toCharArray();

      for (int j = 0; j < currentString.length; j++) {
        char c = currentString[j];
        state = state.getNextNode(c);
        state.supportedKeys.add(i);

        if (j == (currentString.length - 1)) {
          stringIndexToRepresentingNode.set(i, state); //F
          representingNodeToStringIndex.put(state, i); //E

          if (!state.isLeafInAhoCorasickGraph()) {
            //remove node from graph if it is not a leaf
            //graph reduction
            stringIndexToRepresentingNode.set(i, rootNode);
          }
        }
      }
    }

    //Get reverse bfs ordering
    Deque<UkkonenTrieNode> bfsQueue = new ArrayDeque<>();
    firstNodeInReverseBFSOrder = rootNode;
    bfsQueue.add(rootNode);
    rootNode.depth = 0;

    while (!bfsQueue.isEmpty()) {
      UkkonenTrieNode curState = bfsQueue.poll();

      for (Iterator<UkkonenTrieNode> it = curState.iterator(); it.hasNext(); ) {
        UkkonenTrieNode nextState = it.next();
        //Both of these would indicate an invalid next state (epsilon transition or no trans.)
        if (nextState == null || nextState == rootNode) {
          continue;
        }

        bfsQueue.add(nextState);
        nextState.depth = curState.depth + 1;
        nextState.bfsSuccessor = firstNodeInReverseBFSOrder;
        firstNodeInReverseBFSOrder = nextState;

        // If the fail state is an end state, then the string represented by the fail state is
        // actually a suffix of our current string and can thus be represented by it.
        // Therefore, we need to remove it from our overlap graph.
        int representedFailIndex = representingNodeToStringIndex.get(nextState.getFail());
        if (representedFailIndex != -1) {
          stringIndexToRepresentingNode.set(representedFailIndex, rootNode); //Reduce graph
        }
      }
    }
  }

  /**
   * This function will build the approximately longest Hamilton path using our modified AC machine.
   * How this works is best described by a quote from the aforementioned paper:
   *
   * <p>Theorem 4 suggests how the greedy heuristics can be implemented using the AC machine. To find
   * the pairwise overlaps in descending order, follow all failure paths starting from the states
   * represented by the strings in R. All paths have to be followed simultaneously and the longest
   * available overlap has to be selected at each stage. This suggests an implementation as follows.
   * Traverse the states of the AC machine in reversed breadth-first order. At each state we have to
   * know which of the failure paths pass through that state. Each passing path represents an
   * overlap. Choose any overlap that is not forbidden by earlier selections.
   */
  private void greedilyBuildHamiltonPath() {
    for (int i = 0; i < keys.size(); i++) {
      if (stringIndexToRepresentingNode.get(i) != rootNode) { //is string i part of reduced graph
        // We have not yet selected anything and the fail node is clearly on the failure path
        // starting at i
        stringIndexToRepresentingNode.get(i).getFail().pCandidate.add(i);
        firstStringInComponent.set(i, i);
        lastStringInComponent.set(i, i);
      } else {
        forbidden.set(i, true);
      }
    }

    UkkonenTrieNode currentState = firstNodeInReverseBFSOrder.bfsSuccessor;
    while (currentState != rootNode) {
      if (!currentState.pCandidate.isEmpty()) {
        for (int index : currentState.supportedKeys) {
          if (forbidden.get(index) || currentState.pCandidate.isEmpty()) {
            //Note that the second check was not included in the original algorithm!
            //This prevents IndexOutOfBoundsErrors which occur for large problem spaces
            continue;
          }

          int firstCandidate = currentState.pCandidate.get(0);

          if (firstStringInComponent.get(firstCandidate) == index) {
            if (currentState.pCandidate.size() <= 1) {
              continue;
            } else {
              firstCandidate = currentState.pCandidate.get(1);
              currentState.pCandidate.remove(1);
            }
          } else {
            currentState.pCandidate.remove(0);
          }

          hamiltonPath.add(new Edge<>(firstCandidate, index, currentState.depth));
          forbidden.set(index, true);
          firstStringInComponent
              .set(lastStringInComponent.get(index), firstStringInComponent.get(firstCandidate));
          lastStringInComponent
              .set(firstStringInComponent.get(firstCandidate), lastStringInComponent.get(index));
        }
        currentState.getFail().pCandidate.addAll(currentState.pCandidate);
      }
      currentState = currentState.bfsSuccessor;
    }
  }

  /**
   * getSCS returns the SCS generated by Ukkonens algorithm. It pieces together the disconnected
   * components in the Hamilton Path which can no longer be combined using the greedy heuristic.
   *
   * @return the approximate shortest common superstring generated by the algorithm
   */
  public String getSCS() {
    List<Integer> graphComponents = new ArrayList<>();
    for (int i = 0; i < forbidden.size(); i++) {
      if (!forbidden.get(i)) {
        graphComponents.add(i);
      }
    }

    Map<Integer, Edge<Integer, Integer>> allEdges = hamiltonPath.stream().collect(
        Collectors.toMap(edge -> edge.fst, edge -> edge));

    //Generate total path
    List<String> graphComponentStrings = new ArrayList<>();

    for (int startEdge : graphComponents) {
      if (allEdges.containsKey(startEdge)) {
        graphComponentStrings.add(componentToString(allEdges, startEdge));
      } else { //Singular Node
        graphComponentStrings.add(keys.get(startEdge));
      }
    }

    return String.join("", graphComponentStrings);
  }


  /**
   * componentToString is a helper function that generates the substring represented by a component
   * of the Hamilton Path. This component is uniquely identified by startNode.
   */
  private String componentToString(Map<Integer, Edge<Integer, Integer>> allEdges,
      Integer startNode) {
    StringBuilder builder = new StringBuilder();
    Edge<Integer, Integer> currentEdge = allEdges.get(startNode);
    builder.append(keys.get(currentEdge.fst));

    int nextNode = startNode;
    while (true) {
      currentEdge = allEdges.get(nextNode);

      if (currentEdge == null) {
        break;
      }
      builder.append(keys.get(currentEdge.snd).substring(currentEdge.weight));
      allEdges.remove(nextNode);
      nextNode = currentEdge.snd;
    }
    return builder.toString();
  }
}

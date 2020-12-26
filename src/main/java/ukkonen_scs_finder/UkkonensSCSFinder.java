/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package ukkonen_scs_finder;

import ac_string_matcher.AhoCorasickTrie;
import ac_string_matcher.AhoCorasickTrieFactory;
import alphabet.LanguageParameterFactory;
import java.util.Map.Entry;
import trie_nodes.UkkonenTrieNode;
import java.util.*;
import java.util.stream.Collectors;
import alphabet.LanguageParameters;
import trie_nodes.UkkonenTrieNodeFactory;

/**
 * An implementation of Esko Ukkonen's linear time algorithm for finding approximate shortest common
 * superstrings. It tries to find the longest Hamiltonian Path in the weighted overlap graph of our
 * keys via a greedy heuristic. This path corresponds to an approximate SCS. We compute the greedy
 * heuristic fast using the AhoCorasick String Matcher.
 * <p>
 * The algorithm is described in detail here: https://link.springer.com/article/10.1007/BF01840391.
 *
 * @author : Markus Walder
 * @since : 26.12.2020, Sa.
 **/
public class UkkonensSCSFinder {

  UkkonenTrieNode rootNode;
  UkkonenTrieNode firstNodeInReverseBFSOrder;
  List<UkkonenTrieNode> allNodes;

  private List<String> keys;
  private List<UkkonenTrieNode> stringIndexToEndNodes;
  private Map<UkkonenTrieNode, Integer> endNodesToStringIndex;

  //Keep track of Hamilton Path Building
  private List<Boolean> forbidden;
  private List<Edge<Integer, Integer>> hamiltonPath;
  private List<Integer> FIRST;
  private List<Integer> LAST;

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
    stringIndexToEndNodes = new ArrayList<>(keys.size());
    forbidden = new ArrayList<>(keys.size());
    FIRST = new ArrayList<>(keys.size());
    LAST = new ArrayList<>(keys.size());

    for (int i = 0; i < keys.size(); i++) {
      stringIndexToEndNodes.add(null);
      forbidden.add(false);
      FIRST.add(-1);
      LAST.add(-1);
    }
    endNodesToStringIndex = new HashMap<>();

    preprocessTrie();
    buildHamiltonPath();
  }

  public static UkkonensSCSFinder createFromKeys(List<String> keys) {
    return new UkkonensSCSFinder(keys,
        LanguageParameterFactory.createLanguageParametersFromKeys(keys));
  }

  public static UkkonensSCSFinder createFromParams(List<String> keys,
      LanguageParameters params) {
    return new UkkonensSCSFinder(keys, params);
  }

  private void preprocessTrie() {
    //Reduces Graph, Sets up the supported keys of all the nodes (stores how many strings are
    // dependent on the node -- have a prefix of the substring represented by the node)
    for (int i = 0; i < keys.size(); i++) {
      UkkonenTrieNode state = rootNode;
      rootNode.supportedKeys.add(i);
      char[] currentString = keys.get(i).toCharArray();

      for (int j = 0; j < currentString.length; j++) {
        char c = currentString[j];
        state = state.getNextNode(c);
        state.supportedKeys.add(i);

        if (j == (currentString.length - 1)) {
          stringIndexToEndNodes.set(i, state);
          endNodesToStringIndex.put(state, i);

          if (!state.isLeafInAhoCorasickGraph()) {
            //remove node from graph if it is not a leaf
            //graph reduction
            stringIndexToEndNodes.set(i, rootNode);
          }
        }
      }
    }

    Deque<UkkonenTrieNode> bfsQueue = new ArrayDeque<>();
    firstNodeInReverseBFSOrder = rootNode;
    bfsQueue.add(rootNode);
    rootNode.depth = 0;

    while (!bfsQueue.isEmpty()) {
      UkkonenTrieNode curState = bfsQueue.poll();
      for (Iterator<UkkonenTrieNode> it = curState.iterator(); it.hasNext(); ) {
        UkkonenTrieNode nextState = it.next();
        if (nextState == null || nextState == rootNode) {
          continue;
        }

        bfsQueue.add(nextState);
        nextState.depth = curState.depth + 1;
        nextState.bfsSuccessor = firstNodeInReverseBFSOrder;
        firstNodeInReverseBFSOrder = nextState;

        if (endNodesToStringIndex.containsKey(nextState.getFail())) {
          stringIndexToEndNodes.set(endNodesToStringIndex.get(nextState.getFail()), rootNode);
        }
      }
    }
  }

  private void buildHamiltonPath() {
    for (int i = 0; i < keys.size(); i++) {
      if (stringIndexToEndNodes.get(i) != rootNode) {
        stringIndexToEndNodes.get(i).getFail().pCandidate.add(i);
        FIRST.set(i, i);
        LAST.set(i, i);
      } else {
        forbidden.set(i, true);
      }
    }

    UkkonenTrieNode currentState = firstNodeInReverseBFSOrder;
    while (currentState != rootNode) {
      if (!currentState.pCandidate.isEmpty()) {
        for (int index : currentState.supportedKeys) {
          if (forbidden.get(index) || currentState.pCandidate.isEmpty()) {
            continue; //TODO Changed
          }
          int firstCandidate = currentState.pCandidate.peekFirst();

          if (FIRST.get(firstCandidate) == index) {
            if (currentState.pCandidate.size() <= 1) {
              continue; //TODO
            } else {
              int tmp = currentState.pCandidate.poll();
              firstCandidate = currentState.pCandidate.poll();
              currentState.pCandidate.addFirst(tmp);
            }
          } else {
            currentState.pCandidate.poll();
          }

          hamiltonPath.add(new Edge<>(firstCandidate, index, currentState.depth));
          forbidden.set(index, true);
          FIRST.set(LAST.get(index), FIRST.get(firstCandidate));
          LAST.set(FIRST.get(firstCandidate), LAST.get(index));
        }
        currentState.getFail().pCandidate.addAll(currentState.pCandidate);
      }
      currentState = currentState.bfsSuccessor;
    }
  }

  public String getSCS() {
    Map<Integer, Integer> edgeOccurrences = new HashMap<>();
    hamiltonPath.forEach(edge ->
    {
      edgeOccurrences.putIfAbsent(edge.fst, 0);
      edgeOccurrences.putIfAbsent(edge.snd, 0);
      edgeOccurrences.replace(edge.snd, edgeOccurrences.get(edge.snd) + 1);
    });
    List<Integer> edgesWithNoIncomingPaths = edgeOccurrences.entrySet().stream()
                                                 .filter(entry -> entry.getValue() == 0)
                                                 .map(Entry::getKey)
                                                 .collect(Collectors.toList());

    Map<Integer, Edge<Integer, Integer>> allEdges = hamiltonPath.stream().collect(
        Collectors.toMap(edge -> edge.fst, edge -> edge));

    int totalComp = 0;

    //Generate total path
    StringBuilder builder = new StringBuilder();
    for (int startEdge : edgesWithNoIncomingPaths) {
      totalComp++;
      List<Integer> component = new ArrayList<>();
      String str = compToString(allEdges, startEdge, component);
      builder.append(str);
    }

    //Add all components in the reduced graph that are singular and have no overlaps
    List<String> debugStringIndexToEndNodes = new ArrayList<>();
    for (int i = 0; i < stringIndexToEndNodes.size(); i++) {
      if (stringIndexToEndNodes.get(i) != rootNode) {
        if (!edgeOccurrences.containsKey(i)) {
          builder.append(keys.get(i));
          totalComp++;
        }
      }
    }
    System.out.println("Number of Components is: " + totalComp);
    return builder.toString();
  }

  public String compToString(Map<Integer, Edge<Integer, Integer>> allEdges, Integer startNode,
      List<Integer> component) {
    StringBuilder builder = new StringBuilder();
    Edge<Integer, Integer> currentEdge = allEdges.get(startNode);
    builder.append(keys.get(currentEdge.fst));
    component.add(currentEdge.fst);

    int nextNode = startNode;
    while (true) {
      currentEdge = allEdges.get(nextNode);

      if (currentEdge == null) {
        break;
      }
      component.add(currentEdge.snd);
      builder.append(keys.get(currentEdge.snd).substring(currentEdge.weight));
      allEdges.remove(nextNode);
      nextNode = currentEdge.snd;
    }
    return builder.toString();
  }

  public static void main(String[] args) {
    List<String> tests = List.of("DD", "B99", "aac", "caDcD", "acaB",
        "D9");//List.of("AKI", "ELE", "KIKIELE", "LEATA", "KIRA", "LEA");
    UkkonensSCSFinder builder = UkkonensSCSFinder.createFromKeys(tests);
    System.out.println(builder.getSCS());
  }
}

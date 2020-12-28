/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package actrie;

import static org.junit.Assert.assertEquals;

import alphabet.LanguageParameterFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.junit.BeforeClass;
import org.junit.Test;
import trienodes.ACTrieNode;

/**
 *
 * @author : Markus Walder
 * @since : 26.12.2020, Sa.
 */
public class AhoCorasicTrieTester {

  //Thanks for the inspiration for this example:
  // https://staff.fmi.uvt.ro/~mircea.marin/lectures/ADS/StringMatching.pdf
  private static List<String> defaultData = List.of("other", "theater", "tattoo", "potato");
  private static AhoCorasickTrie<ACTrieNode> defaultTrie;

  @BeforeClass
  public static void init_default_trie() {
    defaultTrie = AhoCorasickTrieFactory.createAhoCorasickTrieFromParams(defaultData,
        LanguageParameterFactory.defaultParameter);
  }

  @Test
  public void trie_is_built_correctly() {
    AhoCorasickTrie<ACTrieNode> trie = AhoCorasickTrieFactory.createAhoCorasickTrieFromParams(
        List.of("her", "he", "it"), LanguageParameterFactory.defaultParameter);

    //ARRANGE
    ACTrieNode rootNode = trie.rootNode;
    List<ACTrieNode> rootChildren = rootNode.getSuccessorNodes().stream()
                                        .filter(acTrieNode -> acTrieNode != rootNode)
                                        .collect(Collectors.toList());
    ACTrieNode hNode = rootNode.getNextNode('h');
    ACTrieNode heNode = hNode.getNextNode('e');
    ACTrieNode herNode = heNode.getNextNode('r');

    //ASSERT
    assertEquals("children of root should be [h, i] using keys [her he it]",
        rootChildren.stream().map(acTrieNode -> acTrieNode.parentChar).collect(Collectors.toList()),
        List.of('h', 'i'));

    assertEquals("children of hNode should be [e] using keys [her he it]",
        hNode.getSuccessorNodes().stream().filter(Objects::nonNull)
            .map(acTrieNode -> acTrieNode.parentChar).collect(Collectors.toList()),
        List.of('e'));

    assertEquals("children of heNode should be [r] using keys [her he it]",
        heNode.getSuccessorNodes().stream().filter(Objects::nonNull)
            .map(acTrieNode -> acTrieNode.parentChar).collect(Collectors.toList()),
        List.of('r'));

    assertEquals("children of herNode should be [] using keys [her he it]",
        herNode.getSuccessorNodes().stream().filter(Objects::nonNull)
            .map(acTrieNode -> acTrieNode.parentChar).collect(Collectors.toList()),
        List.of());
  }

  @Test
  public void failure_function_is_computed_correctly() {
    //ARRANGE
    ACTrieNode rootNode = defaultTrie.rootNode;
    ACTrieNode potNode = rootNode.getNextNode('p').getNextNode('o').getNextNode('t'); //fail: h
    ACTrieNode theNode = rootNode.getNextNode('t').getNextNode('h').getNextNode('e'); //fail: root
    ACTrieNode otherNode = rootNode.getNextNode('o').getNextNode('t').getNextNode('h')
                               .getNextNode('e').getNextNode('r'); //fail: e
    ACTrieNode otheNode = rootNode.getNextNode('o').getNextNode('t').getNextNode('h')
                              .getNextNode('e');

    //ASSERT
    assertEquals("pot node should fail to ot node", potNode.getFail(),
        rootNode.getNextNode('o').getNextNode('t'));
    assertEquals("the node should fail to root node", theNode.getFail(), rootNode);
    assertEquals("root node should fail to rootNode", rootNode.getFail(), rootNode);
    assertEquals("other node should fail to root node", otherNode.getFail(), rootNode);
    assertEquals("othe node should fail to the node", otheNode.getFail(), theNode);
  }

  @Test
  public void dfa_reduces_connections_correctly() {
    //In this example we wish to see how the DFA has different connections than our vanilla graph

    //State tat should be connected to -- th with h, tatt with t

    //ARRANGE
    ACTrieNode root = defaultTrie.rootNode;

    ACTrieNode oState = root.getDFATransition('o');
    ACTrieNode tState = root.getDFATransition('t');
    ACTrieNode pState = root.getDFATransition('p');

    ACTrieNode potState = root.getDFATransition('p').getDFATransition('o').getDFATransition('t');
    ACTrieNode potaState = potState.getDFATransition('a');
    ACTrieNode potatState = potaState.getDFATransition('t');
    ACTrieNode potatoState = potatState.getDFATransition('o');

    ACTrieNode othState = root.getDFATransition('o').getDFATransition('t').getDFATransition('h');
    ACTrieNode thState = root.getDFATransition('t').getDFATransition('h');

    ACTrieNode taState = root.getDFATransition('t').getDFATransition('a');
    ACTrieNode tattState = taState.getDFATransition('t').getDFATransition('t');

    //ASSERT
    // State pot should be connected to state oth with "h", pota with 'a',
    // o with 'o', t with 't', p with 'p'
    // And root default
    assertEquals(new HashSet<>(potState.getAllPossibleDFATransitions()),
        new HashSet<>(List.of(othState, potaState, root, oState, pState, tState)));

    //State potat should be connected to th with "h" , tatt with 't', ta with 'a' and potato with 'o'
    //and pState + rootState Tests multiple fail paths at once
    assertEquals(new HashSet<>(potatState.getAllPossibleDFATransitions()),
        new HashSet<>(List.of(tattState, thState, taState, potatoState, root, pState)));
  }
}

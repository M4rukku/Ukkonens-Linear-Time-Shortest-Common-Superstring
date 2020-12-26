/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package trie_nodes;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import alphabet.LanguageParameterFactory;
import alphabet.LanguageParameters;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 *
 * @author : Markus Walder
 * @since : 26.12.2020, Sa.
 */
@RunWith(JUnit4.class)
public class ACTrieNodeTester {
  private ACTrieNode root;

  // Both Construction and Accessing Tree Nodes are too simple to break
  // Errors will only occur if LanguageParameter is not set up correctly!
  // In such a case we might get NullPointerExceptions!
  @Test
  public void is_leaf_in_ac_graph_checker_works(){
    //Arrange
    ACTrieNodeFactory factory = new ACTrieNodeFactory();
    LanguageParameters parameters = LanguageParameterFactory.defaultParameter;
    root = factory.createFromDefaultValues(parameters, false, '$');

    //he her it
    ACTrieNode hNode = factory.createFromDefaultValues(parameters, false, 'h');
    ACTrieNode heNode = factory.createFromDefaultValues(parameters, true, 'e');
    ACTrieNode herNode = factory.createFromDefaultValues(parameters, true, 'r');
    ACTrieNode iNode = factory.createFromDefaultValues(parameters, false, 'i');
    ACTrieNode itNode = factory.createFromDefaultValues(parameters, true, 't');

    root.setNextNode('h', hNode);
    root.setNextNode('i', iNode);
    hNode.setNextNode('e', heNode);
    heNode.setNextNode('r', herNode);
    iNode.setNextNode('t', itNode);

    //ACT & ASSERT
    assertTrue(itNode.isLeafInAhoCorasickGraph());
    assertTrue(herNode.isLeafInAhoCorasickGraph());
    assertFalse(root.isLeafInAhoCorasickGraph());
    assertFalse(heNode.isLeafInAhoCorasickGraph());
  }
}

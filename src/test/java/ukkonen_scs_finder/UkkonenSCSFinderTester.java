/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package ukkonen_scs_finder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import alphabet.LanguageParameterFactory;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import trie_nodes.UkkonenTrieNode;

/**
 *
 * @author : Markus Walder
 * @since : 26.12.2020, Sa.
 */
public class UkkonenSCSFinderTester {

  @Test
  public void algorithm_finds_valid_superstring() {
    assertTrue(
        SCSTester.testRandomlyGeneratedSCS(5000, 5, 12));
  }

  @Test
  public void depth_and_supporters_are_correct(){
    //ARRANGE
    List<String> keys = List.of("aki", "ele", "kiki", "kira", "lea");
    UkkonensSCSFinder finder = UkkonensSCSFinder.createFromParams(keys, LanguageParameterFactory.defaultParameter);

    UkkonenTrieNode root = finder.rootNode;
    UkkonenTrieNode kiNode = root.getNextNode('k').getNextNode('i');
    //ASSERT
    //The supporters of KI should be kira and kiki
    assertEquals(new HashSet<>(kiNode.supportedKeys), Set.of(2, 3));
    //The supporters of root should be 0, 1, 2, 3, 4
    assertEquals(new HashSet<>(root.supportedKeys), Set.of(1, 2, 3, 4, 0));

    //Depth of ki should be 2
    assertEquals(2, kiNode.depth);
    assertEquals(0, root.depth);
  }

  @Test
  public void created_test_string_is_correctly(){
    //ARRANGE
    List<String> keys = List.of("aki", "ele", "kiki", "kira", "lea");
    UkkonensSCSFinder finder = UkkonensSCSFinder.createFromParams(keys, LanguageParameterFactory.defaultParameter);

    //ACT
    assertTrue(Set.of("eleakirakiki", "eleakikira").contains(finder.getSCS()));
  }

}

/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package actrie;

import static org.junit.Assert.assertEquals;

import alphabet.LanguageParameterFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;

/**
 *
 * @author : Markus Walder
 * @since : 26.12.2020, Sa.
 */
public class KeywordTextMatcherTester {

  @Test
  public void finds_all_matches() {
    //ARRANGE
    List<String> dictionary = List.of("her", "she", "herself", "sherman");
    String textBody = "sheherselfwasconfusedlookingforheshermanwashisname";

    List<Match> expectedMatchesList = List.of(
        new Match("she", 0, 2),
        new Match("her", 3, 5),
        new Match("herself", 3, 9),
        new Match("she", 33, 35),
        new Match("her", 34, 36),
        new Match("sherman", 33, 39));

    Set<Match> expectedMatches = new HashSet<>(expectedMatchesList);

    //ACT
    KeywordTextMatcher matcher = KeywordTextMatcher.createFromParameters(
        LanguageParameterFactory.defaultParameter, dictionary);

    //ASSERT
    assertEquals(expectedMatches, new HashSet<>(matcher.matchText(textBody)));
  }
}

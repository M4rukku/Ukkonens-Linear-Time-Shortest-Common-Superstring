/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package ukkonen_scs_finder;

import static org.junit.Assert.assertTrue;

import alphabet.LanguageParameterFactory;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author : Markus Walder
 * @since : 26.12.2020, Sa.
 */
public class UkkonenSCSFinderTester {

  @Test
  public void algorithm_finds_valid_superstring() {
    assertTrue(
        SCSTester.testRandomlyGeneratedSCS(500, 5, 12));

    assertTrue(
        SCSTester.testRandomlyGeneratedSCS(1500, 5, 12));
  }

  @Test
  public void test(){
    List<String> test = List.of("her", "him", "she", "herself", "forever", "evermore");
    UkkonensSCSFinder finder = UkkonensSCSFinder.createFromParams(test, LanguageParameterFactory.defaultParameter);
    System.out.println(finder.getSCS());
  }
}

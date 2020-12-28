/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package actrie;

import java.util.Objects;

/**
 * A way to report matches found in a string. It defines where and which word was matched.
 *
 * @author Markus Walder
 * @since 26.12.2020, Sa.
 */
public class Match {

  public String word;
  public int startPosition;
  public int endPosition;

  /**
   * Creates a new Match Object based on Parameters.
   *
   * @param word the String matched
   * @param startPosition the position where the word occurred in the text
   * @param endPosition where the word ends in the text
   */
  public Match(String word, int startPosition, int endPosition) {
    this.word = word;
    this.startPosition = startPosition;
    this.endPosition = endPosition;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Match match = (Match) o;
    return startPosition == match.startPosition
               && endPosition == match.endPosition
               && word.equals(match.word);
  }

  @Override
  public int hashCode() {
    return Objects.hash(word, startPosition, endPosition);
  }
}

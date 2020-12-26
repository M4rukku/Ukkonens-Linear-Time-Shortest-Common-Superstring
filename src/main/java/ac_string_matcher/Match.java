package ac_string_matcher;

import java.util.Objects;

public class Match {

  public String word;
  public int startPosition, endPosition;

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
    return startPosition == match.startPosition &&
               endPosition == match.endPosition &&
               word.equals(match.word);
  }

  @Override
  public int hashCode() {
    return Objects.hash(word, startPosition, endPosition);
  }
}

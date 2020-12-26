package ac_string_matcher;

public class Match {

  public String word;
  public int startPosition, endPosition;

  public Match(String word, int startPosition, int endPosition) {
    this.word = word;
    this.startPosition = startPosition;
    this.endPosition = endPosition;
  }
}

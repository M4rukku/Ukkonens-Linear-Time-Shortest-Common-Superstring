import java.util.*;

public class UkkonenTrieNode extends AhoCorasickTrieNode {
    //Decorator for AhoCorasickTrieNode
    int depth;
    List<Integer> supportedKeys;
    UkkonenTrieNode bfsSuccessor;
    //sOnFailureFromFiAndNotHContainsNoOverlap between i and any j in supportedKeys
    Deque<Integer> pCandidate = new ArrayDeque<>();


    UkkonenTrieNode(LanguageParameters parameters, boolean isEndOfWord, char pch) {
        super(parameters, isEndOfWord, pch);
        supportedKeys = new ArrayList<>();
    }

    public UkkonenTrieNode getNextNode(char input) {
        return (UkkonenTrieNode) go.get(parameters.map(input));
    }

    public void setNextNode(char input, UkkonenTrieNode node) {
        go.set(parameters.map(input), node);
    }

    public static UkkonenTrieNode createFromParams(LanguageParameters parameters, boolean isEndOfWord, char pch) {
        return new UkkonenTrieNode(parameters, isEndOfWord, pch);
    }

    public Iterator<UkkonenTrieNode> iterator() {
        return go.stream().map(node -> (UkkonenTrieNode) node).iterator();
    }

    public UkkonenTrieNode getFail(){
        return (UkkonenTrieNode) super.getFail();
    }
}

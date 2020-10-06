import java.util.ArrayList;
import java.util.List;

public class AhoCorasickTrieNode {
    protected final LanguageParameters parameters;
    List<AhoCorasickTrieNode> go; //Null Represents Fail
    private AhoCorasickTrieNode fail;

    //For the Final Automaton

    public boolean isEndOfWord = false;
    public char parentChar;

    List<String> output = new ArrayList<>();


    public AhoCorasickTrieNode(LanguageParameters parameters, boolean isEndOfWord, char pch) {
        int alphabetSize = parameters.ALPHABET_SIZE;
        this.parameters = parameters;

        this.go = new ArrayList<>(alphabetSize);
        this.transitions = new ArrayList<>(alphabetSize);

        for (int i = 0; i < alphabetSize; i++) go.add(null);
        for (int i = 0; i < alphabetSize; i++) transitions.add(null);

        this.isEndOfWord = isEndOfWord;
        parentChar = pch;
    }

    //TRIE STUFF

    public AhoCorasickTrieNode getNextNode(char input) {
        return go.get(parameters.map(input));
    }
    public void setNextNode(char input, AhoCorasickTrieNode node) {
        go.set(parameters.map(input), node);
    }

    //DFS STUFF

    private final List<AhoCorasickTrieNode> transitions;
    public AhoCorasickTrieNode getTransitions(char input) {
        return transitions.get(parameters.map(input));
    }

    public void setTransitions(char input, AhoCorasickTrieNode node) {
        transitions.set(parameters.map(input), node);
    }

    public AhoCorasickTrieNode getFail() {
        return fail;
    }
    public void setFail(AhoCorasickTrieNode fail) {
        this.fail = fail;
    }

    //Utility method that says whether node is a leaf in the AhoCorasick Trie
    // different from end of word!

    private boolean leafComputed = false;
    private boolean leaf = false;

    public boolean isLeafInAhoCorasickGraph() {
        if (leafComputed) return leaf;
        else {
            leafComputed = true;
            for (AhoCorasickTrieNode nextState : go) {
                if (nextState != null) {
                    leaf = false;
                    return false;
                }
            }
            leaf = true;
            return true;
        }
    }

    public static AhoCorasickTrieNode createFromParams(LanguageParameters parameters, boolean isEndOfWord, char pch) {
        return new AhoCorasickTrieNode(parameters, isEndOfWord, pch);
    }
}
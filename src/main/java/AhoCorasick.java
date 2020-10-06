import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;

/*
 * Implementation of the AhoCorasick Algorithm
 * Takes a set of strings and builds a Trie from the given data
 * It exposes - The Trie Nodes, The Root for further processing with Ukkonen's Algorithm and a simple pattern matcher
 * You can also pass in a node that takes - LanguageParameters,
 */
public class AhoCorasick {
    private List<String> keys;
    private LanguageParameters parameters;
    private NodeFactory nodeFactory;

    public List<AhoCorasickTrieNode> trieNodes = new ArrayList<>();
    public AhoCorasickTrieNode rootNode;


    private AhoCorasick(List<String> keys, LanguageParameters parameters, NodeFactory nodeFactory) {
        this.keys = keys;
        this.parameters = parameters;
        this.nodeFactory = nodeFactory;

        //Test whether constructor fits!
        this.rootNode = nodeFactory.createFromDefaultValues(parameters, false, '$');
        trieNodes.add(rootNode);

        preprocessTrie();
    }

    public static AhoCorasick createAhoCorasickTrieFromParams(List<String> keys, LanguageParameters parameters) {
        return new AhoCorasick(keys, parameters, new NodeFactory(AhoCorasickTrieNode.class));
    }

    public static AhoCorasick getAhoCorasickTrieFromKeys(List<String> keys) {
        return new AhoCorasick(keys, LanguageParameters.createLanguageParametersFromNames(keys), new NodeFactory(AhoCorasickTrieNode.class));
    }
    //Custom Nodes
    public static AhoCorasick createAhoCorasickTrieFromParamsWithCustomNodes(List<String> keys, LanguageParameters parameters, Class<? extends  AhoCorasickTrieNode> nodeClass) {
        return new AhoCorasick(keys, parameters, new NodeFactory(nodeClass));
    }

    public static AhoCorasick getAhoCorasickTrieFromKeysWithCustomNodes(List<String> keys, Class<? extends  AhoCorasickTrieNode> nodeClass) {
        return new AhoCorasick(keys, LanguageParameters.createLanguageParametersFromNames(keys), new NodeFactory(nodeClass));
    }


    private void preprocessTrie() {
        buildGotoFunction(keys);
        calculateFailureFunction();
        buildDFA();
    }

    private void buildGotoFunction(List<String> keys) {
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            AhoCorasickTrieNode current = rootNode;

            int charIndex = 0;
            char[] keyChars = key.toCharArray();

            while (charIndex < keyChars.length &&
                    (current.getNextNode(keyChars[charIndex]) != null)) {

                current = current.getNextNode(keyChars[charIndex]);
                charIndex++;
            }

            for (int ind = charIndex; ind < keyChars.length; ind++) {

                char currentChar = keyChars[ind];
                boolean isLeaf = (ind == (keyChars.length - 1));

                AhoCorasickTrieNode newNode = nodeFactory.createFromDefaultValues(parameters, isLeaf, currentChar);

                if (isLeaf)
                    newNode.output.add(key);

                current.setNextNode(currentChar, newNode);
                current = newNode;
                trieNodes.add(newNode);
            }
        }

        // Set all unused paths to go back to the root node
        for (int go = 0; go < rootNode.go.size(); go++) {
            if (rootNode.go.get(go) == null) {
                rootNode.go.set(go, rootNode);
            }
        }
    }

    private void calculateFailureFunction() {
        rootNode.setFail(rootNode);

        Deque<AhoCorasickTrieNode> bfsQueue = new ArrayDeque<>();

        for (AhoCorasickTrieNode child : rootNode.go) {
            if (child != rootNode) {
                bfsQueue.add(child);
                child.setFail(rootNode);
            }
        }

        while (!bfsQueue.isEmpty()) {
            AhoCorasickTrieNode curParentState = bfsQueue.poll();

            for (AhoCorasickTrieNode childState : curParentState.go) {
                if (childState == null)
                    continue;

                bfsQueue.add(childState);
                AhoCorasickTrieNode failureState = curParentState.getFail();
                char path = childState.parentChar;

                //Terminates because root will never fail
                while (failureState.getNextNode(path) == null) {
                    failureState = failureState.getFail();
                }

                childState.setFail(failureState.getNextNode(path));
                childState.output.addAll(childState.getFail().output);
            }
        }

    }

    private void buildDFA() {
        Deque<AhoCorasickTrieNode> bfsQueue = new ArrayDeque<>();

        for (char c : parameters.alphabet) {
            rootNode.setTransitions(c, rootNode.getNextNode(c));
            if (rootNode.getNextNode(c) != rootNode)
                bfsQueue.add(rootNode.getNextNode(c));
        }

        while (!bfsQueue.isEmpty()) {
            AhoCorasickTrieNode curNode = bfsQueue.poll();
            for (char c : parameters.alphabet) {
                if (curNode.getNextNode(c) != null) {
                    bfsQueue.add(curNode.getNextNode(c));
                    curNode.setTransitions(c, curNode.getNextNode(c));
                } else {
                    curNode.setTransitions(c, curNode.getFail().getTransitions(c));
                }
            }
        }
    }

    public Set<String> patternMatch(String text) {
        AhoCorasickTrieNode currentNode = rootNode;
        Set<String> returnSet = new HashSet<>();

        for (char c : text.toCharArray()) {
            currentNode = currentNode.getTransitions(c);
            returnSet.addAll(currentNode.output);
        }

        return returnSet;
    }

    public static void main(String[] args) {
//        AhoCorasick trie = createAhoCorasickTrieFromParams(List.of("hello", "world", "is", "leiwand"),
        AhoCorasick trie = getAhoCorasickTrieFromKeys(List.of("DD", "B99", "aac", "caDcD", "acaB", "D9"));
//                LanguageParameters.defaultParameter);
        System.out.println(trie.patternMatch("sjkldfhelloalksdjis"));
    }
}

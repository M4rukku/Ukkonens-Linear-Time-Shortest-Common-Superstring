import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

//* A quick and dirty pythonesque duck typing factory -
// It will construct something that extends AhoCorasiclTrieNode, if it implements the correct constructor interface
// LanguageParameters, boolean, char
// */
public class NodeFactory {

    private Constructor<? extends AhoCorasickTrieNode> nodeConstructor;

    NodeFactory(Class<? extends AhoCorasickTrieNode> NodeType) {
        Class[] classes = new Class[3];
        classes[0] = LanguageParameters.class;
        classes[1] = boolean.class;
        classes[2] = char.class;
        try {
            nodeConstructor = NodeType.getDeclaredConstructor(classes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("The class passed to NodeFactory does not implement the right constructor protocol", e);
        }
    }

    public <T extends AhoCorasickTrieNode> AhoCorasickTrieNode createFromDefaultValues(LanguageParameters parameters, boolean isEndOfWord, char parentChar) {
        try {
            return nodeConstructor.newInstance(parameters, isEndOfWord, parentChar);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException("Something went wrong when creating an AhoCorasickTreeNode in the NodeFactory!", e);
        }
    }
}

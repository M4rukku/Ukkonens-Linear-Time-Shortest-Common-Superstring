/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package trie_nodes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import trie_nodes.ACTrieNode;
import alphabet.LanguageParameters;
import trie_nodes.AbstractACNodeFactory;

//* A quick and dirty pythonesque duck typing factory -
// It will construct something that extends AhoCorasiclTrieNode, if it implements the correct constructor interface
// language.LanguageParameters, boolean, char
// */
public class ClassTypeNodeFactory<T extends ACTrieNode> implements
    AbstractACNodeFactory<T> {

  private Constructor<T> nodeConstructor;

  public ClassTypeNodeFactory(Class<T> nodeType) {
    Class<?>[] classes = new Class[3];
    classes[0] = LanguageParameters.class;
    classes[1] = boolean.class;
    classes[2] = char.class;
    try {
      nodeConstructor = nodeType.getDeclaredConstructor(classes);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(
          "The class passed to NodeFactory does not implement the right constructor protocol", e);
    }
  }

  /**
   * A simple factory method that takes a set of default parameters and then returns a new Suffix
   * Trie Node based on it.
   *
   * @param parameters  The {@link LanguageParameters} that define our underlying Alphabet
   * @param isEndOfWord Indicates whether this Suffix Trie Node represents the end of a word
   * @param parentChar  Character - that tells us the character of our parent
   * @return Returns a new Trie Node
   */
  @Override
  public T createFromDefaultValues(
      LanguageParameters parameters, boolean isEndOfWord, Character parentChar) {
    try {
      return nodeConstructor.newInstance(parameters, isEndOfWord, parentChar);
    } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
      throw new RuntimeException(
          "Something went wrong when creating an AhoCorasickTreeNode in the NodeFactory!", e);
    }
  }
}

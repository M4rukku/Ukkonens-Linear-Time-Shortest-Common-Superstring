/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package org.marukku.ukkonenscs.trienodes;

import org.marukku.ukkonenscs.alphabet.LanguageParameter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * A very powerful factory that can create any type of TrieNode which extends ACTrieNode using
 * reflection. One needs to pass in the class of the tree node one wants to create in the
 * constructor and then the factory will generate the node iff, the node constructor is of the form
 * [LanguageParameters, Boolean, Character].
 *
 * @author Markus Walder
 * @since 26.12.2020, Sa.
 */
public class ClassTypeNodeFactory<T extends ACTrieNode> implements
    AbstractACNodeFactory<T> {

  private Constructor<T> nodeConstructor;


  /**
   * The Constructor takes a class which needs to have a constructor of the form
   * [LanguageParameters, Boolean, Character], otherwise it will throw a {@link
   * NoSuchMethodException}.
   *
   * @param nodeType Class of the NodeType that we want to generate
   * @throws NoSuchMethodException whenever the class does not have the right constructor
   */
  public ClassTypeNodeFactory(Class<T> nodeType) throws NoSuchMethodException {
    Class<?>[] classes = new Class[3];
    classes[0] = LanguageParameter.class;
    classes[1] = boolean.class;
    classes[2] = char.class;

    nodeConstructor = nodeType.getDeclaredConstructor(classes);
  }

  /**
   * A simple factory method that takes a set of default parameters and then returns a new Suffix
   * Trie Node of a custom type based on it.
   *
   * @param parameters  The {@link LanguageParameter} that defines our underlying Alphabet
   * @param isEndOfWord Boolean - Indicates whether this Suffix Trie Node represents the end of a
   *                    word
   * @param parentChar  Character - that tells us the character leading to this node
   * @return Returns a new Trie Node based on the nodeType passed to the constructor
   */
  @Override
  public T createFromDefaultValues(
      LanguageParameter parameters, boolean isEndOfWord, Character parentChar) {
    try {
      return nodeConstructor.newInstance(parameters, isEndOfWord, parentChar);
    } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
      throw new RuntimeException(
          "Something went wrong when creating an AhoCorasickTreeNode in the NodeFactory!", e);
    }
  }
}

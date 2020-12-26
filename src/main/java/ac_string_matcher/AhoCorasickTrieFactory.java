package ac_string_matcher;

import alphabet.LanguageParameters;
import java.util.List;
import trie_nodes.ACTrieNode;
import trie_nodes.ACTrieNodeFactory;
import trie_nodes.AbstractACNodeFactory;

public class AhoCorasickTrieFactory {

  //Default ACTNodes
  public static AhoCorasickTrie<ACTrieNode> createAhoCorasickTrieFromParams(List<String> keys,
      LanguageParameters parameters) {
    return new AhoCorasickTrie<>(keys, parameters, new ACTrieNodeFactory());
  }

  //Custom Class Nodes
  public static <T extends ACTrieNode> AhoCorasickTrie<T> createAhoCorasickTrieFromParamsWithNodeFactory(
      List<String> keys,
      LanguageParameters parameters, AbstractACNodeFactory<T> factory) {
    return new AhoCorasickTrie<>(keys, parameters, factory);
  }

  public static <T extends ACTrieNode> AhoCorasickTrie<T> getAhoCorasickTrieFromKeysWithNodeFactory(
      List<String> keys, AbstractACNodeFactory<T> factory) {

    return new AhoCorasickTrie<>(keys, LanguageParameters.createLanguageParametersFromNames(keys),
        factory);
  }
}

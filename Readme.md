## Notes

This is an implementation of the AhoCorasick efficient string matcher (https://dl.acm.org/doi/10.1145/360825.360855) and Ukkonens' linear time approximate shortest common superstring finder (https://link.springer.com/article/10.1007/BF01840391).

Originally inspired by IMCs 32BIDS coding challenge (already finished), where I implemented the initial version, I decided to clean up and publish my code (under a MIT license).

Both algorithms can be used independently; you can find usage examples either in the tests or in the javadoc. Should you need to build a new algorithm based on ACs' string matcher, you can create a new node extending ACTrieNode and a corresponding implementation of an AbstractACNodeFactory which you then can inject into the AhoCorasickTrieFactory.

Should you have any questions feel free to contact me.
Cheers, M4rukku
 
## Usage

**Find SCS (Ukkonen):**
```java
 List<String> keys = List.of("aki", "ele", "kiki", "kira", "lea"); 
 UkkonenSCSFinder finder = UkkonenSCSFinder.createFromKeys(keys); 
 String scs = finder.getSCS(); 
 ```
**Find all matches of a set of keys in a text (AC):** 
```java
 StringDictionaryMatcher matcher = StringDictionaryMatcher.createFromParameters(
     LanguageParameterFactory.defaultParameter, keys); 
 List<Match> matches = matcher.matchText(text); 
 ```
 
## A quick intro to Language Parameters

The class LanguageParameter is a utility class which encodes language information. In particular, it stores the characters that embody the alphabet, as well as a mapper function between the characters inside and the integers from 0 (inclusive) to the alphabet size (exclusive). We need this mapping function because our nodes have a "goto" function that is basically an array linking our current nodes with successor nodes. The successor node in the trie for character c can be found by accessing gotoArray(mapped(c)). In the aftermath, it might have been easier to replace the array with a HashMap, but for small alphabets an array encoding is faster. Nonetheless, the LanguageParameter class pops-up all over the place in my implementation; so it is important to be aware of it.
<br><br>
**Create a new LanguageParameter: (from Alphabet alone)**
```java
private static final List<Character> lowerCaseEnglishAlphabet =
      "abcdefghijklmnopqrstuvwxyz".chars().mapToObj(c -> (char) c).collect(Collectors.toList());
      
LanguageParameter englishLowerCaseLanguageParameter =
       LanguageParameterFactory.createLanguageParametersFromAlphabet(lowerCaseEnglishAlphabet);
```

 **Create a new LanguageParameter: (from Alphabet + Custom Mapping Function)** 
```java
LanguageParameter englishLowerCaseLanguageParameter =
    LanguageParameterFactory.createLanguageParametersFromParams(myChar -> (int) (myChar - 'a'), lowerCaseEnglishAlphabet);
```

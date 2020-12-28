This is an implementation of the AhoCorasick efficient string matcher (https://dl.acm.org/doi/10.1145/360825.360855) and Ukkonens' linear time approximate shortest common superstring finder (https://link.springer.com/article/10.1007/BF01840391).

Originally inspired by IMCs 32BIDS coding challenge (already finished), where I implemented the initial version, I decided to clean up and publish my code (under a MIT license).

Both algorithms can be used independently; you can find usage examples either in the tests or in the javadoc. Should you need to build a new algorithm based on ACs' string matcher, you can create a new node extending ACTrieNode and a corresponding implementation of an AbstractACNodeFactory which you then can inject into the AhoCorasickTrieFactory.

Should you have any questions feel free to contact me.
Cheers, M4rukku
 

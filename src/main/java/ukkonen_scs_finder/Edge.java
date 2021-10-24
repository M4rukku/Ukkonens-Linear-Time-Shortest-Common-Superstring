/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package ukkonen_scs_finder;

public class Edge<T, E> {
    T fst;
    T snd;
    E weight;

    Edge(T fst, T snd, E weight) {
        this.fst = fst;
        this.snd = snd;
        this.weight = weight;
    }
}

/*
 *  Copyright (c) MIT License
 *  2020, Markus Walder (https://github.com/M4rukku)
 */

package ukkonen_scs_finder;

/**
 * A helper class representing a weighted edge.
 * @param <T> The type representing nodes (usually integers).
 * @param <E> The type of the weight associated with the edge.
 */
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

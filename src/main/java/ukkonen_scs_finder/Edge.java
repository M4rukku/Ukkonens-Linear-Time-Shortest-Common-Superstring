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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * Stores global parameters needed in both AhoCorasick and Ukkonen's Algorithm
 * Can be sent to them via dependency injection
 */
public class LanguageParameters {
    public int ALPHABET_SIZE;
    public Function<Character, Integer> mapper;
    public List<Character> alphabet;

    private LanguageParameters(int alphabetSize, Function<Character, Integer> mapper, List<Character> alphabet) {
        ALPHABET_SIZE = alphabetSize;
        this.mapper = mapper;
        this.alphabet = alphabet;
    }

    public static LanguageParameters createLanguageParametersFromParams
            (int alphabetSize, Function<Character, Integer> mapper, List<Character> alphabet) {
        return new LanguageParameters(alphabetSize, mapper, alphabet);
    }

    public static LanguageParameters createLanguageParametersFromNames(List<String> names) {
        //First get the five characters and build a mapper from the chars to 0-4
        HashMap<Character, Integer> alphabetMapper = new HashMap<>();
        int count = 0;
        for (String name : names) {
            for (char c : name.toCharArray()) {
                if (!alphabetMapper.containsKey(c)) {
                    alphabetMapper.put(c, count);
                    count++;
                }
            }
        }
        return new LanguageParameters(alphabetMapper.size(), alphabetMapper::get, new ArrayList<>(alphabetMapper.keySet()));
    }

    public int map(char input) {
        return mapper.apply(input);
    }

    static final private List<Character> lowerCaseStandardAlphabet =
            "abcdefghijklmnopqrstuvwxyz".chars().mapToObj(c -> (char) c).collect(Collectors.toList());

    public static LanguageParameters defaultParameter =
            new LanguageParameters(26, myChar -> myChar - 'a', lowerCaseStandardAlphabet);

}

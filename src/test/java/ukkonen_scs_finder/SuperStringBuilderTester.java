package ukkonen_scs_finder;

import alphabet.LanguageParameterFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import alphabet.LanguageParameters;

public class SuperStringBuilderTester {

    int totalSize = 0;
    List<String> testKeys;
    LanguageParameters params;

    SuperStringBuilderTester(int numTestCases, LanguageParameters params, int minStringLength, int maxStringLength){
        this.params = params;
        testKeys = generateTestCases(numTestCases, minStringLength, maxStringLength);
    }

    public void evaluateTest(String result){
        System.out.println(result);
        System.out.println("Compression factor is: " +  (double) totalSize / (double) result.length());
        System.out.println("Points are: " +  (double) (totalSize - result.length()) / (double) totalSize);
        for (String key : testKeys) {
            if(!result.contains(key)){
                System.out.println("Test failed for string - " + result);
                System.out.println("Test failed with key - " + key);
            }
        }
    }

    private List<String> generateTestCases(int numTestCases, int minStringLength, int maxStringLength){
        List<Character> alphabet = params.getAlphabet();
        //Build Strings of Size 2 - 5
        List<String> testSet = new ArrayList<>(numTestCases);

        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < numTestCases; i++) {
            builder.delete(0, builder.length());
            int size = random.nextInt(maxStringLength-minStringLength) + minStringLength;
            totalSize += size;
            for (int j = 0; j < size; j++) {
                builder.append(alphabet.get(random.nextInt(alphabet.size())));
            }
            testSet.add(builder.toString());
        }
        return testSet;
    }

    public static void handleTestCase(int numTestCases, LanguageParameters params, int minStringLength, int maxStringLength){
        SuperStringBuilderTester tester = new SuperStringBuilderTester(500, params, minStringLength, maxStringLength);
        UkkonensSCSFinder builder = UkkonensSCSFinder.createFromKeys(tester.testKeys);
       tester.evaluateTest(builder.getSCS());
    }

    //Uses Default Language Parameter
    public static void handleTestCase(int numTestCases, int minStringLength, int maxStringLength){
        SuperStringBuilderTester tester = new SuperStringBuilderTester(500,
                LanguageParameterFactory.defaultParameter, minStringLength, maxStringLength);
        UkkonensSCSFinder builder = UkkonensSCSFinder.createFromKeys(tester.testKeys);
       tester.evaluateTest(builder.getSCS());
    }

    public static void main(String[] args) {
        handleTestCase(500, 2, 5);
    }
}

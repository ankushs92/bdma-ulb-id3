import com.google.common.collect.ImmutableSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class ID3 {
//    buying       v-high, high, med, low
//    maint        v-high, high, med, low
//    doors        2, 3, 4, 5-more
//    persons      2, 4, more
//    lug_boot     small, med, big
//    safety       low, med, high
    private static final String COMMA_DELIM = ",";
    private static final String TARGET_ATTRIBUTE = "car_eval";
    private static final String FAILURE = "failure";
    private static final boolean IS_LEAF_NODE = true;
    private static final Set<String> INPUT_ATTRIBUTES = ImmutableSet.of(
            "buying", "maint", "doors", "persons", "lug_boot", "safety", TARGET_ATTRIBUTE
    );
    private static final String FILE_LOCATION= "/Users/ankushsharma/Downloads/car_eval.csv";

    public static void main(final String[] args) throws IOException {

        final List<String[]> trainingDataSet = Files.lines(Paths.get(FILE_LOCATION))
                                                    .map( str -> str.split(COMMA_DELIM))
                                                    .collect(toList());

        final DecisionTree decisionTree = computeBestPossibleDecisionTree(INPUT_ATTRIBUTES, TARGET_ATTRIBUTE, trainingDataSet);
    }

    private static DecisionTree computeBestPossibleDecisionTree(
        final Set<String> inputAttributes,
        final String targetAttributeIndex,
        final List<String[]> trainingDataSet
    )
    {
        if(CollectionUtil.isNullOrEmpty(trainingDataSet)) {
            return new DecisionTree(ImmutableSet.of(new Node<>(FAILURE, IS_LEAF_NODE)));
        }

        if(CollectionUtil.isNullOrEmpty(inputAttributes)) {
            final String mostFreqValue = findMostFreqValue(trainingDataSet);
            return new DecisionTree(ImmutableSet.of(new Node<>(mostFreqValue, IS_LEAF_NODE)));
        }

        final List<String> targetAttributeValues = trainingDataSet.stream().map( array -> array[5]).collect(toList());
        final int targetAttributesPossibleValues = targetAttributeValues
                                                                  .stream()
                                                                  .distinct()
                                                                  .collect(Collectors.toList())
                                                                  .size();

        if(targetAttributesPossibleValues == 1) {
            return new DecisionTree(ImmutableSet.of(new Node<>( trainingDataSet.get(0)[5], IS_LEAF_NODE)));
        }

        final double entropy = entropy(targetAttributeValues);

        return null;
    }


    private static String findMostFreqValue(final List<String[]> collection) {
        final Map<String, Long> map = collection.stream()
                                                .collect(groupingBy(array -> array[5], Collectors.counting()));

        final Optional<Entry<String, Long>> resultOpt = map.entrySet()
                                                           .stream()
                                                           .max(Comparator.comparing(Entry::getValue));
        String result = Strings.EMPTY;
        if(resultOpt.isPresent()) {
            result = resultOpt.get().getKey();
        }
        return result;
    }

    private static double entropy(final List<String> collection) {
        final int total = collection.size();
        final Map<String, Long> map = collection.stream()
                                                .collect(groupingBy(str -> str , Collectors.counting()));

        double entropy = 0.0;
        for(final Entry<String, Long> entry : map.entrySet()) {
            final double probability = entry.getValue() / total;
            entropy +=  -(probability * log2(probability));
        }
        return entropy;
    }

    private static double entropySplit(final List<String> collection) {

    }

    private static double log2(double n) {
        return Math.log(n) / Math.log(2);
    }


}

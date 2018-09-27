import com.google.common.collect.ImmutableSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.*;

public class ID3 {

    private static final String COMMA_DELIM = ",";
    private static final int TARGET_ATTRIBUTE_INDEX = 6;
    private static final String FAILURE = "failure";
    private static final boolean IS_LEAF_NODE = true;
    private static final List<String> INPUT_ATTRIBUTES = Arrays.asList(
            "buying", "maint", "doors", "persons", "lug_boot", "safety"
    );
    private static final String FILE_LOCATION= "/Users/ankushsharma/Downloads/car_eval.csv";

    public static void main(final String[] args) throws IOException {

         List<String[]> trainingDataSet = Files.lines(Paths.get(FILE_LOCATION))
                                                    .map( str -> str.split(COMMA_DELIM))
                                                    .collect(toList());

         DecisionTree decisionTree = computeBestPossibleDecisionTree(INPUT_ATTRIBUTES, TARGET_ATTRIBUTE_INDEX, trainingDataSet);
    }

    private static DecisionTree computeBestPossibleDecisionTree(
         List<String> inputAttributes,
         int targetAttributeIndex,
         List<String[]> trainingDataSet
    )
    {
        if(isNullOrEmpty(trainingDataSet)) {
            return new DecisionTree(singletonList(new Node<>(FAILURE, IS_LEAF_NODE)));
        }

        if(isNullOrEmpty(inputAttributes)) {
            String  mostFreqValue = findMostFreqValue(trainingDataSet);
            return new DecisionTree(singletonList(new Node<>(mostFreqValue, IS_LEAF_NODE)));
        }

        int numOfColumns = trainingDataSet.get(0).length;
        Map<Integer, List<String>> indexWithColumnsMap = new HashMap<>();
        for(int i = 0; i < numOfColumns; i++) {
            int columnNo = i; //have to do this because of "effectively final" issue with lambda expressions
            if(columnNo != targetAttributeIndex) {
                List<String> columnsForI = trainingDataSet.stream().map(array -> array[columnNo]).collect(toList());
                indexWithColumnsMap.put(columnNo, columnsForI);
            }
        }
        List<String> targetAttributeValues = trainingDataSet.stream().map( array -> array[targetAttributeIndex]).collect(toList());
        int targetAttributesPossibleValues = targetAttributeValues
                                                                  .stream()
                                                                  .distinct()
                                                                  .collect(toList())
                                                                  .size();

        if(targetAttributesPossibleValues == 1) {
            return new DecisionTree(singletonList(new Node<>(trainingDataSet.get(0)[targetAttributeIndex], IS_LEAF_NODE)));
        }

        double entropy = entropy(targetAttributeValues);
        System.out.println("Total Entropy is : " + entropy);
        Map<Integer, Double> columnIndexToEntropyGainMap = new HashMap<>();
        for(Entry<Integer, List<String>> indexWithColumn : indexWithColumnsMap.entrySet()) {
            int columnIndex = indexWithColumn.getKey();
            List<String> column = indexWithColumn.getValue();
            double totalColumnSize = column.size();
            Map<String, Long> columnValuesWithCount = column.stream()
                                                             .collect(groupingBy(str -> str, Collectors.counting()));
            System.out.println("Calculating Entropy Split for Column with index : " + columnIndex);
            double entropySplit = 0;
            for(Entry<String, Long> columnValWithCount : columnValuesWithCount.entrySet()) {
                String columnValue = columnValWithCount.getKey();
                double count = columnValWithCount.getValue();
                List<String> classAttributeColumnValueThatMatchesAttrColVal = trainingDataSet.stream()
                                                                                             .filter( array -> {
                                                                                                  //We are looking for array value that matches column index
                                                                                                  return array[columnIndex].equalsIgnoreCase(columnValue);
                                                                                              })
                                                                                              .map( array -> array[targetAttributeIndex])
                                                                                              .collect(toList());

                entropySplit += ((count / totalColumnSize) * entropy(classAttributeColumnValueThatMatchesAttrColVal));
                double informationGain = entropy - entropySplit;
                columnIndexToEntropyGainMap.put(columnIndex, informationGain);
            }
            System.out.println("Entropy Split for Index " + columnIndex + " is " + entropySplit);
        }

        columnIndexToEntropyGainMap.forEach(( columnIndex, informationGain) -> {
            System.out.println("Information gain for Column with Index : " + columnIndex + " is " + informationGain);
        });
        int indexOfAttrWithHighestGain = columnIndexToEntropyGainMap.entrySet()
                                                                    .stream()
                                                                    .max(Map.Entry.comparingByValue())
                                                                    .get()
                                                                    .getKey();
        System.out.println("Index with Highest Gain is " + indexOfAttrWithHighestGain);

        List<String> splitAttribute = indexWithColumnsMap.entrySet()
                                                         .stream()
                                                         .filter( entry -> entry.getKey().equals(indexOfAttrWithHighestGain))
                                                         .collect(toList())
                                                         .get(0)
                                                         .getValue();

        return null;
    }


    private static String findMostFreqValue(List<String[]> collection) {
        Map<String, Long> map = collection.stream()
                                                .collect(groupingBy(array -> array[5], Collectors.counting()));

        Optional<Entry<String, Long>> resultOpt = map.entrySet()
                                                           .stream()
                                                           .max(Comparator.comparing(Entry::getValue));
        String result = EMPTY;
        if(resultOpt.isPresent()) {
            result = resultOpt.get().getKey();
        }
        return result;
    }

    private static double entropy(List<String> collection) {
        double total = collection.size();
        Map<String, Long> map = collection.stream()
                                          .collect(groupingBy(str -> str , Collectors.counting()));

        double entropy = 0.0;
        for(Entry<String, Long> entry : map.entrySet()) {
            double probability = Double.valueOf(entry.getValue()) /  total;
            entropy += - (probability * log2(probability));
        }
        return entropy;
    }

    private static double log2(double n) {
        return Math.log(n) / Math.log(2);
    }


    // ========= Classes
    private static class Node<T> {

        private final T content;
        private final boolean isLeaf;

        public Node(
                final T content,
                final boolean isLeaf
        )
        {
            this.content = content;
            this.isLeaf = isLeaf;
        }

        public T getContent() {
            return content;
        }

        public boolean isLeaf() {
            return isLeaf;
        }
    }

    private static class DecisionTree {

        private final List<Node> nodes;

        public DecisionTree(final List<Node> nodes) {
            this.nodes = nodes;
        }


        public List<Node> getNodes() {
            return nodes;
        }
    }

    //========== Utility methods
    private static final String EMPTY = "";

    public static boolean hasText(String text) {
        boolean result = false;
        if(Objects.isNull(text)) {
            result = true;
        }
        for(char character : text.toCharArray()) {
            if(!Character.isWhitespace(character)) {
                result = true;
                break;
            }
        }
        return result;
    }


    private static <T> boolean isNullOrEmpty(Collection<T> collection) {
        boolean result = false;
        if(Objects.isNull(collection) || collection.isEmpty()) {
            result = true;
        }
        return result;
    }

}

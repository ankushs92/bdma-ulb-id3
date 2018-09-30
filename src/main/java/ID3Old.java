import com.google.common.collect.ImmutableSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.*;

public class ID3Old {

//    private static final String COMMA_DELIM = ",";
//    private static int TARGET_ATTRIBUTE_INDEX = 6;
//    private static final String FAILURE = "failure";
//    private static final boolean IS_LEAF_NODE = true;
//    private static final boolean IS_NOT_LEAF_NODE = false;
//    private static final boolean IS_NOT_ROOT_NODE = false;
//    private static final boolean IS_ROOT_NODE = true;
//    private static final String FILE_LOCATION = "/Users/ankushsharma/Downloads/car_eval.csv";
//
//    public static void main(final String[] args) throws IOException {
//
//        List<String[]> trainingDataSet = Files.lines(Paths.get(FILE_LOCATION))
//                .map(str -> str.split(COMMA_DELIM))
//                .collect(toList());
//
//        List<Integer> columnIndexes = new ArrayList<>();
//        int totalColumns = trainingDataSet.get(0).length;
//        for (int i = 0; i < totalColumns ; i++) {
//            if(i != TARGET_ATTRIBUTE_INDEX) {
//                columnIndexes.add(i);
//            }
//        }
//        System.out.println("Input Indexes " + columnIndexes);
//        List<Node> nodes = buildTree(columnIndexes, TARGET_ATTRIBUTE_INDEX, trainingDataSet);
//        printNodes(nodes.get(0));
////        nodes.forEach(node -> {
////            System.out.println("Here");
////            if(node.isRoot) {
////                System.out.println("Root " + node.content);
////            }
////            if(node.isLeaf) {
////                System.out.println("Leaf " + node.content);
////            }
////            else {
////                System.out.println( "Intermediate node " + node.content);
////            }
////
////            System.out.println(" -------------------------------------------------------");
////
//////            System.out.println(node);
//////            System.out.println(" -------------------------------------------------------");
////        });
//
////        printNodes(nodes);
//    }
//
//
//    private static List<Node> buildTree(
//            List<Integer> inputAttributesIndexes,
//            int targetAttributeIndex,
//            List<String[]> trainingDataSet
//    )
//    {
//        if(isNullOrEmpty(trainingDataSet)) {
//            return singletonList(new Node<>(FAILURE, Collections.emptyList(), IS_ROOT_NODE, IS_NOT_LEAF_NODE));
//        }
//
//        if(isNullOrEmpty(inputAttributesIndexes)) {
//            String  mostFreqValue = findMostFreqValue(trainingDataSet, targetAttributeIndex);
//            return singletonList(new Node<>(mostFreqValue, Collections.emptyList(), IS_ROOT_NODE, IS_NOT_LEAF_NODE));
//        }
//
//        int numOfColumns = inputAttributesIndexes.size();
//        Map<Integer, List<String>> indexWithColumnsMap = new HashMap<>();
//        for(int i = 0; i < numOfColumns; i++) {
//            int columnNo = i; //have to do this because of "effectively final" issue with lambda expressions
//            if(columnNo != targetAttributeIndex) {
//                List<String> columnsForI = trainingDataSet.stream().map(array -> array[columnNo]).collect(toList());
//                indexWithColumnsMap.put(columnNo, columnsForI);
//            }
//        }
//        List<String> targetAttributeValues = trainingDataSet.stream().map( array -> array[targetAttributeIndex]).collect(toList());
//        int targetAttributesPossibleValues = targetAttributeValues
//                .stream()
//                .distinct()
//                .collect(toList())
//                .size();
//        if(targetAttributesPossibleValues == 1) {
//            return singletonList(new Node<>(trainingDataSet.get(0)[targetAttributeIndex], Collections.emptyList(), IS_NOT_ROOT_NODE, IS_LEAF_NODE));
//        }
//
//        double entropy = entropy(targetAttributeValues);
//        Map<Integer, Double> columnIndexToEntropyGainMap = new HashMap<>();
//        for(Entry<Integer, List<String>> indexWithColumn : indexWithColumnsMap.entrySet()) {
//            int columnIndex = indexWithColumn.getKey();
//            List<String> column = indexWithColumn.getValue();
//            double totalColumnSize = column.size();
//            Map<String, Long> columnValuesWithCount = column.stream()
//                    .collect(groupingBy(str -> str, counting()));
//            double entropySplit = 0;
//            for(Entry<String, Long> columnValWithCount : columnValuesWithCount.entrySet()) {
//                String columnValue = columnValWithCount.getKey();
//                double count = columnValWithCount.getValue();
//                List<String> classAttributeColumnValueThatMatchesAttrColVal = trainingDataSet.stream()
//                        .filter( array -> {
//                            //We are looking for array value that matches column index
//                            return array[columnIndex].equalsIgnoreCase(columnValue);
//                        })
//                        .map( array -> array[targetAttributeIndex])
//                        .collect(toList());
//
//                entropySplit += ((count / totalColumnSize) * entropy(classAttributeColumnValueThatMatchesAttrColVal));
//                double informationGain = entropy - entropySplit;
//                columnIndexToEntropyGainMap.put(columnIndex, informationGain);
//            }
//            System.out.println("Entropy Split for Index " + columnIndex + " is " + entropySplit);
//        }
//
//        columnIndexToEntropyGainMap.forEach(( columnIndex, informationGain) -> {
//            System.out.println("Information gain for Column with Index : " + columnIndex + " is " + informationGain);
//        });
//        int indexOfAttrWithHighestGain = columnIndexToEntropyGainMap.entrySet()
//                .stream()
//                .max(Map.Entry.comparingByValue())
//                .get()
//                .getKey();
//        System.out.println("Index with Highest Gain is " + indexOfAttrWithHighestGain);
//
//        List<String> splitAttribute = indexWithColumnsMap.entrySet()
//                .stream()
//                .filter( entry -> entry.getKey().equals(indexOfAttrWithHighestGain))
//                .collect(toList())
//                .get(0)
//                .getValue();
//
//
//
//
//
//
//
//
//
//
//        List<Node> rootChildNodes = new LinkedList<>();
//        Node rootNode = new Node<>(indexOfAttrWithHighestGain, rootChildNodes, IS_ROOT_NODE, IS_NOT_LEAF_NODE);
//
//        if(!isNullOrEmpty(splitAttribute)) {
//            List<String> possibleValuesOfSplitAttribute = splitAttribute.stream()
//                    .distinct()
//                    .collect(toList());
//
//            for(String possibleValue : possibleValuesOfSplitAttribute) {
//                System.out.println("Possible " + possibleValue);
//                List<Node> childNodes = new LinkedList<>();
//                Node childNode = new Node<>(possibleValue, childNodes, IS_NOT_ROOT_NODE, IS_NOT_LEAF_NODE);
//                rootChildNodes.add(childNode);
////                System.out.println("One of the Possible value for Index : " + indexOfAttrWithHighestGain + " is " + possibleValue +  " It is not a leaf node, nor a Root node.");
//                List<String[]> subsetTrainingSet = trainingDataSet.stream()
//                        .filter( array -> array[indexOfAttrWithHighestGain].equalsIgnoreCase(possibleValue))
//                        .collect(toList());
//                if(isNullOrEmpty(subsetTrainingSet)) {
//                    rootChildNodes.add(new Node<>(findMostFreqValue(subsetTrainingSet, indexOfAttrWithHighestGain), Collections.emptyList(), IS_NOT_ROOT_NODE, IS_LEAF_NODE));
//                }
//                else {
//                    List<String[]> truncatedTrainingDataSet = trainingDataSet.stream()
//                            .filter( array -> array[indexOfAttrWithHighestGain].equalsIgnoreCase(possibleValue))
//                            .map( array -> {
//                                String[] result = new String[array.length];
//                                result[indexOfAttrWithHighestGain] = EMPTY;
//                                //Removing the 5th row
//                                // Collect all elements with the exception of the 5th
//                                //The new String[]  will have 5 columns, the columns before 5th will remain where they are
//                                //The columns after 5th will be shited to new index = old index - 1
//                                for(int j =0 ; j < array.length ; j++) {
//                                    if(j != indexOfAttrWithHighestGain) {
//                                        if( j < indexOfAttrWithHighestGain ) {
//                                            result[j] = array[j];
//                                        }
//                                        else {
//                                            result[j] = array[j -1];
//                                        }
//                                    }
//                                }
//                                return result;
//                            })
//                            .collect(toList());
//
////                    truncatedTrainingDataSet.forEach( r-> {
////                        System.out.println(Arrays.asList(r));
////                    });
//                    List<Integer> remainingColumnIndexes = inputAttributesIndexes.stream()
//                            .filter( index -> !index.equals(indexOfAttrWithHighestGain))
//                            .collect(toList());
//                    System.out.println("Remaining column indexes " + remainingColumnIndexes);
//                    childNodes.addAll(buildTree(remainingColumnIndexes, TARGET_ATTRIBUTE_INDEX, truncatedTrainingDataSet));
//                }
//            }
//        }
//        //List<String[]> trainingDataSet
//        return Collections.singletonList(rootNode);
//    }
//
////    private static Node buildTreeByNode(
////            Node parentNode,
////            int columnIndex,
////    )
////    {
////
////    }
//
////    private static DecisionTree buildDecisionTree(
////         List<Integer> inputAttributesIndexes,
////         int targetAttributeIndex,
////         List<String[]> trainingDataSet
////    )
////    {
////        if(isNullOrEmpty(trainingDataSet)) {
////            return new DecisionTree(singletonList(new Node<>(FAILURE, Collections.emptyList(), IS_NOT_ROOT_NODE, IS_LEAF_NODE)));
////        }
////
////        if(isNullOrEmpty(inputAttributesIndexes)) {
////            String  mostFreqValue = findMostFreqValue(trainingDataSet, targetAttributeIndex);
////            return new DecisionTree(singletonList(new Node<>(mostFreqValue, Collections.emptyList(), IS_NOT_ROOT_NODE, IS_LEAF_NODE)));
////        }
////
////        int numOfColumns = trainingDataSet.get(0).length;
////        Map<Integer, List<String>> indexWithColumnsMap = new HashMap<>();
////        for(int i = 0; i < numOfColumns; i++) {
////            int columnNo = i; //have to do this because of "effectively final" issue with lambda expressions
////            if(columnNo != targetAttributeIndex) {
////                List<String> columnsForI = trainingDataSet.stream().map(array -> array[columnNo]).collect(toList());
////                indexWithColumnsMap.put(columnNo, columnsForI);
////            }
////        }
////        List<String> targetAttributeValues = trainingDataSet.stream().map( array -> array[targetAttributeIndex]).collect(toList());
////        int targetAttributesPossibleValues = targetAttributeValues
////                                                                  .stream()
////                                                                  .distinct()
////                                                                  .collect(toList())
////                                                                  .size();
////
////        if(targetAttributesPossibleValues == 1) {
////            return new DecisionTree(singletonList(new Node<>(trainingDataSet.get(0)[targetAttributeIndex], Collections.emptyList(), IS_NOT_ROOT_NODE, IS_LEAF_NODE)));
////        }
////
////        double entropy = entropy(targetAttributeValues);
////        System.out.println("Total Entropy is : " + entropy);
////        Map<Integer, Double> columnIndexToEntropyGainMap = new HashMap<>();
////        for(Entry<Integer, List<String>> indexWithColumn : indexWithColumnsMap.entrySet()) {
////            int columnIndex = indexWithColumn.getKey();
////            List<String> column = indexWithColumn.getValue();
////            double totalColumnSize = column.size();
////            Map<String, Long> columnValuesWithCount = column.stream()
////                                                             .collect(groupingBy(str -> str, Collectors.counting()));
////            System.out.println("Calculating Entropy Split for Column with index : " + columnIndex);
////            double entropySplit = 0;
////            for(Entry<String, Long> columnValWithCount : columnValuesWithCount.entrySet()) {
////                String columnValue = columnValWithCount.getKey();
////                double count = columnValWithCount.getValue();
////                List<String> classAttributeColumnValueThatMatchesAttrColVal = trainingDataSet.stream()
////                                                                                             .filter( array -> {
////                                                                                                  //We are looking for array value that matches column index
////                                                                                                  return array[columnIndex].equalsIgnoreCase(columnValue);
////                                                                                              })
////                                                                                              .map( array -> array[targetAttributeIndex])
////                                                                                              .collect(toList());
////
////                entropySplit += ((count / totalColumnSize) * entropy(classAttributeColumnValueThatMatchesAttrColVal));
////                double informationGain = entropy - entropySplit;
////                columnIndexToEntropyGainMap.put(columnIndex, informationGain);
////            }
////            System.out.println("Entropy Split for Index " + columnIndex + " is " + entropySplit);
////        }
////
////        columnIndexToEntropyGainMap.forEach(( columnIndex, informationGain) -> {
////            System.out.println("Information gain for Column with Index : " + columnIndex + " is " + informationGain);
////        });
////        int indexOfAttrWithHighestGain = columnIndexToEntropyGainMap.entrySet()
////                                                                    .stream()
////                                                                    .max(Map.Entry.comparingByValue())
////                                                                    .get()
////                                                                    .getKey();
////        System.out.println("Index with Highest Gain is " + indexOfAttrWithHighestGain);
////
////        List<String> splitAttribute = indexWithColumnsMap.entrySet()
////                                                         .stream()
////                                                         .filter( entry -> entry.getKey().equals(indexOfAttrWithHighestGain))
////                                                         .collect(toList())
////                                                         .get(0)
////                                                         .getValue();
////
////        List<Node> childNodes = new LinkedList<>();
////        Node rootNode = new Node<>(indexOfAttrWithHighestGain, childNodes, IS_ROOT_NODE, IS_NOT_LEAF_NODE);
////
////        if(!isNullOrEmpty(splitAttribute)) {
////            List<String> possibleValues = splitAttribute.stream()
////                                                        .distinct()
////                                                        .collect(toList());
////            for(String possibleValue : possibleValues) {
////                List<String[]> subsetTrainingSet = trainingDataSet.stream()
////                                                                  .filter( array -> array[indexOfAttrWithHighestGain].equalsIgnoreCase(possibleValue))
////                                                                  .collect(toList());
////
////                if(isNullOrEmpty(subsetTrainingSet)) {
////                    childNodes.add(new Node<>(findMostFreqValue(subsetTrainingSet, indexOfAttrWithHighestGain), Collections.emptyList(), IS_NOT_ROOT_NODE, IS_LEAF_NODE));
////                }
////                else {
////                    List<String[]> truncatedTrainingDataSet = trainingDataSet.stream()
////                                                                             .map( array -> {
////                                                                               String[] result = new String[array.length - 1];
////                                                                               //Removing the 5th row
////                                                                               // Collect all elements with the exception of the 5th
////                                                                               //The new String[]  will have 5 columns, the columns before 5th will remain where they are
////                                                                               //The columns after 5th will be shited to new index = old index - 1
////                                                                               for(int j =0 ; j < array.length - 1; j++) {
////                                                                                   if(j != indexOfAttrWithHighestGain) {
////                                                                                       if( j < indexOfAttrWithHighestGain ) {
////                                                                                           result[j] = array[j];
////                                                                                       }
////                                                                                       else {
////                                                                                           result[j] = array[j -1];
////                                                                                       }
////                                                                                   }
////                                                                               }
////                                                                               return result;
////                                                                             })
////                                                                             .collect(toList());
////                    List<Integer> remainingColumnIndexes = inputAttributesIndexes.stream()
////                                                                                 .filter( index -> !index.equals(indexOfAttrWithHighestGain))
////                                                                                 .collect(toList());
////
////                    buildDecisionTree(remainingColumnIndexes, TARGET_ATTRIBUTE_INDEX - 1, truncatedTrainingDataSet);
////                }
////            }
////        }
////        return null;
//////        return new DecisionTree(Collections.singletonList(node));
////    }
//
//    private static void printNodes(Node node) {
//        if(node.isLeaf) {
//            System.out.println("Leaf Node " + node.information);
//        }
//        for(Object nodes : node.childNodes) {
//            printNodes((Node) nodes);
//        }
//    }
//
//    private static String findMostFreqValue(List<String[]> collection, int columnIndex) {
//        Map<String, Long> map = collection.stream()
//                .collect(groupingBy(array -> array[columnIndex], counting()));
//
//        Optional<Entry<String, Long>> resultOpt = map.entrySet()
//                .stream()
//                .max(Comparator.comparing(Entry::getValue));
//        String result = EMPTY;
//        if(resultOpt.isPresent()) {
//            result = resultOpt.get().getKey();
//        }
//        return result;
//    }
//
//    private static double entropy(List<String> collection) {
//        double total = collection.size();
//        Map<String, Long> map = collection.stream()
//                .collect(groupingBy(str -> str , counting()));
//
//        double entropy = 0.0;
//        for(Entry<String, Long> entry : map.entrySet()) {
//            double probability = Double.valueOf(entry.getValue()) /  total;
//            entropy += - (probability * log2(probability));
//        }
//        return entropy;
//    }
//
//    private static double log2(double n) {
//        return Math.log(n) / Math.log(2);
//    }
//
//
//    // ========= Classes
//    private static class Node<T> {
//
//        private final T information;
//        private final List<Node> childNodes;
//        private final boolean isLeaf;
//        private final boolean isRoot;
//
//        public Node(
//                final T information,
//                final List<Node> childNodes,
//                final boolean isLeaf,
//                final boolean isRoot
//        )
//        {
//            this.information = information;
//            this.childNodes = childNodes;
//            this.isLeaf = isLeaf;
//            this.isRoot = isRoot;
//        }
//
//
//        @Override
//        public String toString() {
//            return "Node{" +
//                    "information=" + information +
//                    ", childNodes=" + childNodes +
//                    ", isLeaf=" + isLeaf +
//                    ", isRoot=" + isRoot +
//                    '}';
//        }
//    }
//
//    private static class DecisionTree {
//
//        private final List<Node> nodes;
//
//        public DecisionTree(final List<Node> nodes) {
//            this.nodes = nodes;
//        }
//    }
//
//    //========== Utility methods
//    private static final String EMPTY = "";
//
//
//    private static <T> boolean isNullOrEmpty(Collection<T> collection) {
//        boolean result = false;
//        if(Objects.isNull(collection) || collection.isEmpty()) {
//            result = true;
//        }
//        return result;
//    }
//
//    //                    List<String[]> truncatedTrainingDataSet = trainingDataSet.stream()
////                                                                             .map( array -> {
////                                                                                String[] result = new String[array.length - 1];
////                                                                                //Removing the 5th row
////                                                                                // Collect all elements with the exception of the 5th
////                                                                                //The new String[]  will have 5 columns, the columns before 5th will remain where they are
////                                                                                //The columns after 5th will be shited to new index = old index - 1
////                                                                                for(int j =0 ; j < array.length - 1; j++) {
////                                                                                    if(j != indexOfAttrWithHighestGain) {
////                                                                                        if( j < indexOfAttrWithHighestGain ) {
////                                                                                            result[j] = array[j];
////                                                                                        }
////                                                                                        else {
////                                                                                            result[j] = array[j -1];
////                                                                                        }
////                                                                                    }
////                                                                                }
////                                                                                return result;
////                                                                             })
////                                                                            .collect(toList());
//

}

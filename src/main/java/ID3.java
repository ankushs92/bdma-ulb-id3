import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.*;

public class ID3 {

    private static final String COMMA_DELIM = ",";
    private static final String FAILURE = "failure";
    private static final boolean IS_LEAF_NODE = true;
    private static final boolean IS_NOT_LEAF_NODE = false;
    private static final boolean IS_NOT_ROOT_NODE = false;
    private static final boolean IS_ROOT_NODE = true;
    private static final Map<Integer, String> CATEGORIES = new HashMap<>();

    public static void main(String[] args) throws IOException {
        if(isNullOrEmpty(Arrays.asList(args))) {
            throw new RuntimeException("Please pass in the following Values : Car Evaluation Data Set (1st parameter), Index of Target attribute(2nd parameter)");
        }

        String fileLocation = args[0];
        if(!hasText(fileLocation)) {
            throw new RuntimeException("Please pass the file location!");
        }

        int targetAttributeIndex = Integer.valueOf(args[1]);

        List<String[]> trainingDataSetWithHeader = Files.lines(Paths.get(fileLocation))
                                                        .map(str -> str.split(COMMA_DELIM))
                                                        .collect(toList());

        String[] headers = trainingDataSetWithHeader.stream().findFirst().get();
        for(int j = 0 ; j < headers.length ; j++) {
            CATEGORIES.put(j, headers[j]);
        }
        System.out.println(CATEGORIES);

        List<String[]> trainingDataSet = trainingDataSetWithHeader.stream()
                                                                .skip(1)
                                                                .collect(toList());
        List<Integer> columnIndexes = new ArrayList<>();
        int totalColumns = trainingDataSetWithHeader.get(0).length;
        for (int i = 0; i < totalColumns ; i++) {
            if(i != targetAttributeIndex) {
                columnIndexes.add(i);
            }
        }
        Node node = buildTree(columnIndexes, targetAttributeIndex, trainingDataSet, "Root ");
        printNodes(node, "");
    }
    private static void printNodes(Node node, String spacing) {
        if(node.isLeaf) {
            String label;
            try {
                label = node.information;
            }
            catch (Exception ex) {
                label = node.information;
            }
            System.out.println(spacing + label);
        }

        for(Node childNode : node.childNodes) {
            String label = childNode.name;
            System.out.println(spacing + "---> " + label);
            printNodes(childNode, spacing + "|    ");
        }
    }

    private static Node buildTree(
            List<Integer> inputAttributesIndexes,
            int targetAttributeIndex,
            List<String[]> trainingDataSet,
            String nodeName
    )
    {
        if(isNullOrEmpty(trainingDataSet)) {
            return new Node(FAILURE, FAILURE, emptyList(), IS_LEAF_NODE, IS_NOT_ROOT_NODE);
        }

        if(isNullOrEmpty(inputAttributesIndexes)) {
            String mostFreqValue = findMostFreqValue(trainingDataSet, targetAttributeIndex);
            return new Node(nodeName , CATEGORIES.get(targetAttributeIndex) + ": " + mostFreqValue, emptyList(), IS_LEAF_NODE, IS_NOT_ROOT_NODE);
        }

        Map<Integer, List<String>> indexWithColumnsMap = new HashMap<>();
        for(Integer columnIndex : inputAttributesIndexes) {
            if(!columnIndex.equals(targetAttributeIndex)) {
                List<String> columnsForI = trainingDataSet.stream().map(array -> array[columnIndex]).collect(toList());
                indexWithColumnsMap.put(columnIndex, columnsForI);
            }

        }
        List<String> targetAttributeValues = trainingDataSet.stream().map( array -> array[targetAttributeIndex]).collect(toList());
        List<String> targetAttributesDistinctValues = targetAttributeValues.stream().distinct().collect(toList());
        int targetAttributesPossibleValues = targetAttributesDistinctValues.size();


        if(targetAttributesPossibleValues == 1) {
            String leafValue = targetAttributesDistinctValues.get(0);
//            Node leafNode = new Node(CATEGORIES.get(targetAttributeIndex), targetAttributesDistinctValues.get(0), emptyList(), IS_LEAF_NODE, IS_NOT_ROOT_NODE);
//            return new Node(nodeName, leafValue, singletonList(leafNode), IS_NOT_LEAF_NODE, IS_ROOT_NODE);
            return new Node(nodeName, CATEGORIES.get(targetAttributeIndex) + ": " + leafValue, Collections.emptyList(), IS_LEAF_NODE, IS_NOT_ROOT_NODE);
        }

        double entropy = entropy(targetAttributeValues);
        Map<Integer, Double> columnIndexToEntropyGainMap = new HashMap<>();
        for(Entry<Integer, List<String>> indexWithColumn : indexWithColumnsMap.entrySet()) {
            int columnIndex = indexWithColumn.getKey();
            List<String> column = indexWithColumn.getValue();
            double totalColumnSize = column.size();
            Map<String, Long> columnValuesWithCount = column.stream()
                                                            .collect(groupingBy(str -> str, counting()));
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
//            System.out.println("Entropy Split for Index " + columnIndex + " is " + entropySplit);
        }

        columnIndexToEntropyGainMap.forEach(( columnIndex, informationGain) -> {
            System.out.println("Information gain for Column with Index : " + columnIndex + " is " + informationGain);
        });
        int indexOfAttrWithHighestGain = columnIndexToEntropyGainMap.entrySet()
                                                                    .stream()
                                                                    .max(Map.Entry.comparingByValue())
                                                                    .get()
                                                                    .getKey();

        List<String> splitAttribute = indexWithColumnsMap.entrySet()
                                                         .stream()
                                                         .filter( entry -> entry.getKey().equals(indexOfAttrWithHighestGain))
                                                         .collect(toList())
                                                         .get(0)
                                                         .getValue();

        List<Node> childNodes = new LinkedList<>();
        Node node = new Node(nodeName, String.valueOf(indexOfAttrWithHighestGain), childNodes, IS_NOT_LEAF_NODE, IS_ROOT_NODE);

        if(!isNullOrEmpty(splitAttribute)) {
            List<String> possibleValuesOfSplitAttribute = splitAttribute.stream()
                                                                        .distinct()
                                                                        .collect(toList());

            for(String possibleValue : possibleValuesOfSplitAttribute) {
                System.out.println("Possible Value " + possibleValue);

                List<String[]> truncatedTrainingDataSet = trainingDataSet.stream()
                                                                        .filter( array -> array[indexOfAttrWithHighestGain].equalsIgnoreCase(possibleValue))
                                                                        .collect(toList());

                List<Integer> remainingColumnIndexes = inputAttributesIndexes.stream()
                                                                             .filter( index -> !index.equals(indexOfAttrWithHighestGain))
                                                                             .collect(toList());

                System.out.println("Remaining column Indexes " + remainingColumnIndexes);
                System.out.println("targetAttributeIndex " + targetAttributeIndex);
                Node childNode = buildTree(remainingColumnIndexes, targetAttributeIndex, truncatedTrainingDataSet, CATEGORIES.get(indexOfAttrWithHighestGain) + "---> " + possibleValue);
                childNodes.add(childNode);
            }
        }
//        Node node = new Node(nodeName, String.valueOf(indexOfAttrWithHighestGain), childNodes, IS_NOT_LEAF_NODE, IS_ROOT_NODE);

        return node;
    }


    private static String findMostFreqValue(List<String[]> collection, int columnIndex) {
        Map<String, Long> map = collection.stream()
                                                .collect(groupingBy(array -> array[columnIndex], counting()));

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
                                          .collect(groupingBy(str -> str , counting()));

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
    private static class Node {

        private final String name;
        private final String information;
        private final List<Node> childNodes;
        private final boolean isLeaf;
        private final boolean isRoot;

        public Node(
                final String name,
                final String information,
                final List<Node> childNodes,
                final boolean isLeaf,
                final boolean isRoot
        )
        {
            this.name = name;
            this.information = information;
            this.childNodes = childNodes;
            this.isLeaf = isLeaf;
            this.isRoot = isRoot;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "name='" + name + '\'' +
                    ", information='" + information + '\'' +
                    ", childNodes=" + childNodes +
                    ", isLeaf=" + isLeaf +
                    ", isRoot=" + isRoot +
                    '}';
        }
    }

    //========== Utility methods
    private static final String EMPTY = "";

    private static boolean hasText(String text){
        if(Objects.isNull(text)){
            return false;
        }
        for(Character ch : text.toCharArray()){
            if(!Character.isWhitespace(ch)){
                return true;
            }
        }
        return false;
    }



    private static <T> boolean isNullOrEmpty(Collection<T> collection) {
        boolean result = false;
        if(Objects.isNull(collection) || collection.isEmpty()) {
            result = true;
        }
        return result;
    }


}

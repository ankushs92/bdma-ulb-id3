import com.google.common.collect.ImmutableSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ID3 {
//    buying       v-high, high, med, low
//    maint        v-high, high, med, low
//    doors        2, 3, 4, 5-more
//    persons      2, 4, more
//    lug_boot     small, med, big
//    safety       low, med, high
    private static final String TARGET_ATTRIBUTE = "car_eval";
    private static final String FAILURE = "failure";
    private static final boolean IS_LEAF_NODE = true;
    private static final Set<String> INPUT_ATTRIBUTES = ImmutableSet.of(
            "buying", "maint", "doors", "persons", "lug_boot", "safety"
    );
    private static final String FILE_LOCATION= "/Users/ankushsharma/Downloads/car_eval.csv";

    public static void main(final String[] args) throws IOException {

        final List<String[]> trainingDataSet = Files.lines(Paths.get(FILE_LOCATION))
                                               .map( str -> str.split(","))
                                               .collect(Collectors.toList());

        trainingDataSet.forEach( d -> {
            System.out.println(d);
        });


    }

    public DecisionTree computeBestPossibleDecisionTree(
        final Set<String> inputAttributes,
        final String targetAttribute,
        final List<String[]> trainingDataSet
    )
    {
        if(CollectionUtil.isNullOrEmpty(trainingDataSet)) {
            return new DecisionTree(ImmutableSet.of(new Node<>(FAILURE, IS_LEAF_NODE)));
        }

        if(CollectionUtil.isNullOrEmpty(inputAttributes)) {

//            final String mostFreqClassValue = trainingDataSet
//                                                        .stream()
//                                                        .max
//                        ;
        }
        return null;
    }



}

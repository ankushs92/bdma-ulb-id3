import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
    List<Integer> list;

    public static void main(String[] args) {

        List<String> list = Arrays.asList("Sunny","Sunny","Sunny","Overcast","Overcast","rainy");
        List<String> list1 = list.stream().distinct().collect(Collectors.toList());
        System.out.println(list);
        System.out.println(list1);

        List<Integer> intList = Arrays.asList(1,2,3,4,5,6);
        System.out.println(intList);
        List<Integer> intList1 = intList.stream().filter( i -> i% 2 ==0).collect(Collectors.toList());
        System.out.println(intList1);

    }

    //                                                                        .map( array -> {
//                                                                            String[] result = new String[array.length];
//                                                                            result[indexOfAttrWithHighestGain] = EMPTY;
//                                                                            //Removing the 5th row
//                                                                            // Collect all elements with the exception of the 5th
//                                                                            //The new String[]  will have 5 columns, the columns before 5th will remain where they are
//                                                                            //The columns after 5th will be shited to new index = old index - 1
//                                                                            for(int j =0 ; j < array.length ; j++) {
//                                                                                if(j != indexOfAttrWithHighestGain) {
//                                                                                    if( j < indexOfAttrWithHighestGain ) {
//                                                                                        result[j] = array[j];
//                                                                                    }
//                                                                                    else {
//                                                                                        result[j] = array[j -1];
//                                                                                    }
//                                                                                }
//                                                                            }
//                                                                            return result;
//                                                                        })

}

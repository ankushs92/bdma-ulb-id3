import java.util.*;
import java.util.stream.Collectors;

public class CollectionUtil {

    public static <T> boolean isNullOrEmpty(final Collection<T> collection) {
        boolean result = false;
        if(Objects.isNull(collection) || collection.isEmpty()) {
            result = true;
        }
        return result;
    }

    public static void main(String[] args) {
        String[] array = (String[]) Arrays.asList("a", "a", "b", "c", "c", "d").toArray();
        String[] array1 = (String[]) Arrays.asList("a", "a", "b", "c", "c", "d").toArray();
        String[] array2 = (String[]) Arrays.asList("a", "a", "b", "c", "c", "f").toArray();
        String[] array3 = (String[]) Arrays.asList("a", "a", "b", "c", "c", "f").toArray();
        List<String[]> list = Arrays.asList(array, array1, array2, array3);
        Map<Integer, List<String[]>> map = list.stream()
            .collect(Collectors.groupingBy((String[] s) -> s[5]));


    }

}

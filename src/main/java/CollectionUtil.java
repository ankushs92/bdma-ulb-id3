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
        List<String> list = Arrays.asList("a","b","c","a");
        Map<Integer, List<String>> m = list.stream().collect(Collectors.groupingBy(String::length));
    }

}

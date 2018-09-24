import java.util.Objects;

public class Strings {

    public static final String EMPTY = "";

    public static boolean hasText(final String text) {
        boolean result = false;
        if(Objects.isNull(text)) {
            result = true;
        }
        for(final char character : text.toCharArray()) {
            if(!Character.isWhitespace(character)) {
                result = true;
                break;
            }
        }
        return result;
    }
}

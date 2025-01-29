package example.utils.string;

public class StringUtils {
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
}

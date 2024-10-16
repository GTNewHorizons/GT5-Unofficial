package gregtech.api.util;

import java.util.Arrays;

public class StringUtils {

    public static String getRepetitionOf(char c, int length) {
        final char[] chars = new char[length];
        Arrays.fill(chars, c);
        return new String(chars);
    }

}

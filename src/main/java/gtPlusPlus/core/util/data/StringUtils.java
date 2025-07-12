package gtPlusPlus.core.util.data;

import java.util.ArrayList;

import gregtech.api.util.CustomGlyphs;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.Utils;

public class StringUtils {

    // spotless:off
    private static final char[] SUPER_SCRIPTS = new char[]{
        CustomGlyphs.SUPERSCRIPT0.charAt(0),
        CustomGlyphs.SUPERSCRIPT1.charAt(0),
        CustomGlyphs.SUPERSCRIPT2.charAt(0),
        CustomGlyphs.SUPERSCRIPT3.charAt(0),
        CustomGlyphs.SUPERSCRIPT4.charAt(0),
        CustomGlyphs.SUPERSCRIPT5.charAt(0),
        CustomGlyphs.SUPERSCRIPT6.charAt(0),
        CustomGlyphs.SUPERSCRIPT7.charAt(0),
        CustomGlyphs.SUPERSCRIPT8.charAt(0),
        CustomGlyphs.SUPERSCRIPT9.charAt(0)};
    private static final char[] SUB_SCRIPTS = new char[]{
        CustomGlyphs.SUBSCRIPT0.charAt(0),
        '\u2081',
        '\u2082',
        '\u2083',
        '\u2084',
        '\u2085',
        '\u2086',
        '\u2087',
        '\u2088',
        '\u2089'
    };
    // spotless:on

    public static String superscript(String input) {
        final char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c >= '0' && c <= '9') {
                chars[i] = SUPER_SCRIPTS[c - '0'];
            }
        }
        return new String(chars);
    }

    public static String subscript(String input) {
        final char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c >= '0' && c <= '9') {
                chars[i] = SUB_SCRIPTS[c - '0'];
            } else if (c == '?') {
                chars[i] = CustomGlyphs.SUBSCRIPT_QUESTION_MARK.charAt(0);
            }
        }
        return new String(chars);
    }

    public static String firstLetterCaps(String string) {
        if (string == null) {
            return null;
        }
        return string.substring(0, 1)
            .toUpperCase() + string.substring(1);
    }

    /**
     * Is this a special regex character for delimination? (.$|()[]{}^?*+\\)
     *
     * @param aChar - The char to test
     * @return - Is this a special character?
     */
    public static boolean isSpecialCharacter(char aChar) {
        return "\".$|()[]{}^?*+\\".indexOf(aChar) >= 0;
    }

    public static boolean isEscaped(String aString) {
        return aString.charAt(0) == '\\';
    }

    public static String splitAndUppercase(String aInput, String aDelim) {

        if (!isEscaped(aDelim)) {
            boolean isSpecial = false;
            for (int o = 0; o < aInput.length(); o++) {
                if (isSpecialCharacter(aInput.charAt(o))) {
                    isSpecial = true;
                    break;
                }
            }
            if (isSpecial) {
                aDelim = "\\" + aDelim;
            }
        }

        Logger.INFO("Splitting " + aInput);
        String[] aSplit = aInput.split(aDelim);
        Logger.INFO(aSplit.length + " parts.");
        if (aSplit.length == 0) {
            return aInput;
        } else {
            ArrayList<String> aTemp = new ArrayList<>();
            for (String s : aSplit) {
                Logger.INFO("Found: " + s);
                s = s.replace(".", "");
                s = Utils.sanitizeString(s);
                s = firstLetterCaps(s);
                Logger.INFO("Formatted & Captilized: " + s);
                aTemp.add(s);
            }
            Logger.INFO("Rebuilding");
            StringBuilder aReturn = new StringBuilder();
            for (String s : aTemp) {
                aReturn.append(s);
                Logger.INFO("Step: " + aReturn);
            }
            return aReturn.toString();
        }
    }

    public static long uppercaseCount(String input) {
        final char[] chars = input.toCharArray();
        int count = 0;
        for (char c : chars) {
            if (Character.isUpperCase(c)) {
                count++;
            }
        }
        return count;
    }
}

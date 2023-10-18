package gtPlusPlus.core.util.data;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.Utils;

public class StringUtils {

    public static String superscript(String str) {
        str = str.replaceAll("0", "\u2070");
        str = str.replaceAll("1", "\u00B9");
        str = str.replaceAll("2", "\u00B2");
        str = str.replaceAll("3", "\u00B3");
        str = str.replaceAll("4", "\u2074");
        str = str.replaceAll("5", "\u2075");
        str = str.replaceAll("6", "\u2076");
        str = str.replaceAll("7", "\u2077");
        str = str.replaceAll("8", "\u2078");
        str = str.replaceAll("9", "\u2079");
        return str;
    }

    public static String subscript(String str) {
        str = str.replaceAll("0", "\u2080");
        str = str.replaceAll("1", "\u2081");
        str = str.replaceAll("2", "\u2082");
        str = str.replaceAll("3", "\u2083");
        str = str.replaceAll("4", "\u2084");
        str = str.replaceAll("5", "\u2085");
        str = str.replaceAll("6", "\u2086");
        str = str.replaceAll("7", "\u2087");
        str = str.replaceAll("8", "\u2088");
        str = str.replaceAll("9", "\u2089");
        return str;
    }

    public static boolean containsSuperOrSubScript(final String s) {
        if (s.contains(StringUtils.superscript("0"))) {
            return true;
        } else if (s.contains(StringUtils.superscript("1"))) {
            return true;
        } else if (s.contains(StringUtils.superscript("2"))) {
            return true;
        } else if (s.contains(StringUtils.superscript("3"))) {
            return true;
        } else if (s.contains(StringUtils.superscript("4"))) {
            return true;
        } else if (s.contains(StringUtils.superscript("5"))) {
            return true;
        } else if (s.contains(StringUtils.superscript("6"))) {
            return true;
        } else if (s.contains(StringUtils.superscript("7"))) {
            return true;
        } else if (s.contains(StringUtils.superscript("8"))) {
            return true;
        } else if (s.contains(StringUtils.superscript("9"))) {
            return true;
        }
        if (s.contains(StringUtils.subscript("0"))) {
            return true;
        } else if (s.contains(StringUtils.subscript("1"))) {
            return true;
        } else if (s.contains(StringUtils.subscript("2"))) {
            return true;
        } else if (s.contains(StringUtils.subscript("3"))) {
            return true;
        } else if (s.contains(StringUtils.subscript("4"))) {
            return true;
        } else if (s.contains(StringUtils.subscript("5"))) {
            return true;
        } else if (s.contains(StringUtils.subscript("6"))) {
            return true;
        } else if (s.contains(StringUtils.subscript("7"))) {
            return true;
        } else if (s.contains(StringUtils.subscript("8"))) {
            return true;
        } else if (s.contains(StringUtils.subscript("9"))) {
            return true;
        }
        return false;
    }

    public static String firstLetterCaps(String data) {
        String firstLetter = data.substring(0, 1).toUpperCase();
        String restLetters = data.substring(1).toLowerCase();
        return firstLetter + restLetters;
    }

    public static <V> String getDataStringFromArray(V[] parameterTypes) {
        if (parameterTypes == null || parameterTypes.length == 0) {
            return "empty/null";
        } else {
            StringBuilder aData = new StringBuilder();
            for (V y : parameterTypes) {
                if (y != null) {
                    aData.append(", ").append(y);
                }
            }
            return aData.toString();
        }
    }

    /**
     * Is this a special regex character for delimination? (.$|()[]{}^?*+\\)
     * 
     * @param aChar - The char to test
     * @return - Is this a special character?
     */
    public static boolean isSpecialCharacter(char aChar) {
        return aChar == '"' || aChar == '.'
                || aChar == '$'
                || aChar == '|'
                || aChar == '('
                || aChar == ')'
                || aChar == '['
                || aChar == ']'
                || aChar == '{'
                || aChar == '}'
                || aChar == '^'
                || aChar == '?'
                || aChar == '*'
                || aChar == '+'
                || aChar == '\\';
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
                }
            }
            if (isSpecial) {
                aDelim = "\\" + aDelim;
            }
        }

        Logger.INFO("Splitting " + aInput);
        String[] aSplit = aInput.split(aDelim);
        Logger.INFO("Split into " + aSplit == null ? "" + 0 : aSplit.length + " parts.");
        if (aSplit == null || aSplit.length == 0) {
            return aInput;
        } else {
            AutoMap<String> aTemp = new AutoMap<>();
            for (String s : aSplit) {
                Logger.INFO("Found: " + s);
                s = s.replace(".", "");
                s = Utils.sanitizeString(s);
                s = firstLetterCaps(s);
                Logger.INFO("Formatted & Captilized: " + s);
                aTemp.put(s);
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

    public static long uppercaseCount(String aString) {
        return aString.chars().filter(Character::isUpperCase).count();
    }
}

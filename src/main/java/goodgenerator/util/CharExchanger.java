package goodgenerator.util;

import net.minecraft.util.EnumChatFormatting;

public class CharExchanger {

    public static final String[] tierName = new String[] {
            EnumChatFormatting.RED + "ULV" + EnumChatFormatting.RESET,
            EnumChatFormatting.GRAY + "LV" + EnumChatFormatting.RESET,
            EnumChatFormatting.AQUA + "MV" + EnumChatFormatting.RESET,
            EnumChatFormatting.GOLD + "HV" + EnumChatFormatting.RESET,
            EnumChatFormatting.DARK_PURPLE + "EV" + EnumChatFormatting.RESET,
            EnumChatFormatting.DARK_BLUE + "IV" + EnumChatFormatting.RESET,
            EnumChatFormatting.LIGHT_PURPLE + "LuV" + EnumChatFormatting.RESET,
            EnumChatFormatting.WHITE + "ZPM" + EnumChatFormatting.RESET,
            EnumChatFormatting.DARK_AQUA + "UV" + EnumChatFormatting.RESET,
            EnumChatFormatting.DARK_RED + "UHV" + EnumChatFormatting.RESET,
            EnumChatFormatting.GREEN + "UEV" + EnumChatFormatting.RESET,
    };

    public static char shifter(int unicode){
        return (char)unicode;
    }

    public static boolean isValidCompareExpressChar(char c) {
        return Character.isDigit(c) || c == '<' || c == '>' || c == '=' || c == '!';
    }

    public static boolean isValidCompareExpress(String exp) {
        if (exp.length() < 2) return false;
        for (char c: exp.toCharArray())
            if (!isValidCompareExpressChar(c)) return false;
        char c1 = exp.charAt(0), c2 = exp.charAt(1);
        String subExp = "" + c1;
        if (!Character.isDigit(c2)) subExp = subExp + c2;
        switch (subExp) {
            case ">":
            case "<":
            case ">=":
            case "<=":
            case "==":
            case "!=":
                break;
            default: return false;
        }
        if (exp.length() == subExp.length()) return false;
        for (int i = subExp.length(); i < exp.length(); i ++) {
            if (!Character.isDigit(exp.charAt(i))) return false;
        }
        return true;
    }

    /**
     *  ">" : 1 <BR>
     *  "<" : 2 <BR>
     *  "==" : 13 <BR>
     *  "!=" : 14 <BR>
     *  ">=" : 11 <BR>
     *  "<=" : 12 <BR>
     *  INVALID : -1
     */
    public static int getOperator(String exp){
        char c1, c2;
        int ret;
        if (exp.length() < 1) return -1;
        c1 = exp.charAt(0);
        switch (c1) {
            case '>': ret = 1;break;
            case '<': ret = 2;break;
            case '=': ret = 3;break;
            case '!': ret = 4;break;
            default: return -1;
        }
        if (exp.length() > 1) c2 = exp.charAt(1);
        else return ret;
        if (c2 == '=') {
            ret += 10;
        }
        return ret;
    }

    public static boolean compareExpression(String exp, int num) {
        int op = getOperator(exp);
        String NumExp = exp;
        String[] opChar = new String[]{">", "<", "=", "!"};
        if (op == -1) throw new IllegalArgumentException();
        for (String re: opChar) NumExp = NumExp.replace(re, "");
        long num2 = 0;
        for (char c: NumExp.toCharArray()) {
            num2 *=10;
            num2 += c - '0';
        }
        switch (op) {
            case 1: return num > num2;
            case 2: return num < num2;
            case 13: return num == num2;
            case 14: return num != num2;
            case 11: return num >= num2;
            case 12: return num <= num2;
            default: return false;
        }
    }

    public static String formatNumber(String exp) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < exp.length(); ++i) {
            if (Character.isDigit(exp.charAt(i))) {
                int cnt = 0, prt = i;
                while (i < exp.length() && Character.isDigit(exp.charAt(i))) {
                    i ++;
                    cnt ++;
                }
                while (prt < exp.length() && Character.isDigit(exp.charAt(prt))) {
                    sb.append(exp.charAt(prt));
                    prt ++;
                    cnt --;
                    if (cnt % 3 == 0 && cnt != 0) sb.append(",");
                }
            }
            if (i < exp.length()) sb.append(exp.charAt(i));
        }
        return sb.toString();
    }

    public static String[] genString(String content, int len) {
        String[] ret = new String[len];
        while (len > 0) {
            len --;
            ret[len] = content;
        }
        return ret;
    }
}

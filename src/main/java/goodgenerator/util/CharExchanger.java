package goodgenerator.util;

import net.minecraft.util.EnumChatFormatting;

public class CharExchanger {

    public static final String[] tierName = new String[] { EnumChatFormatting.RED + "ULV" + EnumChatFormatting.RESET,
        EnumChatFormatting.GRAY + "LV" + EnumChatFormatting.RESET,
        EnumChatFormatting.AQUA + "MV" + EnumChatFormatting.RESET,
        EnumChatFormatting.GOLD + "HV" + EnumChatFormatting.RESET,
        EnumChatFormatting.DARK_PURPLE + "EV" + EnumChatFormatting.RESET,
        EnumChatFormatting.DARK_BLUE + "IV" + EnumChatFormatting.RESET,
        EnumChatFormatting.LIGHT_PURPLE + "LuV" + EnumChatFormatting.RESET,
        EnumChatFormatting.WHITE + "ZPM" + EnumChatFormatting.RESET,
        EnumChatFormatting.DARK_AQUA + "UV" + EnumChatFormatting.RESET,
        EnumChatFormatting.DARK_RED + "UHV" + EnumChatFormatting.RESET,
        EnumChatFormatting.GREEN + "UEV" + EnumChatFormatting.RESET, };

    public static char shifter(int unicode) {
        return (char) unicode;
    }

    public static String formatNumber(String exp) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < exp.length(); ++i) {
            if (Character.isDigit(exp.charAt(i))) {
                int cnt = 0, prt = i;
                while (i < exp.length() && Character.isDigit(exp.charAt(i))) {
                    i++;
                    cnt++;
                }
                while (prt < exp.length() && Character.isDigit(exp.charAt(prt))) {
                    sb.append(exp.charAt(prt));
                    prt++;
                    cnt--;
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
            len--;
            ret[len] = content;
        }
        return ret;
    }
}

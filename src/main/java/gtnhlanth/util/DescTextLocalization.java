package gtnhlanth.util;

import net.minecraft.util.StatCollector;

public class DescTextLocalization {

    public static String[] addText(String preFix, int length) {
        String[] text = new String[length];
        for (int i = 0; i < length; i++) {
            text[i] = StatCollector.translateToLocal(preFix + "." + i);
        }
        return text;
    }

    public static String addHintNumber(int hintNum) {
        return StatCollector.translateToLocalFormatted("gtnhlanth.tt.hintNumber", hintNum + "");
    }
}

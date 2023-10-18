package gtPlusPlus.xmod.forestry.bees.items;

import net.minecraft.util.StatCollector;

public class FR_StringUtil {

    public static String getLocalizedString(final String key) {
        if (StatCollector.canTranslate(key)) {
            return StatCollector.translateToLocal(key);
        }
        return StatCollector.translateToFallback(key);
    }

}

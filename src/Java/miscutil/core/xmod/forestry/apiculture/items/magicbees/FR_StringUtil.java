package miscutil.core.xmod.forestry.apiculture.items.magicbees;

import net.minecraft.util.StatCollector;

public class FR_StringUtil
{
	public static String getLocalizedString(String key)
	{
		if(StatCollector.canTranslate(key))
		{
			return StatCollector.translateToLocal(key);
		}
		return StatCollector.translateToFallback(key);
	}

	public static String getLocalizedString(String key, Object... objects)
	{
		if(StatCollector.canTranslate(key))
		{
			return String.format(StatCollector.translateToLocal(key), objects);
		}
		return String.format(StatCollector.translateToFallback(key), objects);
	}
}

package miscutil.core.intermod.forestry.apiculture.items.magicbees;

import net.minecraft.util.StatCollector;

public class FR_StringManager
{
	public static String getLocalizedString(String key)
	{
		if(StatCollector.canTranslate(key))
		{
			return StatCollector.translateToLocal(key);
		}
		else
		{
			return StatCollector.translateToFallback(key);
		}
	}

	public static String getLocalizedString(String key, Object... objects)
	{
		if(StatCollector.canTranslate(key))
		{
			return String.format(StatCollector.translateToLocal(key), objects);
		}
		else
		{
			return String.format(StatCollector.translateToFallback(key), objects);
		}
	}
}

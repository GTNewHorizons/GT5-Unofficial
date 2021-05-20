package gtPlusPlus.core.handler;

import java.lang.reflect.Field;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.event.entity.player.AchievementEvent;

public class StopAnnoyingFuckingAchievements {

	/**
	 * Stops me getting fireworks every fucking time I open my inventory upon first loading a dev client.
	 * @param event
	 */
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void FUCK_OFF(AchievementEvent event)	{
		if (Utils.isClient()) {
			doClientStuff();
		}
		if (event.achievement.equals(AchievementList.openInventory)) {
			event.setCanceled(true);
		}
	}

	@SideOnly(Side.CLIENT)
	private final void doClientStuff() {
		Class aMC = ReflectionUtils.getClass("net.minecraft.client.Minecraft");
		if (aMC != null) {
			Field aInstanceMC = ReflectionUtils.getField(aMC, "theMinecraft");
			Object aMcObj = ReflectionUtils.getFieldValue(null, aInstanceMC);
			Class aClazz2 = aMcObj.getClass();
			if (aClazz2 != null) {
				Field aGameSettings = ReflectionUtils.getField(aClazz2, "gameSettings");
				Object aGameSettingsObj = ReflectionUtils.getFieldValue(aInstanceMC, aGameSettings);
				Class aClazz3 = aGameSettingsObj.getClass();
				if (aClazz2 != null) {
					Field ainvHint = ReflectionUtils.getField(aClazz3, "showInventoryAchievementHint");
					ReflectionUtils.setField(aGameSettingsObj, ainvHint, false);
				}
			}
		}
	}

}

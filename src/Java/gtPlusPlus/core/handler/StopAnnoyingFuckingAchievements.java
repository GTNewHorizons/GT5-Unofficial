package gtPlusPlus.core.handler;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.event.entity.player.AchievementEvent;

public class StopAnnoyingFuckingAchievements {

	/**
	 * Stops me getting fireworks every fucking time I open my inventory upon first loading a dev client.
	 * @param event
	 */
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void FUCK_OFF(AchievementEvent event)	{
		if (event.achievement.equals(AchievementList.openInventory)) {
			event.setCanceled(true);
			if (Minecraft.getMinecraft() != null) {
				if (Minecraft.getMinecraft().gameSettings != null) {
					Minecraft.getMinecraft().gameSettings.showInventoryAchievementHint = false;
				}
			}
		}
	}

}

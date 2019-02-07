package gtPlusPlus.core.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.event.entity.player.AchievementEvent;

public class StopAnnoyingFuckingAchievements {

	/**
	 * Stops me getting fireworks every fucking time I open my inventory upon first loading a dev client.
	 * @param event
	 */
	@SubscribeEvent
	public void FUCK_OFF(AchievementEvent event)	{
		if (event.achievement.equals(AchievementList.openInventory)) {
			if (MathUtils.randInt(0, 10) >= 9)
				PlayerUtils.messagePlayer(event.entityPlayer, "Bang! Nah, Just joking, there's no fireworks. :)");
			event.setCanceled(true);
		}
	}

}

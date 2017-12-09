package gtPlusPlus.core.handler.events;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gtPlusPlus.core.util.Utils;
import net.minecraftforge.event.entity.living.ZombieEvent;

public class ZombieBackupSpawnEventHandler {
	
	@SubscribeEvent
	public void onZombieReinforcement(final ZombieEvent.SummonAidEvent event) {
		if (event.summonChance > 0){
			event.setResult(Result.DENY);
			Utils.LOG_MACHINE_INFO("Stopped a Reinforcement Zombie spawning.");
		}
	}
	
}

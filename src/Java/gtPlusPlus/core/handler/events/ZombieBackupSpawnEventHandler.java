package gtPlusPlus.core.handler.events;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gtPlusPlus.core.util.Utils;
import net.minecraftforge.event.entity.living.ZombieEvent;

public class ZombieBackupSpawnEventHandler {
	
	/**
	 * 
	 * Do we really need this pathetic mechanic to exist when it doesn't work properly at all? 
	 * Or , well, maybe you enjoy Zombies spawning IN YOUR FUCKING FACE?!
	 * 
	 */
	
	@SubscribeEvent
	public void onZombieReinforcement(final ZombieEvent.SummonAidEvent event) {
		Utils.LOG_MACHINE_INFO("ZombieEvent.SummonAidEvent.");
		event.setResult(Result.DENY);
	}
	
	@SubscribeEvent
	public void onZombieReinforcement(final ZombieEvent event) {
		Utils.LOG_MACHINE_INFO("ZombieEvent.");
		event.setResult(Result.DENY);
	}
	
}

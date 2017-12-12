package gtPlusPlus.core.handler.events;

import java.lang.reflect.Field;

import org.apache.commons.lang3.reflect.FieldUtils;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gtPlusPlus.api.analytics.SegmentHelper;
import gtPlusPlus.core.util.Utils;
import net.minecraftforge.event.entity.living.ZombieEvent;

public class ZombieBackupSpawnEventHandler {

	/**
	 * 
	 * Do we really need this pathetic mechanic to exist when it doesn't work properly at all? 
	 * Or , well, maybe you enjoy Zombies spawning IN YOUR FUCKING FACE?!
	 * 
	 */

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onZombieReinforcement(final ZombieEvent.SummonAidEvent event) {
		try {
			try {
				Field mChance = FieldUtils.getDeclaredField(this.getClass(), "summonChance", true);
				FieldUtils.removeFinalModifier(mChance, true);
				mChance.set(this, 0);
			}
			catch(Throwable t){}
			if (event.attacker != null){
				SegmentHelper.getInstance().trackUser(event.attacker.getUniqueID().toString(), "Zombie Backup");			
			}
			Utils.LOG_WARNING("[Zombie] ZombieEvent.SummonAidEvent.");
			event.setResult(Result.DENY);
		}
		catch(Throwable t){}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onZombieReinforcement(final ZombieEvent event) {
		try {
			Utils.LOG_WARNING("[Zombie] ZombieEvent.");
			if (event.entity != null){
				Utils.LOG_WARNING("Event Entity: "+event.entity.getCommandSenderName());
			}
			event.setResult(Result.DENY);
		}
		catch(Throwable t){

		}
	}

}

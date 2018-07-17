package gtPlusPlus.plugin.fixes.vanilla;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.preloader.DevHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

public class VanillaBedHeightFix {

	private static final VanillaBedHeightFix mInstance;
	private final Method mSleepInBedAt;

	static {
		mInstance = new VanillaBedHeightFix();
	}

	public VanillaBedHeightFix() {
		if (DevHelper.isValidHelperObject()) {
			Method m = DevHelper.getInstance().getForgeMethod(EntityPlayer.class, "sleepInBedAt", int.class, int.class, int.class);
			if (m != null) {
				mSleepInBedAt = m;
				Utils.registerEvent(this);			
			}
			else {
				mSleepInBedAt = null;
			}
		}
		else {
			mSleepInBedAt = null;			
		}
	}

	/**
	 * Fix created by deNULL - https://github.com/deNULL/BugPatch/blob/master/src/main/java/ru/denull/BugPatch/mod/ClientEvents.java#L45
	 * @param evt - The event where a player sleeps
	 */

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void playerSleepInBed(PlayerSleepInBedEvent evt) {
		if (evt.y <= 0) {
			int correctY = 256 + evt.y;
			if (correctY <= 0) {
				Logger.INFO("You're trying to sleep at y=" + evt.y + ", which is impossibly low. However, fixed y value is " + correctY + ", which is still below 0. Falling back to default behavior.");
			} else {
				Logger.INFO("You're trying to sleep at y=" + evt.y + ". This is probably caused by overflow, stopping original event; retrying with y=" + correctY + ".");
				evt.result = EntityPlayer.EnumStatus.OTHER_PROBLEM;
				if (mSleepInBedAt != null) {
					try {
						mSleepInBedAt.invoke(evt.entityPlayer, evt.x, correctY, evt.z);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						Logger.INFO("Encountered an error trying to sleep.");
					}
				} else {
					Logger.INFO("Method sleepInBedAt was not found in EntityPlayer (wrong MC and/or Forge version?), unable to fix");
				}
			}
		}
	}

}

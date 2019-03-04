package gtPlusPlus.plugin.fixes.vanilla;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.plugin.fixes.interfaces.IBugFix;
import gtPlusPlus.preloader.DevHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

public class VanillaBedHeightFix implements IBugFix {

	private final Method mSleepInBedAt;
	private final IPlugin mParent;

	public VanillaBedHeightFix(IPlugin minstance) {
		mParent = minstance;
		if (DevHelper.isValidHelperObject()) {	
			Method m;
			if (DevHelper.IsObfuscatedEnvironment()) {
				m = ReflectionUtils.getMethod(EntityPlayer.class, "func_71018_a", int.class, int.class,	int.class);
			}
			else {
				m = ReflectionUtils.getMethod(net.minecraft.entity.player.EntityPlayer.class, "sleepInBedAt", int.class, int.class,	int.class);
			}			
			if (m != null) {
				mSleepInBedAt = m;
				mParent.log("Registering Bed Heigh Fix.");
				Utils.registerEvent(this);
			} else {
				mSleepInBedAt = null;
			}
		} else {
			mSleepInBedAt = null;
		}
	}

	public boolean isFixValid() {
		return mSleepInBedAt != null;
	}

	/**
	 * Fix created by deNULL -
	 * https://github.com/deNULL/BugPatch/blob/master/src/main/java/ru/denull/BugPatch/mod/ClientEvents.java#L45
	 * 
	 * @param evt
	 *            - The event where a player sleeps
	 */

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void playerSleepInBed(PlayerSleepInBedEvent evt) {
		Logger.WARNING("Sleep Event Detected. Player is sleeping at Y: " + evt.y);
		if (evt.y <= 0 && isFixValid()) {
			int correctY = 256 + evt.y;
			if (correctY <= 0) {
				Logger.WARNING(
						"You're trying to sleep at y=" + evt.y + ", which is impossibly low. However, fixed y value is "
								+ correctY + ", which is still below 0. Falling back to default behavior.");
			} else {
				Logger.WARNING("You're trying to sleep at y=" + evt.y
						+ ". This is probably caused by overflow, stopping original event; retrying with y=" + correctY
						+ ".");
				evt.result = EntityPlayer.EnumStatus.OTHER_PROBLEM;
				try {
					mSleepInBedAt.invoke(evt.entityPlayer, evt.x, correctY, evt.z);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					Logger.WARNING("Encountered an error trying to sleep.");
				}
			}
		} else if (!isFixValid()) {
			Logger.WARNING(
					"Method sleepInBedAt was not found in EntityPlayer (wrong MC and/or Forge version?), unable to fix");
		}
	}

}

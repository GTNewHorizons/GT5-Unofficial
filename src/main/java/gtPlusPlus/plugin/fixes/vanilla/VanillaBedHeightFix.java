package gtPlusPlus.plugin.fixes.vanilla;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.asm.GTCorePlugin;
import gtPlusPlus.api.objects.Logger;

// TODO move this as a mixin in hodgepodge
public class VanillaBedHeightFix {

    private final Method mSleepInBedAt;

    public VanillaBedHeightFix() {
        Method m = null;
        try {
            m = EntityPlayer.class.getDeclaredMethod(
                GTCorePlugin.isDevEnv() ? "sleepInBedAt" : "func_71018_a",
                int.class,
                int.class,
                int.class);
        } catch (NoSuchMethodException ignored) {}
        if (m != null) {
            mSleepInBedAt = m;
            Logger.INFO("Registering Bed Height Fix.");
            MinecraftForge.EVENT_BUS.register(this);
        } else {
            mSleepInBedAt = null;
        }
    }

    /**
     * Fix created by deNULL -
     * https://github.com/deNULL/BugPatch/blob/master/src/main/java/ru/denull/BugPatch/mod/ClientEvents.java#L45
     *
     * @param evt - The event where a player sleeps
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void playerSleepInBed(PlayerSleepInBedEvent evt) {
        Logger.WARNING("Sleep Event Detected. Player is sleeping at Y: " + evt.y);
        if (evt.y <= 0) {
            int correctY = 256 + evt.y;
            if (correctY <= 0) {
                Logger.WARNING(
                    "You're trying to sleep at y=" + evt.y
                        + ", which is impossibly low. However, fixed y value is "
                        + correctY
                        + ", which is still below 0. Falling back to default behavior.");
            } else {
                Logger.WARNING(
                    "You're trying to sleep at y=" + evt.y
                        + ". This is probably caused by overflow, stopping original event; retrying with y="
                        + correctY
                        + ".");
                evt.result = EntityPlayer.EnumStatus.OTHER_PROBLEM;
                try {
                    mSleepInBedAt.invoke(evt.entityPlayer, evt.x, correctY, evt.z);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    Logger.WARNING("Encountered an error trying to sleep.");
                }
            }
        }
    }
}
